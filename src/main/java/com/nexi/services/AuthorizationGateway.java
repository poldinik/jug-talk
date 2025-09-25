package com.nexi.services;

import com.nexi.iso.MotoAuthorizationRequest;
import com.nexi.iso8583.extension.runtime.ISOSerializer;
import com.nexi.iso8583.extension.runtime.InvalidCreditCardPanException;
import com.nexi.utils.IsoPrinter;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.iso.packager.ISO93APackager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class AuthorizationGateway {

    private static final Logger LOG = LoggerFactory.getLogger(AuthorizationGateway.class.getName());
    private static final String RESET = "\u001B[0m";
    private static final String MTI_COLOR = "\u001B[96m";
    private static final String BITMAP_COLOR = "\u001B[94m";
    private static final String DE_COLOR_1 = "\u001B[92m";
    private static final String DE_COLOR_2 = "\u001B[95m";
    private static final String DE_COLOR_3 = "\u001B[38;5;214m";

    private final ISOSerializer isoSerializer;

    @Inject
    public AuthorizationGateway(ISOSerializer isoSerializer) {
        this.isoSerializer = isoSerializer;
    }

    public static void logFields(ISOMsg msg) throws ISOException {
        for (int i = 2; i <= 128; i++) {
            if (msg.hasField(i)) {
                String value = msg.getString(i);
                if (value == null) continue;

                String colored;
                if (i % 3 == 0)
                    colored = DE_COLOR_1 + value + RESET;
                else if (i % 3 == 1)
                    colored = DE_COLOR_2 + value + RESET;
                else
                    colored = DE_COLOR_3 + value + RESET;

                Log.infof("DE%02d: %s", i, colored);
            }
        }
    }

    public static void printPrimaryBitmapMatrix(ISOMsg msg) {
        byte[] bitmapBytes = new byte[16];
        for (int i = 1; i <= 128; i++) {
            if (msg.hasField(i)) {
                int byteIndex = (i - 1) / 8;
                int bitIndex = 7 - ((i - 1) % 8);
                bitmapBytes[byteIndex] |= (1 << bitIndex);
            }
        }

        for (int row = 0; row < 8; row++) { // prime 8 righe = primary bitmap
            int b = bitmapBytes[row] & 0xFF;
            for (int col = 7; col >= 0; col--) {
                System.out.print((b >> col) & 0x01);
            }
            System.out.println();
        }
    }

    public void pay(MotoAuthorizationRequest motoAuthorizationRequest) {
        try {
            byte[] packed = isoSerializer.serialize(motoAuthorizationRequest, new ISO93APackager());
            ISOMsg msg = IsoPrinter.msg(packed);
            logAuthorizationRequest(msg);
        } catch (InvalidCreditCardPanException e) {
            LOG.error("Error paying authorization request", e);
            Map<String, Object> responseBody = buildViolations(e);
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity(responseBody)
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
    }

    public void logAuthorizationRequest(ISOMsg msg) {
        StringBuilder sb = new StringBuilder();
        String bitMapHex = bitMap(msg);

        sb.append("\n==================== ISO 8583 MESSAGE ====================\n");
        try {
            sb.append("MTI: ").append(MTI_COLOR).append(msg.getMTI()).append(RESET).append("\n");
        } catch (ISOException e) {
            throw new RuntimeException(e);
        }
        sb.append("Bitmap (hex): ").append(BITMAP_COLOR).append(bitMapHex).append(RESET).append("\n");
        sb.append("Primary bitmap (binary, 8x8):\n");

        // primary bitmap
        byte[] bitmapBytes = new byte[16];
        for (int i = 1; i <= 128; i++) {
            if (msg.hasField(i)) {
                int byteIndex = (i - 1) / 8;
                int bitIndex = 7 - ((i - 1) % 8);
                bitmapBytes[byteIndex] |= (1 << bitIndex);
            }
        }
        for (int row = 0; row < 8; row++) {
            int b = bitmapBytes[row] & 0xFF;
            for (int col = 7; col >= 0; col--) {
                sb.append((b >> col) & 0x01);
            }
            sb.append("\n");
        }
        sb.append("\n");
        sb.append("Packed message (UTF-8):\n");
        String colored = colorPackedMessage(msg);
        sb.append(colored).append("\n");
        sb.append("\n");
        sb.append("Data Elements:\n");
        sb.append("\n");
        for (int i = 2; i <= 128; i++) {
            if (msg.hasField(i)) {
                String value = msg.getString(i);
                if (value == null) continue;

                String color;
                if (i % 3 == 0)
                    color = DE_COLOR_1 + value + RESET;
                else if (i % 3 == 1)
                    color = DE_COLOR_2 + value + RESET;
                else
                    color = DE_COLOR_3 + value + RESET;

                sb.append(String.format("DE%02d: %s\n", i, color));
            }
        }


        sb.append("\n");
        sb.append("Utils:\n");
        sb.append("https://www.rapidtables.com/convert/number/hex-to-binary.html?x=").append(bitMapHex).append("\n");
        sb.append("https://paymentcardtools.com/iso-8583-bitmap\n");
        sb.append("============================================================\n");

        // unico log multilinea
        Log.info(sb.toString());
    }

    private Map<String, Object> buildViolations(InvalidCreditCardPanException e) {
        Map<String, String> violation = new HashMap<>();
        violation.put("field", "moto.paymentRequest.pan");
        violation.put("message", e.getMessage()); // il messaggio di violazione

        List<Map<String, String>> violations = new ArrayList<>();
        violations.add(violation);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("title", "Constraint Violation");
        responseBody.put("status", 400);
        responseBody.put("violations", violations);
        return responseBody;
    }

    private static String bitMap(ISOMsg msg) {
        byte[] bitmapBytes = new byte[16];
        for (int i = 1; i <= 128; i++) {
            if (msg.hasField(i)) {
                int byteIndex = (i - 1) / 8;
                int bitIndex = 7 - ((i - 1) % 8);
                bitmapBytes[byteIndex] |= (1 << bitIndex);
            }
        }

        return ISOUtil.hexString(bitmapBytes);
    }

    public static String colorPackedMessage(ISOMsg msg) {
        StringBuilder sb = new StringBuilder();
        String RESET = "\u001B[0m";
        String CYAN_BRIGHT = "\u001B[96m";
        String ORANGE_BRIGHT = "\u001B[38;5;214m";
        String GREEN_BRIGHT = "\u001B[92m";
        String MAGENTA_BRIGHT = "\u001B[95m";
        String BLUE_BRIGHT = "\u001B[94m";

        try {
            sb.append(CYAN_BRIGHT).append(msg.getMTI()).append(RESET);
        } catch (ISOException e) {
            throw new RuntimeException(e);
        }

        // Inserisce la bitmap calcolata dal metodo separato
        String bitmapHex = bitMap(msg);
        sb.append(BLUE_BRIGHT).append(bitmapHex).append(RESET);

        for (int i = 2; i <= 128; i++) {
            if (msg.hasField(i)) {
                String value = msg.getString(i);
                if (value == null) continue;

                String coloredValue;
                if (i % 3 == 0)
                    coloredValue = ORANGE_BRIGHT + value + RESET;
                else if (i % 3 == 1)
                    coloredValue = GREEN_BRIGHT + value + RESET;
                else
                    coloredValue = MAGENTA_BRIGHT + value + RESET;

                sb.append(coloredValue);
            }
        }
        return sb.toString();
    }
}

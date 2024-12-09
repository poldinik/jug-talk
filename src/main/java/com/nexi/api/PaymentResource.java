package com.nexi.api;

import com.nexi.iso.MotoAuthorizationRequest;
import com.nexi.iso8583.extension.runtime.ISOSerializer;
import com.nexi.iso8583.extension.runtime.InvalidCreditCardPanException;
import com.nexi.model.MerchantCategoryCodes;
import com.nexi.model.OperationResponse;
import com.nexi.model.PaymentRequest;
import com.nexi.services.AuthorizationGateway;
import com.nexi.services.SequenceService;
import com.nexi.utils.RRNUtil;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Path("/payments")
public class PaymentResource {

    public static final String ISO_DATE_TIME_FORMAT = "yyMMddHHmmss";
    private final AuthorizationGateway authorizationGateway;
    private final SequenceService sequenceService;

    @Inject
    public PaymentResource(AuthorizationGateway authorizationGateway, SequenceService sequenceService) {
        this.authorizationGateway = authorizationGateway;
        this.sequenceService = sequenceService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Process MO.TO Authorization Request",
            description = "Executes a Mail Order/Telephone Order (MO.TO) authorization request using provided payment details."
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Authorization successful",
                    content = @Content(schema = @Schema(implementation = OperationResponse.class))
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Invalid request payload"
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    public Response moto(@Valid PaymentRequest paymentRequest) {
        String stan = sequenceService.getNextValueFromSequence();
        authorizationGateway.pay(
                MotoAuthorizationRequest.builder()
                        .pan(paymentRequest.getPan())
                        .amount(paymentRequest.getAmount().toString())
                        .stan(stan)
                        .localTransactionDateTime(
                                OffsetDateTime
                                        .now(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern(ISO_DATE_TIME_FORMAT))
                        )
                        .expirationDate(paymentRequest.getExpiry())
                        .functionCode("100")
                        .messageReasonCode("1510")
                        .merchantCategoryCode(MerchantCategoryCodes.MISCELLANEOUS_STORES)
                        .retrievalReferenceNumber(RRNUtil.of(stan))
                        .build()
        );
        return Response.ok(new OperationResponse(LocalDateTime.now(), "OK")).build();
    }


    @Provider
    public static class InvalidCreditCardLuhnViolationExceptionMapper implements ExceptionMapper<InvalidCreditCardPanException> {

        @Override
        public Response toResponse(InvalidCreditCardPanException exception) {
            return Response.ok(
                    Map.of("violations", List.of(
                            Map.of("field", "moto.paymentRequest.pan", "message", exception.getMessage())
                    ))
            ).status(Response.Status.BAD_REQUEST).build();
        }
    }
}

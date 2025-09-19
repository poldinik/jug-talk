package com.nexi.utils;

import io.quarkus.logging.Log;
import org.jpos.iso.ISOBinaryField;
import org.jpos.iso.ISOComponent;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOField;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO93APackager;

import java.util.Map;

public final class IsoPrinter {

    public static String printMsg(ISOMsg isoMsg) {
        return printMsg(isoMsg.getChildren());
    }

    public static String printMsgColored(ISOMsg isoMsg) {
        return printMsgColored(isoMsg.getChildren());
    }


    public static ISOMsg msg(byte[] packed) {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(new ISO93APackager());
        try {
            isoMsg.unpack(packed);
        } catch (ISOException e) {
            Log.error(e);
            throw new RuntimeException(e);
        }
        return isoMsg;
    }

    private static String printMsg(Map map) {
        StringBuilder builder = new StringBuilder();
        map.forEach((t, u) -> builder.append(printMsg((int) t, (ISOComponent) u)));
        return builder.toString();
    }

    private static String printMsgColored(Map map) {
        StringBuilder builder = new StringBuilder();
        map.forEach((t, u) -> builder.append(printMsgColored((int) t, (ISOComponent) u)));
        return builder.toString();
    }

    private static String printMsg(int t, ISOComponent u) {
        try {
            String value = "error in reading";

            if (t == -1)
                return "";

            try {
                MaskerUtil maskerUtil = new MaskerUtil();
                if (u instanceof ISOField) {
                    value = maskerUtil.getMaskedValue(t, u.getValue().toString());
                } else if (u instanceof ISOBinaryField) {
                    StringBuilder list = new StringBuilder();
                    byte[] bytes = (byte[]) u.getValue();
                    int iterator = 0;
                    int bytesForLength = 2;
                    if (t == 119)
                        bytesForLength = 3;
                    while (iterator < bytes.length) {
                        String tag = new String(bytes, iterator, 2);
                        list.append("\n " + t + "." + tag);
                        iterator += 2;
                        int length = Integer.parseInt(new String(bytes, iterator, bytesForLength));
                        iterator += bytesForLength;
                        String content = new String(bytes, iterator, length);
                        list.append(maskerUtil.getMaskedCvvValue(t, tag, content));
                        iterator += length;
                    }
                    value = list.toString();
                }

            } catch (ISOException e) {
                Log.error(e.getMessage());
            }
            return "\nDE" + t + " " + value;
        } catch (Exception e) {
            return "";
        }
    }

    private static String printMsgColored(int t, ISOComponent u) {
        try {
            String value = "error in reading";
            String RESET = "\u001B[0m";
            String RED = "\u001B[31m";
            String GREEN = "\u001B[32m";
            String YELLOW = "\u001B[33m";

            if (t == -1)
                return "";

            MaskerUtil maskerUtil = new MaskerUtil();
            if (u instanceof ISOField) {
                value = maskerUtil.getMaskedValue(t, u.getValue().toString());
                value = GREEN + value + RESET; // colore verde per ISOField
            } else if (u instanceof ISOBinaryField) {
                StringBuilder list = new StringBuilder();
                byte[] bytes = (byte[]) u.getValue();
                int iterator = 0;
                int bytesForLength = 2;
                if (t == 119)
                    bytesForLength = 3;
                while (iterator < bytes.length) {
                    String tag = new String(bytes, iterator, 2);
                    list.append("\n " + YELLOW + t + "." + tag + RESET); // colore giallo per tag
                    iterator += 2;
                    int length = Integer.parseInt(new String(bytes, iterator, bytesForLength));
                    iterator += bytesForLength;
                    String content = new String(bytes, iterator, length);
                    list.append(maskerUtil.getMaskedCvvValue(t, tag, content));
                    iterator += length;
                }
                value = list.toString();
            }

            return "\nDE" + RED + t + RESET + " " + value; // colore rosso per il numero del DE
        } catch (Exception e) {
            return "";
        }
    }

}

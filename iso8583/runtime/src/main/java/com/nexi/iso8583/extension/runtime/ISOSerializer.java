package com.nexi.iso8583.extension.runtime;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.ISOUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

public interface ISOSerializer {

    AtomicInteger counter = new AtomicInteger();

    byte[] serialize(Object object, ISOPackager genericPackager);

    default String rrn() {
        return rrnGenerate();
    }

    static String rrnGenerate() {
        SimpleDateFormat yearFmt = new SimpleDateFormat("yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        String y = yearFmt.format(cal.getTime()).substring(3);
        Integer julianDay = LocalDate.now().getDayOfYear();
        Integer sequence = counter.getAndIncrement();
        try {
            return y + ISOUtil.padleft(Integer.toString(julianDay), 3, '0') + ISOUtil.padleft(Long.toString(sequence), 8, '0');
        } catch (ISOException e) {
            throw new RuntimeException(e);
        }
    }

    default boolean checkLuhn(String cardNo) {
        int nDigits = cardNo.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    default String padLeft(String s, int length, char padChar) {
        if (s.length() >= length) {
            return s;
        }
        StringBuilder sb = new StringBuilder(length);
        while (sb.length() < length - s.length()) {
            sb.append(padChar);
        }
        sb.append(s);
        return sb.toString();
    }

}

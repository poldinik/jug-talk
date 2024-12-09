package com.nexi.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Year;
import java.util.Calendar;

public class RRNUtil {

    public static String of(String stan) {
        int julianDay = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        int currentYear = Year.now().getValue();
        int lastDigit = currentYear % 10;
        return String.format("%s%s%s", lastDigit, julianDay, StringUtils.leftPad(stan, 8, '0'));
    }

}

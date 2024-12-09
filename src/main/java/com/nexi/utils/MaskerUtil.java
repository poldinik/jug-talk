package com.nexi.utils;

import org.apache.commons.lang3.StringUtils;

public class MaskerUtil {

    private static final int NUMBER_OF_UNMASKED_CHARACTERS_DISPLAYED_START = 6;
    private static final int NUMBER_OF_UNMASKED_CHARACTERS_DISPLAYED_END = 4;
    private static final int NUMBER_OF_UNMASKED_CHARACTERS_DISPLAYED_END_14_DIGIT_CARD = 3;
    private static final int NUMBER_OF_UNMASKED_CHARACTERS_DISPLAYED_END_13_DIGIT_CARD = 2;
    private static final int MIN_PAN_LENGTH = 13;
    private static final int MAX_PAN_LENGTH = 19;
    private static final char MASKING_CHARACTER = '*';
    private static final String MASKED_EXPIRY_DATE = "****";
    private static final String MASKED_CVV2 = "***";
    private static final String TAG = "02";

    public MaskerUtil() {
    }

    public String getMaskedValue(int t, String value) {
        switch (t) {
            case 2:
                return getMaskedPan(value);
            case 14:
                return MASKED_EXPIRY_DATE;
            default:
                return value;
        }
    }

    public String getMaskedCvvValue(int t, String tag, String value) {
        if ((t == 48) && (tag.equals(TAG))) {
            return MASKED_CVV2;
        } else {
            return value;
        }
    }

    private static String getMaskedPan(String clearPan) {
        if (!StringUtils.isBlank(clearPan) && clearPan.length() >= MIN_PAN_LENGTH && clearPan.length() <= MAX_PAN_LENGTH) {
            int panLength = clearPan.length();
            int numberOfMaskingCharacters;
            String stars;
            if (panLength > 14) {
                numberOfMaskingCharacters = panLength - (NUMBER_OF_UNMASKED_CHARACTERS_DISPLAYED_START + NUMBER_OF_UNMASKED_CHARACTERS_DISPLAYED_END);
                stars = StringUtils.repeat(MASKING_CHARACTER, numberOfMaskingCharacters);
                return buildMaskedPan(clearPan.replaceAll("([0-9]{6})([0-9]*)([0-9]{4})", "$1" + stars + "$3"));
            } else if (panLength == 14) {
                numberOfMaskingCharacters = panLength - (NUMBER_OF_UNMASKED_CHARACTERS_DISPLAYED_START + NUMBER_OF_UNMASKED_CHARACTERS_DISPLAYED_END_14_DIGIT_CARD);
                stars = StringUtils.repeat(MASKING_CHARACTER, numberOfMaskingCharacters);
                return buildMaskedPan(clearPan.replaceAll("([0-9]{6})([0-9]*)([0-9]{3})", "$1" + stars + "$3"));
            } else {
                numberOfMaskingCharacters = panLength - (NUMBER_OF_UNMASKED_CHARACTERS_DISPLAYED_START + NUMBER_OF_UNMASKED_CHARACTERS_DISPLAYED_END_13_DIGIT_CARD);
                stars = StringUtils.repeat(MASKING_CHARACTER, numberOfMaskingCharacters);
                return buildMaskedPan(clearPan.replaceAll("([0-9]{6})([0-9]*)([0-9]{2})", "$1" + stars + "$3"));
            }
        } else {
            return "";
        }
    }

    private static String buildMaskedPan(Object o) {
        return o == null ? "" : o.toString().replace("\n", "\n    ");
    }
}

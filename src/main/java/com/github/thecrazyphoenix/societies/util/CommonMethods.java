package com.github.thecrazyphoenix.societies.util;

import java.util.regex.Pattern;

public class CommonMethods {
    private static final Pattern NAME_VALIDATOR_PATTERN = Pattern.compile("^[a-zA-Z0-9\\-&#;:.?! ]+$");

    public static boolean isValidName(String value) {
        return NAME_VALIDATOR_PATTERN.matcher(value).matches();
    }
}

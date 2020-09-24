package com.ionshield.planner;

public abstract class Utils {
    public static Double parseDoubleOrNull(String string) {
        if (string == null) return null;
        try {
            return Double.parseDouble(string);
        }
        catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer colorHexToInt(String color) {
        if (color == null) return null;

        String colorString = color.toUpperCase();
        char[] validChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};



        if (!colorString.startsWith("#")) {
            colorString = "#" + colorString;
        }

        if (colorString.length() != 7 || !colorString.startsWith("#")) {
            return null;
        }
        colorString = colorString.substring(1);
        for (int i = 0; i < colorString.length(); i++) {
            boolean ok = false;
            char c = colorString.charAt(i);
            for (char validChar : validChars) {
                if (c == validChar) {
                    ok = true;
                    break;
                }
            }
            if (!ok) {
                return null;
            }
        }
        return Integer.parseInt(colorString, 16);
    }

    public static String colorIntToHex(int color) {
        StringBuilder colorString = new StringBuilder(Integer.toString(color, 16));
        while (colorString.length() < 6) {
            colorString.insert(0, "0");
        }
        colorString.insert(0, "#");
        return colorString.toString().toUpperCase();
    }
}

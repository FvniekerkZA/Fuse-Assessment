package Utils;

public class StringUtil extends Exception {

    private static boolean isWhitespace(String varString) {
        int length = varString.length();
        if (length <= 0) return false;
        for (int i = 0; i < length; i++) if (!Character.isWhitespace(varString.charAt(i)))  return false;
        return true;
    }

    public static boolean isNullOrEmpty(String varString) {
        return varString == null || varString.length() == 0;
    }

    public static boolean isNullOrWhitespace(String varString) {
        return varString == null || isWhitespace(varString);
    }
}

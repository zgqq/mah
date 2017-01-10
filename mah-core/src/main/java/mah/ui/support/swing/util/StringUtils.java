package mah.ui.support.swing.util;

/**
 * Created by zgq on 2017-01-09 19:05
 */
public class StringUtils {

    public static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isIdeographic(c)) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }

    public static String getStrBySpecificLength(String text, int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("length must be greater than 0");
        }
        int index = getIndexBySpecificLength(text, len);
        if (index < 0) {
            return "";
        }

        if (index >= text.length() -1) {
            return text;
        }

        return text.substring(0, index+1)+"...";
    }

    public static int getIndexBySpecificLength(String text,int len) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isIdeographic(c)) {
                length += 2;
            } else {
                length += 1;
            }
            if (length >= len) {
                return i-1;
            }
        }
        return text.length()-1;
    }


}

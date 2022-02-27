package utils;

public class StringUtils {
    public static String concatenateStringNTimes(String string, int count, String addedString) {
        if (count < 0)
            return null;

        if (count == 0)
            return string;

        for (int i = 0; i < count; i++) {
            string = string.concat(addedString);
        }
        return string;
    }
}

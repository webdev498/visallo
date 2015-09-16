package org.visallo.web.clientapi.codegen.util;

import org.visallo.core.exception.VisalloException;

public class NameUtil {
    public static String toClassName(String str) {
        if (str.length() == 0) {
            throw new VisalloException("string cannot be length 0");
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        str = camelCaseString(str);
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String camelCaseString(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == '[' || ch == ']') {
                continue;
            }
            if (ch == '-' || ch == '/') {
                i++;
                result.append(Character.toUpperCase(str.charAt(i)));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static String toFieldName(String str) {
        if (str.length() == 0) {
            throw new VisalloException("string cannot be length 0");
        }
        str = camelCaseString(str);
        return str;
    }
}

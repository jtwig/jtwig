package org.jtwig.functions.util;

public class ClassUtils {
    public static boolean classExists (ClassLoader classLoader, String className) {
        try {
            Class.forName(className, false, classLoader);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}

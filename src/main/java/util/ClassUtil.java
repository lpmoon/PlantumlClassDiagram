package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ClassUtil {
    public static boolean isJdkBasicClass(String clazz) {
        return BASIC_CLASS.contains(clazz) || clazz.startsWith("java.");
    }

    public static boolean isClassIgnorePackage(String clazz) {
        return BASIC_CLASS.contains(clazz);
    }

    public static Set BASIC_CLASS = new HashSet(Arrays.asList("String", "int", "long", "double", "float", "Integer", "Long", "Double", "Float"));
}

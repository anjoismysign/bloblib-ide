package com.github.anjoismysign.bloblibide.libraries;

import java.util.HashMap;
import java.util.Optional;

public class DataTypeLib {

    private static HashMap<String, String> primitiveToWrapper = new HashMap<>() {
        {
            put("byte", "Byte");
            put("short", "Short");
            put("int", "Integer");
            put("long", "Long");
            put("float", "Float");
            put("double", "Double");
            put("boolean", "Boolean");
            put("char", "Character");
        }
    };

    /**
     * Will attempt to convert a primitive data type to its wrapper class.
     *
     * @param primitiveDataType The primitive data type to convert. EqualsIgnoreCase is used.
     * @return True if the primitive data type is a primitive data type.
     */
    public static boolean hasWrapper(String primitiveDataType) {
        for (String key : primitiveToWrapper.keySet()) {
            if (key.equalsIgnoreCase(primitiveDataType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Will attempt to convert a primitive data type to its wrapper class.
     *
     * @param primitiveDataType The primitive data type to convert. EqualsIgnoreCase is used.
     * @return The wrapper class name of the primitive data type.
     */
    public static Optional<String> getWrapper(String primitiveDataType) {
        for (String key : primitiveToWrapper.keySet()) {
            if (key.equalsIgnoreCase(primitiveDataType)) {
                return Optional.of(primitiveToWrapper.get(key));
            }
        }
        return Optional.empty();
    }

    /**
     * @param dataType The data type to check.
     * @return Present if the data type is a primitive data type.
     * If it's already a wrapper, will return itself.
     * Empty if not a primitive data type nor a wrapper.
     */
    public static Optional<String> primitiveToWrapper(String dataType) {
        if (primitiveToWrapper.containsKey(dataType)) {
            return Optional.of(primitiveToWrapper.get(dataType));
        }
        if (primitiveToWrapper.containsValue(dataType)) {
            return Optional.of(dataType);
        }
        return Optional.empty();
    }

    /**
     * Will attempt to convert a primitive data type to its wrapper class.
     * If not a primitive data type, will return itself.
     *
     * @param primitiveDataType The primitive data type to convert.
     * @return The wrapper class of the primitive data type.
     */
    public static String findPrimitiveWrapper(String primitiveDataType) {
        switch (primitiveDataType) {
            case "byte": {
                return "Byte";
            }
            case "short": {
                return "Short";
            }
            case "int": {
                return "Integer";
            }
            case "long": {
                return "Long";
            }
            case "float": {
                return "Float";
            }
            case "double": {
                return "Double";
            }
            case "boolean": {
                return "Boolean";
            }
            case "char": {
                return "Character";
            }
            default: {
                return primitiveDataType;
            }
        }
    }
}

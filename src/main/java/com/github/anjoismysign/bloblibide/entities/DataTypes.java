package com.github.anjoismysign.bloblibide.entities;

import java.util.HashMap;
import java.util.Optional;

public class DataTypes {

    private static final HashMap<String, Custom> customs = new HashMap<>() {{
        put("BigInteger", new Custom("BigInteger",
                "SerializationLib.deserializeBigInteger" +
                        "(%configurationSection%.getString(%path%))"
                , "%configurationSection%.set" +
                "(%path%, SerializationLib.serializeBigInteger(%value%))"));
        put("BigDecimal", new Custom("BigDecimal",
                "SerializationLib.deserializeBigDecimal" +
                        "(%configurationSection%.getString(%path%))"
                , "%configurationSection%.set" +
                "(%path%, SerializationLib.serializeBigDecimal(%value%))"));
        put("UUID", new Custom("UUID",
                "SerializationLib.deserializeUUID" +
                        "(%configurationSection%.getString(%path%))"
                , "%configurationSection%.set" +
                "(%path%, SerializationLib.serializeUUID(%value%))"));
        put("Vector", new Custom("Vector",
                "SerializationLib.deserializeVector" +
                        "(%configurationSection%.getString(%path%))"
                , "%configurationSection%.set" +
                "(%path%, SerializationLib.serializeVector(%value%))"));
        put("BlockVector", new Custom("BlockVector",
                "SerializationLib.deserializeBlockVector" +
                        "(%configurationSection%.getString(%path%))"
                , "%configurationSection%.set" +
                "(%path%, SerializationLib.serializeBlockVector(%value%))"));
        put("Color", new Custom("Color",
                "SerializationLib.deserializeColor" +
                        "(%configurationSection%.getString(%path%))"
                , "%configurationSection%.set" +
                "(%path%, SerializationLib.serializeColor(%value%))"));
        put("Location", new Custom("Location",
                "SerializationLib.deserializeLocation" +
                        "(%configurationSection%.getString(%path%))"
                , "%configurationSection%.set" +
                "(%path%, SerializationLib.serializeLocation(%value%))"));
        put("Block", new Custom("Block",
                "SerializationLib.deserializeBlock" +
                        "(%configurationSection%.getString(%path%))"
                , "%configurationSection%.set" +
                "(%path%, SerializationLib.serializeBlock(%value%))"));
        put("OfflinePlayer", new Custom("OfflinePlayer",
                "SerializationLib.deserializeOfflinePlayer" +
                        "(%configurationSection%.getString(%path%))"
                , "%configurationSection%.set" +
                "(%path%, SerializationLib.serializeOfflinePlayer(%value%))"));
    }};
    private static final HashMap<String, Primitive> primitives = new HashMap<>() {
        {
            put("Byte", new Primitive("Byte", "(byte) %configurationSection%" +
                    ".getInt(%path%)"));
            put("Short", new Primitive("Short", "(short) %configurationSection%" +
                    ".getInt(%path%)"));
            put("Integer", new Primitive("Integer", "%configurationSection%" +
                    ".getInt(%path%)"));
            put("Long", new Primitive("Long", "%configurationSection%" +
                    ".getLong(%path%)"));
            put("Float", new Primitive("Float", "(float) %configurationSection%" +
                    ".getDouble(%path%)"));
            put("Double", new Primitive("Double", "%configurationSection%" +
                    ".getDouble(%path%)"));
            put("Boolean", new Primitive("Boolean", "%configurationSection%" +
                    ".getBoolean(%path%)"));
            put("String", new Primitive("String", "%configurationSection%" +
                    ".getString(%path%)"));
            put("Character", new Primitive("Character", "(char) %configurationSection%" +
                    ".getString(%path%).charAt(0)"));
        }
    };

    public static Optional<Custom> getCustom(String dataType) {
        return Optional.ofNullable(customs.get(dataType));
    }

    public static boolean isCustom(String dataType) {
        return customs.containsKey(dataType);
    }

    public static String getCustomGetter(String dataType) {
        Optional<Custom> optional = getCustom(dataType);
        if (optional.isPresent()) {
            return optional.get().getGetter();
        } else {
            throw new IllegalArgumentException("The data type " + dataType + " is not a custom");
        }
    }

    public static Optional<Primitive> getPrimitive(String dataType) {
        return Optional.ofNullable(primitives.get(dataType));
    }

    public static boolean isPrimitive(String dataType) {
        return primitives.containsKey(dataType);
    }

    public static String getPrimitiveGetter(String dataType) {
        Optional<Primitive> optional = getPrimitive(dataType);
        if (optional.isPresent()) {
            return optional.get().getGetter();
        } else {
            throw new IllegalArgumentException("The data type " + dataType + " is not a primitive");
        }
    }
}

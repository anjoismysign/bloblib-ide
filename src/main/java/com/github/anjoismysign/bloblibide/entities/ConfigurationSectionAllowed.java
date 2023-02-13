package com.github.anjoismysign.bloblibide.entities;

import java.util.HashMap;
import java.util.Optional;

public enum ConfigurationSectionAllowed {
    STRING,
    BYTE,
    SHORT,
    INTEGER,
    LONG,
    FLOAT,
    DOUBLE,
    BOOLEAN,
    CHARACTER,
    BIG_INTEGER,
    BIG_DECIMAL,
    UUID,
    VECTOR,
    BLOCK_VECTOR,
    COLOR,
    LOCATION,
    BLOCK,
    OFFLINE_PLAYER;

    private static final HashMap<ConfigurationSectionAllowed, String> dataTypes = new HashMap<>();
    private static final HashMap<String, ConfigurationSectionAllowed> dataTypesBackwards = new HashMap<>();

    static {
        dataTypes.put(STRING, "String");
        dataTypes.put(BYTE, "Byte");
        dataTypes.put(SHORT, "Short");
        dataTypes.put(INTEGER, "Integer");
        dataTypes.put(LONG, "Long");
        dataTypes.put(FLOAT, "Float");
        dataTypes.put(DOUBLE, "Double");
        dataTypes.put(BOOLEAN, "Boolean");
        dataTypes.put(CHARACTER, "Character");
        dataTypes.put(BIG_INTEGER, "BigInteger");
        dataTypes.put(BIG_DECIMAL, "BigDecimal");
        dataTypes.put(UUID, "UUID");
        dataTypes.put(VECTOR, "Vector");
        dataTypes.put(BLOCK_VECTOR, "BlockVector");
        dataTypes.put(COLOR, "Color");
        dataTypes.put(LOCATION, "Location");
        dataTypes.put(BLOCK, "Block");
        dataTypes.put(OFFLINE_PLAYER, "OfflinePlayer");

        dataTypesBackwards.put("String", STRING);
        dataTypesBackwards.put("Byte", BYTE);
        dataTypesBackwards.put("Short", SHORT);
        dataTypesBackwards.put("Integer", INTEGER);
        dataTypesBackwards.put("Long", LONG);
        dataTypesBackwards.put("Float", FLOAT);
        dataTypesBackwards.put("Double", DOUBLE);
        dataTypesBackwards.put("Boolean", BOOLEAN);
        dataTypesBackwards.put("Character", CHARACTER);
        dataTypesBackwards.put("BigInteger", BIG_INTEGER);
        dataTypesBackwards.put("BigDecimal", BIG_DECIMAL);
        dataTypesBackwards.put("UUID", UUID);
        dataTypesBackwards.put("Vector", VECTOR);
        dataTypesBackwards.put("BlockVector", BLOCK_VECTOR);
        dataTypesBackwards.put("Color", COLOR);
        dataTypesBackwards.put("Location", LOCATION);
        dataTypesBackwards.put("Block", BLOCK);
        dataTypesBackwards.put("OfflinePlayer", OFFLINE_PLAYER);
    }

    /**
     * Returns the ConfigurationSectionAllowed for the given data type.
     *
     * @param dataType The data type to check. Will be trimmed.
     * @return The ConfigurationSectionAllowed for the given data type. Null if not found.
     */
    public static ConfigurationSectionAllowed fromName(String dataType) {
        return dataTypesBackwards.get(dataType.trim());
    }

    public String getDataType() {
        return dataTypes.get(this);
    }

    /**
     * Returns the ConfigurationSectionAllowed for the given data type.
     *
     * @param dataType The data type to check.
     * @return The ConfigurationSectionAllowed for the given data type. Empty if not found.
     */
    public static Optional<ConfigurationSectionAllowed> isPrimitiveNeedsConversion(String dataType) {
        for (String key : PRIMITIVE_NEEDS_CONVERSION.keySet()) {
            if (dataType.startsWith(key)) {
                return Optional.of(PRIMITIVE_NEEDS_CONVERSION.get(key));
            }
        }
        return Optional.empty();
    }

    public boolean needsCustomConversion() {
        return CUSTOM_NEEDS_CONVERSION.containsValue(this);
    }

    public boolean needsPrimitiveConversion() {
        return PRIMITIVE_NEEDS_CONVERSION.containsValue(this);
    }

    public static final HashMap<String, ConfigurationSectionAllowed> PRIMITIVE_NEEDS_CONVERSION = new HashMap<>();

    static {
        PRIMITIVE_NEEDS_CONVERSION.put("Map<String,", ConfigurationSectionAllowed.STRING);
        PRIMITIVE_NEEDS_CONVERSION.put("Map<Byte,", ConfigurationSectionAllowed.BYTE);
        PRIMITIVE_NEEDS_CONVERSION.put("Map<Short,", ConfigurationSectionAllowed.SHORT);
        PRIMITIVE_NEEDS_CONVERSION.put("Map<Integer,", ConfigurationSectionAllowed.INTEGER);
        PRIMITIVE_NEEDS_CONVERSION.put("Map<Long,", ConfigurationSectionAllowed.LONG);
        PRIMITIVE_NEEDS_CONVERSION.put("Map<Float,", ConfigurationSectionAllowed.FLOAT);
        PRIMITIVE_NEEDS_CONVERSION.put("Map<Double,", ConfigurationSectionAllowed.DOUBLE);
        PRIMITIVE_NEEDS_CONVERSION.put("Map<Boolean,", ConfigurationSectionAllowed.BOOLEAN);
        PRIMITIVE_NEEDS_CONVERSION.put("Map<Character,", ConfigurationSectionAllowed.CHARACTER);
    }

    /**
     * Returns the ConfigurationSectionAllowed for the given data type.
     *
     * @param dataType The data type to check.
     * @return The ConfigurationSectionAllowed for the given data type. Empty if not found.
     */
    public static Optional<ConfigurationSectionAllowed> isCustomNeedsConversion(String dataType) {
        for (String key : CUSTOM_NEEDS_CONVERSION.keySet()) {
            if (dataType.startsWith(key)) {
                return Optional.of(CUSTOM_NEEDS_CONVERSION.get(key));
            }
        }
        return Optional.empty();
    }

    public static final HashMap<String, ConfigurationSectionAllowed> CUSTOM_NEEDS_CONVERSION = new HashMap<>();

    static {
        CUSTOM_NEEDS_CONVERSION.put("Map<BigInteger,", ConfigurationSectionAllowed.BIG_INTEGER);
        CUSTOM_NEEDS_CONVERSION.put("Map<BigDecimal,", ConfigurationSectionAllowed.BIG_DECIMAL);
        CUSTOM_NEEDS_CONVERSION.put("Map<UUID,", ConfigurationSectionAllowed.UUID);
        CUSTOM_NEEDS_CONVERSION.put("Map<Vector,", ConfigurationSectionAllowed.VECTOR);
        CUSTOM_NEEDS_CONVERSION.put("Map<Location,", ConfigurationSectionAllowed.LOCATION);
        CUSTOM_NEEDS_CONVERSION.put("Map<Color,", ConfigurationSectionAllowed.COLOR);
        CUSTOM_NEEDS_CONVERSION.put("Map<OfflinePlayer,", ConfigurationSectionAllowed.OFFLINE_PLAYER);
        CUSTOM_NEEDS_CONVERSION.put("Map<BlockVector,", ConfigurationSectionAllowed.BLOCK_VECTOR);
        CUSTOM_NEEDS_CONVERSION.put("Map<Block,", ConfigurationSectionAllowed.BLOCK);
    }
}

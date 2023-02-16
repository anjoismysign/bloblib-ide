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
    OFFLINE_PLAYER,
    WORLD,
    WEATHER_TYPE,
    TREE_TYPE,
    PARTICLE,
    MUSIC_INSTRUMENT,
    MATERIAL,
    INSTRUMENT,
    GAMEMODE,
    FLUID,
    ENTITY_TYPE,
    ENTITY_EFFECT,
    ENCHANTMENT,
    EFFECT,
    DYE_COLOR,
    DIFFICULTY,
    CROP_STATE;

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
        dataTypes.put(WORLD, "World");
        dataTypes.put(WEATHER_TYPE, "WeatherType");
        dataTypes.put(TREE_TYPE, "TreeType");
        dataTypes.put(PARTICLE, "Particle");
        dataTypes.put(MUSIC_INSTRUMENT, "MusicInstrument");
        dataTypes.put(MATERIAL, "Material");
        dataTypes.put(INSTRUMENT, "Instrument");
        dataTypes.put(GAMEMODE, "GameMode");
        dataTypes.put(FLUID, "Fluid");
        dataTypes.put(ENTITY_TYPE, "EntityType");
        dataTypes.put(ENTITY_EFFECT, "EntityEffect");
        dataTypes.put(ENCHANTMENT, "Enchantment");
        dataTypes.put(EFFECT, "Effect");
        dataTypes.put(DYE_COLOR, "DyeColor");
        dataTypes.put(DIFFICULTY, "Difficulty");
        dataTypes.put(CROP_STATE, "CropState");

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
        dataTypesBackwards.put("World", WORLD);
        dataTypesBackwards.put("WeatherType", WEATHER_TYPE);
        dataTypesBackwards.put("TreeType", TREE_TYPE);
        dataTypesBackwards.put("Particle", PARTICLE);
        dataTypesBackwards.put("MusicInstrument", MUSIC_INSTRUMENT);
        dataTypesBackwards.put("Material", MATERIAL);
        dataTypesBackwards.put("Instrument", INSTRUMENT);
        dataTypesBackwards.put("GameMode", GAMEMODE);
        dataTypesBackwards.put("Fluid", FLUID);
        dataTypesBackwards.put("EntityType", ENTITY_TYPE);
        dataTypesBackwards.put("EntityEffect", ENTITY_EFFECT);
        dataTypesBackwards.put("Enchantment", ENCHANTMENT);
        dataTypesBackwards.put("Effect", EFFECT);
        dataTypesBackwards.put("DyeColor", DYE_COLOR);
        dataTypesBackwards.put("Difficulty", DIFFICULTY);
        dataTypesBackwards.put("CropState", CROP_STATE);
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
        for (ConfigurationSectionAllowed key : PRIMITIVE_NEEDS_CONVERSION.values()) {
            if (dataType.toUpperCase().startsWith(key.toString())) {
                return Optional.of(PRIMITIVE_NEEDS_CONVERSION.get(key));
            }
        }
        return Optional.empty();
    }

    public boolean hasSafeToStringConversion() {
        return this == BYTE ||
                this == SHORT ||
                this == INTEGER ||
                this == LONG ||
                this == FLOAT ||
                this == DOUBLE ||
                this == BOOLEAN ||
                this == CHARACTER ||
                this == STRING ||
                this == BIG_INTEGER ||
                this == BIG_DECIMAL ||
                this == UUID;
    }


    public static final HashMap<String, ConfigurationSectionAllowed> SHAPE_NEEDS_CONVERSION = new HashMap<>();

    static {
        SHAPE_NEEDS_CONVERSION.put("Map<World,", ConfigurationSectionAllowed.WORLD);
        SHAPE_NEEDS_CONVERSION.put("Map<WeatherType,", WEATHER_TYPE);
        SHAPE_NEEDS_CONVERSION.put("Map<TreeType,", TREE_TYPE);
        SHAPE_NEEDS_CONVERSION.put("Map<Particle,", PARTICLE);
        SHAPE_NEEDS_CONVERSION.put("Map<MusicInstrument,", MUSIC_INSTRUMENT);
        SHAPE_NEEDS_CONVERSION.put("Map<Material,", MATERIAL);
        SHAPE_NEEDS_CONVERSION.put("Map<Instrument,", INSTRUMENT);
        SHAPE_NEEDS_CONVERSION.put("Map<GameMode,", GAMEMODE);
        SHAPE_NEEDS_CONVERSION.put("Map<Fluid,", FLUID);
        SHAPE_NEEDS_CONVERSION.put("Map<EntityType,", ENTITY_TYPE);
        SHAPE_NEEDS_CONVERSION.put("Map<EntityEffect,", ENTITY_EFFECT);
        SHAPE_NEEDS_CONVERSION.put("Map<Enchantment,", ENCHANTMENT);
        SHAPE_NEEDS_CONVERSION.put("Map<Effect,", EFFECT);
        SHAPE_NEEDS_CONVERSION.put("Map<DyeColor,", DYE_COLOR);
        SHAPE_NEEDS_CONVERSION.put("Map<Difficulty,", DIFFICULTY);
        SHAPE_NEEDS_CONVERSION.put("Map<CropState,", CROP_STATE);
    }

    /**
     * Returns the ConfigurationSectionAllowed for the given data type.
     *
     * @param dataType The data type to check.
     * @return The ConfigurationSectionAllowed for the given data type. Empty if not found.
     */
    public static Optional<ConfigurationSectionAllowed> isShapeNeedsConversion(String dataType) {
        for (ConfigurationSectionAllowed key : SHAPE_NEEDS_CONVERSION.values()) {
            if (dataType.toUpperCase().startsWith(key.toString())) {
                return Optional.of(SHAPE_NEEDS_CONVERSION.get(key));
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

    public boolean needsShapeConversion() {
        return SHAPE_NEEDS_CONVERSION.containsValue(this);
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
        for (ConfigurationSectionAllowed key : CUSTOM_NEEDS_CONVERSION.values()) {
            if (dataType.toUpperCase().startsWith(key.toString())) {
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

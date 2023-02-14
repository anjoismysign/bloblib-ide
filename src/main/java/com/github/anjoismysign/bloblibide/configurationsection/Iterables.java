package com.github.anjoismysign.bloblibide.configurationsection;

import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;
import com.github.anjoismysign.bloblibide.libraries.DataTypeLib;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;

public class Iterables {
    /**
     * Will check if DataType is a primitive wrapper or a String
     * since they are the only ones that can be quickly serialized
     * inside org.bukkit.configuration.ConfigurationSection as a List.
     *
     * @param dataType DataType to check
     * @return true if DataType is a primitive wrapper or a String, false otherwise
     */
    public static boolean isQuickIterable(String dataType) {
        return dataType.equals("Integer") || dataType.equals("Double") || dataType.equals("Float") ||
                dataType.equals("Long") || dataType.equals("Short") || dataType.equals("Byte") || dataType.equals("Boolean") ||
                dataType.equals("Character") || dataType.equals("String");
    }

    /**
     * Will check if DataType is a listable which was given support
     * by BlobLib through ConfigurationSectionLib.
     *
     * @param dataType DataType to check
     * @return true if DataType is supported, false otherwise
     */
    public static boolean isCustomQuickIterable(String dataType) {
        return dataType.equals("Vector") || dataType.equals("Location") || dataType.equals("Color")
                || dataType.equals("OfflinePlayer") || dataType.equals("BlockVector") ||
                dataType.equals("Block") || dataType.equals("BigInteger") ||
                dataType.equals("BigDecimal") || dataType.equals("UUID");
    }

    /**
     * Will check if DataType is a String, an ItemStack, an OfflinePlayer,
     * a Vector, a Location or a Color since they are the only ones that
     * can be quickly serialized inside org.bukkit.configuration.ConfigurationSection.
     *
     * @param dataType DataType to check
     * @return true if it has quick support, false otherwise
     */
    public static boolean hasQuickObjectSupport(String dataType) {
        return dataType.equals("ItemStack") || dataType.equals("OfflinePlayer") ||
                dataType.equals("Vector") || dataType.equals("Location") || dataType.equals("Color") ||
                dataType.equals("String");
    }

    public static boolean isPrimitiveOrWrapper(String dataType) {
        return dataType.equals("int") || dataType.equals("double") || dataType.equals("float") ||
                dataType.equals("long") || dataType.equals("short") || dataType.equals("byte") ||
                dataType.equals("boolean") || dataType.equals("char") || dataType.equals("Integer") ||
                dataType.equals("Double") || dataType.equals("Float") || dataType.equals("Long") ||
                dataType.equals("Short") || dataType.equals("Byte") || dataType.equals("Boolean") ||
                dataType.equals("Character") || dataType.equals("BigInteger") || dataType.equals("BigDecimal");
    }

    public static String primitivesGetMethods(ObjectAttribute attribute,
                                              String configurationSectionVariableName) {
        String dataType = attribute.getDataType();
        dataType = DataTypeLib.findPrimitiveWrapper(dataType);
        String pascalAttributeName = NamingConventions.toPascalCase(attribute.getAttributeName());
        switch (dataType) {
            case "Byte": {
                return "(byte) " + configurationSectionVariableName + ".getInt(\"" + pascalAttributeName + "\");";
            }
            case "Short": {
                return "(short) " + configurationSectionVariableName + ".getInt(\"" + pascalAttributeName + "\");";
            }
            case "Integer": {
                return configurationSectionVariableName + ".getInt(\"" + pascalAttributeName + "\");";
            }
            case "Long": {
                return configurationSectionVariableName + ".getLong(\"" + pascalAttributeName + "\");";
            }
            case "Float": {
                return "(float) " + configurationSectionVariableName + ".getDouble(\"" + pascalAttributeName + "\");";
            }
            case "Double": {
                return configurationSectionVariableName + ".getDouble(\"" + pascalAttributeName + "\");";
            }
            case "Boolean": {
                return configurationSectionVariableName + ".getBoolean(\"" + pascalAttributeName + "\");";
            }
            case "Character": {
                return "(char) " + configurationSectionVariableName + ".getString(\"" + pascalAttributeName + "\").charAt(0);";
            }
            case "BigInteger": {
                return "new BigInteger(" + configurationSectionVariableName + ".getString(\"" + pascalAttributeName + "\"));";
            }
            case "BigDecimal": {
                return "new BigDecimal(" + configurationSectionVariableName + ".getString(\"" + pascalAttributeName + "\"));";
            }
            default: {
                return "null; //TODO ConfigurationSectionLib.java error, contact bloblib-ide developers that '" + dataType + "' is not supported";
            }
        }
    }
}

package com.github.anjoismysign.bloblibide.libraries;

import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;

import java.util.Optional;

public class ConfigurationSectionLib {
    /**
     * Will check if DataType is a primitive wrapper or a String
     * since they are the only ones that can be quickly serialized
     * inside org.bukkit.configuration.ConfigurationSection as a List.
     *
     * @param dataType DataType to check
     * @return true if DataType is a primitive wrapper or a String, false otherwise
     */
    public static boolean isQuickListable(String dataType) {
        return dataType.equals("Integer") || dataType.equals("Double") || dataType.equals("Float") ||
                dataType.equals("Long") || dataType.equals("Short") || dataType.equals("Byte") || dataType.equals("Boolean") ||
                dataType.equals("Character") || dataType.equals("String");
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
                dataType.equals("Character");
    }

    public static String getPrimitiveMethod(ObjectAttribute attribute,
                                            String configurationSectionVariableName) {
        String dataType = attribute.getDataType();
        String pascalAttributeName = NamingConventions.toPascalCase(attribute.getAttributeName());
        switch (dataType) {
            case "byte":
            case "Byte": {
                return "(byte) " + configurationSectionVariableName + ".getInt(\"" + pascalAttributeName + "\");";
            }
            case "Short":
            case "short": {
                return "(short) " + configurationSectionVariableName + ".getInt(\"" + pascalAttributeName + "\");";
            }
            case "Integer":
            case "int": {
                return configurationSectionVariableName + ".getInt(\"" + pascalAttributeName + "\");";
            }
            case "Long":
            case "long": {
                return configurationSectionVariableName + ".getLong(\"" + pascalAttributeName + "\");";
            }
            case "Float":
            case "float": {
                return "(float) " + configurationSectionVariableName + ".getDouble(\"" + pascalAttributeName + "\");";
            }
            case "Double":
            case "double": {
                return configurationSectionVariableName + ".getDouble(\"" + pascalAttributeName + "\");";
            }
            case "Boolean":
            case "boolean": {
                return configurationSectionVariableName + ".getBoolean(\"" + pascalAttributeName + "\");";
            }
            case "Character":
            case "char": {
                return "(char) " + configurationSectionVariableName + ".getString(\"" + pascalAttributeName + "\").charAt(0);";
            }
            default: {
                return "null; //TODO ConfigurationSectionLib.java error, contact bloblib-ide developers that '" + dataType + "' is not supported";
            }
        }
    }

    /**
     * Will attempt to parse the DataType of an ObjectAttribute.
     * If it is a List, it will return the name of
     *
     * @param attribute Attribute to parse
     * @return Optional with the parsed DataType, or empty if it is not supported
     */
    public static Optional<String> parseType(ObjectAttribute attribute) {
        String dataType = attribute.getDataType();
        if (!dataType.contains("List")) {
            if (!hasQuickObjectSupport(dataType))
                return Optional.empty();
            return Optional.of(dataType);
        }
        dataType = dataType.replace("<", "");
        dataType = dataType.replace(">", "");
        dataType = dataType.replace("List", "");
        if (!isQuickListable(dataType))
            return Optional.empty();
        return Optional.of(dataType + "List");
    }

    /**
     * Will get attributes from a ConfigurationSection and have them ready to be used.
     *
     * @param attribute                        Attribute to get
     * @param configurationSectionVariableName Variable name of the ConfigurationSection
     * @param function                         Function to append to
     */
    public static void getFromConfigurationSection(ObjectAttribute attribute,
                                                   String configurationSectionVariableName,
                                                   StringBuilder function) {
        String pascalAttributeName = NamingConventions.toPascalCase(attribute.getAttributeName());
        Optional<String> parse = parseType(attribute);
        if (parse.isEmpty()) {
            String dataType = attribute.getDataType();
            if (isPrimitiveOrWrapper(dataType)) {
                function.append("    ").append(dataType).append(" ")
                        .append(attribute.getAttributeName())
                        .append(" = ").append(getPrimitiveMethod(attribute,
                                configurationSectionVariableName)).append("\n");
                return;
            }
            function.append("    ").append("Object").append(" ").append(attribute.getAttributeName())
                    .append(" = ").append(configurationSectionVariableName).append(".get(\"")
                    .append(pascalAttributeName).append("\");\n")
                    .append("    //TODO dataType has no quick parser. Reimplement this attribute yourself");
            return;
        }
        function.append("    ").append(attribute.getDataType()).append(" ")
                .append(attribute.getAttributeName()).append(" = ")
                .append(configurationSectionVariableName).append(".get")
                .append(parse.get()).append("(\"")
                .append(pascalAttributeName).append("\");\n");
    }
}

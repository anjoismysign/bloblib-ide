package com.github.anjoismysign.bloblibide.libraries;

import com.github.anjoismysign.bloblibide.configurationsection.Iterables;
import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;
import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;

import java.util.Optional;

public class ConfigurationSectionLib {

    public static String applySetMethods(ObjectAttribute attribute,
                                         String configurationSectionVariableName) {
        String dataType = attribute.getDataType();
        String attributeName = attribute.getAttributeName();
        String pascalAttributeName = NamingConventions.toPascalCase(attributeName);
        if (isQuickIterable(dataType)) {
            return configurationSectionVariableName + ".set(\"" + pascalAttributeName + "\", " + attributeName + ");";
        }
        if (isCustomQuickIterable(dataType)) {
            String serialized = "SerializationLib.serialize(\"" + attributeName + "\")";
            return configurationSectionVariableName + ".set(\"" + pascalAttributeName + "\", " + serialized + ");";
        }
        if (dataType.startsWith("Map<String, ")) {
            String valueDataType = dataType.replace("Map<String,", "");
            valueDataType = valueDataType.replace(">", "");
            return "ConfigurationSectionLib.serialize" + valueDataType + "Map(" + attributeName + ", " + configurationSectionVariableName + ", \"" + pascalAttributeName + "\");";
        }
        Optional<ConfigurationSectionAllowed> primitive = ConfigurationSectionAllowed.isPrimitiveNeedsConversion(dataType);
        if (primitive.isPresent()) {
            ConfigurationSectionAllowed allowed = primitive.get();
            String valueDataType = dataType.replace("Map<" + allowed.getDataType() + ",", "");
            valueDataType = valueDataType.replace(">", "");
            return "ConfigurationSectionLib.serialize" + valueDataType + "Map(MapLib.toStringKeys(" + attributeName + "), " + configurationSectionVariableName + ", \"" + pascalAttributeName + "\");";
        }
        Optional<ConfigurationSectionAllowed> custom = ConfigurationSectionAllowed.isCustomNeedsConversion(dataType);
        if (custom.isPresent()) {
            ConfigurationSectionAllowed allowed = custom.get();
            String allowedDataType = allowed.getDataType();
            String camelCaseAllowedDataType = NamingConventions.toCamelCase(allowedDataType);
            String valueDataType = dataType.replace("Map<" +
                    allowedDataType + ",", "");
            valueDataType = valueDataType.replace(">", "");
            return "ConfigurationSectionLib.serialize" + valueDataType +
                    "Map(MapLib." + camelCaseAllowedDataType + "ToStringKeys(" + attributeName + "), "
                    + configurationSectionVariableName + ", \"" + pascalAttributeName + "\");";
        }
        switch (dataType) {
            case "List<Vector>":
            case "List<BlockVector>":
            case "List<Location>":
            case "List<Block>":
            case "List<Color>":
            case "List<OfflinePlayer>":
            case "List<UUID>":
            case "List<BigInteger>":
            case "List<BigDecimal>": {
                dataType = dataType.replace("List<", "");
                dataType = dataType.replace(">", "");
                return "ConfigurationSectionLib.serialize" + dataType + "List(" + attributeName + ", " + configurationSectionVariableName + ", \"" + pascalAttributeName + "\");";
            }
            default: {
                return configurationSectionVariableName + ".set(\"" + pascalAttributeName + "\", " + attribute.getAttributeName() + ");\n" +
                        "            //TODO '" + dataType + "' is not supported. Implement it yourself.";
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
        if (!dataType.startsWith("List")) {
            if (dataType.startsWith("Map")) {
                dataType = dataType.replace("Map<", "");
                dataType = dataType.replace(">", "");
                String[] split = dataType.split(",");
                String key = split[0].trim();
                String value = split[1].trim();
                if (!isQuickIterable(value) && !isCustomQuickIterable(value)) {
                    return Optional.empty();
                }
                if (!isQuickIterable(key) && !isCustomQuickIterable(key)) {
                    return Optional.empty();
                }
                return Optional.of(key + "Map");
            }
            if (!hasQuickObjectSupport(dataType))
                return Optional.empty();
            return Optional.of(dataType);
        }
        dataType = dataType.replace(">", "");
        dataType = dataType.replace("List<", "");
        if (!isQuickIterable(dataType) && !isCustomQuickIterable(dataType)) {
            return Optional.empty();
        }
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
        String dataType = attribute.getDataType();
        if (parse.isEmpty()) {
            if (isPrimitiveOrWrapper(dataType)) {
                function.append("    ").append(dataType).append(" ")
                        .append(attribute.getAttributeName())
                        .append(" = ").append(Iterables.primitivesGetMethods(attribute,
                                configurationSectionVariableName)).append("\n");
                return;
            }
            function.append("    ").append("Object").append(" ").append(attribute.getAttributeName())
                    .append(" = ").append(configurationSectionVariableName).append(".get(\"")
                    .append(pascalAttributeName).append("\");\n").append("    //TODO '").append(dataType).append("' has no quick parser. Reimplement this attribute yourself\n\n");
            return;
        }
        if (dataType.startsWith("List")) {
            dataType = dataType.replace("List<", "");
            dataType = dataType.replace(">", "");
            if (isCustomQuickIterable(dataType)) {
                function.append("    ").append(attribute.getDataType()).append(" ")
                        .append(attribute.getAttributeName()).append(" = ConfigurationSectionLib.deserialize")
                        .append(dataType).append("List(").append(configurationSectionVariableName)
                        .append(", \"").append(pascalAttributeName).append("\");\n");
            } else {
                function.append("    ").append(attribute.getDataType()).append(" ")
                        .append(attribute.getAttributeName()).append(" = ")
                        .append(configurationSectionVariableName).append(".getList(\"")
                        .append(pascalAttributeName).append("\");\n")
                        .append("    //TODO '").append(dataType)
                        .append("' has no quick parser. Reimplement this attribute yourself\n\n");
            }
            return;
        }
        if (dataType.startsWith("Map")) {
            dataType = dataType.replace("Map<", "");
            dataType = dataType.replace(">", "");
            String[] split = dataType.split(",");
            ConfigurationSectionAllowed key = ConfigurationSectionAllowed.fromName(split[0]);
            ConfigurationSectionAllowed value = ConfigurationSectionAllowed.fromName(split[1]);
            if (!key.needsPrimitiveConversion() && !key.needsCustomConversion()) {
                function.append("    ").append("Object").append(" ").append(attribute.getAttributeName())
                        .append(" = ").append(configurationSectionVariableName).append(".get(\"")
                        .append(pascalAttributeName).append("\");\n").append("    //TODO '").append(dataType).append("' has no quick parser. Reimplement this attribute yourself\n\n");
                return;
            }
            if (!value.needsPrimitiveConversion() && !value.needsCustomConversion()) {
                function.append("    ").append("Object").append(" ").append(attribute.getAttributeName())
                        .append(" = ").append(configurationSectionVariableName).append(".get(\"")
                        .append(pascalAttributeName).append("\");\n").append("    //TODO '").append(dataType).append("' has no quick parser. Reimplement this attribute yourself\n\n");
                return;
            }
            String keyType = key.getDataType();
            String valueType = value.getDataType();
            if (key.needsPrimitiveConversion()) {
                if (keyType.equals("String"))
                    function.append("    ").append(attribute.getDataType()).append(" ")
                            .append(attribute.getAttributeName()).append(" = ConfigurationSectionLib.deserialize")
                            .append(valueType).append("Map(").append(configurationSectionVariableName)
                            .append(", \"").append(pascalAttributeName).append("\");\n");
                else
                    function.append("    ").append(attribute.getDataType()).append(" ")
                            .append(attribute.getAttributeName()).append(" = MapLib.to")
                            .append(keyType).append("Keys(")
                            .append("ConfigurationSectionLib.deserialize")
                            .append(valueType).append("Map(").append(configurationSectionVariableName)
                            .append(", \"").append(pascalAttributeName).append("\"));\n");
                return;
            }
            if (key.needsCustomConversion()) {
                function.append("    ").append(attribute.getDataType()).append(" ")
                        .append(attribute.getAttributeName()).append(" = MapLib.to").append(keyType).append("Keys(ConfigurationSectionLib.deserialize")
                        .append(valueType).append("Map(").append(configurationSectionVariableName)
                        .append(", \"").append(pascalAttributeName).append("\"));\n");
                return;
            }
            function.append("    ").append("Object").append(" ").append(attribute.getAttributeName())
                    .append(" = ").append(configurationSectionVariableName).append(".get(\"")
                    .append(pascalAttributeName).append("\");\n").append("    //TODO '").append(dataType)
                    .append("' has no quick parser. Reimplement this attribute yourself\n\n");
        }
    }

    /**
     * Will convert BigInteger and BigDecimal to decimal String value before saving to the ConfigurationSection,
     * so it can be read back in as a BigInteger or BigDecimal by parsing the String value.
     *
     * @param attribute                        Attribute to save
     * @param configurationSectionVariableName Variable name of the ConfigurationSection
     * @param saveToFile                       Function to append to
     */
    public static void saveToConfigurationSection(ObjectAttribute attribute,
                                                  String configurationSectionVariableName,
                                                  StringBuilder saveToFile) {
        String applySetMethods = applySetMethods(attribute, configurationSectionVariableName);
        saveToFile.append("    ").append(applySetMethods).append("\n");
    }
}

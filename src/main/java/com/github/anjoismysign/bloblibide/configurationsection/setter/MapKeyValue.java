package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

public class MapKeyValue {

    public static String apply(String dataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName,
                               String key, String value) {
        ConfigurationSectionAllowed keyAllowed = ConfigurationSectionAllowed.fromName(key);
        if (keyAllowed == null)
            return "null; //TODO '" + dataType + "' is not supported. Implement it yourself.";
        if (value.startsWith("List<")) {
            value = value.replace("List<", "")
                    .replace(">", "");
            return MapKeyListValue.
                    apply(dataType, configurationSectionVariableName,
                            pascalAttributeName, attributeName, keyAllowed, value);
        }
        ConfigurationSectionAllowed valueAllowed = ConfigurationSectionAllowed.fromName(value);
        if (valueAllowed == null)
            return "null; //TODO '" + dataType + "' is not supported. Implement it yourself.";
        //assert valueAllowed is quick supported and not inside a list
        if (!keyAllowed.needsShapeConversion()) {
            return noShapeConversion(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName, keyAllowed, value);
        }
        // value needs shape conversion
        return shapeConversion(dataType, configurationSectionVariableName,
                pascalAttributeName, attributeName, keyAllowed, value);
    }

    private static String noShapeConversion(String dataType, String configurationSectionVariableName,
                                            String pascalAttributeName, String attributeName,
                                            ConfigurationSectionAllowed keyAllowed, String value) {
        //todo: revisar si value tiene shape conversion
        ConfigurationSectionAllowed valueAllowed = ConfigurationSectionAllowed.fromName(value);
        if (valueAllowed == null)
            return "null; //TODO '" + dataType + "' is not supported. Implement it yourself.";
        String listType = valueAllowed.getDataType();
        String keyType = keyAllowed.getDataType();
        if (!valueAllowed.needsShapeConversion()) {
            // key uses MapLib.
            // value uses ConfigurationSectionLib.
            // value is not a list
            // -> since it's a Map, we use ConfigurationSectionLib methods
            return "MapLib.to" + keyType + "Keys(ConfigurationSectionLib.serialize" +
                    listType + "Map(" +
                    attributeName + ", " + configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\"));";
        }
        // key uses MapLib.
        // value needs shape conversion
        return "MapLib.to" + keyType + "Keys(" + listType + "Shape.serialize" + listType + "Map(" +
                attributeName + ", " + configurationSectionVariableName + ", \"" +
                pascalAttributeName + "\"));";
    }

    private static String shapeConversion(String dataType, String configurationSectionVariableName,
                                          String pascalAttributeName, String attributeName,
                                          ConfigurationSectionAllowed keyAllowed, String value) {
        //todo: revisar si value tiene shape conversion
        ConfigurationSectionAllowed valueAllowed = ConfigurationSectionAllowed.fromName(value);
        if (valueAllowed == null)
            return "null; //TODO '" + dataType + "' is not supported. Implement it yourself.";
        String listType = valueAllowed.getDataType();
        String keyType = keyAllowed.getDataType();
        if (!valueAllowed.needsShapeConversion()) {
            // key uses Shape conversion.
            // value uses ConfigurationSectionLib.
            // value is not a list
            // -> since it's a Map, we use ConfigurationSectionLib methods
            return keyType + "Shape.to" + keyType + "Keys(ConfigurationSectionLib.serialize" +
                    listType + "Map(" +
                    attributeName + ", " + configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\"));";
        }
        // key uses ShapeConversion.
        // value needs shape conversion
        return keyType + "Shape.to" + keyType + "Keys(" + listType + "Shape.serialize" + listType + "Map(" +
                attributeName + ", " + configurationSectionVariableName + ", \"" +
                pascalAttributeName + "\"));";
    }
}

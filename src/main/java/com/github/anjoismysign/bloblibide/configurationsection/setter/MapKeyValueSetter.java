package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;

public class MapKeyValueSetter {

    public static String apply(String dataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName,
                               String key, String value) {
        ConfigurationSectionAllowed keyAllowed = ConfigurationSectionAllowed.fromName(key);
        if (keyAllowed == null)
            return "//TODO '" + dataType + "' is not supported. Implement it yourself.";
        if (value.startsWith("List<")) {
            value = value.replace("List<", "")
                    .replace(">", "");
            return MapKeyListValueSetter.
                    apply(dataType, configurationSectionVariableName,
                            pascalAttributeName, attributeName, keyAllowed, value);
        }
        ConfigurationSectionAllowed valueAllowed = ConfigurationSectionAllowed.fromName(value);
        if (valueAllowed == null)
            return "//TODO '" + dataType + "' is not supported. Implement it yourself.";
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
                                            ConfigurationSectionAllowed key, String value) {
        ConfigurationSectionAllowed valueAllowed = ConfigurationSectionAllowed.fromName(value);
        if (valueAllowed == null)
            return "//TODO '" + dataType + "' is not supported. Implement it yourself.";
        String listType = valueAllowed.getDataType();
        String camel = NamingConventions.toCamelCase(key.getDataType());
        if (!valueAllowed.needsShapeConversion()) {
            // key uses MapLib.
            // value uses ConfigurationSectionLib.
            // value is not a list
            // -> since it's a Map, we use ConfigurationSectionLib methods
            if (key.hasSafeToStringConversion()) {
                return "ConfigurationSectionLib.serialize" + listType + "Map(MapLib.toStringKeys("
                        + attributeName + "), " + configurationSectionVariableName
                        + ", \"" + pascalAttributeName + "\");";
            } else {
                return "ConfigurationSectionLib.serialize" + listType + "Map(MapLib." +
                        camel + "toStringKeys("
                        + attributeName + "), " + configurationSectionVariableName
                        + ", \"" + pascalAttributeName + "\");";
            }

//            return "MapLib.to" + keyType + "Keys(ConfigurationSectionLib.serialize" +
//                    listType + "Map(" +
//                    attributeName + ", " + configurationSectionVariableName + ", \"" +
//                    pascalAttributeName + "\"));";
        }
        // key uses MapLib.
        // value needs shape conversion
        if (key.hasSafeToStringConversion()) {
            return listType + "Shape.serialize" + listType + "Map(MapLib.toStringKeys(" + attributeName + "), " + configurationSectionVariableName
                    + ", \"" + pascalAttributeName + "\");";
        } else {
            return listType + "Shape.serialize" + listType + "Map(MapLib." + camel +
                    "toStringKeys(" + attributeName + "), " + configurationSectionVariableName
                    + ", \"" + pascalAttributeName + "\");";
        }

//        return "MapLib.to" + keyType + "Keys(" + listType + "Shape.serialize" + listType + "Map(" +
//                attributeName + ", " + configurationSectionVariableName + ", \"" +
//                pascalAttributeName + "\"));";
    }

    private static String shapeConversion(String dataType, String configurationSectionVariableName,
                                          String pascalAttributeName, String attributeName,
                                          ConfigurationSectionAllowed keyAllowed, String value) {
        ConfigurationSectionAllowed valueAllowed = ConfigurationSectionAllowed.fromName(value);
        if (valueAllowed == null)
            return "//TODO '" + dataType + "' is not supported. Implement it yourself.";
        String listType = valueAllowed.getDataType();
        String keyType = keyAllowed.getDataType();
        if (!valueAllowed.needsShapeConversion()) {
            // key uses Shape conversion.
            // value uses ConfigurationSectionLib.
            // value is not a list
            // -> since it's a Map, we use ConfigurationSectionLib methods
            return "ConfigurationSectionLib.serialize" + listType + "Map(" + keyType +
                    "Shape.toStringKeys(" + attributeName + "), " +
                    configurationSectionVariableName + ", \"" + pascalAttributeName + "\");";

//            return keyType + "Shape.to" + keyType + "Keys(ConfigurationSectionLib.serialize" +
//                    listType + "Map(" +
//                    attributeName + ", " + configurationSectionVariableName + ", \"" +
//                    pascalAttributeName + "\"));";
        }
        // key uses ShapeConversion.
        // value needs shape conversion
        return listType + "Shape.serialize" + listType + "Map(" + keyType + "Shape.toStringKeys("
                + attributeName + "), " + configurationSectionVariableName + ", \""
                + pascalAttributeName + "\");";

//        return keyType + "Shape.to" + keyType + "Keys(" + listType + "Shape.serialize" +
//                listType + "Map(" + attributeName + ", " + configurationSectionVariableName +
//                ", \"" + pascalAttributeName + "\"));";
    }
}

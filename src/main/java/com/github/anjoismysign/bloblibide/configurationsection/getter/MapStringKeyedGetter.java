package com.github.anjoismysign.bloblibide.configurationsection.getter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

public class MapStringKeyedGetter {

    public static String apply(String dataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName) {
        String valueDataType = dataType.replace("Map<String,", "");
        valueDataType = valueDataType.replace(">", "");
        if (valueDataType.startsWith("List<")) {
            valueDataType = valueDataType.replace("List<", "");
            valueDataType = valueDataType.replace(">", "");
            return MapStringKeyedListValuedGetter.apply(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName, valueDataType);
        }
        ConfigurationSectionAllowed allowed = ConfigurationSectionAllowed.fromName(dataType);
        if (allowed == null) {
            return "null; //TODO '" + dataType + "' is not supported. Implement it yourself.";
        }
        if (!allowed.needsShapeConversion()) {
            // does not need shape conversion
            return "ConfigurationSectionLib.deserialize" + valueDataType + "Map(" +
                    configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\");";
        }
        // does need custom conversion
        return valueDataType + "Shape.deserialize" + valueDataType + "Map(" +
                configurationSectionVariableName + ", \"" +
                pascalAttributeName + "\");";
    }
}

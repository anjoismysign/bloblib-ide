package com.github.anjoismysign.bloblibide.configurationsection.getter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

public class MapStringKeyedListValuedGetter {

    public static String apply(String dataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName,
                               String listType) {
        ConfigurationSectionAllowed valueAllowed = ConfigurationSectionAllowed.fromName(listType);
        if (valueAllowed == null)
            return "null; //TODO '" + dataType + "' is not supported. Implement it yourself.";
        if (!valueAllowed.needsShapeConversion()) {
            // does not need shape conversion
            return "ConfigurationSectionLib.deserialize" + listType + "ListMap(" +
                    configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\");";
        }
        // needs shape conversion
        return listType + "Shape.deserialize" + listType + "ListMap(" +
                configurationSectionVariableName + ", \"" +
                pascalAttributeName + "\");";
    }
}

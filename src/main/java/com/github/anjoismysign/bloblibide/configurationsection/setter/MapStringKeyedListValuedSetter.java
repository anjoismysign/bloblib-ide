package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

public class MapStringKeyedListValuedSetter {

    public static String apply(String dataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName,
                               String listType) {
        ConfigurationSectionAllowed valueAllowed = ConfigurationSectionAllowed.fromName(listType);
        if (valueAllowed == null)
            return "//TODO '" + dataType + "' is not supported. Implement it yourself.";
        if (!valueAllowed.needsShapeConversion()) {
            // does not need shape conversion
            return "ConfigurationSectionLib.serialize" + listType + "ListMap(" +
                    attributeName + ", " + configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\");";
        }
        // needs shape conversion
        return listType + "Shape.serialize" + listType + "ListMap(" +
                attributeName + ", " + configurationSectionVariableName + ", \"" +
                pascalAttributeName + "\");";
    }
}

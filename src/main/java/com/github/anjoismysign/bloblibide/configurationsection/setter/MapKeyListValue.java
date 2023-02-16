package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

public class MapKeyListValue {

    public static String apply(String dataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName,
                               ConfigurationSectionAllowed keyAllowed, String value) {
        ConfigurationSectionAllowed valueAllowed = ConfigurationSectionAllowed.fromName(value);
        if (valueAllowed == null)
            return "null; //TODO '" + dataType + "' is not supported. Implement it yourself.";
        if (!keyAllowed.needsShapeConversion()) {
            // key does not need shape conversion
            // since it's a Map, we use ConfigurationSectionLib methods
            return MapCustomKeyListValue.apply(configurationSectionVariableName,
                    pascalAttributeName, attributeName, keyAllowed, valueAllowed);
        }
        // key needs shape conversion
        return MapShapeKeyListValue.apply(configurationSectionVariableName,
                pascalAttributeName, attributeName, keyAllowed, valueAllowed);
    }
}

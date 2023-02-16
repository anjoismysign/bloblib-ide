package com.github.anjoismysign.bloblibide.configurationsection.getter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

public class MapKeyListValueGetter {

    public static String apply(String dataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName,
                               ConfigurationSectionAllowed keyAllowed, String value) {
        ConfigurationSectionAllowed valueAllowed = ConfigurationSectionAllowed.fromName(value);
        if (valueAllowed == null)
            return "null; //TODO '" + dataType + "' is not supported. Implement it yourself.";
        if (!keyAllowed.needsShapeConversion()) {
            // key does not need shape conversion
            // since it's a Map, we use ConfigurationSectionLib methods
            return MapCustomKeyListValueGetter.apply(configurationSectionVariableName,
                    pascalAttributeName, attributeName, keyAllowed, valueAllowed);
        }
        // key needs shape conversion
        return MapShapeKeyListValueGetter.apply(configurationSectionVariableName,
                pascalAttributeName, attributeName, keyAllowed, valueAllowed);
    }
}

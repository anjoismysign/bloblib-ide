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
            return MapKeyListValue.apply(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName, keyAllowed, value);
        }
        ConfigurationSectionAllowed allowed = ConfigurationSectionAllowed.fromName(dataType);
        if (allowed == null) {
            return "null; //TODO '" + dataType + "' is not supported. Implement it yourself.";
        }
        if (!allowed.needsCustomConversion()) {
            // does not need custom conversion
            return "ConfigurationSectionLib.serialize" + valueDataType + "Map(" +
                    attributeName + ", " + configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\");";
        }
        // does need custom conversion
        return valueDataType + "Shape.serialize" + valueDataType + "Map(" +
                attributeName + ", " + configurationSectionVariableName + ", \"" +
                pascalAttributeName + "\");";
    }
}

package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

public class MapCustom {

    public static String apply(String dataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName) {
        String mapDataType = dataType.replace("Map<", "").replace(">", "");
        String[] split = mapDataType.split(",");
        String key = split[0].trim();
        String value = split[1].trim();
        ConfigurationSectionAllowed allowed = ConfigurationSectionAllowed.fromName(key);
        if (allowed == null)
            return "null; //TODO '" + dataType + "' is not supported. Implement it yourself.";
        return MapKeyValue.apply(dataType, configurationSectionVariableName,
                pascalAttributeName, attributeName, key, value);
    }
}

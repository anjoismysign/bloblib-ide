package com.github.anjoismysign.bloblibide.configurationsection.setter;

public class MapSetter {

    public static String apply(String dataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName) {
        String mapDataType = dataType.replace("Map<", "").replace(">", "");
        String[] split = mapDataType.split(",");
        String key = split[0].trim();
        String value = split[1].trim();
        return MapKeyValue.apply(dataType, configurationSectionVariableName,
                pascalAttributeName, attributeName, key, value);
    }
}

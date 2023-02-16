package com.github.anjoismysign.bloblibide.configurationsection.getter;

public class MapGetter {

    public static String apply(String dataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName) {
        String mapDataType = dataType.replace("Map<", "").replace(">", "");
        String[] split = mapDataType.split(",");
        String key = split[0].trim();
        String value = split[1].trim();
        return MapKeyValueGetter.apply(dataType, configurationSectionVariableName,
                pascalAttributeName, attributeName, key, value);
    }
}

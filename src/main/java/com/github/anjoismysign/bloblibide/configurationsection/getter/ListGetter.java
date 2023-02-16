package com.github.anjoismysign.bloblibide.configurationsection.getter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

public class ListGetter {

    public static String apply(String originalDataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName) {
        String dataType = originalDataType.replace("List<", "");
        dataType = dataType.substring(0, dataType.length() - 1);
        ConfigurationSectionAllowed allowed = ConfigurationSectionAllowed.fromName(dataType);
        if (allowed.needsPrimitiveConversion()) {
            return configurationSectionVariableName + ".get" +
                    dataType + "List(\"" +
                    pascalAttributeName + "\");";
        }
        if (allowed.needsCustomConversion()) {
            return "ConfigurationSectionLib.deserialize" + dataType + "List(" +
                    configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\");";
        }
        if (allowed.needsShapeConversion()) {
            return dataType + "Shape.deserialize" + dataType + "List(" +
                    configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\");";
        }
        return "null; //TODO '" + originalDataType + "' is not supported. Implement it yourself.";
    }
}

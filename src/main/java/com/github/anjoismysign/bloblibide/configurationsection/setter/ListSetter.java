package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

public class ListSetter {

    public static String apply(String originalDataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName) {
        String dataType = originalDataType.replace("List<", "");
        dataType = dataType.substring(0, dataType.length() - 1);
        ConfigurationSectionAllowed allowed = ConfigurationSectionAllowed.fromName(dataType);
        if (allowed.needsPrimitiveConversion()) {
            return configurationSectionVariableName + ".set(\"" +
                    pascalAttributeName + "\", " +
                    attributeName + ");";

        }
        if (allowed.needsCustomConversion()) {
            return "ConfigurationSectionLib.serialize" + dataType + "List(" +
                    attributeName + ", " +
                    configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\");";
        }
        if (allowed.needsShapeConversion()) {
            return dataType + "Shape.serialize" + dataType + "List(" +
                    attributeName + ", " +
                    configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\");";
        }
        return "//TODO '" + originalDataType + "' is not supported. Implement it yourself.";
    }
}

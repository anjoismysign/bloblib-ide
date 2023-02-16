package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

import java.util.Optional;

public class ListSetter {

    public static String apply(String originalDataType, String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName) {
        String dataType = originalDataType.replace("List<", "");
        Optional<ConfigurationSectionAllowed> optional = ConfigurationSectionAllowed.isPrimitiveNeedsConversion(dataType);
        if (optional.isPresent()) {
            ConfigurationSectionAllowed allowed = optional.get();
            return configurationSectionVariableName + ".set(\"" +
                    pascalAttributeName + "\", " +
                    attributeName + ");";

        }
        optional = ConfigurationSectionAllowed.isCustomNeedsConversion(dataType);
        if (optional.isPresent()) {
            ConfigurationSectionAllowed allowed = optional.get();
            dataType = allowed.getDataType();
            StringBuilder function = new StringBuilder();
            function.append("ConfigurationSectionLib.serialize")
                    .append(dataType).append("List(").append(attributeName)
                    .append(", ")
                    .append(configurationSectionVariableName)
                    .append(", \"").append(pascalAttributeName).append("\");\n");
        }
        optional = ConfigurationSectionAllowed.isShapeNeedsConversion(dataType);
        if (optional.isPresent()) {
            ConfigurationSectionAllowed allowed = optional.get();
            dataType = allowed.getDataType();
            return dataType + "Shape.serialize" + dataType + "List(" +
                    attributeName + ", " +
                    configurationSectionVariableName + ", \"" +
                    pascalAttributeName + "\");";
        }
        return "null; //TODO '" + originalDataType + "' is not supported. Implement it yourself.";
    }
}

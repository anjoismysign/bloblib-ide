package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.configurationsection.Iterables;
import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;

public class Setter {

    public static String apply(ObjectAttribute attribute,
                               String configurationSectionVariableName) {
        String dataType = attribute.getDataType();
        String attributeName = attribute.getAttributeName();
        String pascalAttributeName = NamingConventions.toPascalCase(attributeName);
        if (Iterables.isQuickIterable(dataType)) {
            return QuickIterable.apply(configurationSectionVariableName, pascalAttributeName,
                    attributeName);
        }
        if (Iterables.isCustomQuickIterable(dataType)) {
            return CustomQuickIterable.apply(configurationSectionVariableName,
                    pascalAttributeName, attributeName);
        }
        if (dataType.startsWith("Map<String, ")) {
            return MapStringKeyed.apply(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName);
        }
        if (dataType.startsWith("Map<")) {
            return MapCustom.apply(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName);
        }
        switch (dataType) {
            case "List<Vector>":
            case "List<BlockVector>":
            case "List<Location>":
            case "List<Block>":
            case "List<Color>":
            case "List<OfflinePlayer>":
            case "List<UUID>":
            case "List<BigInteger>":
            case "List<BigDecimal>": {
                dataType = dataType.replace("List<", "");
                dataType = dataType.replace(">", "");
                return "ConfigurationSectionLib.serialize" + dataType + "List(" + attributeName + ", " + configurationSectionVariableName + ", \"" + pascalAttributeName + "\");";
            }
            default: {
                return configurationSectionVariableName + ".set(\"" + pascalAttributeName + "\", " + attribute.getAttributeName() + ");\n" +
                        "            //TODO '" + dataType + "' is not supported. Implement it yourself.";
            }
        }
    }
}

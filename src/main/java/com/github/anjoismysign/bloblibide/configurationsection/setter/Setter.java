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
            return QuickIterableSetter.apply(configurationSectionVariableName, pascalAttributeName,
                    attributeName);
        }
        if (Iterables.isCustomQuickIterable(dataType)) {
            return CustomQuickIterableSetter.apply(configurationSectionVariableName,
                    pascalAttributeName, attributeName);
        }
        if (dataType.startsWith("Map<String, ")) {
            return MapStringKeyedSetter.apply(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName);
        }
        if (dataType.startsWith("Map<")) {
            return MapSetter.apply(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName);
        }
        if (dataType.startsWith("List<")) {
            return ListSetter.apply(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName);
        }
        //it's shape
        return "SerializationLib.deserialize" + dataType + "(" +
                configurationSectionVariableName + ".get(\"" + pascalAttributeName + "\"));";
    }
}

package com.github.anjoismysign.bloblibide.configurationsection.getter;

import com.github.anjoismysign.bloblibide.configurationsection.Iterables;
import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;

public class Getter {

    public static String apply(ObjectAttribute attribute,
                               String configurationSectionVariableName) {
        String dataType = attribute.getDataType();
        String attributeName = attribute.getAttributeName();
        String pascalAttributeName = NamingConventions.toPascalCase(attributeName);
        if (Iterables.isQuickIterable(dataType)) {
            return Iterables.primitivesGetMethods(attribute,
                    configurationSectionVariableName);
        }
        if (Iterables.isCustomQuickIterable(dataType)) {
            return CustomQuickIterableGetter.apply(configurationSectionVariableName,
                    pascalAttributeName, dataType);
        }
        if (dataType.startsWith("Map<String, ")) {
            return MapStringKeyedGetter.apply(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName);
        }
        if (dataType.startsWith("Map<")) {
            return MapGetter.apply(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName);
        }
        if (dataType.startsWith("List<")) {
            return ListGetter.apply(dataType, configurationSectionVariableName,
                    pascalAttributeName, attributeName);
        }
        //it's shape
        return "SerializationLib.deserialize" + dataType + "(" +
                configurationSectionVariableName + ".get(\"" + pascalAttributeName + "\"));";
    }
}

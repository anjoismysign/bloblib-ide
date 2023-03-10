package com.github.anjoismysign.bloblibide.configurationsection.getter;

import com.github.anjoismysign.bloblibide.configurationsection.Iterables;
import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;
import com.github.anjoismysign.bloblibide.libraries.DataTypeLib;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;

import java.util.Optional;

public class Getter {

    public static void getFromConfigurationSection(ObjectAttribute attribute,
                                                   String configurationSectionVariableName,
                                                   StringBuilder function) {
        String applyGetMethods = apply(attribute, configurationSectionVariableName);
        function.append("    ").append(attribute.getDataType()).append(" ")
                .append(attribute.getAttributeName()).append(" = ")
                .append(applyGetMethods).append("\n");
    }

    public static String apply(ObjectAttribute attribute, String configurationSectionVariableName) {
        String getter = getter(attribute, configurationSectionVariableName);
        if (!getter.endsWith(";"))
            getter = getter + ";";
        return getter;
    }

    private static String getter(ObjectAttribute attribute,
                                 String configurationSectionVariableName) {
        String dataType = attribute.getDataType();
        String attributeName = attribute.getAttributeName();
        String pascalAttributeName = NamingConventions.toPascalCase(attributeName);
        Optional<String> wrapper = DataTypeLib.primitiveToWrapper(dataType);
        if (wrapper.isPresent() && Iterables.isQuickIterable(wrapper.get())) {
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
                configurationSectionVariableName + ".getString(\"" + pascalAttributeName + "\"));";
    }
}

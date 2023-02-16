package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.configurationsection.Iterables;
import com.github.anjoismysign.bloblibide.entities.ObjectAttribute;
import com.github.anjoismysign.bloblibide.libraries.DataTypeLib;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;

import java.util.Optional;

public class Setter {

    public static void saveToConfigurationSection(ObjectAttribute attribute,
                                                  String configurationSectionVariableName,
                                                  StringBuilder saveToFile) {
        String applySetMethods = apply(attribute, configurationSectionVariableName);
        saveToFile.append("    ").append(applySetMethods).append("\n");
    }

    public static String apply(ObjectAttribute attribute, String configurationSectionVariableName) {
        String setter = setter(attribute, configurationSectionVariableName);
        if (!setter.endsWith(";"))
            setter = setter + ";";
        return setter;
    }

    private static String setter(ObjectAttribute attribute,
                                 String configurationSectionVariableName) {
        String dataType = attribute.getDataType();
        String attributeName = attribute.getAttributeName();
        String pascalAttributeName = NamingConventions.toPascalCase(attributeName);
        Optional<String> wrapper = DataTypeLib.primitiveToWrapper(dataType);
        if (wrapper.isPresent() && Iterables.isQuickIterable(wrapper.get())) {
            return QuickIterableSetter.apply(configurationSectionVariableName, pascalAttributeName,
                    attributeName);
        }
        if (Iterables.isCustomQuickIterable(dataType)) {
            return CustomQuickIterableSetter.apply(attributeName,
                    configurationSectionVariableName, pascalAttributeName);
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
        return configurationSectionVariableName + ".set(\"" + pascalAttributeName + "\", " +
                "SerializationLib.serialize(" + attributeName + "));";
    }
}

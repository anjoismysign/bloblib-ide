package com.github.anjoismysign.bloblibide.configurationsection.setter;

public class CustomQuickIterable {

    public static String apply(String attributeName, String configurationSectionVariableName, String pascalAttributeName) {
        String serialized = "SerializationLib.serialize(\"" + attributeName + "\")";
        return configurationSectionVariableName + ".set(\"" + pascalAttributeName + "\", " + serialized + ");";
    }
}

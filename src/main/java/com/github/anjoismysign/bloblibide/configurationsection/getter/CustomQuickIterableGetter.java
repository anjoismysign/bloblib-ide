package com.github.anjoismysign.bloblibide.configurationsection.getter;

public class CustomQuickIterableGetter {

    public static String apply(String configurationSectionVariableName,
                               String pascalAttributeName, String dataType) {
        return "SerializationLib.deserialize" + dataType + "(" +
                configurationSectionVariableName + ".getString(\"" + pascalAttributeName + "\"));";
    }
}

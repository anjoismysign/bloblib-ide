package com.github.anjoismysign.bloblibide.configurationsection.setter;

public class QuickIterable {

    public static String apply(String configurationSectionVariableName, String pascalAttributeName, String attributeName) {
        return configurationSectionVariableName + ".set(\"" + pascalAttributeName + "\", " + attributeName + ");";
    }
}

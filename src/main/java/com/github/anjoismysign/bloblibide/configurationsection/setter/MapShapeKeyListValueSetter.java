package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;

public class MapShapeKeyListValueSetter {

    public static String apply(String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName,
                               ConfigurationSectionAllowed keyAllowed,
                               ConfigurationSectionAllowed valueAllowed) {
        String listType = valueAllowed.getDataType();
        String keyType = keyAllowed.getDataType();
        if (!valueAllowed.needsShapeConversion()) {
            // key needs shape conversion, but
            // value does not need shape conversion
            return "ConfigurationSectionLib.serialize" + listType + "ListMap(" +
                    keyType + "Shape.toStringKeys(" + attributeName + "), " +
                    configurationSectionVariableName + ", \"" + pascalAttributeName + "\");";
//            return keyType + "Shape.to" + keyType + "Keys(ConfigurationSectionLib.serialize" +
//                    listType + "ListMap(" +
//                    attributeName + ", " + configurationSectionVariableName + ", \"" +
//                    pascalAttributeName + "\"));";
        }
        // key needs shape conversion,
        // value needs shape conversion
        return listType + "Shape.serialize" + listType + "ListMap(" +
                keyType + "Shape.toStringKeys(" + attributeName + "), " +
                configurationSectionVariableName + ", \"" + pascalAttributeName + "\");";
//        return keyType + "Shape.to" + keyType + "Keys(" + listType + "Shape.serialize" + listType + "ListMap(" +
//                attributeName + ", " + configurationSectionVariableName + ", \"" +
//                pascalAttributeName + "\"));";
    }
}

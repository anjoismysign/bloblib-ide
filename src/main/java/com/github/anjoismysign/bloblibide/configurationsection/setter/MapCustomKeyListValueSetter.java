package com.github.anjoismysign.bloblibide.configurationsection.setter;

import com.github.anjoismysign.bloblibide.entities.ConfigurationSectionAllowed;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;

public class MapCustomKeyListValueSetter {

    public static String apply(String configurationSectionVariableName,
                               String pascalAttributeName, String attributeName,
                               ConfigurationSectionAllowed key,
                               ConfigurationSectionAllowed value) {
        String listType = value.getDataType();
//        String keyType = key.getDataType();
        if (!value.needsShapeConversion()) {
            // key and value do not need shape conversion
            String camel = NamingConventions.toCamelCase(key.getDataType());
            return "ConfigurationSectionLib.serialize" + listType + "ListMap(" +
                    "MapLib." + camel + "toStringKeys(" + attributeName + "), " +
                    configurationSectionVariableName + ", \"" + pascalAttributeName + "\");";
//            return "MapLib.to" + keyType + "Keys(ConfigurationSectionLib.serialize" +
//                    listType + "ListMap(" +
//                    attributeName + ", " + configurationSectionVariableName + ", \"" +
//                    pascalAttributeName + "\"));";
        }
        // value needs shape conversion
        String camel = NamingConventions.toCamelCase(key.getDataType());
        return listType + "Shape.serialize" + listType + "ListMap(" +
                "MapLib." + camel + "toStringKeys(" + attributeName + "), " +
                configurationSectionVariableName + ", \"" + pascalAttributeName + "\");";
//        return "MapLib.to" + keyType + "Keys(" + listType + "Shape.serialize" + listType + "ListMap(" +
//                attributeName + ", " + configurationSectionVariableName + ", \"" +
//                pascalAttributeName + "\"));";
    }
}

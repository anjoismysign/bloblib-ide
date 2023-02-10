package com.github.anjoismysign.bloblibide.entities;

import com.github.anjoismysign.bloblibide.libraries.NamingConventions;

public class ObjectAttribute {
    private final String dataType, attributeName;

    public static ObjectAttribute parse(String input) {
        String[] split = input.split(" ");
        return new ObjectAttribute(split[0], split[1]);
    }

    public ObjectAttribute(String dataType, String attributeName) {
        this.dataType = dataType;
        this.attributeName = attributeName;
    }

    public String getDataType() {
        return dataType;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String encapsulate() {
        return "private " + dataType + " " + attributeName + ";\n";
    }

    /**
     * Will initialize the attribute to a new instance of the data type.
     * NOTE! Will use default constructor!
     * Example for ArrayList&lt;String&gt;:
     * <p>
     * this.list = new ArrayList&lt;String&gt;();
     *
     * @return The initialization code.
     */
    public String initialize() {
        return "this." + attributeName + " = new " + dataType + "();\n";
    }

    public String getter() {
        String pascal = NamingConventions.toPascalCase(attributeName);
        return "public " + dataType + " get" + pascal + "() {\n" +
                "    return " + attributeName + ";\n" +
                "}\n\n";
    }

    public String setter() {
        String pascal = NamingConventions.toPascalCase(attributeName);
        return "public void set" + pascal + "(" + dataType + " " + attributeName + ") {\n" +
                "    this." + attributeName + " = " + attributeName + ";\n" +
                "}\n\n";
    }
}

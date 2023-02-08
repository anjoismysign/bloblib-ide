package com.github.anjoismysign.bloblibide.entities;

public class ObjectAttribute {
    private final String dataType, name;

    public static ObjectAttribute parse(String input) {
        String[] split = input.split(" ");
        return new ObjectAttribute(split[0], split[1]);
    }

    public ObjectAttribute(String dataType, String name) {
        this.dataType = dataType;
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getName() {
        return name;
    }
}

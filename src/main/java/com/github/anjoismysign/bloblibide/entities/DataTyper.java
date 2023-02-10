package com.github.anjoismysign.bloblibide.entities;

import com.github.anjoismysign.bloblibide.libraries.NamingConventions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataTyper extends HashMap<String, List<String>> {

    private boolean includesList = false;

    /**
     * Creates a DataTyper from a raw string
     * Will be able to read using the
     * following format:
     * Split dataTypes by ';'.
     * Split attribute names by ','.
     * Between the dataType and attribute name/s
     * there should be an empty space.
     * It is allowed to add spaces before the
     * dataType and after the attribute name/s.
     * The whitespace will be trimmed.
     * Example:
     * <pre>
     * String name,lastname ; int age ; double income,expenses
     * String name,lastname;int age;double income,expenses
     * </pre>
     *
     * @param raw The raw string
     * @return The DataTyper
     */
    public static DataTyper fromRaw(String raw) {
        DataTyper dataTyper = new DataTyper();
        String[] split = raw.split(";");
        for (String s : split) {
            s = s.trim();
            dataTyper.parseDataType(s);
        }
        return dataTyper;
    }

    public DataTyper() {
        super();
    }

    public void parseDataType(String input) {
        String[] split = input.split(" ");
        if (split.length != 2)
            throw new IllegalArgumentException("Invalid input");
        String dataType = split[0];
        String[] names = split[1].split(",");
        if (names.length == 1)
            add(dataType, names[0]);
        else
            for (String name : names)
                add(dataType, name);
    }

    /**
     * Will add an attribute to the DataTyper.
     *
     * @param dataType The data type of the attribute
     * @param name     The name of the attribute.
     *                 Will be converted to camelCase.
     */
    public void add(String dataType, String name) {
        if (dataType.startsWith("List<")) {
            includesList = true;
        }
        name = NamingConventions.toCamelCase(name);
        if (this.containsKey(dataType)) {
            this.get(dataType).add(name);
        } else {
            List<String> list = new ArrayList<>();
            list.add(name);
            this.put(dataType, list);
        }
    }

    public List<ObjectAttribute> listAttributes() {
        List<ObjectAttribute> list = new ArrayList<>();
        keySet().forEach(dataType -> {
            List<String> names = get(dataType);
            names.forEach(name -> list.add(new ObjectAttribute(dataType, name)));
        });
        return list;
    }

    public List<String> encapsulate() {
        List<String> list = new ArrayList<>();
        keySet().forEach(dataType -> {
            List<String> names = get(dataType);
            list.add("private " + dataType + " " + String.join(", ", names) + ";\n");
        });
        return list;
    }

    public String getDependencyInjection() {
        StringBuilder builder = new StringBuilder();
        listAttributes().forEach(attribute ->
                builder.append(attribute.getDataType()).append(" ").append(attribute.getAttributeName()).append(","));
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public String injectDependency() {
        StringBuilder builder = new StringBuilder();
        listAttributes().forEach(attribute -> {
            String attributeName = attribute.getAttributeName();
            builder.append("this.").append(attributeName).append(" = ")
                    .append(attributeName).append(";\n");
        });
        return builder.toString();
    }

    public boolean includesList() {
        return includesList;
    }
}

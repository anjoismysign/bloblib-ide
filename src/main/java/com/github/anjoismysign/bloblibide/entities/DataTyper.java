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
     * Split dataTypes by ';' semicolon.
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

    /**
     * Will parse a single dataType and add it to the DataTyper.
     * It allows for multiple attributes to be added at once.
     * DataType and attributes are separated by an empty space.
     * Attributes are separated by a comma.
     * Example:
     * <pre>
     *     String name,lastname
     *     int age
     *     List&lt;String&gt; names
     *     </pre>
     *
     * @param input The input string
     */
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

    /**
     * Will list all ObjectAttributes inside
     * the DataTyper.
     *
     * @return The list of ObjectAttributes
     */
    public List<ObjectAttribute> listAttributes() {
        List<ObjectAttribute> list = new ArrayList<>();
        keySet().forEach(dataType -> {
            List<String> names = get(dataType);
            names.forEach(name -> list.add(new ObjectAttribute(dataType, name)));
        });
        return list;
    }

    /**
     * Will encapsulate attributes giving an output
     * such as:
     * <pre>
     *     private final String name;
     *     private boolean isAlive;
     *     </pre>
     *
     * @param areFinal If the attributes should be final
     * @return The encapsulated attributes
     */
    public List<String> encapsulate(boolean areFinal) {
        List<String> list = new ArrayList<>();
        keySet().forEach(dataType -> {
            List<String> names = get(dataType);
            if (areFinal)
                list.add("private final " + dataType + " " + String.join(", ", names) + ";\n");
            else
                list.add("private " + dataType + " " + String.join(", ", names) + ";\n");
        });
        return list;
    }

    /**
     * Will encapsulate attributes (not final) with
     * an output such as:
     * <pre>
     *     private String name;
     *     private boolean isAlive;
     *     </pre>
     *
     * @return The encapsulated attributes
     */
    public List<String> encapsulate() {
        return encapsulate(false);
    }

    /**
     * What goes inside the constructor parameters
     *
     * @return The constructor parameters
     */
    public String getConstructorParameters() {
        StringBuilder builder = new StringBuilder();
        listAttributes().forEach(attribute ->
                builder.append(attribute.getDataType()).append(" ")
                        .append(attribute.getAttributeName()).append(","));
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    /**
     * What goes inside the constructor body
     *
     * @return The constructor body
     */
    public String getConstructorBody() {
        StringBuilder builder = new StringBuilder();
        listAttributes().forEach(attribute -> {
            String attributeName = attribute.getAttributeName();
            builder.append("this.").append(attributeName).append(" = ")
                    .append(attributeName).append(";\n");
        });
        return builder.toString();
    }

    /**
     * If it contains a List&lt;?&gt;
     *
     * @return If it contains a List&lt;?&gt;
     */
    public boolean includesList() {
        return includesList;
    }
}

package com.github.anjoismysign.bloblibide.entities;

import com.github.anjoismysign.bloblibide.libraries.DataTypeLib;
import com.github.anjoismysign.bloblibide.libraries.NamingConventions;

import java.util.*;

public class DataTyper extends HashMap<String, List<String>> {
    private boolean includesList = false;
    private boolean includesMap = false;
    private final Set<String> mapKeys;
    private final Set<String> mapValues;
    private final Set<String> listValues;

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
        mapKeys = new HashSet<>();
        mapValues = new HashSet<>();
        listValues = new HashSet<>();
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
     * Will also convert primitives to their wrapper class.
     * Will also convert ArrayList to List and HashMap to Map.
     * This is done on purpose for improving maintainability.
     *
     * @param dataType The data type of the attribute
     * @param name     The name of the attribute.
     *                 Will be converted to camelCase.
     */
    public void add(String dataType, String name) {
        dataType = dataType.replace("ArrayList", "List");
        dataType = dataType.replace("HashMap", "Map");
        if (dataType.startsWith("List<")) {
            String generic = dataType.substring(5, dataType.length() - 1);
            String wrapper = DataTypeLib.getWrapper(generic).orElse(generic);
            listValues.add(wrapper);
            dataType = dataType.replace(generic, wrapper);
            includesList = true;
        }
        if (dataType.startsWith("Map<")) {
            String generic = dataType.substring(4, dataType.length() - 1);
            String[] split = generic.split(",");
            String key = split[0].trim();
            String value = split[1].trim();
            String keyWrapper = DataTypeLib.getWrapper(key).orElse(key);
            mapKeys.add(keyWrapper);
            String valueWrapper = DataTypeLib.getWrapper(value).orElse(value);
            mapValues.add(valueWrapper);
            dataType = dataType.replace(key, keyWrapper);
            dataType = dataType.replace(value, valueWrapper);
            includesMap = true;
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

    /**
     * If it contains a Map&lt;?&gt;
     *
     * @return If it contains a Map&lt;?&gt;
     */
    public boolean includesMap() {
        return includesMap;
    }

    public Set<String> getListValues() {
        return listValues;
    }

    public Set<String> getMapKeys() {
        return mapKeys;
    }

    public Set<String> getMapValues() {
        return mapValues;
    }

    public boolean containsDataTypeInMap(String dataType) {
        return mapKeys.contains(dataType) || mapValues.contains(dataType);
    }

    public boolean containsDataTypeInList(String dataType) {
        return listValues.contains(dataType);
    }
}

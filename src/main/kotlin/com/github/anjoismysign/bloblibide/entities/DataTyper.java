package com.github.anjoismysign.bloblibide.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class DataTyper extends HashMap<String, List<String>> {

    /**
     * Creates a DataTyper from a raw string
     * Will be able to read using the
     * following format:
     * Split dataTypes by ';'.
     * Split variable names by ','.
     * Between the dataType and variable name/s
     * there should be an empty space.
     * It is allowed to add spaces before the
     * dataType and after the variable name/s.
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
    public static DataTyper fromRaw(String raw){
        DataTyper dataTyper = new DataTyper();
        String[] split = raw.split(";");
        for (String s : split) {
            s = s.trim();
            dataTyper.parseDataType(s);
        }
        return dataTyper;
    }

    public DataTyper(){
        super();
    }

    public void parseDataType(String input){
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

    public void add(String dataType, String name) {
        if (this.containsKey(dataType)) {
            this.get(dataType).add(name);
        } else {
            List<String> list = new ArrayList<>();
            list.add(name);
            this.put(dataType, list);
        }
    }
}

package com.github.anjoismysign.bloblibide.entities;

public class Primitive {
    private final String dataType;
    protected final String getter;

    public Primitive(String dataType, String getterMethod) {
        this.dataType = dataType;
        this.getter = getterMethod;
    }

    /**
     * Returns the data type of this attribute.
     *
     * @return the data type of this attribute.
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Returns the getter method for this attribute.
     * Needs to have %configurationSection% and %path% in it.
     *
     * @return the getter method for this attribute.
     */
    public String getGetter() {
        return getter;
    }

    /**
     * Returns the getter method for this attribute.
     *
     * @param configurationSection The configuration section to get the value from.
     * @param path                 The path to get the value from.
     * @return the getter method for this attribute.
     */
    public String getGetterMethod(String configurationSection, String path) {
        return getGetter().replace("%configurationSection%", configurationSection)
                .replace("%path%", path);
    }
}

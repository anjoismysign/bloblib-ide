package com.github.anjoismysign.bloblibide.entities;

public class Custom extends Primitive {
    private final String setter;

    public Custom(String dataType, String getter, String setter) {
        super(dataType, getter);
        this.setter = setter;
    }

    /**
     * Returns the setter method for this custom data type.
     * Needs to have %configurationSection%, %path% and %value% in it.
     *
     * @return the setter method for this attribute.
     */
    public String getSetter() {
        return setter;
    }

    public String getSetterMethod(String configurationSection, String path, String value) {
        return setter.replace("%configurationSection%", configurationSection)
                .replace("%path%", path)
                .replace("%value%", value);
    }

    public String getListGetterMethod(String configurationSection, String path) {
        String dataType = getDataType();
        dataType = dataType.replace("List<", "");
        dataType = dataType.replace(">", "");
        return "ConfigurationSectionLib.deserialize" + dataType + "List(" +
                configurationSection + ".getString(" + path + "))";
    }

    public String getListSetterMethod(String list, String configurationSection, String path) {
        String dataType = getDataType();
        dataType = dataType.replace("List<", "");
        dataType = dataType.replace(">", "");
        return "ConfigurationSectionLib.serialize" + dataType + "List(" + list + ",  " + configurationSection + ",  \"" + path + "\");";

    }
}

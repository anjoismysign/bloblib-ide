package com.github.anjoismysign.bloblibide.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to encapsulate and initialize provided ObjectAttributes.
 * Used inside ClassBuider.
 *
 * @author anjoismysign
 */
public class DefaultAttributes extends ArrayList<ObjectAttribute> {
    public DefaultAttributes() {
        super();
    }

    /**
     * Will encapsulate all attributes.
     *
     * @return List of all encapsulated attributes.
     */
    public List<String> encapsulate() {
        List<String> list = new ArrayList<>();
        for (ObjectAttribute attribute : this) {
            list.add(attribute.encapsulate());
        }
        return list;
    }

    /**
     * Will initialize all attributes.
     *
     * @return List of all initialized attributes.
     */
    public List<String> initialize() {
        List<String> list = new ArrayList<>();
        for (ObjectAttribute attribute : this) {
            list.add(attribute.initialize());
        }
        return list;
    }
}

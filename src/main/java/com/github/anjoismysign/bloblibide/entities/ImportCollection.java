package com.github.anjoismysign.bloblibide.entities;

import java.util.ArrayList;

public class ImportCollection extends ArrayList<String> {
    public ImportCollection() {
        super();
    }

    /**
     * Adds a package to the import list if it is not already present
     * @param importPackage The package to add
     */
    public void addImport(String importPackage) {
        if (importPackage.charAt(importPackage.length() - 1) == ';')
            throw new IllegalArgumentException("Import should be a package name, not a statement");
        if (!contains(importPackage)) {
            add(importPackage);
        }
    }

    /**
     * Returns a string containing all the imports
     * ready to be added to the top of the .java file.
     * @return A string containing all the imports in the list
     */
    public String importPackages() {
        StringBuilder builder = new StringBuilder();
        //split list in two, one for java packages, one for other packages
        ArrayList<String> javaPackages = new ArrayList<>();
        ArrayList<String> otherPackages = new ArrayList<>();
        for (String importPackage : this) {
            if (importPackage.startsWith("java")) {
                javaPackages.add(importPackage);
            } else {
                otherPackages.add(importPackage);
            }
        }
        otherPackages.forEach(s -> builder.append("import ").append(s).append(";\n"));
        builder.append("\n");
        javaPackages.forEach(s -> builder.append("import ").append(s).append(";\n"));
        builder.append("\n");
        return builder.toString();
    }
}

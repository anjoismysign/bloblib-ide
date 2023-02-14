package com.github.anjoismysign.bloblibide.entities;

public class Shape extends Custom {
    private final String importStatement;

    public Shape(String dataType, String getter, String setter, String importStatement) {
        super(dataType, getter, setter);
        this.importStatement = importStatement;
    }

    public String getImportStatement() {
        return importStatement;
    }
}

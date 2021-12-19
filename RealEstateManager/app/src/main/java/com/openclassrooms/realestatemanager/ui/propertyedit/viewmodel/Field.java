package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

public class Field {
    private final FieldKey key;
    private String value;

    public Field(FieldKey key) {
        this.key = key;
    }

    public FieldKey getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void clear(){
        this.value = null;
    }
}

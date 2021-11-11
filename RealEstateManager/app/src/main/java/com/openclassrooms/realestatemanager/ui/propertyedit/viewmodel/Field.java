package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import android.util.Log;

import com.openclassrooms.realestatemanager.tag.Tag;

public class Field {
    private final FieldKey key;
    private String value;
    private boolean valueChanged;

    public Field(FieldKey key) {
        this.key = key;
    }

    public FieldKey getKey() {
        return key;
    }

    public String getValue() {
        //Log.d(Tag.TAG, "Field.getValue() called. key=["+ getKey() + "] value=[" + value + "]");
        return value;
    }

    public void setValue(String value) {
        this.valueChanged = true;
        this.value = value;
        //Log.d(Tag.TAG, "Field.setValue([" + value + "]) called. key=["+ getKey() + "] value=[" + this.value + "]");
    }

    public void clear(){
        this.value = null;
        this.valueChanged = false;
        //Log.d(Tag.TAG, "Field.invalidate() called. key=["+ getKey() + "] value=[" + value + "]");
    }
}

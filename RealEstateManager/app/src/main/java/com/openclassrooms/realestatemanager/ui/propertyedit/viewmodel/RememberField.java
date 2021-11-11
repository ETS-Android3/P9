package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import android.util.Log;

import com.openclassrooms.realestatemanager.tag.Tag;

public class RememberField {
    private final RememberFieldKey key;
    private String value;
    private boolean valueChanged;

    public RememberField(RememberFieldKey key) {
        this.key = key;
    }

    public RememberFieldKey getKey() {
        return key;
    }

    public String getValue() {
        if (key == RememberFieldKey.LATITUDE)
            Log.d(Tag.TAG, "RememberField.getValue() called. key=["+ getKey() + "] value=[" + value + "]");
        return value;
    }

    public void setValue(String value) {
        this.valueChanged = true;
        this.value = value;
        if (key == RememberFieldKey.LATITUDE)
            Log.d(Tag.TAG, "RememberField.setValue([" + value + "]) called. key=["+ getKey() + "] value=[" + this.value + "]");
    }

    public void clear(){
        this.value = null;
        this.valueChanged = false;
        if (key == RememberFieldKey.LATITUDE)
            Log.d(Tag.TAG, "RememberField.invalidate() called. key=["+ getKey() + "] value=[" + value + "]");
    }
}

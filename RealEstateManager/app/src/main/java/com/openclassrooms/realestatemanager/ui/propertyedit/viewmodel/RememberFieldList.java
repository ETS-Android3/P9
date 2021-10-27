package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import android.util.Log;

import com.openclassrooms.realestatemanager.tag.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RememberFieldList {
    private List<RememberField> fields = Arrays.asList(
            new RememberField(RememberFieldKey.ADDRESS_TITLE),
            new RememberField(RememberFieldKey.ADDRESS),
            new RememberField(RememberFieldKey.PRICE),
            new RememberField(RememberFieldKey.SURFACE),
            new RememberField(RememberFieldKey.ROOMS),
            new RememberField(RememberFieldKey.DESCRIPTION),
            new RememberField(RememberFieldKey.POINT_OF_INTEREST),
            new RememberField(RememberFieldKey.ENTRY_DATE),
            new RememberField(RememberFieldKey.SALE_DATE),
            new RememberField(RememberFieldKey.AGENT_NAME),
            new RememberField(RememberFieldKey.PROPERTY_TYPE_NAME)
    );

    private RememberField findField(RememberFieldKey key) {
        for (RememberField field : fields) {
            if (field.getKey() == key){
                return field;
            }
        }
        return null;
    }

    public String getValue(RememberFieldKey key) {
        RememberField field = findField(key);
        if (field != null) {
            return field.getValue();
        }
        return null;
    }

    public void setValue(RememberFieldKey key, String value){
        RememberField field = findField(key);
        if (field != null) {
            field.setValue(value);
        }
    }

    public void clearAll(){
        Log.d(Tag.TAG, "RememberFieldList.invalidate() called");
        for (RememberField field : fields) {
            field.clear();
        }
    }

    public void clearKey(RememberFieldKey key){
        Log.d(Tag.TAG, "RememberFieldList.invalidate() called");
        RememberField field = findField(key);
        if (field != null) {
            field.clear();
        }
    }
}

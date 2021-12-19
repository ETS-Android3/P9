package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import java.util.Arrays;
import java.util.List;

public class FieldList {
    private final List<Field> fields = Arrays.asList(
            new Field(FieldKey.ADDRESS_TITLE),
            new Field(FieldKey.ADDRESS),
            new Field(FieldKey.PRICE),
            new Field(FieldKey.SURFACE),
            new Field(FieldKey.ROOMS),
            new Field(FieldKey.DESCRIPTION),
            new Field(FieldKey.POINT_OF_INTEREST),
            new Field(FieldKey.ENTRY_DATE),
            new Field(FieldKey.SALE_DATE),
            new Field(FieldKey.AGENT_ID),
            new Field(FieldKey.AGENT_NAME),
            new Field(FieldKey.PROPERTY_TYPE_ID),
            new Field(FieldKey.PROPERTY_TYPE_NAME),
            new Field(FieldKey.LATITUDE),
            new Field(FieldKey.LONGITUDE)
    );

    private Field findField(FieldKey key) {
        for (Field field : fields) {
            if (field.getKey() == key){
                return field;
            }
        }
        return null;
    }

    public String getValue(FieldKey key) {
        Field field = findField(key);
        if (field != null) {
            return field.getValue();
        }
        return null;
    }

    public void setValue(FieldKey key, String value){
        Field field = findField(key);
        if (field != null) {
            field.setValue(value);
        }
    }

    public void clear(){
        for (Field field : fields) {
            field.clear();
        }
    }
}

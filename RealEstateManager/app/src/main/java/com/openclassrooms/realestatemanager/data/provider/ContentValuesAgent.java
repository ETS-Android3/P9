package com.openclassrooms.realestatemanager.data.provider;

import android.content.ContentValues;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;

public class ContentValuesAgent {

    public static final String AGENT_KEY_ID = "id";
    public static final String AGENT_KEY_NAME = "name";
    public static final String AGENT_KEY_EMAIL = "email";
    public static final String AGENT_KEY_PHONE = "phone";
    private static final String EXCEPTION_MESSAGE = "Missing argument \"%s\" to create Agent";

    private static String formatExceptionMessage(String key){
        return String.format(EXCEPTION_MESSAGE, key);
    }

    public static Agent createAgent(ContentValues values) {
        long id = PropertyConst.AGENT_ID_NOT_INITIALIZED;
        if (values.containsKey(AGENT_KEY_ID)) id = values.getAsLong(AGENT_KEY_ID);

        if (!values.containsKey(AGENT_KEY_NAME))
            throw new IllegalArgumentException(formatExceptionMessage(AGENT_KEY_NAME));
        if (!values.containsKey(AGENT_KEY_EMAIL))
            throw new IllegalArgumentException(formatExceptionMessage(AGENT_KEY_EMAIL));
        if (!values.containsKey(AGENT_KEY_PHONE))
            throw new IllegalArgumentException(formatExceptionMessage(AGENT_KEY_PHONE));

        return new Agent(id,
                values.getAsString(AGENT_KEY_NAME),
                values.getAsString(AGENT_KEY_EMAIL),
                values.getAsString(AGENT_KEY_PHONE));
    }
}

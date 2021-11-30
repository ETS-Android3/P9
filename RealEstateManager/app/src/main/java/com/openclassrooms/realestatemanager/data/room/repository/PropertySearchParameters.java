package com.openclassrooms.realestatemanager.data.room.repository;

import android.util.Pair;

import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PropertySearchParameters {

    private final static String FIELD_NAME_PRICE = "price";
    private final static String FIELD_NAME_SURFACE = "surface";
    private final static String FIELD_NAME_ROOMS = "rooms";
    private final static String FIELD_NAME_DESCRIPTION = "description";
    private final static String FIELD_NAME_POINT_OF_INTEREST = "points_of_interest";
    private final static String FIELD_NAME_ADDRESS = "address";
    private final static String FIELD_NAME_ADDRESS_TITLE = "address_title";
    private final static String FIELD_NAME_ENTRY_DATE = "entry_date";
    private final static String FIELD_NAME_SALE_DATE = "sale_date";
    private final static String FIELD_NAME_PROPERTY_TYPE_ID = "property_type_id";
    private final static String FIELD_NAME_AGENT_ID = "agent_id";

    private Pair<Integer, Integer> price;
    private Pair<Integer, Integer> surface;
    private Pair<Integer, Integer> rooms;
    private String description;
    private String pointOfInterest;
    private String address;
    private String addressTitle;
    private Pair<Long, Long> entryDate;
    private Pair<Long, Long> saleDate;
    private Long propertyTypeId;
    private Long agentId;

    public void setPrice(Pair<Integer, Integer> price) {
        this.price = price;
    }

    public void setSurface(Pair<Integer, Integer> surface) {
        this.surface = surface;
    }

    public void setRooms(Pair<Integer, Integer> rooms) {
        this.rooms = rooms;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPointOfInterest(String pointOfInterest) {
        this.pointOfInterest = pointOfInterest;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public void setEntryDate(Pair<Long, Long> entryDate) {
        this.entryDate = entryDate;
    }

    public void setSaleDate(Pair<Long, Long> saleDate) {
        this.saleDate = saleDate;
    }

    public void setPropertyTypeId(Long propertyTypeId) {
        this.propertyTypeId = propertyTypeId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public void Clear(){
        price = null;
        surface = null;
        rooms = null;
        description = null;
        pointOfInterest = null;
        address = null;
        addressTitle = null;
        entryDate = null;
        saleDate = null;
        propertyTypeId = null;
        agentId = null;
    }

    private boolean containsWhere(StringBuilder query){
        return query.toString().toLowerCase().contains("where");
    }

    private void addInterval(StringBuilder query, List<Object> args, Pair<Integer, Integer> interval, String fieldName){
        if ((interval != null) && (interval.first != null) && (interval.second != null)) {
            if (containsWhere(query))
                query.append("AND ");
            else
                query.append("WHERE ");
            query.append(String.format("((%s >= ?) and (%s <= ?)) ", fieldName, fieldName));
            args.add(interval.first);
            args.add(interval.second);
        }
    }

    private void addString(StringBuilder query, List<Object> args, String value, String fieldName){
        if ((value != null) && (!value.trim().isEmpty())){
            if (containsWhere(query))
                query.append("AND ");
            else
                query.append("WHERE ");
            query.append(String.format("(%s LIKE ?) ", fieldName));
            // for like statement arg must be surronded by "%"
            args.add("%"+value+"%");
        }
    }

    //buildMaterialDateRangePicker
    private void addDate(StringBuilder query, List<Object> args, Pair<Long, Long> interval, String fieldName){
        if ((interval != null) && (interval.first != null) && (interval.second != null)) {
            if (containsWhere(query))
                query.append("AND ");
            else
                query.append("WHERE ");
            query.append(String.format("((%s >= ?) and (%s <= ?)) ", fieldName, fieldName));
            args.add(interval.first);
            args.add(interval.second);
        }
    }

    private void addLong(StringBuilder query, List<Object> args, Long value, String fieldName){
        if (value != null) {
            if (containsWhere(query))
                query.append("AND ");
            else
                query.append("WHERE ");
            query.append(String.format("(%s = ?) ", fieldName));
            args.add(value);
        }
    }

    public SimpleSQLiteQuery getQuery(){
        StringBuilder query = new StringBuilder();
        List<Object> args = new ArrayList<>();

        query.append("SELECT * FROM PROPERTY ");

        addInterval(query, args, price, FIELD_NAME_PRICE);
        addInterval(query, args, surface, FIELD_NAME_SURFACE);
        addInterval(query, args, rooms, FIELD_NAME_ROOMS);
        addString(query, args, description, FIELD_NAME_DESCRIPTION);
        addString(query, args, pointOfInterest, FIELD_NAME_POINT_OF_INTEREST);
        addString(query, args, address, FIELD_NAME_ADDRESS);
        addString(query, args, addressTitle, FIELD_NAME_ADDRESS_TITLE);
        addDate(query, args, entryDate, FIELD_NAME_ENTRY_DATE);
        addDate(query, args, saleDate, FIELD_NAME_SALE_DATE);
        addLong(query, args, propertyTypeId, FIELD_NAME_PROPERTY_TYPE_ID);
        addLong(query, args, agentId, FIELD_NAME_AGENT_ID);

        query.append("ORDER BY property.id ");

        SimpleSQLiteQuery simpleSQLiteQuery = new SimpleSQLiteQuery(query.toString(), args.toArray());
        return simpleSQLiteQuery;
    }

}

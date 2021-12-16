package com.openclassrooms.realestatemanager.data.room.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyDetailData;
import com.openclassrooms.realestatemanager.data.room.model.PropertyLocationData;
import com.openclassrooms.realestatemanager.data.room.model.PropertyRange;

import java.util.List;

@Dao
public interface PropertyDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long insert(Property property);

    @Update
    int update(Property property);

    @Query("DELETE FROM property WHERE property.id = :id")
    int delete(long id);

    @Query("select property.*, " +
           "agent.email as agent_email, agent.name as agent_name, agent.phone as agent_phone, " +
           "property_type.name as property_type_name "+
           "from property " +
           "left join agent on property.agent_id = agent.id " +
           "left join property_type on property.property_type_id = property_type.id " +
           "where property.id = :id")
    LiveData<PropertyDetailData> getPropertyDetailById(long id);

    @Query("select id, price, address_title, latitude, longitude " +
           "from property " +
           "where (not ((latitude = 0) and (longitude = 0))) and (id <> :id)")
    LiveData<List<PropertyLocationData>> getOtherPropertiesLocationById(long id);

    @Query("select id, price, address_title, latitude, longitude " +
            "from property " +
            "where (not ((latitude = 0) and (longitude = 0))) and (id = :id)")
    LiveData<PropertyLocationData> getPropertyLocationById(long id);

    @Query("select id, price, address_title, latitude, longitude " +
            "from property " +
            "where (not ((latitude = 0) and (longitude = 0)))")
    LiveData<List<PropertyLocationData>> getPropertiesLocation();

    @Query("SELECT * FROM property WHERE property.id = :id")
    Cursor getPropertyByIdWithCursor(Long id);

    @Query("SELECT * FROM property ORDER BY property.id")
    Cursor getPropertiesWithCursor();

    @RawQuery(observedEntities = Property.class)
    LiveData<List<Property>> getPropertiesWithFilterLiveData(SimpleSQLiteQuery query);

    @RawQuery(observedEntities = Property.class)
    List<Property> getPropertiesWithFilter(SimpleSQLiteQuery query);

    @Query("select min(price) as min_price, max(price) as max_price, " +
            "min(surface) as min_surface, max(surface) as max_surface, " +
            "min(rooms) as min_rooms, max(rooms) as max_rooms " +
            "from property")
    LiveData<PropertyRange> getPropertiesMinMaxRanges();
}

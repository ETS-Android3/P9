package com.openclassrooms.realestatemanager.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyDetailData;
import com.openclassrooms.realestatemanager.data.room.model.PropertyLocationData;

import java.util.List;

@Dao
public interface PropertyDao {
    @Query("SELECT * FROM property ORDER BY property.id")
    LiveData<List<Property>> getProperties();

    @Query("SELECT * FROM property WHERE property.id = :id")
    LiveData<Property> getPropertyById(Long id);

    @Query("SELECT property.id FROM property ORDER BY property.id LIMIT 1")
    Long getFirstPropertyId();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long insert(Property property);

    @Update
    int update(Property property);

    @Query("DELETE FROM property WHERE property.id = :id")
    int delete(long id);

    //For Search
    @RawQuery(observedEntities = Property.class)
    LiveData<List<Property>> getSearch(SupportSQLiteQuery query);

    @Query("select property.*, " +
           "agent.email as agent_email, agent.name as agent_name, agent.phone as agent_phone, " +
           "property_category.name as property_category_name, "+
           "property_type.name as property_type_name "+
           "from property " +
           "left join agent on property.agent_id = agent.id " +
           "left join property_category on property.property_category_id = property_category.id " +
           "left join property_type on property.property_type_id = property_type.id " +
           "where property.id = :id")
    LiveData<PropertyDetailData> getPropertyDetailById(long id);

    @Query("select id, price, address_title, latitude, longitude " +
           "from property " +
           "where (not ((latitude = 0) and (longitude = 0))) and (id <> :id)")
    LiveData<List<PropertyLocationData>> getOtherPropertiesLocationById(long id);
}

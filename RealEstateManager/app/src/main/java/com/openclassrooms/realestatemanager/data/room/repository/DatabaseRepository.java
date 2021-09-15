package com.openclassrooms.realestatemanager.data.room.repository;

import com.openclassrooms.realestatemanager.data.room.dao.AgentDao;
import com.openclassrooms.realestatemanager.data.room.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data.room.dao.PropertyCategoryDao;
import com.openclassrooms.realestatemanager.data.room.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.room.dao.PropertyTypeDao;

public class DatabaseRepository {

    private final AgentRepository agentRepository;
    private final PhotoRepository photoRepository;
    private final PropertyCategoryRepository propertyCategoryRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyTypeRepository propertyTypeRepository;

    public DatabaseRepository(AgentRepository agentRepository,
                              PhotoRepository photoRepository,
                              PropertyCategoryRepository propertyCategoryRepository,
                              PropertyRepository propertyRepository,
                              PropertyTypeRepository propertyTypeRepository) {
        this.agentRepository = agentRepository;
        this.photoRepository = photoRepository;
        this.propertyCategoryRepository = propertyCategoryRepository;
        this.propertyRepository = propertyRepository;
        this.propertyTypeRepository = propertyTypeRepository;
    }

    public AgentRepository getAgentRepository() {
        return agentRepository;
    }

    public PhotoRepository getPhotoRepository() {
        return photoRepository;
    }

    public PropertyCategoryRepository getPropertyCategoryRepository() {
        return propertyCategoryRepository;
    }

    public PropertyRepository getPropertyRepository() {
        return propertyRepository;
    }

    public PropertyTypeRepository getPropertyTypeRepository() {
        return propertyTypeRepository;
    }
}

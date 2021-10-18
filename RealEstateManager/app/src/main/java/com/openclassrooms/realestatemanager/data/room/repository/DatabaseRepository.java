package com.openclassrooms.realestatemanager.data.room.repository;

public class DatabaseRepository {

    private final AgentRepository agentRepository;
    private final PhotoRepository photoRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyTypeRepository propertyTypeRepository;

    public DatabaseRepository(AgentRepository agentRepository,
                              PhotoRepository photoRepository,
                              PropertyRepository propertyRepository,
                              PropertyTypeRepository propertyTypeRepository) {
        this.agentRepository = agentRepository;
        this.photoRepository = photoRepository;
        this.propertyRepository = propertyRepository;
        this.propertyTypeRepository = propertyTypeRepository;
    }

    public AgentRepository getAgentRepository() {
        return agentRepository;
    }

    public PhotoRepository getPhotoRepository() {
        return photoRepository;
    }

    public PropertyRepository getPropertyRepository() {
        return propertyRepository;
    }

    public PropertyTypeRepository getPropertyTypeRepository() {
        return propertyTypeRepository;
    }
}

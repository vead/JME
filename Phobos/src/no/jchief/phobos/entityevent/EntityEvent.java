/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.entityevent;

import no.jchief.phobos.component.Component;
import no.jchief.phobos.entity.Entity;
import no.jchief.phobos.entity.EntityId;

/**
 *
 * @author Karsten
 */
public class EntityEvent {
    
    private Entity entity;
    private EntityId entityId;
    private Component component;
    private EventType eventType;
    
    public enum EventType
    {
          Add,Change,Remove;
    }
    
    public EntityEvent(Entity entity, Component component, EventType eventType)
    {
        this.entity=entity;
        this.component=component;
        this.eventType=eventType;
        this.entityId=entity.getId();
    }
    
     public EntityEvent(EntityId entityId, Component component, EventType eventType)
    {
        this.entity=null;
        this.component=component;
        this.eventType=eventType;
        this.entityId=entityId;
    }
    
     /**
     * @return the entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * @return the entityId
     */
    public EntityId getEntityId() {
        return entityId;
    }

    /**
     * @return the component
     */
    public <T extends Component>T getComponent() {
        return (T)component;
    }
    
    /**
     * @return the eventType
     */
    public EventType getEventType() {
        return eventType;
    }
    
    public Class getComponentClass() {
        return component.getClass();
    }
    
}

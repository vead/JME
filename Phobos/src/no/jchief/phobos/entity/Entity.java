/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.entity;

import no.jchief.phobos.component.Component;
import no.jchief.phobos.entityevent.EntityEvent;

/**
 *
 * @author Karsten
 */
public class Entity {

    private EntityId id;
    private Component[] components;
    private Class[] componentClasses;
    private EntitySet entitySet;
    
    public Entity(EntityId id, Component[] components, Class<? extends Component>[] componentClasses,EntitySet entitySet) {
        this.id = id;
        this.componentClasses = componentClasses;
        this.components = components;
        this.entitySet=entitySet; 
    }

    public Entity(EntityId id, Class<? extends Component>[] componentClasses,EntitySet entitySet) {
        this.id = id;
        this.componentClasses = componentClasses;
        this.components = new Component[componentClasses.length];
        this.entitySet=entitySet;
    }

    public <T extends Component> void setComponent(T component) {

        for (int i = 0; i < componentClasses.length; i++) {
            if (component.getClass() == componentClasses[i]) {
                if (components[i] == null) {
                   entitySet.getEntitySystem().processEntityEvent(new EntityEvent(this, component, EntityEvent.EventType.Add));
                } else {
                    entitySet.getEntitySystem().processEntityEvent(new EntityEvent(this, component, EntityEvent.EventType.Change));
                }
                components[i] = component;
                return;

            }
        }
        entitySet.getEntitySystem().processEntityEvent(new EntityEvent(this, component, EntityEvent.EventType.Change));
        
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < componentClasses.length; i++) {
            if (componentClasses[i] == componentClass) {
                if (components[i] == null) {
                    return;
                }

                entitySet.getEntitySystem().processEntityEvent(new EntityEvent(this, components[i], EntityEvent.EventType.Remove));
                components[i] = null;

                return;
            }
        }
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (int i = 0; i < componentClasses.length; i++) {
            if (componentClasses[i] == componentClass) {
                return (T) components[i];
            }
        }

        return null;
    }

    public void update(EntityEvent entityEvent) {
        if (entityEvent.getEntity() == this) {
            return;
        }
        
        for (int i = 0; i < componentClasses.length; i++) {
            
            if (entityEvent.getComponentClass() == componentClasses[i]) {
                
                if (entityEvent.getEventType() == EntityEvent.EventType.Remove) {
                    components[i]=null;
                } else {
                    components[i]=entityEvent.getComponent();
                }
                return;
            }
        }

    }

    public void destroy() {
        for (int i = 0; i < componentClasses.length; i++) {
            if (components[i] != null) {
                entitySet.getEntitySystem().processEntityEvent(new EntityEvent(this, components[i], EntityEvent.EventType.Remove));
            }
        }
        components = null;
        componentClasses = null;

        entitySet.getEntitySystem().removeEntity(this);
    }

    public EntityId getId() {
        return id;
    }

}

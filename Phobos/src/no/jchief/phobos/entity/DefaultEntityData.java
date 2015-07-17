/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import no.jchief.phobos.component.Component;
import no.jchief.phobos.support.IdGenerator;

/**
 *
 * @author Karsten
 */
public class DefaultEntityData implements EntityData {

    private ConcurrentMap<Class, ConcurrentMap<EntityId, ? extends Component>> componentMaps;
    private IdGenerator idGenerator;

    public DefaultEntityData() {
        componentMaps = new ConcurrentHashMap<Class, ConcurrentMap<EntityId, ? extends Component>>();
        idGenerator = new IdGenerator();
    }

    public EntityId newEntity() {
        return new EntityId(idGenerator.getNextId());
    }


    public void removeEntity(EntityId entity) {
        for (ConcurrentMap<EntityId, ? extends Component> componentMap : componentMaps.values()) {
            componentMap.remove(entity);
        }
    }

    public void addComponent(EntityId entity, Component component) {
        ConcurrentMap<EntityId, ? extends Component> componentMap = componentMaps.get(component.getClass());

        if (componentMap == null) {
            componentMap = new ConcurrentHashMap();
            componentMaps.put(component.getClass(), componentMap);
        }
        ((ConcurrentMap<EntityId, Component>) componentMap).put(entity, component);
    }

    public <T extends Component> void removeComponent(EntityId entity, Class<T> componentClass) {
        ConcurrentMap<EntityId, ? extends Component> componentMap = componentMaps.get(componentClass);

        if (componentMap != null) {
            ((ConcurrentMap<EntityId, T>) componentMap).remove(entity);
        }
    }

    public <T extends Component> T getComponent(EntityId entity, Class<T> componentClass) {
        ConcurrentMap<EntityId, ? extends Component> componentMap = componentMaps.get(componentClass);

        if (componentMap == null) {
            componentMap = new ConcurrentHashMap();
            componentMaps.put(componentClass, componentMap);
            return null;
        }
        return ((ConcurrentMap<EntityId, T>) componentMap).get(entity);
    }

    public <T extends Component> boolean hasComponent(EntityId entity, Class<T> componentClass) {
        ConcurrentMap<EntityId, ? extends Component> componentMap = componentMaps.get(componentClass);

        if (componentMap == null) {
            componentMap = new ConcurrentHashMap();
            componentMaps.put(componentClass, componentMap);
            return false;
        }
        return ((ConcurrentMap<EntityId, T>) componentMap).containsKey(entity);
    }

    /*
    public <T extends Component> List<T> getAllComponentsOfEntity(EntityId entity) {
            LinkedList<T> components = new LinkedList<T>();

            for (ConcurrentMap<EntityId, ? extends Component> componentMap : componentMaps.values())
            {
                if (componentMap != null) {
                    T componentFromThisEntity = (T) componentMap.get(entity);

                    if (componentFromThisEntity != null) {
                        components.addLast(componentFromThisEntity);
                    }
                }
            }
            return components;
    }*/
    
    public Set<EntityId> getAllEntitysWithComponents(Class<? extends Component> ... components)
    {
        ConcurrentMap<EntityId, ? extends Component> componentMap = componentMaps.get(components[0]);
        Set<EntityId> set = new HashSet();

         if (componentMap == null) {
            componentMap = new ConcurrentHashMap();
            componentMaps.put(components[0], componentMap);
            return set;
        }
        
        for (EntityId v : componentMap.keySet())
        {
            set.add(v);
        }
        
        Iterator<EntityId> iterator = set.iterator();
        EntityId value;
        while(iterator.hasNext())
        {
            value = iterator.next();
            
            for(int i=1;i<components.length;i++)
            {
                componentMap = componentMaps.get(components[i]);
                if(componentMap.containsKey(value)==false)
                {
                    iterator.remove();
                    break;
                }
            }
        }
        
        return set;
    }
    
}

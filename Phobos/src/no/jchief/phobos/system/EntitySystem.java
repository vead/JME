/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.system;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import no.jchief.phobos.component.Component;
import no.jchief.phobos.entity.Entity;
import no.jchief.phobos.entity.EntityData;
import no.jchief.phobos.entity.EntityId;
import no.jchief.phobos.entity.EntitySet;
import no.jchief.phobos.entityevent.EntityEvent;
import no.jchief.phobos.entityevent.EntityEventListener;

/**
 *
 * @author Karsten
 */
public class EntitySystem {
    
    //public static final EntitySystem instance = new EntitySystem();
    
    private List<EntityEventListener> entityChangeListeners;
    private EntityData entityData;
    
    
    public EntitySystem(EntityData entityData)
    {
        this.entityData=entityData;
        entityChangeListeners = new CopyOnWriteArrayList<EntityEventListener>();
    }
    
    public void addEntityChangeListener(EntityEventListener changeListener)
    {
        entityChangeListeners.add(changeListener);
    }
    
    public void removeEntityChangeListener(EntityEventListener changeListener)
    {
        entityChangeListeners.remove(changeListener);
    }
    
    
    public void createEntity(Component... components)
    {
        EntityId value = entityData.newEntity();
        EntityEvent events[] = new EntityEvent[components.length];
        
        for(int i=0;i<components.length;i++)
        {
            entityData.addComponent(value, components[i]);
            events[i] = new EntityEvent(value,components[i],EntityEvent.EventType.Add);
        }
        
        for(EntityEventListener listener : this.entityChangeListeners)
        {
            if (fitsToListener(value,listener))
            {
                for(int i=0;i<components.length;i++)
                {
                    listener.receiveEntityEvent(events[i]);
                }
                
            }
        }
    }
    
    private boolean fitsToListener(EntityId entityId,EntityEventListener listener)
    {
        Class<? extends Component>[] intrestedIn= listener.componentsIntrestedIn();
        
        for(int i=0;i<intrestedIn.length;i++)
        {
            if(entityData.hasComponent(entityId, intrestedIn[i])== false)
                return false;
        }
        
        return true;
    }
    
    public void removeEntity(Entity entity)
    {
        entityData.removeEntity(entity.getId());
    }
    
    public void processEntityEvent(EntityEvent event)
    {
        if(event.getEventType() == EntityEvent.EventType.Remove)
        {
            for(int i=0;i<entityChangeListeners.size();i++)
            {
                if (fitsToListener(event.getEntityId(),entityChangeListeners.get(i)))
                {
                    entityChangeListeners.get(i).receiveEntityEvent(event);
                }
            }
            
            entityData.removeComponent(event.getEntityId(), event.getComponentClass());
            

        }else{
            entityData.addComponent(event.getEntityId(), event.getComponent());
            
            for(int i=0;i<entityChangeListeners.size();i++)
            {
                if (fitsToListener(event.getEntityId(),entityChangeListeners.get(i)))
                {
                    entityChangeListeners.get(i).receiveEntityEvent(event);
                }
            }
        }
        

    }

    /*
    private boolean fitsToListener(Class componentClass,EntityEventListener listener)
    {
        Class<? extends Component>[] intrestedIn= listener.componentsIntrestedIn();
        
        for(int i=0;i<intrestedIn.length;i++)
        {
            if(componentClass == intrestedIn[i])
                return true;
        }
        
        return false;
    }*/
    
    public EntitySet getEntitySet(Class<? extends Component> ... componentClasses)
    {
        Map<EntityId, Entity> entityMap = new HashMap<EntityId, Entity>();        
                
        EntitySet entitySet = new EntitySet();
        
        
        Set set = entityData.getAllEntitysWithComponents(componentClasses);
        Iterator<EntityId> iterator =set.iterator();
        while(iterator.hasNext())
        {
            EntityId value = iterator.next();
            Component components[] = new Component[componentClasses.length];
            
            for(int i=0;i<componentClasses.length;i++)
            {
                components[i] = entityData.getComponent(value, componentClasses[i]);
            }
            
            Entity entity = new Entity(value,components,componentClasses,entitySet);
            entityMap.put(value,entity);
        }
        
        entitySet.init(this,entityMap, componentClasses);
        return entitySet;
    }
    
    /*
    public static EntitySystem getInstance()
    {
        return instance;
    }*/
    
}

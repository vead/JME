/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import no.jchief.phobos.component.Component;
import no.jchief.phobos.entityevent.EntityEvent;
import no.jchief.phobos.entityevent.EntityEventListener;
import no.jchief.phobos.system.EntitySystem;

/**
 *
 * @author Karsten
 */
public class EntitySet implements EntityEventListener {

    private Map<EntityId,Entity> entities;
    private ConcurrentLinkedQueue<EntityEvent> changeEvents;
    
    private Class componentClasses[];
    
    private Set addedEntities;
    private Set changedEntities;
    private Set removedEntities;
    
    private boolean isNew=true;
    
    private EntitySystem entitySystem;
    

    
    public void init(EntitySystem entitySystem, Map<EntityId,Entity> entities, Class<? extends Component> componentClasses[])
    {
        this.entities=entities;
        this.entitySystem=entitySystem;
        this.componentClasses = componentClasses;
        changeEvents = new ConcurrentLinkedQueue<EntityEvent>();
        addedEntities = new HashSet();
        changedEntities = new HashSet();
        removedEntities = new HashSet();
        
        Iterator<Entity> iterator = entities.values().iterator();
        while(iterator.hasNext())
        {
            addedEntities.add(iterator.next());
        }
        
        entitySystem.addEntityChangeListener(this);
    }
    
    public boolean applyChanges()
    {
        if(isNew)
        {
            isNew=false;
            return true;
        }else if(changeEvents.isEmpty())
        {
            return false;
        }
        
        getAddedEntities().clear();
        getChangedEntities().clear();
        getRemovedEntities().clear();
        
        Iterator<EntityEvent> iterator = changeEvents.iterator();
        while(iterator.hasNext())
        {
            EntityEvent ev = iterator.next();
            
            EntityId id = ev.getEntityId();
            Entity entity = entities.get(id);
            
            if(entity == null)
            {
                entity = new Entity(id,componentClasses, this);
                entities.put(id, entity);
            }
            
            entity.update(ev);
            
            if(ev.getEventType() == EntityEvent.EventType.Change)
            {
                getChangedEntities().add(entity);
            }else if(ev.getEventType() == EntityEvent.EventType.Add){
                getAddedEntities().add(entity);
            }else{
                getRemovedEntities().add(entity);
            }
            
            iterator.remove();
        }
        
        Iterator<Entity> iteratorRemoved = removedEntities.iterator();
        while(iteratorRemoved.hasNext())
        {
            entities.remove(iteratorRemoved.next().getId());
        }
        
        return true;
    }
    
    
    public void receiveEntityEvent(EntityEvent changeEvent) 
    {
      //  for(int i=0;i<componentClasses.length;i++)
     //   {
      //      if(changeEvent.getComponentClass() == componentClasses[i])
      //      {
                changeEvents.add(changeEvent);
                return;
      //      }
     //   }
    }
    
    public void release()
    {
        entitySystem.removeEntityChangeListener(this);
        
        getAddedEntities().clear();
        getChangedEntities().clear();
        getRemovedEntities().clear();
        changeEvents.clear();
    }
    
    
    public Iterator<Entity> getIterator()
    {
        return entities.values().iterator();
    }
    
    public boolean isEmpty()
    {
        return entities.isEmpty();
    }
    
    
    public Class<? extends Component>[] componentsIntrestedIn() {
        return componentClasses;
    }

    /**
     * @return the addedEntities
     */
    public Set getAddedEntities() {
        return addedEntities;
    }

    /**
     * @return the changedEntities
     */
    public Set getChangedEntities() {
        return changedEntities;
    }

    /**
     * @return the removedEntities
     */
    public Set getRemovedEntities() {
        return removedEntities;
    }
    
    public EntitySystem getEntitySystem()
    {
        return entitySystem;
    }


    
}

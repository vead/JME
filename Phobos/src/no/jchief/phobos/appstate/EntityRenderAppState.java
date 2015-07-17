/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.appstate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import no.jchief.phobos.PhobosMain;
import no.jchief.phobos.component.InSceneComponent;
import no.jchief.phobos.component.MovementComponent;
import no.jchief.phobos.component.PositionComponent;
import no.jchief.phobos.component.VisualComponent;
import no.jchief.phobos.control.EntityControl;
import no.jchief.phobos.entity.Entity;
import no.jchief.phobos.entity.EntitySet;
import no.jchief.phobos.system.EntitySystem;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 *
 * @author Karsten
 */
public class EntityRenderAppState extends AbstractAppState {
    private PhobosMain pho;
    private EntitySet entitySet;
    private Map<Entity,EntityControl> controls;
    
    private AssetManager assetManager;
    
    private Node rootNode;
    
    public EntityRenderAppState(Node node)
    {
        rootNode=node;
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager,app);
        pho = (PhobosMain)app;
        
        EntitySystem entitySystem = pho.getEntitySystem();
        this.assetManager = app.getAssetManager();
        
        controls = new HashMap<Entity,EntityControl>();
        
        entitySet = entitySystem.getEntitySet(InSceneComponent.class, MovementComponent.class, PositionComponent.class, VisualComponent.class);
//        entitySet = entitySystem.getEntitySet(PositionComponent.class, VisualComponent.class);
        
    }
    
    @Override
    public void update(float tpf) {

        if(entitySet.applyChanges())
        {
            add(entitySet.getAddedEntities());
            changed(entitySet.getChangedEntities());
            removed(entitySet.getRemovedEntities());
        }
        
    }
    
    private void add(Set<Entity> set)
    {
        Iterator<Entity> iterator = set.iterator();
        
        while(iterator.hasNext())
        {
            Entity entity = iterator.next();
            EntityControl ec = new EntityControl(entity,assetManager);
            controls.put(entity,ec);
            rootNode.attachChild(ec.getSpatial());
        }   
    }
    
    private void changed(Set<Entity> set)
    {
        Iterator<Entity> iterator = set.iterator();
        
        while(iterator.hasNext())
        {
            controls.get(iterator.next()).updateEntity();
        }    
    }
    
    private void removed(Set<Entity> set)
    {
        Iterator<Entity> iterator = set.iterator();
        
        while(iterator.hasNext())
        {
            Entity entity = iterator.next();
            EntityControl ec = controls.get(entity);
            ec.remove();
            rootNode.detachChild(ec.getSpatial());
            controls.remove(entity);
            
        }       
    }
    
    
}

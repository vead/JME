/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.appstate;

import java.util.Iterator;

import no.jchief.phobos.PhobosMain;
import no.jchief.phobos.component.MovementComponent;
import no.jchief.phobos.component.PositionComponent;
import no.jchief.phobos.component.VelocityComponent;
import no.jchief.phobos.entity.Entity;
import no.jchief.phobos.entity.EntitySet;
import no.jchief.phobos.system.EntitySystem;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;

/**
 *
 * @author Karsten
 */
public class MovementAppState extends AbstractAppState {
	private PhobosMain pho;
    private EntitySet entitySet;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
    	pho = (PhobosMain)app;
        EntitySystem entitySystem = pho.getEntitySystem();
        entitySet = entitySystem.getEntitySet(MovementComponent.class, PositionComponent.class);//, VelocityComponent.class);
    }

    @Override
    public void update(float tpf) {

        entitySet.applyChanges();

        Iterator<Entity> iterator = entitySet.getIterator();
//        System.out.println("HOW OFTEN DO I HAVE TO COME HERE???  changed: " + entitySet.getChangedEntities().size() );
//        System.out.println("HOW OFTEN DO I HAVE TO COME HERE???  added: " + entitySet.getAddedEntities().size() );

        while (iterator.hasNext()) {
            Entity entity = iterator.next();

            MovementComponent mc = entity.getComponent(MovementComponent.class);
            PositionComponent pc = entity.getComponent(PositionComponent.class);
//            VelocityComponent vc = entity.getComponent(VelocityComponent.class);
            
            
            if(mc.getMovement().x != 0 || mc.getMovement().y != 0 || mc.getMovement().z != 0) {
                
                Vector3f vec3f = pc.getLocation().add(mc.getMovement().mult(tpf*10));

                entity.setComponent(new PositionComponent(vec3f, pc.getRotation()));
            }
        }
    }

}


/*
 * 
 */




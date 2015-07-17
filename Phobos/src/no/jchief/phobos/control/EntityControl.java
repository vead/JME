/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.control;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.jchief.phobos.component.InSceneComponent;
import no.jchief.phobos.component.MovementComponent;
import no.jchief.phobos.component.PositionComponent;
import no.jchief.phobos.component.VisualComponent;
import no.jchief.phobos.entity.Entity;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Sphere;



/**
 *
 * @author Karsten
 */
public final class EntityControl extends AbstractControl {

    private static final Logger logger = Logger.getLogger(EntityControl.class.getName());

    private Entity entity;
    private AssetManager manager;
    private String currentModelName;
    private Spatial currentModel;

    private List<AnimControl> animControls;
    private List<AnimChannel> animChannels;

    public EntityControl(Entity entity, AssetManager manager) {
        this.entity = entity;
        this.manager = manager;
        
        setSpatial(new Node());
        updateEntity();
    }


    public void updateEntity() {
        
        if (entity == null) {
            return;
        }
        if (!updateVisualRep()) {
            return;
        }
        if (!updateLocation()) {
            return;
        }
        if (!updateAnimation()) {
            return;
        }
    }
    
    public void remove()
    {
        entity=null;
        
        if( animControls != null)
            animControls.clear();
        if( animChannels != null)
            animChannels.clear();
    }

    private boolean updateVisualRep() {
        VisualComponent visRep = entity.getComponent(VisualComponent.class);
        if (visRep != null) {
            if (currentModelName != null && currentModelName.equals(visRep.getAssetName())) {
                return true;
            } else if ( visRep.isBasicMesh() && currentModel != null) {
            	return true;
            } else {
                if (currentModel != null) {
                    setAnimControls(null);
                    currentModel.removeFromParent();
                }
                if ( visRep.isBasicMesh() ) {
                    System.out.println("CREATING GEO!");
                	currentModel = new Geometry("Well hello then", visRep.getMesh());
                	currentModel.setMaterial( visRep.getMat() );
                	currentModel.setUserData("lol", "if you see this I have survived the entity system");
                } else {
	                currentModelName = visRep.getAssetName();
	                currentModel = manager.loadModel(currentModelName);
	                setAnimControls(currentModel);
                }
                System.out.println("CurrentModelName: " + currentModelName);
                System.out.println("CurrentModel: " + currentModel);
                ((Node) spatial).attachChild(currentModel);
            }
        } else {
    //dispose ourselves if the entity has no VisualRepComponent anymore..
            setAnimControls(null);
            spatial.removeFromParent();
            entity.removeComponent(InSceneComponent.class);
            return false;
        }
        return true;
    }

    private boolean updateLocation() {
        PositionComponent position = entity.getComponent(PositionComponent.class);
        
       if (position != null) {
            spatial.setLocalTranslation(position.getLocation());
            spatial.setLocalRotation(position.getRotation());
        }
        return true;
    }

    private boolean updateAnimation() {
        MovementComponent movement = entity.getComponent(MovementComponent.class);
        if (movement != null && movement.getMovement().length() > 0) {
            setAnimation("Walk");
        } else {
            setAnimation("stand");
        }
        return true;
    }

    private void setAnimation(String name) {
        if (animChannels != null) {
            for (Iterator<AnimChannel> it = animChannels.iterator(); it.hasNext();) {
                AnimChannel animChannel = it.next();
                if (animChannel.getAnimationName() == null || !animChannel.getAnimationName().equals(name)) {
                    animChannel.setAnim(name);
                    logger.log(Level.INFO, "Setting anim {0}", name);
                    if (animChannel.getControl().getAnim(name) != null) {
                    }
                }
            }
        }
    }

    private void setAnimControls(Spatial spatial) {
        if (spatial == null) {
            if (animControls != null) {
                for (Iterator<AnimControl> it = animControls.iterator(); it.hasNext();) {
                    AnimControl animControl = it.next();
                    animControl.clearChannels();
                }
            }
            animControls = null;
            animChannels = null;
            return;
        }
        SceneGraphVisitorAdapter visitor = new SceneGraphVisitorAdapter() {
            @Override
            public void visit(Geometry geom) {
                super.visit(geom);
                checkForAnimControl(geom);
            }

            @Override
            public void visit(Node geom) {
                super.visit(geom);
                checkForAnimControl(geom);
            }

            private void checkForAnimControl(Spatial geom) {
                AnimControl control = geom.getControl(AnimControl.class);
                if (control == null) {
                    return;
                }
                if (animControls == null) {
                    animControls = new LinkedList<AnimControl>();
                }
                if (animChannels == null) {
                    animChannels = new LinkedList<AnimChannel>();
                }
                animControls.add(control);
                animChannels.add(control.createChannel());
            }
        };
        spatial.depthFirstTraversal(visitor);
    }

    @Override
    protected void controlUpdate(float tpf) {
    }
    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
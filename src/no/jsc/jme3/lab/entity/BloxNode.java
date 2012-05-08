package no.jsc.jme3.lab.entity;

import no.jsc.jme3.lab.AwesomeSpaceGame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class BloxNode extends Node {
	private AssetManager am;
	private Box box;
	private float size = AwesomeSpaceGame.unitSize;
	
	public BloxNode() {
		super();
	}
	
	public BloxNode(String name, AssetManager am) {
		super();
		this.am = am;
		this.setName(name+"BloxNode");
		
		box = new Box( Vector3f.ZERO, size/2, size/2, size/2 );
		Geometry boxGeo = new Geometry(name+"Geo", box);
	    	    	    
        Material boxMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", new ColorRGBA(0.6f, 1, 0.6f, 0.5f));
        boxMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        //boxMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
        boxGeo.setMaterial(boxMat);
        boxGeo.setQueueBucket(Bucket.Translucent);
        this.attachChild( boxGeo );
        
//        RigidBodyControl control = new RigidBodyControl(new BoxCollisionShape(new Vector3f(box.getXExtent(), box.getYExtent(), box.getZExtent())), 0f);
//        control.setFriction(0.5f);
//        control.setPhysicsLocation(location);
//        this.addControl(control);
		
	}
	
	
}

package no.jsc.jme3.lab.geom;

import no.jsc.jme3.lab.AwesomeSpaceGame;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Blox extends Geometry {
		
	public Blox(String name,  AssetManager am) {
		super();
		float size = AwesomeSpaceGame.unitSize;
	    /** A cube with base color "leaking" through a partially transparent texture */
		Box box;
		box = new Box( Vector3f.ZERO, size/2, size/2, size/2 );

			
	    setName( name );
	    setMesh( box );

		//addControl(new RigidBodyControl(0f));
	    	    
        Material boxMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", new ColorRGBA(0.6f, 1, 0.6f, 0.5f));
        boxMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        //boxMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
        setMaterial(boxMat);
        setQueueBucket(Bucket.Translucent);
		
	}
	



	
}
package no.jsc.jme3.lab.geom;

import no.jsc.jme3.lab.AwesomeSpaceGame;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Blox extends Geometry {
		
	public Blox(Vector3f center, AssetManager am) {
		super();
		float size = AwesomeSpaceGame.unitSize;
	    /** A cube with base color "leaking" through a partially transparent texture */
	    Box box = new Box( center, size/2, size/2, size/2 );
	    setName("LeakyBox");
	    setMesh( box );
	    
	    //Material boxMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
	    //boxMat.setTexture("ColorMap", am.loadTexture("Textures/Terrain/Rock2/rock.jpg"));
	    //boxMat.setColor("Color", new ColorRGBA(0.4f,1f,0.4f, 0.8f)); // green
	    //boxMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha); // activate transparency
	    
	    
        Material boxMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", new ColorRGBA(0.6f, 1, 0.6f, 0.5f));
        boxMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        //boxMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
        setMaterial(boxMat);
        setQueueBucket(Bucket.Translucent);
		
	}
	
	public Vector3f neighborBloxLocByDir( ) {
		/*
		switch( dir ) {
			case XLEFT :	return this.box.getCenter().add( new Vector3f(-size,0,0) ); 
			case XRIGHT :	return this.box.getCenter().add( new Vector3f(size,0,0) );
			case YUP :		return this.box.getCenter().add( new Vector3f(0,size,0) );
			case YDOWN :	return this.box.getCenter().add( new Vector3f(0,-size,0) );
			case ZNEAR :	return this.box.getCenter().add( new Vector3f(0,0,size) );
			case ZBACK :	return this.box.getCenter().add( new Vector3f(0,0,-size) );
		}
		*/
		
		return null;
	}
	
}
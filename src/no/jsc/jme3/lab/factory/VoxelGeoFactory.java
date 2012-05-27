package no.jsc.jme3.lab.factory;

import no.jsc.jme3.lab.AwesomeSpaceGame;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.shape.Box;
import com.jme3.util.BufferUtils;

public class VoxelGeoFactory {
	private static float unitsize = AwesomeSpaceGame.unitSize;
	
	public static Geometry createCube(String name, AssetManager am) {
		Box box = new Box( Vector3f.ZERO, unitsize/2, unitsize/2, unitsize/2 );
		
		Geometry boxGeo = new Geometry(name, box);
	    	    	    
        Material boxMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", new ColorRGBA(0.6f, 0.6f, 1.0f, 0.5f));
        boxMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        //boxMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
        boxGeo.setMaterial(boxMat);
        boxGeo.setQueueBucket(Bucket.Translucent);
     
        return boxGeo;
	}
	
	public static Mesh createW45(String name, AssetManager am ) {
		Mesh w45 = new Mesh();
		
		Vector3f [] vertices = new Vector3f[4];
		vertices[0] = new Vector3f(0,0,0);
		vertices[1] = new Vector3f(3,0,0);
		vertices[2] = new Vector3f(0,3,0);
		vertices[3] = new Vector3f(3,3,0);
		
		
		Vector2f[] texCoord = new Vector2f[4];
		texCoord[0] = new Vector2f(0,0);
		texCoord[1] = new Vector2f(1,0);
		texCoord[2] = new Vector2f(0,1);
		texCoord[3] = new Vector2f(1,1);
		
		int [] indexes = { 2,0,1, 1,3,2 };
		
		w45.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		w45.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
		w45.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
		w45.updateBound();
		return w45;
	}

}

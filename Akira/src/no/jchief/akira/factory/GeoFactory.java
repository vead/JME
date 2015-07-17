package no.jchief.akira.factory;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;

public class GeoFactory {
			
    private static AssetManager am;
    
    public static void init(AssetManager assetManager) {
    	am = assetManager;
    }
    
	public static Spatial createDebugOrigin() {
		Material redMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		Material greenMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		Material blueMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		
		redMat.setColor("Color", ColorRGBA.Red);
		greenMat.setColor("Color", ColorRGBA.Green);
		blueMat.setColor("Color", ColorRGBA.Blue);
		
		Arrow originX = new Arrow(Vector3f.ZERO);
		Arrow originY = new Arrow(Vector3f.ZERO);
		Arrow originZ = new Arrow(Vector3f.ZERO);
		Geometry originXgeo = new Geometry("DebugArrowOriginX", originX);
		Geometry originYgeo = new Geometry("DebugArrowOriginY", originY);
		Geometry originZgeo = new Geometry("DebugArrowOriginZ", originZ);
		originXgeo.setMaterial(redMat);
		originYgeo.setMaterial(blueMat);
		originZgeo.setMaterial(greenMat);
		originX.setLineWidth(3);
		originY.setLineWidth(1);
		originZ.setLineWidth(3);
		originX.setArrowExtent(Vector3f.UNIT_X);
		originY.setArrowExtent(Vector3f.UNIT_Y);
		originZ.setArrowExtent(Vector3f.UNIT_Z);
		originXgeo.setLocalScale(40);
		originYgeo.setLocalScale(30);
		originZgeo.setLocalScale(40);
		
		Node node = new Node();
		node.attachChild(originXgeo);
		node.attachChild(originYgeo);
		node.attachChild(originZgeo);
		
		return node;
	}
	
	public static Geometry createDebugMark(AssetManager am, ColorRGBA color) {
		Sphere sphere = new Sphere(6, 6, 0.04f);
		Geometry debugMark = new Geometry("DebugMark", sphere);
		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		debugMark.setMaterial(material);
		
		return debugMark;
	}
	
	public static Geometry createDebugArrow(AssetManager am, ColorRGBA color, Vector3f direction) {
		Arrow arrow = new Arrow( direction );
		Geometry arrowGeo = new Geometry("DebugArrow", arrow);

		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		arrowGeo.setMaterial( material );
		
		return arrowGeo;
	}
	
	public static Geometry createDebugLine(AssetManager am, ColorRGBA color, Vector3f start, Vector3f end) {
		Line line = new Line( start, end );
		Geometry lineGeo = new Geometry("DebugLine", line);

		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		lineGeo.setMaterial( material );
		
		return lineGeo;
	}

	public static Spatial createSimpleScene(AssetManager am ) {
		// Scene node
		Node scene = new Node("Scene");
		
		// Light
		Vector3f direction = new Vector3f(-0.1f, -0.7f, -1).normalizeLocal();
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(direction);
		dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
		scene.addLight(dl);
		       
        // Helpfull origin locator
		scene.attachChild( createDebugOrigin() );
       
        // Placeholder ground (for checking collisions).
        Node mapNode = new Node("mapNode");
        Quad ground = new Quad(40f, 40f);
        Geometry groundGeo = new Geometry("groundGeo", ground);
        Material groundMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        groundMat.setColor("Color", new ColorRGBA(0.2f, 0.2f, 0.2f, 0.6f));
        groundGeo.setMaterial( groundMat );
        groundGeo.setLocalTranslation(new Vector3f(0, -0.0001f, 40f));
        groundGeo.setLocalRotation( new Quaternion().fromAngleAxis(-FastMath.PI/2, Vector3f.UNIT_X));
        mapNode.attachChild(groundGeo); 
        scene.attachChild(mapNode);

        // Grid
        Node gridNode = new Node();
        Material gridLineMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        gridLineMat.setColor("Color", new ColorRGBA(0.6f, 0.6f, 0.6f, 0.2f));
        gridLineMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        
        for (int i = 0; i < 40; i++ ) {
        	Line xGridline = new Line(new Vector3f(i,0.0001f,0), new Vector3f(i,0.0001f,40));
        	Line yGridline = new Line(new Vector3f(0,0.0001f,i), new Vector3f(40,0.0001f,i));
        	Geometry xGridLineGeo = new Geometry("xGridLineGeo", xGridline);
        	Geometry yGridLineGeo = new Geometry("yGridLineGeo", yGridline);
        	xGridLineGeo.setQueueBucket(Bucket.Translucent);
        	yGridLineGeo.setQueueBucket(Bucket.Translucent);
        	xGridLineGeo.setMaterial(gridLineMat);
        	yGridLineGeo.setMaterial(gridLineMat);
        	gridNode.attachChild( xGridLineGeo );
        	gridNode.attachChild( yGridLineGeo );
        }
        scene.attachChild(gridNode);
        
        return scene;
	}
	
	public static Geometry createUnitGeo( Vector3f loc, ColorRGBA color) {
		Geometry unit = new Geometry("DebugMark", new Box(0.2f, 0.4f, 0.2f));
		unit.setLocalTranslation( loc );
		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		unit.setMaterial(material);
		
		return unit;
	}
	
	public static void addWireframe( Geometry geo, AssetManager am ) {
        Mesh wireframe = geo.getMesh().clone();
        Geometry wireframeGeo = new Geometry(geo.getName() + "Wire", wireframe);
        Material wireframeMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        wireframeMat.setColor("Color", ColorRGBA.White);
        wireframeMat.getAdditionalRenderState().setWireframe(true);
        wireframeGeo.setMaterial(wireframeMat);
        wireframeGeo.setLocalTranslation( geo.getLocalTranslation() );
        wireframeGeo.setLocalRotation( geo.getLocalRotation() );
        geo.getParent().attachChild( wireframeGeo );
	}

}

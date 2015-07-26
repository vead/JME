package no.jchief.stuxblox.factory;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.MeshCollisionShape;
import com.jme3.bullet.collision.shapes.SimplexCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
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
    private static BulletAppState bas;
    
    public static void init(AssetManager assetManager, BulletAppState bulletAppState) {
    	am = assetManager;
    	bas = bulletAppState;
    }
    	
	public static Spatial createDebugOrigin() {
		Material redMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		Material yellowMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		Material blueMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		
		redMat.setColor("Color", ColorRGBA.Red);
		yellowMat.setColor("Color", ColorRGBA.Yellow);
		blueMat.setColor("Color", ColorRGBA.Blue);
		
		Arrow originX = new Arrow(Vector3f.ZERO);
		Arrow originY = new Arrow(Vector3f.ZERO);
		Arrow originZ = new Arrow(Vector3f.ZERO);
		Geometry originXgeo = new Geometry("DebugArrowOriginX", originX);
		Geometry originYgeo = new Geometry("DebugArrowOriginY", originY);
		Geometry originZgeo = new Geometry("DebugArrowOriginZ", originZ);
		originXgeo.setMaterial(redMat);
		originYgeo.setMaterial(yellowMat);
		originZgeo.setMaterial(blueMat);
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
	
	public static Geometry createDebugMark(ColorRGBA color) {
		Sphere sphere = new Sphere(6, 6, 0.04f);
		Geometry debugMark = new Geometry("DebugMark", sphere);
		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		debugMark.setMaterial(material);
		
		return debugMark;
	}
	
	public static Geometry createDebugArrow(ColorRGBA color, Vector3f direction) {
		Arrow arrow = new Arrow( direction );
		Geometry arrowGeo = new Geometry("DebugArrow", arrow);

		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		arrowGeo.setMaterial( material );
		
		return arrowGeo;
	}
	
	public static Geometry createDebugLine(ColorRGBA color, Vector3f start, Vector3f end) {
		Line line = new Line( start, end );
		Geometry lineGeo = new Geometry("DebugLine", line);

		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		lineGeo.setMaterial( material );
		
		return lineGeo;
	}

	public static Node createSimpleScene( ) {
		// Scene node
		Node scene = new Node("Scene");
		
		// Light
		Vector3f direction = new Vector3f(-0.1f, -0.7f, -1).normalizeLocal();
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(direction);
		dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
		scene.addLight(dl);
		       
        // Helpfull origin locator
		scene.attachChild(GeoFactory.createDebugOrigin());
       
        // Placeholder ground (for checking collisions).
        Node mapNode = new Node("mapNode");
        Box box = new Box(20f, 0.5f, 20f);
        Geometry groundGeo = new Geometry("groundGeo", box);
        Material groundMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        groundMat.setColor("Color", new ColorRGBA(0.2f, 0.2f, 0.2f, 0.6f));
        groundGeo.setMaterial( groundMat );
        groundGeo.setLocalTranslation(new Vector3f(20f, -0.5000f, 20f));
        //groundGeo.setLocalRotation( new Quaternion().fromAngleAxis(-FastMath.PI/2, Vector3f.UNIT_X));
        
        CollisionShape groundShape = CollisionShapeFactory.createBoxShape(groundGeo);
		RigidBodyControl groundPhy = new RigidBodyControl(groundShape, 0f);
		groundGeo.addControl( groundPhy );
		
		bas.getPhysicsSpace().add( groundPhy );   
        mapNode.attachChild( groundGeo );
        
        // A cube. Will bounce on floor.
        Geometry cubeGeo = new Geometry("cubeGeo", new Box(.5f,.5f,.5f));
        Material cubeMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        cubeMat.setColor("Color", ColorRGBA.LightGray);
        cubeGeo.setMaterial( cubeMat );
        cubeGeo.setLocalTranslation(new Vector3f(2, 10f, 8f));
        cubeGeo.setLocalRotation( new Quaternion().fromAngleAxis(-FastMath.PI/1.2f, Vector3f.UNIT_X));
        CollisionShape boxShape = CollisionShapeFactory.createBoxShape(cubeGeo);
//        System.out.println("boxShape margin: " + boxShape. );
//        boxShape.setMargin(2f);
        boxShape.setScale(new Vector3f(1f, .85f, .85f ));
//        cubeGeo.updateGeometricState();
//        cubeGeo.updateModelBound();
        RigidBodyControl cubePhy = new RigidBodyControl(boxShape, 1f);
//        cubePhy.setPhysicsLocation( cubeGeo.getLocalTranslation() );
//        cubePhy.setPhysicsRotation( cubeGeo.getLocalRotation() );

//        RigidBodyControl cubePhy = new RigidBodyControl(CollisionShapeFactory.createMeshShape(, 1f);
//        cubePhy.
        cubeGeo.addControl( cubePhy );
        bas.getPhysicsSpace().add( cubePhy );
  
        scene.attachChild(cubeGeo);
        
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
	
	public static Spatial createUnitGeo( ColorRGBA color) {
		Geometry unit = new Geometry("DebugMark", new Box(1f, 2f, 1f));
//		unit.setLocalTranslation( loc );
		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		unit.setMaterial(material);
		
		return unit;
	}
	
	public static void addWireframe( Geometry geo ) {
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

package no.jschief.lupercal.poc.waypointing;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.*;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Quad;
import no.jschief.lupercal.pcg.MapGen;
import no.jschief.lupercal.poc.waypointing.abilities.BuildAbility;
import no.jschief.lupercal.poc.waypointing.abilities.MakeWaypointAbility;
import no.jschief.lupercal.poc.waypointing.abilities.SelectAbility;
import no.jschief.lupercal.poc.waypointing.abilities.SpawnAlienAbility;
import no.jschief.lupercal.poc.waypointing.entities.Player;
import no.jschief.lupercal.poc.waypointing.hud.Hud;
import no.jschief.lupercal.poc.waypointing.util.PerfTimers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WaypointDiscovery extends SimpleApplication {
	MyInputManager input;
	RtsCam rtsCam;
	Hud hud;
	PerfTimers perftimers;
	ArrayList units;
	
	Player player;
	Node mapNode;
	MapGen mapGen;

//	public static final Quaternion YAW090    = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(0,1,0));
//	public static final Quaternion ROT_LEFT  = new Quaternion().fromAngleAxis(FastMath.PI/32,   new Vector3f(0,1,0));

	public static final float unitSize = 1f;

	@Override
	public void simpleInitApp() {
		configCamera();
		
		Logger.getLogger("").setLevel(Level.SEVERE);
		
		input = new MyInputManager( this, inputManager, rtsCam );
		hud = new Hud(guiFont, guiNode, settings.getWidth());
		perftimers = new PerfTimers( );
		
		player = new Player();
		player.addAbility(new SelectAbility("Select", this ));
		player.addAbility(new BuildAbility("Build", this));
		player.addAbility(new MakeWaypointAbility("MakeWaypoint", this));
		player.addAbility(new SpawnAlienAbility("SpawnAlien", this));
		
		
//		units.add( new Unit("Alien. WAARRGGGH.", new Vector3f(2f,0f,2f)));
		
		
		createScene();
		
		mapGen = new MapGen(this, 234813395L);
//		mapGen.volonoi();
		Long time = System.currentTimeMillis();
//		rootNode.attachChild( mapGen.volonoi() );


//		Node cn =  mapGen.createCorridor(new Vector3f(3f,0f,3f), new Vector3f(6f,0f,3f));
//		rootNode.attachChild( cn );
		
//		Material defaultMat = new Material(this.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//		defaultMat.setColor("Color", ColorRGBA.Yellow);
//		Material internalMat = new Material(this.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//		internalMat.setColor("Color", ColorRGBA.LightGray);
//		
//		Line l1 = new Line(new Vector3f(2f,0f,0f), new Vector3f(0f,0f,2f));
//		Line l2 = new Line(new Vector3f(4f,0f,3f), new Vector3f(6f,0f,6f));
//		Line l3 = new Line(new Vector3f(4.5f,0f,5f), new Vector3f(3f,0f,6f));
//		Line l4 = new Line(new Vector3f(3f,0f,2f), new Vector3f(2f,0f,3f));
//		Geometry l1Geo = new Geometry("edge", l1);
//		Geometry l2Geo = new Geometry("edge", l2);
//		Geometry l3Geo = new Geometry("edge", l3);
//		Geometry l4Geo = new Geometry("edge", l4);
//		l1Geo.setMaterial( defaultMat );
//		l2Geo.setMaterial( defaultMat );
//		l3Geo.setMaterial( defaultMat );
//		l4Geo.setMaterial( defaultMat );
////		rootNode.attachChild( l1Geo );
////		rootNode.attachChild( l2Geo );
////		rootNode.attachChild( l3Geo );
//		rootNode.attachChild( l4Geo );
//		l1Geo.setCullHint( CullHint.Never );
//		l2Geo.setCullHint( CullHint.Never );
//		l3Geo.setCullHint( CullHint.Never );
//		l4Geo.setCullHint( CullHint.Never );
//		l1Geo.updateModelBound();
//		l2Geo.updateModelBound();
//		l3Geo.updateModelBound();
////		l4Geo.updateModelBound();
//		
//		Ray ray1 = new Ray( l1.getStart(), l1.getEnd());
//		ray1.setLimit( l1.getStart().subtract( l1.getEnd()).length() );
//		Ray ray2 = new Ray( l2.getStart(), l2.getEnd());
//		ray1.setLimit( l2.getStart().subtract( l2.getEnd()).length() );
//		Ray ray3 = new Ray( l3.getStart(), l3.getEnd());
//		ray1.setLimit( l3.getStart().subtract( l3.getEnd()).length() );
//		Ray ray4 = new Ray( l4.getStart(), l4.getStart().subtract( l4.getEnd() ));
//		ray4.setLimit( l4.getStart().subtract( l4.getEnd()).length() );
//		
//		CollisionResults results = new CollisionResults();
////		if ( cn.collideWith(ray1, results) != 0 ) {
////			System.out.println("Ray1 collided");
////			l1Geo.setMaterial( internalMat );
////		}
////		if ( cn.collideWith(ray2, results) != 0 ) {
////			System.out.println("Ray2 collided");
////			l2Geo.setMaterial( internalMat );
////		}
////		if ( cn.collideWith(ray3, results) != 0 ) {
////			System.out.println("Ray3 collided");
////			l3Geo.setMaterial( internalMat );
////		}
//		if ( cn.collideWith(ray4, results) != 0 ) {
//			System.out.println("Ray4 collided " + ray4.getOrigin() + " to " + ray4.getDirection());
//			System.out.println("Ray4 direction to CN " + cn.worldToLocal(ray4.getDirection(), null));
//			
//			Geometry rayArrow = GeoFactory.createDebugArrow(this.getAssetManager(), ColorRGBA.Cyan, ray4.getDirection());
//			rayArrow.setLocalTranslation(ray4.getOrigin());
//			rootNode.attachChild( rayArrow );
////			
////			Geometry lineArrow = GeoFactory.createDebugArrow(this.getAssetManager(), ColorRGBA.Green, l4.getEnd());
////			lineArrow.setLocalTranslation(l4.getStart());
////			rootNode.attachChild( lineArrow );
//			 
//			l4Geo.setMaterial( internalMat );
//		}
//
//		for( CollisionResult cr : results ) {
//			System.out.println( cr.getGeometry().toString() );
//			System.out.println( "ContactPOint: " + cr.getContactPoint() );
//			System.out.println( "ContactNormal: " +  cr.getContactNormal() );
//			System.out.println( "Distance: " + cr.getDistance() );
//			
//			
//			Geometry geo = GeoFactory.createDebugMark(this.getAssetManager(), ColorRGBA.Blue);
//			geo.setLocalTranslation(cr.getContactPoint());
////			cr.getGeometry().getParent().attachChild( geo );
//			rootNode.attachChild( geo );
//		}
//		System.out.println("Collisions: " + results.size() );
//		rootNode.attachChild( mapGen.createCorridor(new Vector3f(10f,0f,10f), new Vector3f(14f,0f,6f)) );
//		rootNode.attachChild( mapGen.createCorridor(new Vector3f(10f,0f,10f), new Vector3f(14f,0f,14f)) );
//		rootNode.attachChild( mapGen.createCorridor(new Vector3f(10f,0f,10f), new Vector3f(6f,0f,14f)) );
//		rootNode.attachChild( mapGen.createCorridor(new Vector3f(10f,0f,10f), new Vector3f(6f,0f,6f)) );
		
		System.out.println("completed voronoi in: " + (System.currentTimeMillis() - time) + " ms");
//		System.out.println("  VORONOI generation breakdown:\n" + this.perftimers.showtimer( "VORO") );
//		rootNode.attachChild( mapGen.internalStructure() );
//		System.out.println( rootNode.getChild("InternalStructure") );
		
		inputManager.setCursorVisible(true);
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		inputManager.setCursorVisible(true);
		
//		updateMovement( tpf );
//		updateHud( tpf );
		player.update( tpf );
		
	}
	
	private void configCamera() {
//		cam.setAxes(Vector3f.UNIT_X.negate(), Vector3f.UNIT_Y, Vector3f.UNIT_Z.negate());
		inputManager.removeListener(flyCam);
		rtsCam = new RtsCam(cam, rootNode);
		rtsCam.setCenter(new Vector3f(5,0.5f,5));

		inputManager.setCursorVisible(true);
	}
	
	private void createScene() {
		// Light
		Vector3f direction = new Vector3f(-0.1f, -0.7f, -1).normalizeLocal();
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(direction);
		dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
		rootNode.addLight(dl);
		       
        // Helpfull origin locator
        rootNode.attachChild(GeoFactory.createDebugOrigin(assetManager));
        
        
        
        // Placeholder ground (for checking collisions).
        mapNode = new Node("mapNode");
        Quad ground = new Quad(40f, 40f);
        Geometry groundGeo = new Geometry("groundGeo", ground);
        Material groundMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        groundMat.setColor("Color", new ColorRGBA(0.2f, 0.2f, 0.2f, 0.6f));
        groundGeo.setMaterial( groundMat );
        groundGeo.setLocalTranslation(new Vector3f(0, -0.0001f, 40f));
        groundGeo.setLocalRotation( new Quaternion().fromAngleAxis(-FastMath.PI/2, Vector3f.UNIT_X));
        mapNode.attachChild(groundGeo); 
        rootNode.attachChild(mapNode);

        // Grid
        Node gridNode = new Node();
        Material gridLineMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
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
        rootNode.attachChild(gridNode);
	}

	public CollisionResult rayPicker(Node targetEnviroment) {
		return rayPicker( inputManager.getCursorPosition(), targetEnviroment );
	}
	
	public CollisionResult rayPicker(Vector2f click2d, Node targetEnviroment) {
        Vector3f rayOrigin = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
        Vector3f rayDirection = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(rayOrigin).normalizeLocal();

		return rayPicker(rayOrigin, rayDirection, targetEnviroment);
	}
	
	public CollisionResult rayPicker(Vector3f rayOrigin, Vector3f rayDirection, Node targetEnviroment) {
		
	    // Reset results list.
	    CollisionResults results = new CollisionResults();
	    // Aim the ray from the clicked spot forwards.
	    Ray ray = new Ray(rayOrigin, rayDirection);
	    // Collect intersections between ray and all nodes in results list.
	    targetEnviroment.collideWith(ray, results);

        return results.getClosestCollision();
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Hud getHud() {
		return hud;
	}

	public void setHud(Hud hud) {
		this.hud = hud;
	}

	public static void main(String[] args) {
		WaypointDiscovery wd = new WaypointDiscovery();
		
		wd.setShowSettings( false );
		wd.setDisplayStatView( false );
		wd.setDisplayFps( true );
		wd.start();
	}

	public Node getMapNode() {
		return mapNode;
	}

	public void setMapNode(Node mapNode) {
		this.mapNode = mapNode;
	}
//
//	public GeoFactory getGeoFactory() {
//		return geoFactory;
//	}
//
//	public void setGeoFactory(GeoFactory geoFactory) {
//		this.geoFactory = geoFactory;
//	}


    public RtsCam getRtsCam() {
        return rtsCam;
    }

    public PerfTimers getPerftimers() {
		return perftimers;
	}


}

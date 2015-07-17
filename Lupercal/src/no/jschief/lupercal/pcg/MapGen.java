package no.jschief.lupercal.pcg;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.*;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Line;
import com.jme3.terrain.geomipmap.TerrainGrid;
import com.jme3.terrain.geomipmap.TerrainGridLodControl;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.grid.FractalTileLoader;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.noise.ShaderUtils;
import com.jme3.terrain.noise.basis.FilteredBasis;
import com.jme3.terrain.noise.filter.IterativeFilter;
import com.jme3.terrain.noise.filter.OptimizedErode;
import com.jme3.terrain.noise.filter.PerturbFilter;
import com.jme3.terrain.noise.filter.SmoothFilter;
import com.jme3.terrain.noise.fractal.FractalSum;
import com.jme3.terrain.noise.modulator.NoiseModulator;
import com.jme3.texture.Texture;
import no.jschief.lupercal.poc.waypointing.GeoFactory;
import no.jschief.lupercal.poc.waypointing.WaypointDiscovery;
import no.jschief.lupercal.poc.waypointing.util.SiteType;
import no.jschief.lupercal.voronoi2.GraphEdge;
import no.jschief.lupercal.voronoi2.Site;
import no.jschief.lupercal.voronoi2.Voronoi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/*
 * 1. Generate a 
 */




public class MapGen {
	Random randomizer;
	WaypointDiscovery wd;
	
	private Material defaultMat;
	private Material internalMat;
	private Material minedMat;
	private Material wallMat;
	private Material voidMat;
	private Material minerMat;
	private ArrayList<Vector3f> roomLocations;
	
	public MapGen(WaypointDiscovery wd, long seed) {
		this.wd = wd;
		this.randomizer = new Random(seed);
		
		// Mats for painting
		defaultMat = new Material(wd.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		defaultMat.setColor("Color", ColorRGBA.Yellow);
		internalMat = new Material(wd.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		internalMat.setColor("Color", ColorRGBA.LightGray);
		minedMat = new Material(wd.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		minedMat.setColor("Color", ColorRGBA.Blue);
		wallMat = new Material(wd.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		wallMat.setColor("Color", ColorRGBA.Brown);
		voidMat = new Material(wd.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		voidMat.setColor("Color", ColorRGBA.Magenta);
		minerMat = new Material(wd.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		minerMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		minerMat.setColor("Color", new ColorRGBA(1.0f, 0f, 0f, 0.4f));
		
		roomLocations = new ArrayList<Vector3f>();

        this.endlessTerrain();
	}

    private Material mat_terrain;
    private TerrainGrid terrain;
    private float grassScale = 64;
    private float dirtScale = 16;
    private float rockScale = 128;
    private CharacterControl player3;
    private FractalSum base;
    private PerturbFilter perturb;
    private OptimizedErode therm;
    private SmoothFilter smooth;
    private IterativeFilter iterate;

    public void endlessTerrain() {
//        ScreenshotAppState state = new ScreenshotAppState();
//        wd.getStateManager().attach(state);

        // TERRAIN TEXTURE material
        this.mat_terrain = new Material(wd.getAssetManager(), "Common/MatDefs/Terrain/HeightBasedTerrain.j3md");

        // Parameters to material:
        // regionXColorMap: X = 1..4 the texture that should be appliad to state X
        // regionX: a Vector3f containing the following information:
        //      regionX.x: the start height of the region
        //      regionX.y: the end height of the region
        //      regionX.z: the texture scale for the region
        //  it might not be the most elegant way for storing these 3 values, but it packs the data nicely :)
        // slopeColorMap: the texture to be used for cliffs, and steep mountain sites
        // slopeTileFactor: the texture scale for slopes
        // terrainSize: the total size of the terrain (used for scaling the texture)
        // GRASS texture
        Texture grass = wd.getAssetManager().loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        this.mat_terrain.setTexture("region1ColorMap", grass);
//        this.mat_terrain.setVector3("region1", new Vector3f(15, 200, this.grassScale));
        this.mat_terrain.setVector3("region1", new Vector3f(10, 15, this.grassScale));

        // DIRT texture
        Texture dirt = wd.getAssetManager().loadTexture("Textures/Terrain/splat/dirt.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        this.mat_terrain.setTexture("region2ColorMap", dirt);
        this.mat_terrain.setVector3("region2", new Vector3f(0, 20, this.dirtScale));

        // ROCK texture
        Texture rock = wd.getAssetManager().loadTexture("Textures/Terrain/Rock2/rock.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        this.mat_terrain.setTexture("region3ColorMap", rock);
        this.mat_terrain.setVector3("region3", new Vector3f(198, 260, this.rockScale));

        this.mat_terrain.setTexture("region4ColorMap", rock);
        this.mat_terrain.setVector3("region4", new Vector3f(198, 260, this.rockScale));

        this.mat_terrain.setTexture("slopeColorMap", rock);
        this.mat_terrain.setFloat("slopeTileFactor", 32);

        this.mat_terrain.setFloat("terrainSize", 513);

        this.base = new FractalSum();
        this.base.setRoughness(1.0f);
        this.base.setFrequency(1.0f);
        this.base.setAmplitude(0.4f);
        this.base.setLacunarity(2.12f);
        this.base.setOctaves(8);
        this.base.setScale(0.02125f);
        this.base.addModulator(new NoiseModulator() {

            @Override
            public float value(float... in) {
                return ShaderUtils.clamp(in[0] * 0.5f + 0.5f, 0, 1);
            }
        });

        FilteredBasis ground = new FilteredBasis(this.base);

        this.perturb = new PerturbFilter();
        this.perturb.setMagnitude(0.119f);

        this.therm = new OptimizedErode();
        this.therm.setRadius(5);
        this.therm.setTalus(0.011f);

        this.smooth = new SmoothFilter();
        this.smooth.setRadius(1);
        this.smooth.setEffect(1.7f);

        this.iterate = new IterativeFilter();
        this.iterate.addPreFilter(this.perturb);
        this.iterate.addPostFilter(this.smooth);
        this.iterate.setFilter(this.therm);
        this.iterate.setIterations(1);

        ground.addPreFilter(this.iterate);

        this.terrain = new TerrainGrid("terrain", 33, 129, new FractalTileLoader(ground, 32f));
        this.terrain.setMaterial(this.mat_terrain);
        this.terrain.setLocalTranslation(0, 0, 0);
        this.terrain.setLocalScale(2f, 1f, 2f);
        wd.getRootNode().attachChild(this.terrain);
        System.out.println( ""+this.terrain.getCurrentCell() );
        System.out.println( ""+this.terrain.getChildren().size());
        System.out.println( ""+this.terrain.getMaterial() );
        System.out.println( ""+this.terrain.getBatchHint().toString() );
        System.out.println( ""+this.terrain.toString() );


        TerrainLodControl control = new TerrainGridLodControl(this.terrain, wd.getRtsCam().getCamera() );
        control.setLodCalculator(new DistanceLodCalculator(33, 2.7f)); // patch size, and a multiplier
        this.terrain.addControl(control);



        wd.getRtsCam().getCamera().setLocation(new Vector3f(0, 300, 0));

        wd.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));

    }
	
	public Node volonoi() {
//		wd.getPerftimers().mark("VORO", "START");
		Node voroNode = new Node("VolonoiNode");
		
		// Array for Voronoi points
		float[] xArray = new float[600];
		float[] yArray = new float[600];
		
		// Fill array with random points
		for ( int i = 0; i < 600; i++ ) {
			xArray[i] = (float)(randomizer.nextFloat() * (20.0  + 1.0));
			yArray[i] = (float)(randomizer.nextFloat() * (20.0  + 1.0));
		}

//		wd.getPerftimers().mark("VORO", "RNDPOINT DONE");
		
		// Generate the Voronoi diagram
		Voronoi v = new Voronoi(0.01);
		LinkedList<GraphEdge> allEdges = (LinkedList<GraphEdge>)v.generateVoronoi(xArray, yArray, -200.0f, 200.0f, -200.0f, 200.0f);

//		wd.getPerftimers().mark("VORO", "VOROEDGES DONE");
		
		// A node for Geometry used to mine thru the Voronoi
		Vector3f entrance = new Vector3f(3f,0f,3f);
		Vector2f entrance2d = new Vector2f( entrance.x, entrance.z);
		Node minerNode = this.createMinerNode( entrance );
		voroNode.attachChild( minerNode );
				
		// Draw the whole voronoi diagram.
		Iterator<GraphEdge> iterator = allEdges.iterator();
		while( iterator.hasNext() ) {
			GraphEdge ge = iterator.next();
			
			Vector3f from = new Vector3f( ge.x1, 0f, ge.y1);
			Vector3f to   = new Vector3f( ge.x2, 0f, ge.y2);
			
			Line edge = new Line(from, to);
			Geometry edgeGeo = new Geometry("edge", edge);
			edgeGeo.setMaterial( defaultMat );
			voroNode.attachChild( edgeGeo );
			edgeGeo.setCullHint( CullHint.Never );
			edgeGeo.updateModelBound();
			
			if ( this.checkIntersect( minerNode, from, to) ) {
				edgeGeo.setMaterial( minedMat );
				
				ge.hide = true;
				
				if ( ge.siteA.type == null ) {
					ge.siteA.type = SiteType.INTERIOR;
//					Geometry interiorMark = GeoFactory.createDebugMark(wd.getAssetManager(), ColorRGBA.White);
//					interiorMark.setLocalTranslation( ge.siteA.coord.x, 0f, ge.siteA.coord.y );
//					voroNode.attachChild( interiorMark );
				}
				if ( ge.siteB.type == null ) {
					ge.siteB.type = SiteType.INTERIOR;
//					Geometry interiorMark = GeoFactory.createDebugMark(wd.getAssetManager(), ColorRGBA.White);
//					interiorMark.setLocalTranslation( ge.siteB.coord.x, 0f, ge.siteB.coord.y );
//					voroNode.attachChild( interiorMark );
				}

			}

			edgeGeo.setUserData("edge", ge);
			ge.edgeGeo = edgeGeo;

		}

//		wd.getPerftimers().mark("VORO", "DRAW DONE");
		
		Site entranceSite = v.getSiteByNbr(0);
		// Draw the whole voronoi diagram.
		for ( int i = 0; i < v.getSites().length; i++) {
			Site s = v.getSiteByNbr(i);
			
			for ( Vector3f room : roomLocations ) {
				if (s.coord.subtract( new Vector2f( room.x, room.z )).length() < 2f) {
					s.closeToRoom = true;
					s.type = SiteType.INTERIOR;
				}
			}
			
			if ( s.type == SiteType.INTERIOR ) {
//				System.out.println("INTERIOR site found. Adjacent sites#: " + s.adjacentSites.size() );
				for ( int j = 0; j < s.adjacentSites.size(); j++ ) {
					Site adjS = s.adjacentSites.get(j);
//					System.out.println("     adj[" + j + "]  " + adjS.type);
					if ( adjS.type == null ) {
//						System.out.println("     adj[" + j + "]  setting WALL");
						adjS.type = SiteType.WALL;
//						Geometry interiorMark = GeoFactory.createDebugMark(wd.getAssetManager(), ColorRGBA.Orange);
//						interiorMark.setLocalTranslation( adjS.coord.x, 0f, adjS.coord.y );
//						voroNode.attachChild( interiorMark );
					}
				}
			}
			
			if ( s.coord.subtract( entrance2d ).length() < entranceSite.coord.subtract( entrance2d ).length() ) {
				entranceSite = s;
			}
		}
		

//		wd.getPerftimers().mark("VORO", "SITE CLASSIFICATION DONE");
		
		entranceSite.entrance = true;
		Geometry entranceMark = GeoFactory.createDebugMark(wd.getAssetManager(), ColorRGBA.Pink);
		entranceMark.setLocalTranslation( entranceSite.coord.x, 0f, entranceSite.coord.y );
		voroNode.attachChild( entranceMark );
		
		for (int i = 0; i < allEdges.size(); i++ ) {
			GraphEdge ge = allEdges.get(i);
//			System.out.println("processing[" + i + "]  A: " + ge.siteA.type + "   B: " + ge.siteB.type);
			if ( ge.siteA.type == SiteType.INTERIOR && ge.siteB.type == SiteType.INTERIOR)  {
				ge.edgeGeo.setMaterial( internalMat );
				ge.hide = true;
			}
			if ( ge.siteA.type == SiteType.WALL && ge.siteB.type == SiteType.WALL)  {
				ge.edgeGeo.setMaterial( wallMat );
				ge.hide = true;
			}
			if ( ge.siteA.type == null && ge.siteB.type == null)  {
				ge.edgeGeo.setMaterial( voidMat );
				ge.hide = true;
			}
			
			// Entrance siteA
			if ( ge.siteA == entranceSite && ge.siteB.type == SiteType.WALL ) {
//				ge.edgeGeo.setMaterial( voidMat );w
				ge.hide = true;
			}

			// Entrance siteB
			if ( ge.siteB == entranceSite && ge.siteA.type == SiteType.WALL ) {
//				ge.edgeGeo.setMaterial( voidMat );
				ge.hide = true;
			}
		}
		

//		wd.getPerftimers().mark("VORO", "VORONOI DONE");
		// Make an entrance
		
		return voroNode;
	}
	
	private boolean checkIntersect(Node minerNode, Vector3f from, Vector3f to) {
		boolean answer = false;		
		
		Ray ray = new Ray( from, to.subtract( from ) );
		ray.setLimit( to.subtract( from ).length() );
		CollisionResults results = new CollisionResults();

		if ( minerNode.collideWith( ray , results) > 0) {
			for( CollisionResult cr : results ) {

//				if ( !cr.getGeometry().getName().contains("Debug") ) {
				if ( cr.getGeometry().getName().contains("MinerGeo")
						&& ray.getLimit() > cr.getDistance() ) {

//					System.out.println( cr.getGeometry().toString() );
//					System.out.println( "ContactPoint: " + cr.getContactPoint() );
//					System.out.println( "ContactNormal: " +  cr.getContactNormal() );
//					System.out.println( "Distance: " + cr.getDistance() + " limit: " + ray.getLimit());

//					Geometry rayArrow = GeoFactory.createDebugArrow(wd.getAssetManager(), ColorRGBA.Cyan, to.subtract( from ).normalize().mult( from.subtract(to).length()) );
//					rayArrow.setLocalTranslation(ray.getOrigin());
//					cr.getGeometry().getParent().attachChild( rayArrow );

					Geometry geo = GeoFactory.createDebugMark(wd.getAssetManager(), ColorRGBA.Blue);
					geo.setLocalTranslation(cr.getContactPoint());
					cr.getGeometry().getParent().attachChild( geo );
					
					answer = true;
				}
			}
			
		}
				
		return answer;
	}

	private Node createMinerNode(Vector3f entrance) {
		Node n = new Node("MinerNode");
		
		n.attachChild( createCorridor(entrance, new Vector3f(6f,0f,6f)) );
		this.roomLocations.add( new Vector3f(6f,0f,6f) );
		this.roomLocations.add( new Vector3f(6f,0f,16f) );
//		n.attachChild( createRoom( new Vector3f(6f,0f,6f) ));
		n.attachChild( createCorridor(new Vector3f(6f,0f,6f), new Vector3f(11f,0f,3f)) );
		n.attachChild( createCorridor(new Vector3f(11f,0f,3f), new Vector3f(15f,0f,10f)) );
		

		return n;
	}
	
	private Geometry createCorridor(Vector3f from, Vector3f to) {
		Box miner = new Box( Vector3f.ZERO, from.subtract( to ).mult(0.5f).length(), 0.1f, 0.1f);
		Geometry minerGeo = new Geometry("CorridorMinerGeo", miner);
		
		minerGeo.setQueueBucket(Bucket.Translucent);
		minerGeo.setMaterial( minerMat );

		float angle = Vector3f.UNIT_X.angleBetween( from.subtract( to ).normalize() );

		if (from.z > to.z ) angle = -angle;

		minerGeo.setLocalRotation( new Quaternion().fromAngleAxis(angle, Vector3f.UNIT_Y) );
		minerGeo.setLocalTranslation( from.interpolate(to, 0.5f) );
		minerGeo.updateModelBound();
		
		return minerGeo;
	}

//	public Geometry makeEdgeWall(Vector3f from, Vector3f to) {
//		Quad quad = new Quad(to.subtract(from).length(), 0.5f);
//		Geometry quadGeo = new Geometry("QuadGeo", quad);
//		quadGeo.setLocalTranslation(from);
//		quadGeo.lookAt(to, Vector3f.UNIT_Y);
//		Material quadMat = new Material(wd.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//		quadMat.setColor("Color", ColorRGBA.Yellow);
//		quadGeo.setMaterial(quadMat);
//		return quadGeo;
//	}
//	
	
}

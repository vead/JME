package no.jsc.jme3.lab; 

import com.jme3.bullet.BulletAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Triangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Plane.Side;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.WireBox;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;

import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.jsc.jme3.lab.control.*;
import no.jsc.jme3.lab.domain.Item;
import no.jsc.jme3.lab.domain.Player;
import no.jsc.jme3.lab.entity.BloxNode;
import no.jsc.jme3.lab.entity.FadeInfo;
import no.jsc.jme3.lab.entity.Spacecraft;
import no.jsc.jme3.lab.geom.Blox;
import jme3test.bullet.BombControl;

public class AwesomeSpaceGame extends SimpleApplication implements ActionListener, PhysicsCollisionListener {
	public static final Quaternion YAW090    = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(0,1,0));
	public static final Quaternion ROT_LEFT  = new Quaternion().fromAngleAxis(FastMath.PI/32,   new Vector3f(0,1,0));

	private BulletAppState bulletAppState;
	private boolean debugWireframe = false;
	//character
	ForceCharacterControl characterControl;
	Player player;

	// Contruction Nodes
	ArrayList<Node> masterNodeList = new ArrayList<Node>();
	public static final float unitSize = 1f;
	private int entitySequence = 0;
	
	// Fading geometry
	ArrayList<FadeInfo> fadeout = new ArrayList<FadeInfo>();
	

	public static enum Enviroment { SPACEWALK, INTERIOR, SHIP }
	Enviroment enviroment = Enviroment.INTERIOR;
	//temp vectors
	Vector3f walkDirection = new Vector3f();
	//terrain
	TerrainQuad terrain;
	RigidBodyControl terrainPhysicsNode;
	//Materials
	Material matRock;
	Material matWire;
	Material matBullet;
	float airTime = 0;
	//Input states
	boolean left = false, right = false, forward = false, back = false, up = false, down = false, ctrl = false;
	Vector3f camDirection;
	//bullet + explosion
	Sphere bullet;
	SphereCollisionShape bulletCollisionShape;
	ParticleEmitter effect;
	//brick wall
	Box brick;
	float bLength = 0.8f;
	float bWidth = 0.4f;
	float bHeight = 0.4f;
	FilterPostProcessor fpp;
	// Debug visuals
	Geometry debugMark1;
	Geometry debugMark2;
	Geometry debugMark3;
	Geometry debugAxisX;
	Geometry debugAxisY;
	Geometry debugAxisZ;
	Geometry debugArrow1;
	Geometry debugArrow2;
	Geometry debugArrow3;
	Geometry debugGrid;

	//HUD
	BitmapText linVelBT_res;
	BitmapText globXVelBT_res;
	BitmapText globYVelBT_res;
	BitmapText globZVelBT_res;
	BitmapText activeItem_res;
	ColorRGBA green = new ColorRGBA(0.3f,1.0f,0.3f,1f);
	ColorRGBA red = new ColorRGBA(0.7f,0.3f,0.3f,1f);
	DecimalFormat df = new DecimalFormat("#####.##");

	public static void main(String[] args) {
		AwesomeSpaceGame asg = new AwesomeSpaceGame();
		asg.setShowSettings( false );
		asg.setDisplayStatView( false );
		asg.setDisplayFps( true );
		asg.start();
		asg.testSnaps();
	}

	@Override
	public void simpleInitApp() {
//		Logger.getLogger("com.jme").setLevel(Level.OFF);
		Logger.getLogger("").setLevel(Level.SEVERE);
		bulletAppState = new BulletAppState();
		bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);

		stateManager.attach(bulletAppState);

		configKeys();
		configFilter();
		prepareBullet();
		prepareEffect();
		prepareDebugVisuals();
		createLight();
		createSky();
		createTerrain();
		createWall();
		createCharacter();
		createHud();

	}

	private void configFilter() {
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
		fpp.addFilter(bloom);
		viewPort.addProcessor(fpp);
	}

	private void configKeys() {
		inputManager.addMapping("StrafeLeft", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("StrafeRight", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_C));
		inputManager.addMapping("EngageM1", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping("EngageM2", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

		inputManager.addMapping("Hotbar1", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("Hotbar2", new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping("Hotbar3", new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping("Hotbar4", new KeyTrigger(KeyInput.KEY_4));
		inputManager.addMapping("Hotbar5", new KeyTrigger(KeyInput.KEY_5));
		inputManager.addMapping("Hotbar6", new KeyTrigger(KeyInput.KEY_6));
		inputManager.addMapping("Hotbar7", new KeyTrigger(KeyInput.KEY_7));
		inputManager.addMapping("Hotbar8", new KeyTrigger(KeyInput.KEY_8));

		inputManager.addMapping("ctrl", new KeyTrigger(KeyInput.KEY_LCONTROL), new KeyTrigger(KeyInput.KEY_RCONTROL));
		inputManager.addMapping("debugWireframe", new KeyTrigger(KeyInput.KEY_T));
		inputManager.addMapping("Space", new KeyTrigger(KeyInput.KEY_RETURN));
		inputManager.addMapping("togglePlayerEnviro", new KeyTrigger(KeyInput.KEY_F));

		inputManager.addListener(this, "StrafeLeft", "StrafeRight", "Forward", "Back", "Up", "Down");
		inputManager.addListener(this, "ctrl", "debugWireframe", "space", "togglePlayerEnviro");
		inputManager.addListener(this, "Hotbar1", "Hotbar2", "Hotbar3", "Hotbar4", "Hotbar5", "Hotbar6", "Hotbar7", "Hotbar8");
		inputManager.addListener(this, "EngageM1", "EngageM2");

		inputManager.setCursorVisible(false);
	}

	private void prepareBullet() {
		bullet = new Sphere(32, 32, 0.4f, true, false);
		bullet.setTextureMode(TextureMode.Projected);
		bulletCollisionShape = new SphereCollisionShape(0.4f);
		matBullet = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		matBullet.setColor("Color", ColorRGBA.Green);
		matBullet.setColor("m_GlowColor", ColorRGBA.Green);
		getPhysicsSpace().addCollisionListener(this);
	}

	private void prepareEffect() {
		int COUNT_FACTOR = 1;
		float COUNT_FACTOR_F = 1f;
		effect = new ParticleEmitter("Flame", Type.Triangle, 32 * COUNT_FACTOR);
		effect.setSelectRandomImage(true);
		effect.setStartColor(new ColorRGBA(1f, 0.4f, 0.05f, (1f / COUNT_FACTOR_F)));
		effect.setEndColor(new ColorRGBA(.4f, .22f, .12f, 0f));
		effect.setStartSize(1.3f);
		effect.setEndSize(2f);
		effect.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
		effect.setParticlesPerSec(0);
		effect.setGravity(0,-5f,0);
		effect.setLowLife(.4f);
		effect.setHighLife(.5f);
		effect.setInitialVelocity(new Vector3f(0, 7, 0));
		effect.setVelocityVariation(1f);
		effect.setImagesX(2);
		effect.setImagesY(2);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
		mat.setTexture("Texture", assetManager.loadTexture("Effects/Explosion/flame.png"));
		effect.setMaterial(mat);
		effect.setLocalScale(100);
		rootNode.attachChild(effect);
	}

	/** A red ball that marks the last spot that was "hit" by the "shot". */
	protected void prepareDebugVisuals() {
		Material debugMat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		debugMat1.setColor("Color", ColorRGBA.Red);
		Material debugMat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		debugMat2.setColor("Color", ColorRGBA.Green);
		Material debugMat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		debugMat3.setColor("Color", ColorRGBA.Blue);
		Material debugMat4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		debugMat4.setColor("Color", ColorRGBA.White);
		
		Sphere sphere1 = new Sphere(6, 6, 0.04f);
		debugMark1 = new Geometry("debugMark1", sphere1);
		debugMark1.setMaterial(debugMat1);
		
		Sphere sphere2 = new Sphere(6, 6, 0.04f);
		debugMark2 = new Geometry("debugMark2", sphere2);
		debugMark2.setMaterial(debugMat2);
		
		Sphere sphere3 = new Sphere(6, 6, 0.04f);
		debugMark3 = new Geometry("debugMark3", sphere3);
		debugMark3.setMaterial(debugMat3);
		
		Arrow axisX = new Arrow(Vector3f.ZERO);
		axisX.setArrowExtent(Vector3f.UNIT_X);
		debugAxisX = new Geometry("debugArrowX", axisX);
		debugAxisX.setMaterial(debugMat1);
		Arrow axisY = new Arrow(Vector3f.ZERO);
		axisY.setArrowExtent(Vector3f.UNIT_Y);
		debugAxisY = new Geometry("debugArrowXY", axisY);
		debugAxisY.setMaterial(debugMat3);
		Arrow axisZ = new Arrow(Vector3f.ZERO);
		axisZ.setArrowExtent(Vector3f.UNIT_Z);
		debugAxisZ = new Geometry("debugArrowZ", axisZ);
		debugAxisZ.setMaterial(debugMat2);
				
		Arrow arrow1 = new Arrow(Vector3f.ZERO);
		debugArrow1 = new Geometry("debugArrow1", arrow1);
		debugArrow1.setMaterial(debugMat2);
		Arrow arrow2 = new Arrow(Vector3f.ZERO);
		debugArrow2 = new Geometry("debugArrow2", arrow2);
		debugArrow2.setMaterial(debugMat1);
		Arrow arrow3 = new Arrow(Vector3f.ZERO);
		debugArrow3 = new Geometry("debugArrow2", arrow3);
		debugArrow3.setMaterial(debugMat3);

		Arrow originX = new Arrow(Vector3f.ZERO);
		Arrow originY = new Arrow(Vector3f.ZERO);
		Arrow originZ = new Arrow(Vector3f.ZERO);
		Geometry originXgeo = new Geometry("debugArrowOriginX", originX);
		Geometry originYgeo = new Geometry("debugArrowOriginY", originY);
		Geometry originZgeo = new Geometry("debugArrowOriginZ", originZ);
		originXgeo.setMaterial(debugMat1);
		originYgeo.setMaterial(debugMat3);
		originZgeo.setMaterial(debugMat2);
		originX.setLineWidth(3);
		originY.setLineWidth(3);
		originZ.setLineWidth(3);
		originX.setArrowExtent(Vector3f.UNIT_X);
		originY.setArrowExtent(Vector3f.UNIT_Y);
		originZ.setArrowExtent(Vector3f.UNIT_Z);
		originXgeo.setLocalScale(140);
		originYgeo.setLocalScale(140);
		originZgeo.setLocalScale(140);
		originXgeo.setLocalTranslation(0.0f, 16.0f, 0.0f);
		originYgeo.setLocalTranslation(0.0f, 16.0f, 0.0f);
		originZgeo.setLocalTranslation(0.0f, 16.0f, 0.0f);
		rootNode.attachChild( originXgeo );
		rootNode.attachChild( originYgeo );
		rootNode.attachChild( originZgeo );
	}

	private void createHud() {
		ColorRGBA color = new ColorRGBA(0.4f,1.0f,0.4f,1f);

		float lineHeight = guiFont.getCharSet().getRenderedSize();

		BitmapText linVelBT = new BitmapText(guiFont, false);
		BitmapText globXVelBT = new BitmapText(guiFont, false);
		BitmapText globYVelBT = new BitmapText(guiFont, false);
		BitmapText globZVelBT = new BitmapText(guiFont, false);
		BitmapText xhair = new BitmapText(guiFont, false);
		linVelBT_res = new BitmapText(guiFont, false);
		globXVelBT_res = new BitmapText(guiFont, false);
		globYVelBT_res = new BitmapText(guiFont, false);
		globZVelBT_res = new BitmapText(guiFont, false);
		BitmapText activeItem = new BitmapText(guiFont, false);
		activeItem_res = new BitmapText(guiFont, false);

		linVelBT.setSize(lineHeight);
		globXVelBT.setSize(lineHeight);
		globYVelBT.setSize(lineHeight);
		globZVelBT.setSize(lineHeight);
		xhair.setSize(lineHeight);
		linVelBT_res.setSize(lineHeight);
		globXVelBT_res.setSize(lineHeight);
		globYVelBT_res.setSize(lineHeight);
		globZVelBT_res.setSize(lineHeight);
		activeItem.setSize(lineHeight);
		activeItem_res.setSize(lineHeight);

		linVelBT.setColor(color);   
		globXVelBT.setColor(color);   
		globYVelBT.setColor(color);   
		globZVelBT.setColor(color);
		xhair.setColor(ColorRGBA.White);
		linVelBT_res.setColor(color);   
		globXVelBT_res.setColor(color);   
		globYVelBT_res.setColor(color);   
		globZVelBT_res.setColor(color);
		activeItem.setColor(ColorRGBA.Orange);
		activeItem_res.setColor(ColorRGBA.Orange);

		linVelBT.setText(  "Velocity:");             // the text
		globXVelBT.setText("  X-Axis:");             // the text
		globYVelBT.setText("  Y-Axis:");             // the text
		globZVelBT.setText("  Z-Axis:");             // the text
		xhair.setText("+");             // the text
		activeItem.setText("ActiveItem: ");
		activeItem_res.setText("none");

		linVelBT.setLocalTranslation(   4, lineHeight * 5, 0); // position
		globXVelBT.setLocalTranslation( 4, lineHeight * 4, 0); // position
		globYVelBT.setLocalTranslation( 4, lineHeight * 3, 0); // position
		globZVelBT.setLocalTranslation( 4, lineHeight * 2, 0); // position
		xhair.setLocalTranslation( settings.getWidth()/2, settings.getHeight()/2 + lineHeight/2, 0); // position
		linVelBT_res.setLocalTranslation(   linVelBT.getLineWidth() + 10, lineHeight * 5, 0); // position
		globXVelBT_res.setLocalTranslation( linVelBT.getLineWidth() + 10, lineHeight * 4, 0); // position
		globYVelBT_res.setLocalTranslation( linVelBT.getLineWidth() + 10, lineHeight * 3, 0); // position
		globZVelBT_res.setLocalTranslation( linVelBT.getLineWidth() + 10, lineHeight * 2, 0); // position


		activeItem.setLocalTranslation( 100, lineHeight * 2, 0);
		activeItem_res.setLocalTranslation( 110 + activeItem.getLineWidth(), lineHeight * 2,0);

		//System.out.println("linVelVT.cen: " + linVelBT.toString() + " guiFont...R.size: " + guiFont.getCharSet().getRenderedSize() );

		guiNode.attachChild(linVelBT);
		guiNode.attachChild(globXVelBT);
		guiNode.attachChild(globYVelBT);
		guiNode.attachChild(globZVelBT);
		guiNode.attachChild(xhair);
		guiNode.attachChild(activeItem);
		guiNode.attachChild(activeItem_res);
		guiNode.attachChild(linVelBT_res);
		guiNode.attachChild(globXVelBT_res);
		guiNode.attachChild(globYVelBT_res);
		guiNode.attachChild(globZVelBT_res);

		Picture pic = new Picture("HUD Picture");
		pic.setImage(assetManager, "Textures/ColoredTex/Monkey.png", true);
		pic.setWidth(settings.getWidth()/2);
		pic.setHeight(settings.getHeight()/2);
		pic.setPosition(settings.getWidth()/4, settings.getHeight()/4);
		//guiNode.attachChild(pic);
	}

	private void createWall() {
		float xOff = -144;
		float zOff = -40;
		float startpt = bLength / 4 - xOff;
		float height = 6.1f;
		brick = new Box(Vector3f.ZERO, bLength, bHeight, bWidth);
		brick.scaleTextureCoordinates(new Vector2f(1f, .5f));
		for (int j = 0; j < 15; j++) {
			for (int i = 0; i < 4; i++) {
				Vector3f vt = new Vector3f(i * bLength * 2 + startpt, bHeight + height, zOff);
				addBrick(vt);
			}
			startpt = -startpt;
			height += 1.01f * bHeight;
		}
	}

	private void addBrick(Vector3f ori) {
		Geometry reBoxg = new Geometry("brick", brick);
		reBoxg.setMaterial(matRock);
		reBoxg.setLocalTranslation(ori);
		reBoxg.addControl(new RigidBodyControl(1.5f));
		reBoxg.setShadowMode(ShadowMode.CastAndReceive);
		this.rootNode.attachChild(reBoxg);
		this.getPhysicsSpace().add(reBoxg);
	}

	private void createLight() {
		Vector3f direction = new Vector3f(-0.1f, -0.7f, -1).normalizeLocal();
		DirectionalLight dl = new DirectionalLight();
		dl.setDirection(direction);
		dl.setColor(new ColorRGBA(1f, 1f, 1f, 1.0f));
		rootNode.addLight(dl);
	}

	private void createSky() {
		rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
	}

	private void createTerrain() {
		matRock = new Material(assetManager, "Common/MatDefs/Terrain/TerrainLighting.j3md");
		matRock.setBoolean("useTriPlanarMapping", false);
		matRock.setBoolean("WardIso", true);
		matRock.setTexture("AlphaMap", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));
		Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
		Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
		grass.setWrap(WrapMode.Repeat);
		matRock.setTexture("DiffuseMap", grass);
		matRock.setFloat("DiffuseMap_0_scale", 64);
		Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/dirt.jpg");
		dirt.setWrap(WrapMode.Repeat);
		matRock.setTexture("DiffuseMap_1", dirt);
		matRock.setFloat("DiffuseMap_1_scale", 16);
		Texture rock = assetManager.loadTexture("Textures/Terrain/splat/road.jpg");
		rock.setWrap(WrapMode.Repeat);
		matRock.setTexture("DiffuseMap_2", rock);
		matRock.setFloat("DiffuseMap_2_scale", 128);
		Texture normalMap0 = assetManager.loadTexture("Textures/Terrain/splat/grass_normal.jpg");
		normalMap0.setWrap(WrapMode.Repeat);
		Texture normalMap1 = assetManager.loadTexture("Textures/Terrain/splat/dirt_normal.png");
		normalMap1.setWrap(WrapMode.Repeat);
		Texture normalMap2 = assetManager.loadTexture("Textures/Terrain/splat/road_normal.png");
		normalMap2.setWrap(WrapMode.Repeat);
		matRock.setTexture("NormalMap", normalMap0);
		matRock.setTexture("NormalMap_1", normalMap2);
		matRock.setTexture("NormalMap_2", normalMap2);
		matWire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		matWire.setColor("Color", ColorRGBA.Green);

		AbstractHeightMap heightmap = null;
		try {
			//heightmap = new ImageBasedHeightMap(ImageToAwt.convert(heightMapImage.getImage(), false, true, 0), 0.25f);
			heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 0.25f);
			heightmap.load();

		} catch (Exception e) {
			e.printStackTrace();
		}

		terrain = new TerrainQuad("terrain", 65, 513, heightmap.getHeightMap());
		List<Camera> cameras = new ArrayList<Camera>();
		cameras.add(getCamera());
		TerrainLodControl control = new TerrainLodControl(terrain, cameras);
		terrain.addControl(control);
		terrain.setMaterial(matRock);
		terrain.setLocalTranslation(0.0f,-1.0f,0.0f);
		terrain.setModelBound(new BoundingBox());
		terrain.updateModelBound();
		terrain.setLocalScale(new Vector3f(2, 2, 2));

		terrainPhysicsNode = new RigidBodyControl(CollisionShapeFactory.createMeshShape(terrain), 0);
		terrain.addControl(terrainPhysicsNode);
		rootNode.attachChild(terrain);
		getPhysicsSpace().add(terrainPhysicsNode);
	}

	private void createCharacter() {
		CapsuleCollisionShape capsule = new CapsuleCollisionShape(1.5f, 2f);
		characterControl = new ForceCharacterControl(capsule, 0.1f);
		Node characterNode = new Node("CharacterNode");
		characterNode.addControl(characterControl);
		characterControl.setPhysicsLocation(new Vector3f(0, 22, 0));
		System.out.println( flyCam.getRotationSpeed() + "");
		flyCam.setRotationSpeed(1.8f);
		characterControl.setForceDamping(0.8f);

		rootNode.attachChild(characterNode);
		getPhysicsSpace().add(characterControl);

		player = new Player(this, "Vead");
		Item[] testToolItems = {new Item("CreateMasterBlox"),
				new Item("GrowBlox"),
				new Item("RayCaster"),
				new Item("PlasmaBomb")};
		player.setItems( testToolItems );
	}

	@Override
	public void simpleUpdate(float tpf) {
		//System.out.println(tpf);
		//Vector3f viewDir = characterControl.getViewDirection().normalize();

		updateMovement( tpf );
		updateHud( tpf );
		updateFadeout();
	}

	private void updateMovement(float tpf) {

		Vector3f viewDir = cam.getDirection().normalize();
		Vector3f leftDir = YAW090.mult(viewDir).normalize();
		float pushForceAmp = 3f;

		if (enviroment.equals( Enviroment.INTERIOR)) {
			viewDir.y = 0;
			walkDirection.set(0, 0, 0);
			if (left) {
				walkDirection.addLocal(leftDir.mult(0.2f));
			}
			if (right) {
				walkDirection.addLocal(leftDir.negate().mult(0.2f));
			}
			if (forward) {
				walkDirection.addLocal(viewDir.mult(0.2f));
				//character.getControllerId().setVelocityForTimeInterval(new javax.vecmath.Vector3f(0,0.5f,0), 1000f);
			}
			if (back) {
				walkDirection.addLocal(viewDir.negate().mult(0.1f));
			}
			if (up) {
				characterControl.jump();
			}
			if (!characterControl.onGround()) {
				airTime = airTime + tpf;
			} else {
				airTime = 0;
			}
			characterControl.setViewDirection(viewDir);
			characterControl.setWalkDirection(walkDirection);
		} else if (enviroment.equals( Enviroment.SPACEWALK)) {
			if (left) {
				Vector3f pushForce = cam.getLeft().clone();
				pushForce.multLocal( pushForceAmp );
				//pushForce.y = 7.5f;
				characterControl.applyCentralForce(pushForce);
			}
			if (right) {
				Vector3f pushForce = cam.getLeft().clone().negate();
				pushForce.multLocal( pushForceAmp );
				//pushForce.y = 7.5f;
				characterControl.applyCentralForce(pushForce);
			}
			if (forward) {
				Vector3f pushForce = cam.getDirection();
				pushForce.multLocal( pushForceAmp );
				//pushForce.y = 7.5f;
				characterControl.applyCentralForce(pushForce);
				//character.getControllerId().setVelocityForTimeInterval(new javax.vecmath.Vector3f(0,0.5f,0), 1000f);
			}
			if (back) {
				Vector3f pushForce = cam.getDirection().negate();
				pushForce.multLocal( pushForceAmp );
				//pushForce.y = 7.5f;
				//System.out.println("Back: " + pushForce + "   dir: " + cam.getDirection());
				characterControl.applyCentralForce(pushForce);
			}
			if (up) {
				Vector3f pushForce = cam.getDirection().cross(cam.getLeft());
				pushForce.multLocal( pushForceAmp );
				//System.out.println("Up: " + pushForce + "   dir: " + cam.getDirection() + "    left: " + cam.getLeft() + " cross: " + cam.getDirection().cross(cam.getLeft()));
				//pushForce.y = 7.5f;
				characterControl.applyCentralForce(pushForce);
			}
			if (down) {
				Vector3f pushForce = cam.getLeft().cross(cam.getDirection());
				pushForce.multLocal( pushForceAmp );
				//pushForce.y = 7.5f;
				//System.out.println("Down: " + pushForce + "   dir: " + cam.getDirection());
				characterControl.applyCentralForce(pushForce);
			}
		}
		// + 0.1f local Y
		cam.setLocation(characterControl.getPhysicsLocation());
	}

	private void updateHud(float tpf) {
		linVelBT_res.setText(Math.round(characterControl.getForceDirection().length())+"");
		globXVelBT_res.setText(df.format(characterControl.getForceDirection().x)+"");
		globYVelBT_res.setText(df.format(characterControl.getForceDirection().y)+"");
		globZVelBT_res.setText(df.format(characterControl.getForceDirection().z)+"");

		if (characterControl.getForceDirection().x > 0) {
			globXVelBT_res.setColor(green);
		} else {
			globXVelBT_res.setColor(red);
		}
		if (characterControl.getForceDirection().y > 0) {
			globYVelBT_res.setColor(green);
		} else {
			globYVelBT_res.setColor(red);
		}
		if (characterControl.getForceDirection().z > 0) {
			globZVelBT_res.setColor(green);
		} else {
			globZVelBT_res.setColor(red);
		}

	}

	// TODO Depricated
	protected void rotateCamera(float value, Vector3f axis){
		Matrix3f mat = new Matrix3f();
		float mouseSensitivity = 1f;
		mat.fromAngleNormalAxis(mouseSensitivity * value, axis);

		Vector3f up = cam.getUp();
		Vector3f left = cam.getLeft();
		Vector3f dir = cam.getDirection();

		mat.mult(up, up);
		mat.mult(left, left);
		mat.mult(dir, dir);

		Quaternion q = new Quaternion();
		q.fromAxes(left, up, dir);
		q.normalize();

		cam.setAxes(q);
		System.out.println("pre: " + dir + "   after: " + cam.getDirection());
	}

	private void togglePlayerEnviroment() {
		if ( enviroment.equals( Enviroment.INTERIOR )) {
			System.out.println("to space");
			// TODO Give a tiny up forcePush
			characterControl.setForceDamping(0.4f);
			characterControl.setJumpSpeed(0);
			characterControl.setFallSpeed(0);
			characterControl.setGravity(0);
			characterControl.setInSpace( true );
			enviroment = Enviroment.SPACEWALK;
		} else {
			System.out.println("to ground");
			// TODO Define defaults elsewhere?
			characterControl.setForceDamping(0.8f);
			characterControl.setJumpSpeed(20);
			characterControl.setFallSpeed(30);
			characterControl.setGravity(30);
			characterControl.setInSpace( false );
			enviroment = Enviroment.INTERIOR;
		}
	}
	
	@Override
	public void onAction(String binding, boolean isPressed, float tpf) {
		inputManager.setCursorVisible(false);
		if (binding.equals("StrafeLeft")) {
			if (isPressed)	left = true;	else	left = false;        	
		} else if (binding.equals("StrafeRight")) {
			if (isPressed)	right = true;	else	right = false;
		} else if (binding.equals("Forward")) {
			if (isPressed)	forward = true;	else	forward = false;
		} else if (binding.equals("Back")) {
			if (isPressed)	back = true;	else	back = false;
		} else if (binding.equals("Up")) {
			if (isPressed)	up = true;		else	up = false;
		} else if (binding.equals("Down")) {
			if (isPressed)	down = true;	else	down = false;
		} else if (binding.equals("EngageM1") && isPressed) {
			player.activateEquiped(true);
		} else if (binding.equals("EngageM2") && isPressed) {
			player.activateEquiped(false);
		}

		if ( binding.equals("Hotbar1") && isPressed ) {
			player.equipFromHotbar(0);
			activeItem_res.setText( player.getActiveItem().getName() );
		} else if ( binding.equals("Hotbar2") && isPressed ) {
			player.equipFromHotbar(1);
			activeItem_res.setText( player.getActiveItem().getName() );
		} else if ( binding.equals("Hotbar3") && isPressed ) {
			player.equipFromHotbar(2);
			activeItem_res.setText( player.getActiveItem().getName() );
		} else if ( binding.equals("Hotbar4") && isPressed ) {
			player.equipFromHotbar(3);
			activeItem_res.setText( player.getActiveItem().getName() );
		}

		if ( binding.equals("ctrl")) {
			ctrl = isPressed;
		}
		if ( binding.equals("togglePlayerEnviro") && isPressed && ctrl ) {
			togglePlayerEnviroment();
		}
		if ( binding.equals("debugWireframe") && isPressed ) {
			debugWireframe = !debugWireframe;
			if ( debugWireframe ) {
				getPhysicsSpace().enableDebug(assetManager);
			} else {
				getPhysicsSpace().disableDebug();
			}
		}
	}
	
	@Override
	public void collision(PhysicsCollisionEvent event) {
		if (event.getObjectA() instanceof BombControl) {
			final Spatial node = event.getNodeA();
			effect.killAllParticles();
			effect.setLocalTranslation(node.getLocalTranslation());
			effect.emitAllParticles();
		} else if (event.getObjectB() instanceof BombControl) {
			final Spatial node = event.getNodeB();
			effect.killAllParticles();
			effect.setLocalTranslation(node.getLocalTranslation());
			effect.emitAllParticles();
		}
	}

	public Geometry makeBlox(String name) {
		Box box = new Box( Vector3f.ZERO, unitSize/2, unitSize/2, unitSize/2 );
		
		Geometry boxGeo = new Geometry(name, box);
	    	    	    
        Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        boxMat.setColor("Color", new ColorRGBA(0.6f, 0.6f, 1.0f, 0.5f));
        boxMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        //boxMat.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
        
        
        boxGeo.setMaterial(boxMat);
        boxGeo.setQueueBucket(Bucket.Translucent);
     
        return boxGeo;
	}
	private void updateCollisionShape( Geometry geo ) {
		
	}
	
	public boolean validatePlacement2(Geometry geo) {
		float margin = 0.75f;
		ArrayList<Spatial> intersectingSpatials = new ArrayList<Spatial>();
		Transform originalTransform = geo.getLocalTransform();
		
		System.out.println("geo loc: " + geo.getLocalTranslation() );
		geo.scale( margin );
		System.out.println("geo loc: " + geo.getLocalTranslation() );
		
//		((Box)geo.getMesh()).xExtent = 
		
		
//		tempGeo.setLocalTranslation( geo.getWorldBound().getCenter() );
//		tempGeo.setLocalRotation( geo.getLocalRotation() );
//		tempGeo.setLocalTransform( geo.getLocalTransform());
//		System.out.println("tempGeo: " + tempGeo.getWorldBound() + "\ttempGeo.parent: " + tempGeo.getParent() + "\tgeo:" + geo.getWorldBound());
		
		for (Node masterNode : masterNodeList) {
//			System.out.println("[validatePlacement]\tmasterNodeList; " + masterNode.getName() );
			
			for (Spatial s : masterNode.getChildren()) {
//				System.out.println("[validatePlacement]\t\tspatial; " + s.getName() );
				if (!s.equals( geo ) && !s.getName().contains("debug")) {
					if ( s.getWorldBound().intersects( geo.getWorldBound() )) {
						intersectingSpatials.add( s );
					} else {
//						System.out.println("[validatePlacement] valid; " + s.getName() + " and " + geo.getName() );
//						System.out.println("[validatePlacement] valid; wb1 " + s.getWorldBound() + "\n\t\t\twb2 " + tempGeo.getWorldBound() );
						
					}
				} else {
//					System.out.println("[validatePlacement]\t\tIGNORED; " + s.getName() );

				}
			}
		}
		
//		geo.setLocalTransform( originalTransform );
		
		if ( !intersectingSpatials.isEmpty() ) {
			System.out.println("[validatePlacement] INVALID newGeo: " + geo.getName() + ".WB:\t" + geo.getWorldBound() );
			
			for (Spatial s : intersectingSpatials) {
			System.out.println("[validatePlacement]    intersected: " + s.getName() + ".WB:\t" + s.getWorldBound() );
				
				if ( s.getClass().equals(Geometry.class) ) {
					((Geometry)s).getMaterial().setColor("Color", new ColorRGBA( 1.0f, 0.2f, 0.2f, 0.2f));
				}
			}
			return false;
		}
		return true;
	}

	public void updateFadeout() {
		float steptime = 0.2f;
		for (FadeInfo fade : fadeout) {
			// If transparent, increase transparancy
			if ( fade.getGeo().getMaterial().isTransparent() ) {
				
			}
//			<Node>
			if ( timer.getTimeInSeconds() > fade.getCreationTime() + fade.getTotalLifetime() ) {
				fade.getGeo().removeFromParent();
			}
				
			
		}
	}
	
	public void addFadeout( Spatial s, int lifetime ) {
		if ( s.getClass().equals(Geometry.class)) {
			Geometry geo = (Geometry)s;
			fadeout.add( new FadeInfo( timer.getTimeInSeconds(), 4f, geo) );
		}
			
	}
	
	public boolean validatePlacement( Geometry sourceGeo, boolean scale ) {
		
		Transform originalTransform = sourceGeo.getLocalTransform();

		ArrayList<Geometry> intersectingGeos = new ArrayList<Geometry>();
		BoundingBox sourceBb = (BoundingBox)sourceGeo.getModelBound().clone();
		sourceBb.setCenter( sourceGeo.getLocalTranslation() );
//		BoundingBox sourceBb = new BoundingBox( (BoundingBox)sourceGeo.getModelBound() );
		
//		sourceBb.setCenter( geo.getParent().localToWorld( geo.getLocalTranslation(), null) );
//		sourceBb.setCenter( geo.getWorldTranslation() );
//		sourceBb.transform( geo.getWorldTransform() );
		
		float margin = 0.95f;
		sourceBb.setXExtent( sourceBb.getXExtent() * margin );
		sourceBb.setYExtent( sourceBb.getYExtent() * margin );
		sourceBb.setZExtent( sourceBb.getZExtent() * margin );
		WireBox sourceBbWire = new WireBox();
		sourceBbWire.fromBoundingBox( sourceBb );
		sourceBbWire.setLineWidth(3f);
		Geometry sourceBbWireGeo = new Geometry("debugSourceBbWireGeo", sourceBbWire);
        Material sourceBbWireMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        sourceBbWireMat.setColor("Color", ColorRGBA.Black);
        sourceBbWireGeo.setMaterial( sourceBbWireMat );
        sourceBbWireGeo.updateModelBound();
//	        sourceBbWireGeo.setLocalTranslation( geo.getWorldTranslation() );
//	        sourceBbWireGeo.setLocalTransform( geo.getWorldTransform() );
        sourceGeo.getParent().attachChild( sourceBbWireGeo );
	

//		bb.setCenter( geo.getWorldTranslation() );
		
//			System.out.println("[validatePlacement]\tmasterNodeList; " + masterNode.getName() );
			
		for (Spatial s : sourceGeo.getParent().getChildren()) {
//				System.out.println("[validatePlacement]\t\tspatial; " + s.getName() );
			if (!s.equals( sourceGeo ) && s.getClass().equals(Geometry.class) && !s.getName().contains("debug")) {
//					if ( geo.getW)
				Geometry targetGeo = (Geometry)s;
				BoundingBox targetBb = (BoundingBox)targetGeo.getModelBound();
				
				if ( sourceBb.intersects( targetBb ) ) {
//				if ( sourceGeo.getModelBound().intersects( targetGeo.getModelBound() ) ) {
					
					System.out.println("SOURCE WL:\t" + sourceGeo.getModelBound().toString() );
//						System.out.println("       WT:\t" + sourceBb. );
					System.out.println("TARGET WL:\t" + targetGeo.getModelBound().toString() );
//						System.out.println("       WT:\t" + sourceBb. );
					System.out.println("DIST   WT:\t" + sourceBb.distanceTo( targetBb.getCenter()  ) );
					
					WireBox targetWire = new WireBox();
					targetWire.fromBoundingBox( targetBb );
					targetWire.setLineWidth(3f);
					Geometry targetWireGeo = new Geometry("debugIntersectWireGeo", targetWire);
			        Material targetWireMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			        targetWireMat.setColor("Color", ColorRGBA.Red);
			        targetWireGeo.setMaterial( targetWireMat );
			        targetWireGeo.updateModelBound();
//				        intersectGeo.getParent().attachChild( intersecWireGeo );
//				        targetWireGeo.setLocalTranslation( targetGeo.getWorldTranslation() );
//				        targetWireGeo.setLocalTransform( targetGeo.getWorldTransform() );
			        sourceGeo.getParent().attachChild( targetWireGeo );
//				        rootNode.attachChild( intersectGeo )
					intersectingGeos.add( targetGeo );
				} else {
//						System.out.println("[validatePlacement] valid; " + s.getName() + " and " + geo.getName() );
					System.out.println("[validatePlacement] valid; sourceBb: " + sourceBb.toString() );
					System.out.println("[validatePlacement] valid; targetBb: " + targetBb.toString() );
//						.getWorldBound() + "\n\t\t\twb2 " + tempGeo.getWorldBound() );
					
				}
			} else {
//					System.out.println("[validatePlacement]\t\tIGNORED; " + s.getName() );

			}
		}
		
		
//		geo.setLocalTransform( originalTransform );
		
		if ( !intersectingGeos.isEmpty() ) {
//			System.out.println("[validatePlacement] INVALID newGeo: " + geo.getName() + ".WB:\t" + sourceBb );
//			System.out.println("[validatePlacement] \t\t\t WT: " + geo.getWorldTransform() + " WL: " + geo );
			
			for (Geometry g : intersectingGeos) {
//				System.out.println("[validatePlacement]    intersected: " + g.getName() + ".WB:\t" + g.getModelBound() );
//				g.getMaterial().setColor("Color", new ColorRGBA( 1.0f, 0.2f, 0.2f, 0.2f));
			}
			return false;
		}
		return true;
	}

	public void makeMasterBlox() {
		Vector3f inFront = characterControl.getPhysicsLocation();
		inFront.addLocal( cam.getDirection().normalize().mult(3f) );

		Node masterNode = new Node( "MasterNode" + this.entitySequence() );
		masterNode.setLocalRotation( cam.getRotation() );
		masterNode.setLocalTranslation( inFront );
		
		Geometry masterGeo = makeBlox("MasterGeo" + this.entitySequence() );
		masterGeo.getMaterial().setColor("Color", new ColorRGBA(0.6f, 1.0f, 0.6f, 0.5f));
		masterNode.attachChild( masterGeo );
//		this.materialize( masterGeo );
		
		WireBox wBox = new WireBox();
		wBox.fromBoundingBox( ((BoundingBox)masterGeo.getModelBound()) );
		Geometry wBoxGeo = new Geometry("debugMasterWire", wBox);
        Material wBoxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        wBoxMat.setColor("Color", ColorRGBA.White);
        wBoxGeo.setMaterial( wBoxMat );
//        wBoxGeo.scale(1.01f);
		wBoxGeo.updateModelBound();
		masterNode.attachChild( wBoxGeo );
				
		Spacecraft sc = new Spacecraft("MyFirstSpaceShip!" + masterNode.getName());
		sc.addPart(masterGeo.getLocalTranslation() , (byte)1);
		masterNode.setUserData("Spacecraft", sc );


		
		rootNode.attachChild( masterNode );
		masterNodeList.add( masterNode );
		
		RigidBodyControl control = new RigidBodyControl(CollisionShapeFactory.createDynamicMeshShape( masterNode ), 0f);
        control.setFriction(0.5f);
        control.setPhysicsLocation( masterNode.getWorldTranslation() );
        masterNode.addControl( control );
        this.getPhysicsSpace().add( control );
		

		System.out.println("[makeMasterBlox]    nodeRot: " + masterNode.getLocalRotation() + "    nodeRotW: " + masterNode.getWorldRotation() );
		System.out.println("[makeMasterBlox]    geoRot:  " + masterGeo.getLocalRotation() + "    geoRotW:  " + masterGeo.getWorldRotation() );
		System.out.println("[makeMasterBlox]    nodeLoc: " + masterNode.getLocalTranslation() + "    nodeLocW: " + masterNode.getWorldTranslation() );
		System.out.println("[makeMasterBlox]    geoLoc:  " + masterGeo.getLocalTranslation() + "    geoLocW:  " +masterGeo.getWorldTranslation() );

		masterNode.attachChild( debugAxisX );
		masterNode.attachChild( debugAxisY );
		masterNode.attachChild( debugAxisZ );

	}
	
	public CollisionResult rayPicker() {
		// Reset results.
		CollisionResults rayPicker = new CollisionResults();
		CollisionResult closestCollision = null;

		// Aim the ray from cam loc to cam direction.
		Ray ray = new Ray(cam.getLocation(), cam.getDirection());
		
		// Limit ray. Dont want to pick distant geometries.
		ray.setLimit(14f);
		
		// Check every masterNode.
		for (Node node : masterNodeList) {
			node.collideWith(ray, rayPicker);
			if ( rayPicker.size() > 0) {
				if ( closestCollision != null ) {
					if ( rayPicker.getClosestCollision().getDistance() < closestCollision.getDistance() ) {
						closestCollision = rayPicker.getClosestCollision();
					}
				} else {
					closestCollision = rayPicker.getClosestCollision();
				}
			}
		}
		if (closestCollision == null )
			System.out.println("[rayPicker]   no hits");
		
		return closestCollision;
	}
 
	public void rayCaster() {
		
		// Get collision
		CollisionResult collision = this.rayPicker();
		
		//	Process collision
		if ( collision != null ) {
			float dist = collision.getDistance();
			Geometry impactGeo = collision.getGeometry();
			Vector3f impactW = collision.getContactPoint();
			Vector3f impact = impactGeo.getParent().worldToLocal( impactW, null );
			
			System.out.println("[rayCaster]    impactGeo.name " + impactGeo.getName() + "   dist: " + dist );
			System.out.println("[rayCaster]    impact " + impact + "   impactW: " + impactW ); 
			System.out.println("[rayCaster]    normalLocW: " + collision.getContactNormal() );

			// Debugging visuals
			debugMark1.setLocalTranslation(impact);
			impactGeo.getParent().attachChild(debugMark1);

			debugArrow1.setLocalTranslation( impactW );
			((Arrow)debugArrow1.getMesh()).setArrowExtent( collision.getContactNormal() );
			rootNode.attachChild(debugArrow1);

			Vector3f newLoc = new Vector3f( impactW );
			newLoc.addLocal( collision.getContactNormal().mult(0.5f) );
			impactGeo.getParent().worldToLocal(newLoc, newLoc);
			newLoc = snapToGrid( newLoc );
			
			Geometry boxGeo = makeBlox("ExpGeo" + this.entitySequence() );
			boxGeo.setLocalTranslation( newLoc );
			impactGeo.getParent().attachChild( boxGeo );

			impactGeo.getParent().getControl(RigidBodyControl.class).setCollisionShape( CollisionShapeFactory.createDynamicMeshShape( impactGeo.getParent() ));
			
			WireBox wBox = new WireBox();
			wBox.fromBoundingBox( (BoundingBox)boxGeo.getModelBound() );
			Geometry wBoxGeo = new Geometry("debugExpWire", wBox);
	        Material wBoxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	        wBoxMat.setColor("Color", ColorRGBA.White);
	        wBoxGeo.setMaterial( wBoxMat );
	        // Necessary?
			wBoxGeo.updateModelBound();
			wBoxGeo.setLocalTranslation( boxGeo.getLocalTranslation() );
			impactGeo.getParent().attachChild( wBoxGeo );
				
			if ( validatePlacement(boxGeo, true) ) {
				Spacecraft sc = (Spacecraft)impactGeo.getParent().getUserData("Spacecraft");
				sc.addPart( wBoxGeo.getLocalTranslation(), (byte)1 );
			} else {
				addFadeout(boxGeo, 4000);
				addFadeout(wBoxGeo, 4000);
				boxGeo.getMaterial().setColor("Color", new ColorRGBA(1.0f, 0.6f, 0.6f, 0.5f));
			}
			System.out.println("[rayCaster]    mBound:  " + boxGeo.getModelBound() + "    volume:  " +boxGeo.getModelBound().getVolume() );
			
//				OMG?
//				jme3tools.optimize.GeometryBatchFactory.optimize(rootNode);

			debugMark2.setLocalTranslation(newLoc);
			impactGeo.getParent().attachChild(debugMark2);
			

		} else {
			rootNode.detachChild(debugMark1);
			rootNode.detachChild(debugMark2);
			rootNode.detachChild(debugMark3);
			rootNode.detachChild(debugArrow1);
			rootNode.detachChild(debugArrow2);
			rootNode.detachChild(debugArrow3);
		}
	}

	public void plasmaBomb() {
		//shootingChannel.setAnim("Dodge", 0.1f);
		//shootingChannel.setLoopMode(LoopMode.DontLoop);
		Geometry bulletg = new Geometry("bullet", bullet);
		bulletg.setMaterial(matBullet);
		bulletg.setShadowMode(ShadowMode.CastAndReceive);
		bulletg.setLocalTranslation(characterControl.getPhysicsLocation().add(cam.getDirection().mult(2)));
		RigidBodyControl bulletControl = new BombControl(bulletCollisionShape, 1);
		bulletControl.setCcdMotionThreshold(0.1f);
		bulletControl.setLinearVelocity(cam.getDirection().mult(80));
		bulletg.addControl(bulletControl);
		rootNode.attachChild(bulletg);
		getPhysicsSpace().add(bulletControl);
	}

	private PhysicsSpace getPhysicsSpace() {
		return bulletAppState.getPhysicsSpace();
	}
	
	private Vector3f snapToVisualGrid( Vector3f v ) {
		v.setX( (float) (Math.floor(v.getX() * 100) / 100) );
		v.setY( (float) (Math.floor(v.getY() * 100) / 100) );
		v.setZ( (float) (Math.floor(v.getZ() * 100) / 100) );
		return v;
	}
	
	private Vector3f snapToGrid( Vector3f v ) {
		Vector3f r = new Vector3f( v );
		float stepX = unitSize / 2, stepY = unitSize / 2, stepZ = unitSize / 2;
		
		if ( v.x < 0.0000f )	stepX = -stepX;
		if ( v.y < 0.0000f )	stepY = -stepY;
		if ( v.z < 0.0000f )	stepZ = -stepZ;
		
		v.x = ((int)(v.x + stepX) * 10) / 10;
		v.y = ((int)(v.y + stepY) * 10) / 10;
		v.z = ((int)(v.z + stepZ) * 10) / 10;
		
		System.out.println("[snapToGrid] x: " + r.x + " -> " + v.x );
		System.out.println("[snapToGrid] y: " + r.y + " -> " + v.y );
		System.out.println("[snapToGrid] z: " + r.z + " -> " + v.z );
		
		return v;
	}
	
	private void testSnaps() {
		Vector3f v1 = new Vector3f(1.81f, 19.49799f, 22.10006f);

		System.out.println( "v1:        " + snapToVisualGrid( v1 ) );

//		snapToConstructionGrid( v1 );
		System.out.println( "v1 adj:    " + v1 );
		System.out.println( "expect:    (2.01, 19.02, 22.03)" );
		
	}
	
	public int entitySequence() {
		return entitySequence++;
	}
	

}
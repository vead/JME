package no.jchief.stuxblox.unit;

import no.jchief.stuxblox.StuxBloxMain;
import no.jchief.stuxblox.factory.GeoFactory;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


public class Unit {
	private Spatial spatial;
	private Node node;
	private BetterCharacterControl control;
	private float mass = 1;
	
//	private StuxBloxMain sb;

	public Unit(StuxBloxMain sb) {
//		this.sb = sb;
		// Load any model
		spatial = GeoFactory.createUnitGeo(new ColorRGBA(0.2f, 0.8f, 0.2f, 0.6f)); // You can set the model directly to the player. (We just wanted to explicitly show that it's a spatial.)
		node = new Node("unitNode"); // You can create a new node to wrap your player to adjust the location. (This allows you to solve issues with character sinking into floor, etc.)
		spatial.setLocalTranslation(0, 2f, 0);
		spatial.scale(0.8f);
		node.attachChild(spatial); // add it to the wrapper
		node.move(3f,2f,3f); // adjust position to ensure collisions occur correctly.
//		node.setLocalScale(0.5f); // optionally adjust scale of model
		node.attachChild( GeoFactory.createDebugOrigin() );

		// setup animation:
		// control = player.getControl(AnimControl.class);
		// control.addListener(this);
		// channel = control.createChannel();
		// channel.setAnim("stand");

		control = new BetterCharacterControl(1.0f, 4f, mass); // construct
															// character. (If
															// your character
															// bounces, try
															// increasing height
															// and weight.)
		node.addControl(control); // attach to wrapper
		// set basic physical properties:
		control.setJumpForce(new Vector3f(0, 5f, 0));
//		control.setGravity(new Vector3f(0f, 1f, 0f));
		control.setViewDirection( new Vector3f(1f,0f,1f));
		control.warp(new Vector3f(5f,3f,5f)); // warp character into
		// landscape at particular location
		// add to physics state
		sb.getBulletAppState().getPhysicsSpace().add(control);
		sb.getBulletAppState().getPhysicsSpace().addAll(node);
		sb.getRootNode().attachChild(node); // add wrapper to root
	}

	public Spatial getSpatial() {
		return spatial;
	}

	public void setSpatial(Spatial spatial) {
		this.spatial = spatial;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public BetterCharacterControl getControl() {
		return control;
	}

	public void setControl(BetterCharacterControl control) {
		this.control = control;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}
	
	

}

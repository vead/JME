package no.jchief.phobos.appstate;

import java.util.HashMap;
import java.util.Iterator;

import no.jchief.phobos.PhobosMain;
import no.jchief.phobos.component.MovementComponent;
import no.jchief.phobos.component.PlayerComponent;
import no.jchief.phobos.component.PositionComponent;
import no.jchief.phobos.entity.Entity;
import no.jchief.phobos.entity.EntitySet;
import no.jchief.phobos.gameevent.InputEvent;
import no.jchief.phobos.system.EntitySystem;
import no.jchief.phobos.system.GameEventSystem;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class PlayerInputAppState extends AbstractAppState implements ActionListener {
	private HashMap<KeyTrigger, InputEvent.Type> keyMap;
	private PhobosMain pho;
	private InputManager inputManager;
	private EntitySystem entitySystem;
	private EntitySet entitySet;
	private GameEventSystem gameEventSystem;

	boolean shift;
	boolean ctrl;

	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		pho = (PhobosMain)app;
		this.inputManager = pho.getInputManager();
		this.gameEventSystem = pho.getGameEventSystem();
		this.entitySystem = pho.getEntitySystem();

		this.keyMap = new HashMap<KeyTrigger, InputEvent.Type>();

		// Get all entities that have a location, can move and is a player.
		entitySet = entitySystem.getEntitySet(PlayerComponent.class, MovementComponent.class, PositionComponent.class);


		// Let the GES know that GameEvents of type InputEvent are available.
		this.gameEventSystem.registerGenerator( InputEvent.class );

		// Default keyboard bindings
		this.defaultKeyMap();
	}

	private void defaultKeyMap() {



		keyMap.put(new KeyTrigger(KeyInput.KEY_W), InputEvent.Type.UP);
		keyMap.put(new KeyTrigger(KeyInput.KEY_A), InputEvent.Type.LEFT);
		keyMap.put(new KeyTrigger(KeyInput.KEY_S), InputEvent.Type.DOWN);
		keyMap.put(new KeyTrigger(KeyInput.KEY_D), InputEvent.Type.RIGHT);
		keyMap.put(new KeyTrigger(KeyInput.KEY_Q), InputEvent.Type.ROT_CCW);
		keyMap.put(new KeyTrigger(KeyInput.KEY_E), InputEvent.Type.ROT_CW);
		keyMap.put(new KeyTrigger(KeyInput.KEY_R), InputEvent.Type.TILTFWD);
		keyMap.put(new KeyTrigger(KeyInput.KEY_F), InputEvent.Type.TILTBACK);
		keyMap.put(new KeyTrigger(MouseInput.AXIS_WHEEL), InputEvent.Type.ZOOM);
		keyMap.put(new KeyTrigger(MouseInput.BUTTON_LEFT), InputEvent.Type.M1);
		keyMap.put(new KeyTrigger(MouseInput.BUTTON_RIGHT), InputEvent.Type.M2);
		//		
		this.initKeymapping();
	}

	private void initKeymapping() {
		for (KeyTrigger kt : keyMap.keySet()) {
			String press = keyMap.get(kt).toString();

			inputManager.addMapping(press, kt);

			inputManager.addListener(this, press);


		}

		// Add non configurable modifiers; shift and ctrl
		inputManager.addMapping("shift", new KeyTrigger(KeyInput.KEY_LSHIFT));
		inputManager.addMapping("shift", new KeyTrigger(KeyInput.KEY_RSHIFT));
		inputManager.addMapping("ctrl", new KeyTrigger(KeyInput.KEY_LCONTROL));
		inputManager.addMapping("ctrl", new KeyTrigger(KeyInput.KEY_RCONTROL));

		inputManager.addListener(this, "shift", "ctrl");

	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		System.out.println("[IS:onAction]; " + name + " isPressed: " + isPressed);

		if ( name.equals("shift")) {
			shift = isPressed;
		} else if ( name.equals("ctrl") ) {
			ctrl = isPressed;
		} else {

			entitySet.applyChanges();
			Entity entity = entitySet.getIterator().next();

			MovementComponent mc = entity.getComponent(MovementComponent.class);


			// Shift+WASD++ is camera which is not entitified properly. Use event system for now..
			if ( shift ) {

				entity.setComponent(new MovementComponent(Vector3f.ZERO, new Quaternion() )); 

				gameEventSystem.processEvent( new InputEvent( InputEvent.Type.valueOf(name), isPressed, ctrl, shift, inputManager.getCursorPosition()));
			} else {
				float moveFactor;
				if ( isPressed ) {
					moveFactor = 0.2f;
				} else {
					moveFactor = -0.2f;
				}

				switch (InputEvent.Type.valueOf(name)) {
				case LEFT :		entity.setComponent(new MovementComponent(mc.getMovement().add( new Vector3f(-1f, 0f, 0f).multLocal(moveFactor) ), 
								mc.getRotation()));
				break;
				case RIGHT :	entity.setComponent(new MovementComponent(mc.getMovement().add( new Vector3f(1f, 0f, 0f).multLocal(moveFactor) ), 
							mc.getRotation()));
				break;
				case UP :		entity.setComponent(new MovementComponent(mc.getMovement().add( new Vector3f(0f, 0f, -1f).multLocal(moveFactor) ), 
							mc.getRotation()));
				break;
				case DOWN :		entity.setComponent(new MovementComponent(mc.getMovement().add( new Vector3f(0f, 0f, 1f).multLocal(moveFactor) ), 
							mc.getRotation()));
				break;
				case ROT_CW :	entity.setComponent(new MovementComponent(mc.getMovement().add( new Vector3f(0f, 0f, 0f).multLocal(moveFactor) ), 
							mc.getRotation().add( new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,1,0)) )));
				break;
				case ROT_CCW :	entity.setComponent(new MovementComponent(mc.getMovement().add( new Vector3f(0f, 0f, 0f).multLocal(moveFactor) ), 
							mc.getRotation().add( new Quaternion().fromAngleAxis(-FastMath.PI/4,   new Vector3f(0,1,0)) )));
				break;
				default: break;

				//		            entity.setComponent(new MovementComponent(mc.getMovement().add(addVector)));
				}
			}
		}
	}
}
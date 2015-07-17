package no.jchief.akira.system;

import java.util.HashMap;

import no.jchief.akira.AkiraMain;
import no.jchief.akira.gameevent.InputEvent;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public class CameraInputSystem extends AbstractAppState implements ActionListener {
	private HashMap<KeyTrigger, InputEvent.Type> keyMap;
	private AkiraMain akira;
	private InputManager inputManager;
	private GameEventSystem gameEventSystem;

	boolean shift;
	boolean ctrl;

	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.akira = (AkiraMain)app;
		this.inputManager = akira.getInputManager();
		this.gameEventSystem = akira.getGameEventSystem();


		this.keyMap = new HashMap<KeyTrigger, InputEvent.Type>();




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
		if ( !this.isEnabled() ) return;
		
		System.out.println("[CamIS:onAction]; " + name + " isPressed: " + isPressed);

		if ( name.equals("shift")) {
			shift = isPressed;
		} else if ( name.equals("ctrl") ) {
			ctrl = isPressed;
		} else {
			gameEventSystem.processEvent( new InputEvent( InputEvent.Type.valueOf(name), false, isPressed, ctrl, shift, inputManager.getCursorPosition()));
		}
	}
}
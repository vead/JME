package no.jchief.akira.system;

import java.util.HashMap;

import no.jchief.akira.AkiraMain;
import no.jchief.akira.gameevent.InputEvent;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public class InputSystem implements ActionListener {
	
	private AkiraMain akira;
	private InputManager inputManager;
	
	private HashMap<KeyTrigger, InputEvent.Type> keyMap;
	private GameEventSystem gameEventSystem;
	private CameraInputSystem cameraInputSystem;
	private CharacterInputSystem characterInputSystem;
	
	
	
	boolean shift;
	boolean ctrl;
	
	boolean camControl;

	public InputSystem(AkiraMain app) {
		this.akira = app;
		this.inputManager = akira.getInputManager();
		this.gameEventSystem = akira.getGameEventSystem();
		this.camControl = true;

		this.keyMap = new HashMap<KeyTrigger, InputEvent.Type>();

		this.cameraInputSystem = new CameraInputSystem();
		this.characterInputSystem = new CharacterInputSystem();

		this.akira.getStateManager().attach( this.cameraInputSystem );

		// Let the GES know that GameEvents of type InputEvent are available.
		this.gameEventSystem.registerGenerator( InputEvent.class );

		// Default keyboard bindings
		this.defaultKeyMap();
	}

	private void defaultKeyMap() {
		keyMap.put(new KeyTrigger(KeyInput.KEY_ESCAPE), InputEvent.Type.MENU);
		keyMap.put(new KeyTrigger(KeyInput.KEY_TAB), InputEvent.Type.SWAPINPUT);
		keyMap.put(new KeyTrigger(KeyInput.KEY_F6), InputEvent.Type.QUICKSAVE);
		keyMap.put(new KeyTrigger(KeyInput.KEY_F9), InputEvent.Type.QUICKLOAD);
		
		
		
//		keyMap.put(new KeyTrigger(KeyInput.KEY_W), InputEvent.Type.UP);
//		keyMap.put(new KeyTrigger(KeyInput.KEY_A), InputEvent.Type.LEFT);
//		keyMap.put(new KeyTrigger(KeyInput.KEY_S), InputEvent.Type.DOWN);
//		keyMap.put(new KeyTrigger(KeyInput.KEY_D), InputEvent.Type.RIGHT);
//		keyMap.put(new KeyTrigger(KeyInput.KEY_Q), InputEvent.Type.ROT_CCW);
//		keyMap.put(new KeyTrigger(KeyInput.KEY_E), InputEvent.Type.ROT_CW);
//		keyMap.put(new KeyTrigger(KeyInput.KEY_R), InputEvent.Type.TILTFWD);
//		keyMap.put(new KeyTrigger(KeyInput.KEY_F), InputEvent.Type.TILTBACK);
//		keyMap.put(new KeyTrigger(MouseInput.AXIS_WHEEL), InputEvent.Type.ZOOM);
//		keyMap.put(new KeyTrigger(MouseInput.BUTTON_LEFT), InputEvent.Type.M1);
//		keyMap.put(new KeyTrigger(MouseInput.BUTTON_RIGHT), InputEvent.Type.M2);
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
		} else if ( name.equals("SWAPINPUT") && isPressed ) {
			if ( camControl ) {
				System.out.println("[IS:onAction] SWAPPING TO CHARCONTROL");
				this.cameraInputSystem.setEnabled( false );
				this.characterInputSystem.setEnabled( true );
				akira.getStateManager().detach( this.cameraInputSystem );
				akira.getStateManager().attach( this.characterInputSystem );
				camControl = true;
			} else {
				System.out.println("[IS:onAction] SWAPPING TO CAMCONTROL");
				this.cameraInputSystem.setEnabled( true );
				this.characterInputSystem.setEnabled( false );
				akira.getStateManager().detach( this.characterInputSystem );
				akira.getStateManager().attach( this.cameraInputSystem );
				camControl = false;
			}
			gameEventSystem.processEvent( new InputEvent( InputEvent.Type.valueOf(name), false, isPressed, ctrl, shift, inputManager.getCursorPosition()));
		}
	}
}
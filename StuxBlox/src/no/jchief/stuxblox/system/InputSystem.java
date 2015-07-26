package no.jchief.stuxblox.system;

import java.util.HashMap;

import no.jchief.stuxblox.StuxBloxMain;
import no.jchief.stuxblox.gameevent.InputEvent;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public class InputSystem implements ActionListener {
	
	private StuxBloxMain sb;
	private InputManager inputManager;
	
	private HashMap<KeyTrigger, InputEvent.Type> keyMap;
	private GameEventSystem gameEventSystem;
	private CraftInputSystem craftInputSystem;
	private CharacterInputSystem characterInputSystem;
		
	boolean shift;
	boolean ctrl;
	
	boolean craftControl;

	public InputSystem(StuxBloxMain app) {
		this.sb = app;
		this.inputManager = sb.getInputManager();
		this.gameEventSystem = sb.getGameEventSystem();
		this.craftControl = false;

		this.keyMap = new HashMap<KeyTrigger, InputEvent.Type>();

		this.craftInputSystem = new CraftInputSystem();
		this.characterInputSystem = new CharacterInputSystem();

		this.sb.getStateManager().attach( this.characterInputSystem );

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
			if ( craftControl ) {
				System.out.println("[IS:onAction] SWAPPING TO CHARCONTROL");
				this.craftInputSystem.setEnabled( false );
				this.characterInputSystem.setEnabled( true );
				sb.getStateManager().detach( this.craftInputSystem );
				sb.getStateManager().attach( this.characterInputSystem );
				craftControl = true;
			} else {
				System.out.println("[IS:onAction] SWAPPING TO CRAFTCONTROL");
				this.craftInputSystem.setEnabled( true );
				this.characterInputSystem.setEnabled( false );
				sb.getStateManager().detach( this.characterInputSystem );
				sb.getStateManager().attach( this.craftInputSystem );
				craftControl = false;
			}
			gameEventSystem.processEvent( new InputEvent( InputEvent.Type.valueOf(name), isPressed, ctrl, shift, inputManager.getCursorPosition()));
		}
	}
}
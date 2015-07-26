package no.jchief.stuxblox.system;

import java.util.HashMap;

import no.jchief.stuxblox.StuxBloxMain;
import no.jchief.stuxblox.gameevent.InputEvent;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public class CharacterInputSystem extends AbstractAppState implements ActionListener {
	private HashMap<KeyTrigger, InputEvent.Type> keyMap;
	private StuxBloxMain sb;
	private InputManager inputManager;
	private GameEventSystem gameEventSystem;
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		this.sb = (StuxBloxMain)app;
		this.inputManager = sb.getInputManager();
		this.gameEventSystem = sb.getGameEventSystem();

		this.keyMap = new HashMap<KeyTrigger, InputEvent.Type>();

		// Let the GES know that GameEvents of type InputEvent are available.
		this.gameEventSystem.registerGenerator( InputEvent.class );

		// Default keyboard bindings
		this.defaultKeyMap();
	}

	private void defaultKeyMap() {
		
		keyMap.put(new KeyTrigger(KeyInput.KEY_W), InputEvent.Type.FORWARD);
		keyMap.put(new KeyTrigger(KeyInput.KEY_A), InputEvent.Type.LEFT);
		keyMap.put(new KeyTrigger(KeyInput.KEY_S), InputEvent.Type.BACK);
		keyMap.put(new KeyTrigger(KeyInput.KEY_D), InputEvent.Type.RIGHT);
		keyMap.put(new KeyTrigger(KeyInput.KEY_SPACE), InputEvent.Type.UP);
		keyMap.put(new KeyTrigger(KeyInput.KEY_C), InputEvent.Type.DOWN);
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
	}

	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if ( !this.isEnabled() ) return;
		System.out.println("[CharIS:onAction]; " + name + " isPressed: " + isPressed);

		gameEventSystem.processEvent( new InputEvent( InputEvent.Type.valueOf(name),
													  isPressed,
													  sb.getInputSystem().ctrl,
													  sb.getInputSystem().shift,
													  inputManager.getCursorPosition())
		);
		System.out.println("[CharIS:onAction]; done");
	}
}
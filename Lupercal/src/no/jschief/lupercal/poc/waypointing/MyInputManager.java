package no.jschief.lupercal.poc.waypointing;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import no.jschief.lupercal.poc.waypointing.RtsCam.Degree;

public class MyInputManager implements ActionListener, AnalogListener {

	boolean shift;
	boolean ctrl;
	
	WaypointDiscovery wd;
	InputManager inputManager;
	RtsCam rtsCam;
	
	public MyInputManager(WaypointDiscovery wd, InputManager inputmanager, RtsCam rtsCam) {
		this.wd = wd;
		this.inputManager = inputmanager;
		this.rtsCam = rtsCam;
		
		this.configKeys();
	}
	
	@Override
	public void onAction(String binding, boolean isPressed, float tpf) {
//		System.out.println("onAciton; " + binding);
		
//		inputManager.setCursorVisible(true);
		
		int press = isPressed ? 1 : 0;

		char sign = binding.charAt(0);
		if ( sign == '-') {
			press = -press;
		} else if (sign != '+') {
//			return;
		}
		if ( sign == '-' || sign == '+' ) {
			Degree deg = Degree.valueOf(binding.substring(1));
			rtsCam.setDirection(deg.ordinal(), press);
		}
		
		if (binding.equals("EngageM1") && isPressed) {
			wd.getPlayer().activateCurrentAbility( inputManager.getCursorPosition(), false );
		} else if (binding.equals("EngageM2") && isPressed) {
			wd.getPlayer().activateCurrentAbility( inputManager.getCursorPosition(), true );
		}

		if ( binding.equals("Ability1") && isPressed ) {
			System.out.println("A1");
			wd.getPlayer().equipAbility( 1 );
		} else if ( binding.equals("Ability2") && isPressed ) {
			wd.getPlayer().equipAbility( 2 );
		} else if ( binding.equals("Ability3") && isPressed ) {
			wd.getPlayer().equipAbility( 3 );
		} else if ( binding.equals("Ability4") && isPressed ) {
			wd.getPlayer().equipAbility( 4 );
		}

		if ( binding.equals("ctrl")) {
			ctrl = isPressed;
		}
		if ( binding.equals("shift")) {
			shift = isPressed;
		}
		if ( binding.equals("debugModeWaypoints") && isPressed ) {
		}
		if ( binding.equals("debugModeCosts") && isPressed ) {
		}
	}

	private void configKeys() {
		
		inputManager.addMapping("-SIDE", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("+SIDE", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("+FWD", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("-FWD", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("+ROTATE", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("-ROTATE", new KeyTrigger(KeyInput.KEY_E));
		inputManager.addMapping("+TILT", new KeyTrigger(KeyInput.KEY_R));
		inputManager.addMapping("-TILT", new KeyTrigger(KeyInput.KEY_F));
		inputManager.addMapping("-DISTANCE", new KeyTrigger(KeyInput.KEY_Z));
		inputManager.addMapping("+DISTANCE", new KeyTrigger(KeyInput.KEY_X));
		
		inputManager.addMapping("EngageM1", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
		inputManager.addMapping("EngageM2", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

		inputManager.addMapping("Ability1", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addMapping("Ability2", new KeyTrigger(KeyInput.KEY_2));
		inputManager.addMapping("Ability3", new KeyTrigger(KeyInput.KEY_3));
		inputManager.addMapping("Ability4", new KeyTrigger(KeyInput.KEY_4));

		inputManager.addMapping("ctrl", new KeyTrigger(KeyInput.KEY_LCONTROL), new KeyTrigger(KeyInput.KEY_RCONTROL));
		inputManager.addMapping("shift", new KeyTrigger(KeyInput.KEY_LSHIFT), new KeyTrigger(KeyInput.KEY_RSHIFT));
		
		inputManager.addMapping("debugModeWaypoints", new KeyTrigger(KeyInput.KEY_T));
		inputManager.addMapping("debugModeCosts", new KeyTrigger(KeyInput.KEY_U));

		inputManager.addListener(this, "+SIDE", "+FWD", "+ROTATE", "+TILT", "+DISTANCE");
		inputManager.addListener(this, "-SIDE", "-FWD", "-ROTATE", "-TILT", "-DISTANCE");
		
		inputManager.addListener(this, "ctrl", "shift", "debugModeWaypoints", "debugModeCosts");
		inputManager.addListener(this, "Ability1", "Ability2", "Ability3", "Ability4");
		inputManager.addListener(this, "EngageM1", "EngageM2");
	}

	public boolean isShift() {
		return shift;
	}

	public void setShift(boolean shift) {
		this.shift = shift;
	}

	public boolean isCtrl() {
		return ctrl;
	}

	public void setCtrl(boolean ctrl) {
		this.ctrl = ctrl;
	}

	@Override
	public void onAnalog(String name, float value, float tpf) {
		// TODO Auto-generated method stub
		
	}
	
	

}

package no.jchief.thermal.event;

import com.jme3.math.Vector2f;

public class InputEvent extends AbstractGameEvent {

	public static enum Type {
		UP,
		DOWN,
		LEFT,
		RIGHT,
		ROT_CW,
		ROT_CCW,
		TILTFWD,
		TILTBACK,
		IN,
		OUT,
		ZOOM,
		M1,
		M2
	}
	
	public Type type;
	public boolean pressed;
	public boolean ctrl;
	public boolean shift;
	public Vector2f cursor2f;

	public InputEvent(Type ie, boolean isPressed, boolean ctrl, boolean shift, Vector2f cursor2f) {
		super();
		this.type = ie;
		this.pressed = isPressed;
		this.ctrl = ctrl;
		this.shift = shift;
		this.cursor2f = cursor2f;
	}
	
	
	public String toString() {
		return "[InputEvent:toString] type: " + type + " on: " + pressed + " ctrl: " + ctrl + " shift: " + shift + " cursor2f: " + cursor2f.toString();
	}
	

}

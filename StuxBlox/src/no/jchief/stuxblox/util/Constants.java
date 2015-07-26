package no.jchief.stuxblox.util;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class Constants {

	public static final Quaternion YAW090    = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(0,1,0));
	public static final Quaternion ROT_LEFT  = new Quaternion().fromAngleAxis(FastMath.PI/32,   new Vector3f(0,1,0));
	public static final float unitSize = 1f;
}

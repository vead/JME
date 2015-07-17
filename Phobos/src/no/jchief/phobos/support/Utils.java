/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.support;

import com.jme3.math.Vector3f;

/**
 *
 * @author Karsten
 */
public class Utils {
    
    public static float getAngle(Vector3f a, Vector3f b)
    {
        double dy = a.z - b.z;
        double dx = a.x - b.x;
        return (float) (Math.atan2(dx, dy));
    }
    
    
	public static float clamp(float min, float value, float max) {
		if ( value < min ) {
			return min;
		} else if ( value > max ) {
			return max;
		} else {
			return value;
		}
	}
    
}

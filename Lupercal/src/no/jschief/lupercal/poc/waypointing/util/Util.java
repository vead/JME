package no.jschief.lupercal.poc.waypointing.util;


import com.jme3.math.Vector3f;
import no.jschief.lupercal.poc.waypointing.WaypointDiscovery;

public class Util {
	
	public static void snapToGridLocal( Vector3f v ) {
		v = snapToGrid( v );
	}
	
	public static Vector3f snapToGrid( Vector3f v ) {
		
		float unitSize = WaypointDiscovery.unitSize;
		float stepX = unitSize / 2, stepY = unitSize / 2, stepZ = unitSize / 2;
		
		if ( v.x < 0.0000f )	stepX = -stepX;
		if ( v.y < 0.0000f )	stepY = -stepY;
		if ( v.z < 0.0000f )	stepZ = -stepZ;
		
		v.x = ((int)(v.x + stepX) * 10) / 10;
		v.y = ((int)(v.y + stepY) * 10) / 10;
		v.z = ((int)(v.z + stepZ) * 10) / 10;
		
//		System.out.println("[UTIL:snapToGrid] x: " + r.x + " -> " + v.x );
//		System.out.println("[UTIL:snapToGrid] y: " + r.y + " -> " + v.y );
//		System.out.println("[UTIL:snapToGrid] z: " + r.z + " -> " + v.z );
		
		return v;
	}
}

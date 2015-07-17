package no.jchief.thermal.component;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class PositionComponent extends AbstractComponent {
	Vector3f position;
	Quaternion rotation;
	
	public PositionComponent(Vector3f position, Quaternion rotation) {
		super();
		this.position = position;
		this.rotation = rotation;
	}
}

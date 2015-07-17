package no.jchief.thermal.event;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

import no.jchief.thermal.component.GeoComponent;

public class GeoComponentDeltaEvent extends GeoComponent  {

	public GeoComponentDeltaEvent(Vector3f position, Quaternion rotation,
			Mesh mesh, Material mat, ColorRGBA color, Node rootNode) {
		super(position, rotation, mesh, mat, color, rootNode);
		// TODO Auto-generated constructor stub
	}
	
}

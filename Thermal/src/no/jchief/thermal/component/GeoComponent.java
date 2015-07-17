package no.jchief.thermal.component;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

public class GeoComponent extends AbstractComponent {

	public Geometry geometry;

	public GeoComponent(Vector3f position, Quaternion rotation, Mesh mesh, Material mat, ColorRGBA color, Node rootNode) {
		super();
		
		geometry = new Geometry("someGeoComp", mesh);
		geometry.setLocalTranslation( position );
		geometry.setLocalRotation( rotation );
				
		mat.setColor("Color", color);
		geometry.setMaterial( mat );
		rootNode.attachChild( geometry );
				
        Mesh wireframe = geometry.getMesh().clone();
        Geometry wireframeGeo = new Geometry(geometry.getName() + "Wire", wireframe);
        Material wireframeMat = mat.clone();
        wireframeMat.setColor("Color", ColorRGBA.White);
        wireframeMat.getAdditionalRenderState().setWireframe(true);
        wireframeGeo.setMaterial(wireframeMat);
        wireframeGeo.setLocalTranslation( geometry.getLocalTranslation() );
        wireframeGeo.setLocalRotation( geometry.getLocalRotation() );
        geometry.getParent().attachChild( wireframeGeo );
    }
}

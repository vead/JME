package no.jschief.lupercal.poc.waypointing;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;

public class GeoFactory {
			
	public static Spatial createDebugOrigin(AssetManager am) {
		Material redMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		Material greenMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		Material blueMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		
		redMat.setColor("Color", ColorRGBA.Red);
		greenMat.setColor("Color", ColorRGBA.Green);
		blueMat.setColor("Color", ColorRGBA.Blue);
		
		Arrow originX = new Arrow(Vector3f.ZERO);
		Arrow originY = new Arrow(Vector3f.ZERO);
		Arrow originZ = new Arrow(Vector3f.ZERO);
		Geometry originXgeo = new Geometry("DebugArrowOriginX", originX);
		Geometry originYgeo = new Geometry("DebugArrowOriginY", originY);
		Geometry originZgeo = new Geometry("DebugArrowOriginZ", originZ);
		originXgeo.setMaterial(redMat);
		originYgeo.setMaterial(blueMat);
		originZgeo.setMaterial(greenMat);
		originX.setLineWidth(3);
		originY.setLineWidth(1);
		originZ.setLineWidth(3);
		originX.setArrowExtent(Vector3f.UNIT_X);
		originY.setArrowExtent(Vector3f.UNIT_Y);
		originZ.setArrowExtent(Vector3f.UNIT_Z);
		originXgeo.setLocalScale(40);
		originYgeo.setLocalScale(30);
		originZgeo.setLocalScale(40);
		
		Node node = new Node();
		node.attachChild(originXgeo);
		node.attachChild(originYgeo);
		node.attachChild(originZgeo);
		
		return node;
	}
	
	public static Geometry createDebugMark(AssetManager am, ColorRGBA color) {
		Sphere sphere = new Sphere(6, 6, 0.04f);
		Geometry debugMark = new Geometry("DebugMark", sphere);
		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		debugMark.setMaterial(material);
		
		return debugMark;
	}
	
	public static Geometry createDebugArrow(AssetManager am, ColorRGBA color, Vector3f direction) {
		Arrow arrow = new Arrow( direction );
		Geometry arrowGeo = new Geometry("DebugArrow", arrow);

		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		arrowGeo.setMaterial( material );
		
		return arrowGeo;
	}
	
	public static Geometry createDebugLine(AssetManager am, ColorRGBA color, Vector3f start, Vector3f end) {
		Line line = new Line( start, end );
		Geometry lineGeo = new Geometry("DebugLine", line);

		Material material = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color);
		lineGeo.setMaterial( material );
		
		return lineGeo;
	}

	public static void addWireframe( Geometry geo, AssetManager am ) {

	}

}

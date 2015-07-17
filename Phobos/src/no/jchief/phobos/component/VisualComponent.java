/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.component;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Mesh;

/**
 *
 * @author Karsten
 */
public class VisualComponent extends Component {
    
	private boolean basicMesh;
    private String assetName;
    private Mesh mesh;
    private Material mat;
    
    public VisualComponent(boolean basicMesh, String assetName, Mesh mesh, Material mat, ColorRGBA color ) {
    	this.basicMesh = basicMesh;
        this.assetName = assetName;
        this.mesh = mesh;
        this.mat = mat;
        
        this.mat.setColor("Color", color);
    }
    
    public String getAssetName()
    {
        return assetName;
    }

	public boolean isBasicMesh() {
		return basicMesh;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public Material getMat() {
		return mat;
	}
   
	
}

package no.jschief.lupercal.voronoi2;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.scene.Geometry;

import java.io.IOException;

public class GraphEdge implements Savable {
    public float x1, y1, x2, y2;

    public int siteAnr;
    public int siteBnr;
    
    public Site siteA;
    public Site siteB;
    
    public boolean hide;
    
    public Geometry edgeGeo;
    
    public String toString() {
    	return x1 + " " + y1 + " to " + x2 + " " + y2;
    }

	@Override
	public void write(JmeExporter ex) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void read(JmeImporter im) throws IOException {
		// TODO Auto-generated method stub
		
	}
}

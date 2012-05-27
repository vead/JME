package no.jsc.jme3.lab.entity;

import java.io.IOException;
import java.util.HashMap;

import no.jsc.jme3.lab.AwesomeSpaceGame;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class Spacecraft implements Savable {
	private String name;
//	private byte[][][] structure = new byte[500][500][500];
	private HashMap<int[], Part> struct = new HashMap<int[], Part>();
	private final float unitsize = AwesomeSpaceGame.unitSize;
	
	public Spacecraft(String name) {
		this.name = name;
//		struct.containsKey(key)
	}
	
	public void addPart( Vector3f v, byte type ) {
//		v.x 6.0      unitsize 0.5		x = 12
//		v.x 5.5      unitsize 0.5		x = 2.5
//		v.x 0.5      unitsize 0.5		x = 1      
		System.out.println("[Spacecraft.addPart] type: " + type + " v.x: " + v.x + " v.y: " + v.y + " v.z: " + v.z + "\nvector: " + v.toString() + " toArray: " ) ;
		
		this.struct.put(getKeyFromV(v), new Part(type));
	}
	
	public Part getTypeByArray(int x, int y, int z) {
		return struct.get( getKeyFromInts(x, y, z) );
	}
	

	public Part getPartByVector(Vector3f v) {  
		return struct.get( getKeyFromV(v) ); 
	}
	
	private int[] getKeyFromV(Vector3f v) {
		int[] key = new int[3];
		key[0] = (int) (v.x / unitsize);
		key[1] = (int) (v.y / unitsize);
		key[2] = (int) (v.z / unitsize);
		return key;
	}
	
	private int[] getKeyFromInts(int x, int y, int z) {
		int[] key = { x, y, z };
		return key;
	}
	
	public String toString() {
		String retStr = "[Spacecraft]    name: " + this.name;
		return retStr;
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

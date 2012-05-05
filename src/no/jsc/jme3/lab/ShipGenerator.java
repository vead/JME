package no.jsc.jme3.lab;

import java.util.Random;

import com.jme3.scene.Node;


/*
 *  |                |
 *  |                |
 *  | x              | x-end
 *  |         y      |
 *  |________________|
 * 
 * 
 * 1. Random point in X-axis.
 * 2. Grow towards x-end
 */
public class ShipGenerator {
	
	private enum Stage { BASE, INTERNAL_CONST }
	private long seed;
	private int xlength;
	private int ywidth;
	private int zheight;
	
	// Version of ShipGen. Existing ship must be recreated using same version.
	private int VERSION;
	
	private Random randomizer;
	
	public ShipGenerator( long seed, int xlength, int ywidth, int zheight  ) {
		this.seed = seed;
		this.xlength = xlength;
		this.ywidth = ywidth;
		this.zheight = zheight;
		
	      Random randomizer = new Random();

	      //set the seed of aRandom to 1015
	      randomizer.setSeed(seed);
	      //randomizer.nextInt(n);

	      //the number generated will not change
	      //since the seed is always the same
		  for (int x = 0; x < 10; x++) {
			  System.out.println("RND: " + randomizer.nextInt());
		  }
	}

	public Node genIt() {
		Node ship = new Node("SHIP-" + seed);
		ship.attachChild( buildBase() );
		return ship;
	}
	
	private Node buildBase() {
	    // create a floor
//	    mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//	    mat.setTexture("ColorMap", assetManager.loadTexture("Interface/Logo/Monkey.jpg"));
//	    Geometry ground = new Geometry("ground", new Quad(50, 50));
//	    ground.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
//	    ground.setLocalTranslation(-25, -1, 25);
//	    ground.setMaterial(mat);
//	    rootNode.attachChild(ground);
		for (int x = 0; x < xlength; x++) {
			for (int y = 0; y < ywidth; y++) {
				
			}	
		}
		return null;
	}
}

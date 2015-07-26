package no.jchief.stuxblox;

import java.util.logging.Level;
import java.util.logging.Logger;

import no.jchief.stuxblox.factory.GeoFactory;
import no.jchief.stuxblox.system.GameEventSystem;
import no.jchief.stuxblox.system.InputSystem;
import no.jchief.stuxblox.unit.Player;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

public class StuxBloxMain extends SimpleApplication {
	private GameEventSystem gameEventSystem;
	private Player player;
	private InputSystem inputSystem;
	private BulletAppState bulletAppState;

	@Override
	public void simpleInitApp() {

		// Configure logger
		Logger.getLogger("").setLevel(Level.SEVERE);
		
		// Init graphics
		initFilter();

		// Create systems
		gameEventSystem = new GameEventSystem();
		inputSystem = new InputSystem( this );
		
		// Physics
		bulletAppState = new BulletAppState();
		bulletAppState.setThreadingType(BulletAppState.ThreadingType.SEQUENTIAL);
		stateManager.attach(bulletAppState);
		bulletAppState.setDebugEnabled(true);
//		bulletAppState.getPhysicsSpace().setGravity( Vector3f.UNIT_Y.mult(-0.5f) );

		// Initialize factories
		GeoFactory.init(assetManager, bulletAppState);
		
		// Init player
		player = new Player(this);
		
		// FPScam
		flyCam.setMoveSpeed(15);
		cam.setLocation(new Vector3f(16f,18f,24f));
		cam.lookAt(new Vector3f(12f,0f,8f), Vector3f.UNIT_Y);

		// Init simple scene for reference while in dev.
		Node sceneNode = GeoFactory.createSimpleScene();
		rootNode.attachChild( sceneNode );
		viewPort.setBackgroundColor(new ColorRGBA(0.2f, 0.05f, 0.05f, 1.0f));

		// Cursor. Don't want it.
		inputManager.setCursorVisible(false);
	}

	@Override
	public void simpleUpdate(float tpf) {

	}

	// Entrypoint
	public static void main(String[] args) {
		StuxBloxMain sb = new StuxBloxMain();
		sb.setShowSettings( false );
		sb.setDisplayStatView( false );
		sb.setDisplayFps( true );
		sb.start();
	} 


	public BitmapFont getGuiFont() {
		return this.guiFont;
	}

	public AppSettings getSettings() {
		return this.settings;
	}



	public BulletAppState getBulletAppState() {
		return bulletAppState;
	}



	public GameEventSystem getGameEventSystem() {
		return this.gameEventSystem;
	}



	public InputSystem getInputSystem() {
		return this.inputSystem;
	}


    private void initFilter() {
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        BloomFilter bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bloom);
        viewPort.addProcessor(fpp);
    }

}

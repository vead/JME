package no.jchief.akira;

import java.util.logging.Level;
import java.util.logging.Logger;

import no.jchief.akira.camera.RtsCamera;
import no.jchief.akira.factory.GeoFactory;
import no.jchief.akira.system.GameEventSystem;
import no.jchief.akira.system.InputSystem;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

public class AkiraMain extends SimpleApplication {
	private GameEventSystem gameEventSystem;
	private RtsCamera rtsCam;
	private InputSystem inputSystem;

	@Override
	public void simpleInitApp() {

		// Configure logger
		Logger.getLogger("").setLevel(Level.SEVERE);

		// Create systems

		
		GeoFactory.init(assetManager);

		// Init simple scene for reference while in dev.
		rootNode.attachChild( GeoFactory.createSimpleScene(assetManager) );

		// Init the Game Systems
		gameEventSystem = new GameEventSystem();
		rtsCam = new RtsCamera( this );
		inputSystem = new InputSystem( this );

		// Player
//		rootNode.attachChild( GeoFactory.createUnitGeo( new Vector3f(), color) )
		
		
		// Background color
		viewPort.setBackgroundColor(new ColorRGBA(0.2f, 0.05f, 0.05f, 1.0f));

		// Cursor. For clicks.
		inputManager.setCursorVisible(true);	



	}



	@Override
	public void simpleUpdate(float tpf) {

	}

	// Entrypoint
	public static void main(String[] args) {
		AkiraMain akira = new AkiraMain();
		akira.setShowSettings( false );
		akira.setDisplayStatView( false );
		akira.setDisplayFps( true );
		akira.start();
	} 




	// System accessor: GameEventSystem
	public GameEventSystem getGameEventSystem() {
		return gameEventSystem;
	}

	public BitmapFont getGuiFont() {
		return this.guiFont;
	}

	public AppSettings getSettings() {
		return this.settings;
	}


}

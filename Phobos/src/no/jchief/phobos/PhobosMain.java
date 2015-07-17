package no.jchief.phobos;

import java.util.logging.Level;
import java.util.logging.Logger;

import no.jchief.phobos.appstate.CameraAppState;
import no.jchief.phobos.appstate.EntityRenderAppState;
import no.jchief.phobos.appstate.MovementAppState;
import no.jchief.phobos.appstate.PlayerInputAppState;
import no.jchief.phobos.entity.DefaultEntityData;
import no.jchief.phobos.factory.EntityFactory;
import no.jchief.phobos.factory.GeoFactory;
import no.jchief.phobos.system.EntitySystem;
import no.jchief.phobos.system.GameEventSystem;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;

public class PhobosMain extends SimpleApplication {
	private EntitySystem entitySystem;
	private GameEventSystem gameEventSystem;

	@Override
	public void simpleInitApp() {

		// Configure logger
		Logger.getLogger("").setLevel(Level.SEVERE);

		// Create systems
		entitySystem = new EntitySystem(new DefaultEntityData());
		gameEventSystem = new GameEventSystem();

		// Initialize factories
		EntityFactory.init(entitySystem, assetManager);
		GeoFactory.init(assetManager);

		// Init simple scene for reference while in dev. All Geo created here are not managed as entities.
		rootNode.attachChild( GeoFactory.createSimpleScene(assetManager) );

		//Init the Game Systems
		stateManager.attach(new EntityRenderAppState(rootNode));
		stateManager.attach(new MovementAppState());
		stateManager.attach(new PlayerInputAppState());
		stateManager.attach(new CameraAppState());
		//       stateManager.attach(new ExpiresAppState());
		//       stateManager.attach(new CollisionAppState());
		//       stateManager.attach(new EnemyAppState());

		//Creating the player entities
		EntityFactory.createNewPlayer( new Vector3f(1f, 0.4f, 1f) );
		

		EntityFactory.createNewEnemy( new Vector3f(3f, 0.4f, 6f) );
		EntityFactory.createNewEnemy( new Vector3f(5f, 0.4f, 4f) );
				
		
		
		
//       for(int x=-5;x<5;x++) {
//            for(int z=-5;z<5;z++) {
//                EntityFactory.createNewPlayer(new Vector3f(x*10,0,z*10));
//            }
//       }


		viewPort.setBackgroundColor(new ColorRGBA(0.2f, 0.05f, 0.05f, 1.0f));

		// Cursor. For clicks.
		inputManager.setCursorVisible(true);



	}



	@Override
	public void simpleUpdate(float tpf) {

	}

	// Entrypoint
	public static void main(String[] args) {
		PhobosMain pho = new PhobosMain();
		pho.setShowSettings( false );
		pho.setDisplayStatView( false );
		pho.setDisplayFps( true );
		pho.start();
	} 


	// System accessor: EntitySystem
	public EntitySystem getEntitySystem() {
		return entitySystem;
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

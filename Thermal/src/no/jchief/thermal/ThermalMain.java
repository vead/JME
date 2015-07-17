package no.jchief.thermal;


import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.jchief.thermal.component.GeoComponent;
import no.jchief.thermal.entity.Entity;
import no.jchief.thermal.factory.GeoFactory;
import no.jchief.thermal.manager.ComponentManager;
import no.jchief.thermal.manager.GeoManager;
import no.jchief.thermal.manager.LocalControlManager;
import no.jchief.thermal.system.AbstractGameSystem;
import no.jchief.thermal.system.CameraSystem;
import no.jchief.thermal.system.DebugHudSystem;
import no.jchief.thermal.system.GameEventSystem;
import no.jchief.thermal.system.InputSystem;
import no.jchief.thermal.system.PlayerMovementSystem;
import no.jchief.thermal.util.Sequencer;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Box;

public class ThermalMain extends SimpleApplication {
	InputSystem inputSystem;
	GameEventSystem gameEventSystem;
	CameraSystem cameraSystem;
	DebugHudSystem debugHudSystem;
	
	GeoManager geoManager;
	LocalControlManager localControlManager;
	
	PlayerMovementSystem playerMovementSystem;
	
	HashMap<Class<? extends AbstractGameSystem>, AbstractGameSystem> systems;
	HashMap<Class<? extends ComponentManager>, ComponentManager> managers;
	
	
	Sequencer sequencer;

	@Override
	public void simpleInitApp() {
		try {
			// Configure logger
			Logger.getLogger("").setLevel(Level.SEVERE);

			// Init Input
			gameEventSystem = new GameEventSystem();
			systems.put(GameEventSystem.class, gameEventSystem);
			inputSystem = new InputSystem( this.inputManager, this.gameEventSystem );
			inputManager.setCursorVisible(true);

			// Init simple scene
			rootNode.attachChild( GeoFactory.createSimpleScene(assetManager) );

			// Init Camera
			inputManager.removeListener(flyCam);
			cameraSystem = new CameraSystem(this, cam, rootNode);
			cameraSystem.setCenter(new Vector3f(5,0.5f,5));

			// Init HUD, for debugging purposes.
			debugHudSystem = new DebugHudSystem(guiFont, guiNode, settings.getWidth());
			systems.put(DebugHudSystem.class, debugHudSystem);

			// Init utilities
			sequencer = new Sequencer();

			// Init Managers
			geoManager = new GeoManager(this);
			managers.put(GeoManager.class, geoManager);
			
			localControlManager = new LocalControlManager(this);
			managers.put(LocalControlManager.class, localControlManager);
			
			
			// Init Systems
			playerMovementSystem = new PlayerMovementSystem(this);
			systems.put(PlayerMovementSystem.class, playerMovementSystem);
			
			// A player!
			Entity player = new Entity( sequencer.getNextId() );
			geoManager.addComponent( player,
					new GeoComponent( 
							new Vector3f(0f, 0.4f, 0f),
							new Quaternion(),
							new Box(0.2f, 0.4f, 0.2f),
							new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"),
							ColorRGBA.Blue,
							rootNode)
					);
			
			localControlManager.addComponent( player, (GeoComponent)geoManager.getComponentByEntityId(player.entityId));
			
			
			
			Entity enemy1 = new Entity( sequencer.getNextId() );
			geoManager.addComponent( enemy1,
					new GeoComponent( 
							new Vector3f(3f, 0.4f, 6f),
							new Quaternion(),
							new Box(0.2f, 0.4f, 0.2f),
							new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"),
							ColorRGBA.Red,
							rootNode)
					);

			Entity enemy2 = new Entity( sequencer.getNextId() );
			geoManager.addComponent( enemy2,
					new GeoComponent( 
							new Vector3f(5f, 0.4f, 4f),
							new Quaternion(),
							new Box(0.2f, 0.4f, 0.2f),
							new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"),
							ColorRGBA.Pink,
							rootNode)
					);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 




		//		inputManager.setCursorVisible(true);
	}
	
	
	
	public HashMap<Class<? extends AbstractGameSystem>, AbstractGameSystem> getSystems() {
		return systems;
	}

	public HashMap<Class<? extends ComponentManager>, ComponentManager> getManagers() {
		return managers;
	}

	@Override
	public void simpleUpdate(float tpf) {
		inputManager.setCursorVisible(true);

//		player.update( tpf );
		
	}

	public static void main(String[] args) {
		ThermalMain thermal = new ThermalMain();
		thermal.setShowSettings( false );
		thermal.setDisplayStatView( false );
		thermal.setDisplayFps( true );
		thermal.start();
			

	}

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.factory;

import no.jchief.phobos.component.Component;
import no.jchief.phobos.component.InSceneComponent;
import no.jchief.phobos.component.MovementComponent;
import no.jchief.phobos.component.PlayerComponent;
import no.jchief.phobos.component.PositionComponent;
import no.jchief.phobos.component.VelocityComponent;
import no.jchief.phobos.component.VisualComponent;
import no.jchief.phobos.system.EntitySystem;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Karsten
 */
public class EntityFactory {

	private static EntitySystem entitySystem;
	private static AssetManager assetManager;

	public static void init(EntitySystem es, AssetManager am) {
		entitySystem = es;
		assetManager = am;
	}

	public static void createNewPlayer(Vector3f v3f)	{
		Component[] components = new Component[6];
		components[0] = new VisualComponent(true,
				null,
				new Box(0.2f, 0.4f, 0.2f),
				new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"),
				ColorRGBA.Blue); 

		components[1] = new PositionComponent(v3f,new Quaternion());
		components[2] = new MovementComponent(Vector3f.ZERO, new Quaternion());
		components[3] = new InSceneComponent(true);
        components[4] = new PlayerComponent();
        components[5] = new VelocityComponent(Vector3f.ZERO);
		//       components[6] = new CollisionComponent(5);
		entitySystem.createEntity(components);
	}

	public static void createNewEnemy(Vector3f v3f)	{
		Component[] components = new Component[4];
		components[0] = new VisualComponent(true,
				null,
				new Box(0.2f, 0.4f, 0.2f),
				new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md"),
				ColorRGBA.Red); 

		components[1] = new PositionComponent(v3f,new Quaternion());
		components[2] = new MovementComponent(Vector3f.ZERO, new Quaternion());
		components[3] = new InSceneComponent(true);
		//       components[4] = new PlayerComponent();
		//       components[5] = new CollisionComponent(5);
		entitySystem.createEntity(components);
	}
//	public static void createCamera(Vector3f v3f)	{
//		Component[] components = new Component[3];
//		components[0] = new CameraComponent();
//		components[1] = new PositionComponent(v3f,new Quaternion());
//		components[2] = new MovementComponent(Vector3f.ZERO, new Quaternion());
////		components[3] = new InSceneComponent(true);
//		//       components[4] = new PlayerComponent();
//		//       components[5] = new CollisionComponent(5);
//		entitySystem.createEntity(components);
//	}

}

package no.jchief.stuxblox.unit;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.math.Vector3f;

import no.jchief.stuxblox.StuxBloxMain;
import no.jchief.stuxblox.gameevent.AbstractGameEvent;
import no.jchief.stuxblox.gameevent.InputEvent;
import no.jchief.stuxblox.iface.GameEventListener;
import no.jchief.stuxblox.unit.Unit;

public class Player extends Unit implements GameEventListener {

	public Player(StuxBloxMain sb) {
		// Unit constructor creates a standard "human" geo /w physics.
		super(sb);
		
		// Tell the event system that we want to be notified of InputEvents
		sb.getGameEventSystem().registerListener(this, InputEvent.class);
		
		// Plug the camera on our players node
//		sb.getRootNode().detachChild(sb.getFlyByCamera());
//		sb.getFlyByCamera().setUpVector(upVec);
//		sb.getCamera()
//		flyCam.setRotationSpeed(1.8f);
	}

	@Override
	public void processGameEvent(AbstractGameEvent ge) {

		InputEvent ie = (InputEvent)ge;
		
//		int press = ie.pressed ? 1 : 0;
		Vector3f walkDir = super.getControl().getWalkDirection( );
		Vector3f viewDir = super.getControl().getViewDirection();
		Vector3f leftDir = super.getControl().getGravity().cross( viewDir ).normalize();
		
		System.out.println("walkDir: " + walkDir + " viewDir: " + viewDir + "\tleftDir: " + leftDir);
		
		switch (ie.type) {
		case FORWARD :	super.getControl().setWalkDirection( walkDir.add( viewDir )); break;
		case BACK :		super.getControl().setWalkDirection( walkDir.add( viewDir.negate() )); break;
		case LEFT :		super.getControl().setWalkDirection( walkDir.add( leftDir.negate() )); break;
		case RIGHT :	super.getControl().setWalkDirection( walkDir.add( leftDir )); break;
		case UP :		super.getControl().jump(); break;
		case DOWN :		super.getControl().setDucked( ie.pressed ); break;
//		case ROT_CW :	super.getControl().setWalkDirection(ROTATE, -press); break;
//		case ROT_CCW :	super.getControl().setWalkDirection(ROTATE, press); break;
//		case TILTFWD :	super.getControl().setWalkDirection(TILT, press); break;
//		case TILTBACK :	super.getControl().setWalkDirection(TILT, -press); break;
//		case IN :		super.getControl().setWalkDirection(DISTANCE, -press); break;
//		case OUT :		super.getControl().setWalkDirection(DISTANCE, press); break;
		default: Logger.getGlobal().log(Level.SEVERE, "Player.processGameEvent method was asked to process an unknown InputEvent: " + ie); break;
		}
		
	}

}

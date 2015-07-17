package no.jchief.akira.camera;

import java.io.IOException;

import no.jchief.akira.AkiraMain;
import no.jchief.akira.gameevent.AbstractGameEvent;
import no.jchief.akira.gameevent.InputEvent;
import no.jchief.akira.iface.GameEventListener;
import no.jchief.akira.support.Utils;
import no.jchief.akira.system.GameEventSystem;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

public class RtsCamera implements GameEventListener, Control {
	public static enum Degree {
		SIDE,
		FWD,
		ROTATE,
		TILT,
		DISTANCE
	}

	private AkiraMain akira;

	private int[] direction = new int[5];
	private float[] accelPeriod = new float[5];

	private float[] maxSpeed = new float[5];
	private float[] maxAccelPeriod = new float[5];
	private float[] minValue = new float[5];
	private float[] maxValue = new float[5];

	private Vector3f position = new Vector3f();

	private Vector3f center = new Vector3f();
	private float tilt = (float)(Math.PI / 2);
	private float rot = 0;
	private float distance = 15;

	private static final int SIDE = Degree.SIDE.ordinal();
	private static final int FWD = Degree.FWD.ordinal();
	private static final int ROTATE = Degree.ROTATE.ordinal();
	private static final int TILT = Degree.TILT.ordinal();
	private static final int DISTANCE = Degree.DISTANCE.ordinal();

	public RtsCamera( AkiraMain akira) {
		this.akira = akira;

		GameEventSystem ges = akira.getGameEventSystem(); 
		ges.registerListener(InputEvent.class, this);

		//		this.cam = cam;

		// Disable first person camera
		akira.getFlyByCamera().setEnabled(false);
		akira.getInputManager().removeListener(akira.getFlyByCamera());

		this.setCenter( new Vector3f(5,0.5f,5) );

		setMinMaxValues(Degree.SIDE, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
		setMinMaxValues(Degree.FWD, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
		setMinMaxValues(Degree.ROTATE, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY);
		//		setMinMaxValues(Degree.TILT, 0.2f, (float)(Math.PI / 2) - 0.001f);
		setMinMaxValues(Degree.TILT, 0, (float)(Math.PI / 2) - 0.001f);
		setMinMaxValues(Degree.DISTANCE, 2, Float.POSITIVE_INFINITY);

		setMaxSpeed(Degree.SIDE,10f,0.4f);
		setMaxSpeed(Degree.FWD,10f,0.4f);
		setMaxSpeed(Degree.ROTATE,2f,0.4f);
		setMaxSpeed(Degree.TILT,1f,0.4f);
		setMaxSpeed(Degree.DISTANCE,15f,0.4f);

		this.akira.getRootNode().addControl( this );
		
	}

	public void setMaxSpeed(Degree deg, float maxSpd, float accelTime) {
		maxSpeed[deg.ordinal()] = maxSpd/accelTime;
		maxAccelPeriod[deg.ordinal()] = accelTime;
	}

	public void setDirection(int ordinal, int pressed) {
		this.direction[ordinal] = pressed;
	}

	@Override
	public void processGameEvent(AbstractGameEvent ge) {
//		System.out.println("[RtsCAM:processGameEvent] " + ge.toString());
		
		
		
		InputEvent ie = (InputEvent)ge;
		
		if ( ie.charControl ) return;
		
		int press = ie.pressed ? 1 : 0;

		switch (ie.type) {
		case LEFT :	this.setDirection(SIDE, -press); break;
		case RIGHT :	this.setDirection(SIDE, press); break;
		case UP :	this.setDirection(FWD, -press); break;
		case DOWN :	this.setDirection(FWD, press); break;
		case ROT_CW :	this.setDirection(ROTATE, -press); break;
		case ROT_CCW :	this.setDirection(ROTATE, press); break;
		case TILTFWD :	this.setDirection(TILT, press); break;
		case TILTBACK :	this.setDirection(TILT, -press); break;
		case IN :	this.setDirection(DISTANCE, -press); break;
		case OUT :	this.setDirection(DISTANCE, press); break;
		default: break;
		}
	}

	public void write(JmeExporter ex) throws IOException {

	}

	public void read(JmeImporter im) throws IOException {

	}

	//	public Control cloneForSpatial(Spatial spatial) {
	//		CameraSystem other = new RtsCam(cam, spatial);
	////		other.registerWithInput(inputManager);
	//		return other;
	//	}

	public void setSpatial(Spatial spatial) {

	}

	public void setEnabled(boolean enabled) {

	}

	public boolean isEnabled() {
		return true;
	}

	@Override
	public void update(float tpf) {
		for (int i = 0; i < direction.length; i++) {
			int dir = direction[i];
			switch (dir) {
			case -1:
				accelPeriod[i] = Utils.clamp(-maxAccelPeriod[i],accelPeriod[i]-tpf,accelPeriod[i]);
				break;
			case 0:
				if (accelPeriod[i] != 0) {
					double oldSpeed = accelPeriod[i];
					if (accelPeriod[i] > 0) {
						accelPeriod[i] -= tpf;
					} else {
						accelPeriod[i] += tpf;
					}
					if (oldSpeed * accelPeriod[i] < 0) {
						accelPeriod[i] = 0;
					}
				}
				break;
			case 1:
				accelPeriod[i] = Utils.clamp(accelPeriod[i],accelPeriod[i]+tpf,maxAccelPeriod[i]);
				break;
			}

		}


		distance += maxSpeed[DISTANCE] * accelPeriod[DISTANCE] * tpf;
		tilt += maxSpeed[TILT] * accelPeriod[TILT] * tpf;
		rot += maxSpeed[ROTATE] * accelPeriod[ROTATE] * tpf;

		distance = Utils.clamp(minValue[DISTANCE],distance,maxValue[DISTANCE]);
		rot = Utils.clamp(minValue[ROTATE],rot,maxValue[ROTATE]);
		tilt = Utils.clamp(minValue[TILT],tilt,maxValue[TILT]);

		double offX = maxSpeed[SIDE] * accelPeriod[SIDE] * tpf;
		double offZ = maxSpeed[FWD] * accelPeriod[FWD] * tpf;

		center.x += offX * Math.cos(-rot) + offZ * Math.sin(rot);
		center.z += offX * Math.sin(-rot) + offZ * Math.cos(rot);

		position.x = center.x + (float)(distance * Math.cos(tilt) * Math.sin(rot));
		position.y = center.y + (float)(distance * Math.sin(tilt));
		position.z = center.z + (float)(distance * Math.cos(tilt) * Math.cos(rot));


		akira.getCamera().setLocation(position);
		akira.getCamera().lookAt(center, new Vector3f(0,1,0));

//		return System.nanoTime() - start;
	}

//	public float getMaxSpeed(Degree dg) {
//		return maxSpeed[dg.ordinal()];
//	}
//
//	public float getMinValue(Degree dg) {
//		return minValue[dg.ordinal()];
//	}
//
//	public float getMaxValue(Degree dg) {
//		return maxValue[dg.ordinal()];
//	}

	// SIDE and FWD min/max values are ignored
	public void setMinMaxValues(Degree dg, float min, float max) {
		minValue[dg.ordinal()] = min;
		maxValue[dg.ordinal()] = max;
	}

	public void setCenter(Vector3f center) {
		this.center.set(center);
	}

	@Override
	public Control cloneForSpatial(Spatial spatial) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void render(RenderManager rm, ViewPort vp) {
		// TODO Auto-generated method stub
		
	}

}

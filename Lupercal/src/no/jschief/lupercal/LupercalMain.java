package no.jschief.lupercal;

import com.jme3.app.SimpleApplication;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LupercalMain extends SimpleApplication {

	@Override
	public void simpleInitApp() {
		// Configure logger
		Logger.getLogger("").setLevel(Level.SEVERE);
		
		// Create GameServer. This orchestrates all events from player(s) and AI.
		//STSServer server = new STSServer();

		
	}
	
	public static void main(String[] args) {
        LupercalMain luper = new LupercalMain();
		luper.setShowSettings( false );
        luper.setDisplayStatView( false );
        luper.setDisplayFps( true );
        luper.start();
	}

}

package no.jchief.phobos.system;

import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.jchief.phobos.gameevent.AbstractGameEvent;
import no.jchief.phobos.gameevent.GameEventListener;

public class GameEventSystem {
	private HashMap<Class<AbstractGameEvent>, Vector<GameEventListener>> gameEventListenerMap;

	public GameEventSystem() {
		super();
		gameEventListenerMap = new HashMap<Class<AbstractGameEvent>, Vector<GameEventListener>>();
	
	}

	public void processEvent(AbstractGameEvent anyEvent) {
		System.out.println("[GES:processEvent] anyEvent " + anyEvent.toString());
		Vector<GameEventListener> listeners = gameEventListenerMap.get( anyEvent.getClass() );
		
		if ( listeners.size() == 0 ) {
			Logger.getGlobal().log(Level.SEVERE, "A GameEvent was processed without any listener. GameEvent: " + anyEvent.toString());
		}
		
		for ( GameEventListener gel : listeners ) {
			gel.processGameEvent( anyEvent );
		}
		
	}
	
	public void registerListener(Class c, GameEventListener gel ) {
		if ( gameEventListenerMap.containsKey( c )) {
			gameEventListenerMap.get( c ).add( gel );
		} else {
			Vector<GameEventListener> v = new Vector<GameEventListener>();
			v.add( gel );
			gameEventListenerMap.put( c, v);
		}
		
	}
	
	// Every class that generates events manually updates the list of listeners.
	// Could refactor to always generate the map based on all available gameevent types.
	public void registerGenerator( Class c ) {
		if ( !gameEventListenerMap.containsKey( c )) {
			gameEventListenerMap.put( c, new Vector<GameEventListener>() );
		}
	}

	
	
	
	
	

}

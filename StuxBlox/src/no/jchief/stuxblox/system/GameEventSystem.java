package no.jchief.stuxblox.system;

import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import no.jchief.stuxblox.gameevent.AbstractGameEvent;
import no.jchief.stuxblox.iface.GameEventListener;

public class GameEventSystem {
	private HashMap<Class<? extends AbstractGameEvent>, Vector<GameEventListener>> gameEventListenerMap;

	public GameEventSystem() {
		super();
		gameEventListenerMap = new HashMap<Class<? extends AbstractGameEvent>, Vector<GameEventListener>>();
	
	}

	// When events occur, tell all listeners 
	public void processEvent(AbstractGameEvent anyEvent) {
//		System.out.println("[GES:processEvent] anyEvent " + anyEvent.toString());
		Vector<GameEventListener> listeners = gameEventListenerMap.get( anyEvent.getClass() );
		
		if ( listeners.size() == 0 ) {
			Logger.getGlobal().log(Level.SEVERE, "A GameEvent was processed without any listener. GameEvent: " + anyEvent.toString());
		}
		
		for ( GameEventListener gel : listeners ) {
			gel.processGameEvent( anyEvent );
		}
		
	}
	
	
    /**
     * <code>registerListener</code> Registers the GameEventListener to specified game event.
     * Expect calls to gel.processGameEvent after invoking this method.
     * 
     * @param gel
     *            The GameEventListener that will be notified when relevant event occurs.
	 * @param c
	 * 			  Subclass of AbstractGameEvent that GameEventListener needs to be notified of.
     */
	public void registerListener(GameEventListener gel, Class<? extends AbstractGameEvent> c) {
		if ( gameEventListenerMap.containsKey( c )) {
			gameEventListenerMap.get( c ).add( gel );
		} else {
			Vector<GameEventListener> v = new Vector<GameEventListener>();
			v.add( gel );
			gameEventListenerMap.put( c, v);
		}
		
	}
	
	// Every class that generates events updates the list of listeners.
	// Could refactor to always generate the map based on all available gameevent types.
	public void registerGenerator( Class<? extends AbstractGameEvent> c ) {
		if ( !gameEventListenerMap.containsKey( c )) {
			gameEventListenerMap.put( c, new Vector<GameEventListener>() );
		}
	}

	
	
	
	
	

}

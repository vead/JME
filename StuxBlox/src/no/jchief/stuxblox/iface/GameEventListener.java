package no.jchief.stuxblox.iface;

import no.jchief.stuxblox.gameevent.AbstractGameEvent;



public interface GameEventListener {
	void processGameEvent(AbstractGameEvent ge);
}

package no.jchief.akira.iface;

import no.jchief.akira.gameevent.AbstractGameEvent;



public interface GameEventListener {
	void processGameEvent(AbstractGameEvent ge);
}

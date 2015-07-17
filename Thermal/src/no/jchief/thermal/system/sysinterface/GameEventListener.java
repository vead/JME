package no.jchief.thermal.system.sysinterface;

import no.jchief.thermal.event.AbstractGameEvent;


public interface GameEventListener {
	void processGameEvent(AbstractGameEvent ge);
}

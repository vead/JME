package no.jchief.thermal.util;

public class Sequencer {
	private int sequence;

	public Sequencer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getNextId() {
		sequence += 1;
		return sequence;
	}


	
}

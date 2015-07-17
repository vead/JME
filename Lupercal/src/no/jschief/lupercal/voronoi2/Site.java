package no.jschief.lupercal.voronoi2;

import com.jme3.math.Vector2f;
import no.jschief.lupercal.poc.waypointing.util.SiteType;

import java.util.ArrayList;


//used both for sites and for vertices
public class Site {
	
	public Vector2f coord;
	int sitenbr;
	public boolean closeToRoom;
	public SiteType type;
	public ArrayList<Site> adjacentSites;
	public boolean entrance;
	


	public Site() {
		coord = new Vector2f();
		adjacentSites = new ArrayList<Site>();
	}
	
	public void addAdjacentSite(Site s) {
		this.adjacentSites.add( s );
	}
	
	public boolean hasAdjacentInterior() {
		boolean answer = false;
		for (Site s: this.adjacentSites ) {
			if ( s.type == SiteType.INTERIOR && !s.entrance) {
				answer = true;
			}
		}
		
		return answer;
	}
}
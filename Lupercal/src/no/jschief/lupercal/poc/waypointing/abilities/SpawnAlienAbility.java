package no.jschief.lupercal.poc.waypointing.abilities;


import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import no.jschief.lupercal.poc.waypointing.WaypointDiscovery;

public class SpawnAlienAbility extends Ability {
	public SpawnAlienAbility(String name, WaypointDiscovery wd) {
		super();
		this.name = name;
		this.wd = wd;
		this.wd.getHud().addAbility( this );
	}

	@Override
	public void onEquip() {
		this.wd.getHud().setColorByName(ColorRGBA.Orange, this.getName());
	}
	
	@Override
	public void onDequip() {
		this.wd.getHud().setColorByName(ColorRGBA.Gray, this.getName());
	}

	@Override
	public void onActivate(Vector2f cursor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate(float tpf) {
		// TODO Auto-generated method stub
		
	}




}

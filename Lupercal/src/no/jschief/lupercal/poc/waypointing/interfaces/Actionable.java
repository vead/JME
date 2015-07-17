package no.jschief.lupercal.poc.waypointing.interfaces;

import com.jme3.math.Vector2f;

public interface Actionable {
	public void onEquip();
	public void onDequip();
	public void onActivate(Vector2f cursor);
	public void onDeactivate();
	public void onUpdate(float tpf);
}

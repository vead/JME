package no.jschief.lupercal.poc.waypointing.entities;

import com.jme3.math.Vector2f;
import no.jschief.lupercal.poc.waypointing.abilities.Ability;
import no.jschief.lupercal.poc.waypointing.interfaces.Selectable;

import java.util.ArrayList;


public class Player implements Selectable {
	private ArrayList<Ability> abilities;
	private Ability currentAbility;

	public Player() {
		super();
		abilities = new ArrayList<Ability>();
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(ArrayList<Ability> abilities) {
		this.abilities = abilities;
	}
	
	public void addAbility(Ability ability) {
		this.abilities.add( ability );
	}
	
	public void equipAbility(int abilityNumber ) {
		for ( int i = 0; i < this.abilities.size(); i++) {
			Ability a = abilities.get( i );
			
			if (i == abilityNumber - 1 ) {
				currentAbility = a;
//				System.out.println("Equiping ability # " + abilityNumber + " name: " + a.getName() );
				a.onEquip();
			} else {
				a.onDequip();
//				System.out.println("--DEquiping ability # " + abilityNumber + " name: " + a.getName() );
			}
		}
	}
	
	//TODO Implement secondary
	public void activateCurrentAbility(Vector2f cursor, boolean secondary) {
		if ( this.currentAbility != null ) {
			System.out.println("Activating currentAbility: " + currentAbility.getName() + "   secondary: " + secondary );
			if ( !secondary )
				this.currentAbility.onActivate(cursor);
			else
				this.currentAbility.onDeactivate();
		}
	}
	
	public void update( float tpf ) {
//		Update abilities
		for ( int i = 0; i < this.abilities.size(); i++) {
			Ability a = abilities.get( i );
			if ( a.isNeedsUpdate() ) {
				a.onUpdate( tpf );
			}
				
		}
	}
	
	@Override
	public String getInfoLine1() {
		return "Im a player!";
	}

	@Override
	public String getInfoLine2() {
		return "Health: 4";
	}

	@Override
	public String getInfoLine3() {
		return "Ammo: 3";
	}

	@Override
	public String getInfoLine4() {
		return "icanhazninja`???!";
	}
	
	
}
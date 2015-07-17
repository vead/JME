package no.jsc.jme3.lab.domain;

import no.jsc.jme3.lab.AwesomeSpaceGame;

public class Player {
	AwesomeSpaceGame asg;
	
	String name;
	int health;
	Item[] items;
	Item activeItem;

	public Player(AwesomeSpaceGame asg, String name) {
		this.asg = asg;
		this.name = name;
		this.health = 100;
	}
	/*
	 * Activate currently equipped item.
	 * Primary function on boolean true, Secondary on false.
	 * 
	 * Testtools: CreateMasterBlox "GrowBlox" "RayCaster" "PlasmaBomb"
	 */
	public void activateEquiped(boolean primaryFunction) {
		if ( activeItem == null )
			return;
		
		if ( activeItem.getName().equals("CreateMasterBlox") ) {
			asg.makeMasterBlox();
		} else if ( activeItem.getName().equals("RayCaster") ){
			asg.rayCaster();
		} else if ( activeItem.getName().equals("PlasmaBomb") ){
//			asg.plasmaBomb();
		}
		// TODO Auto-generated method stub
		
	}

	/*
	 * Equip the item in hotbar on location i.
	 */
	public void equipFromHotbar(int location) {
		this.activeItem = items[ location ];
		// TODO Auto-generated method stub
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public Item[] getItems() {
		return items;
	}
	public void setItems(Item[] items) {
		this.items = items;
	}
	public Item getActiveItem() {
		return activeItem;
	}
	public void setActiveItem(Item activeItem) {
		this.activeItem = activeItem;
	}
	
	
	

}

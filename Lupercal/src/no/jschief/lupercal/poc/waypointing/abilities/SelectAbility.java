package no.jschief.lupercal.poc.waypointing.abilities;


import com.jme3.collision.CollisionResult;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import no.jschief.lupercal.poc.waypointing.GeoFactory;
import no.jschief.lupercal.poc.waypointing.WaypointDiscovery;

public class SelectAbility extends Ability {

	public SelectAbility(String name, WaypointDiscovery wd) {
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
		CollisionResult collision = this.wd.rayPicker( cursor, wd.getRootNode() );
		
		if ( collision != null ) {
			this.wd.getHud().setSelectedInfoTextL1_res( ""+collision.getGeometry().getName());
			this.wd.getHud().setSelectedInfoTextL2_res( ""+collision.getContactPoint()  );
			this.wd.getHud().setSelectedInfoTextL3_res( ""+collision.getDistance() );
			this.wd.getHud().setSelectedInfoTextL4_res( ""+collision.getGeometry().getUserDataKeys() );
			
			Geometry impactMark = GeoFactory.createDebugMark(this.wd.getAssetManager(), ColorRGBA.Pink);
			impactMark.setLocalTranslation( collision.getContactPoint() );
			collision.getGeometry().getParent().attachChild( impactMark );

			Geometry impactNormal = GeoFactory.createDebugArrow(this.wd.getAssetManager(), ColorRGBA.Pink, collision.getContactNormal());
			impactNormal.setLocalTranslation( collision.getContactPoint() );
			collision.getGeometry().getParent().attachChild( impactNormal );
			
//			collision.getGeometry().rotate( new Quaternion().fromAngleAxis(FastMath.PI / 16, Vector3f.UNIT_Y));
		}
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

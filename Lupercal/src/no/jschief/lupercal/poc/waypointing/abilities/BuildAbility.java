package no.jschief.lupercal.poc.waypointing.abilities;

import com.jme3.collision.CollisionResult;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.math.Plane.Side;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Line;
import no.jschief.lupercal.poc.waypointing.WaypointDiscovery;
import no.jschief.lupercal.poc.waypointing.util.Util;

import java.util.HashMap;
import java.util.Iterator;

public class BuildAbility extends Ability {
	private Segment segment;
	private HashMap<Vector3f, Segment> segments;
	
	private Vector3f currentLocalCursorLocation;
	private int updateWorkCount;
	private float sideWidth;
	
	public BuildAbility(String name, WaypointDiscovery wd) {
		super();
		this.name = name;
		this.wd = wd;
		this.wd.getHud().addAbility( this );
		
		this.updateWorkCount = 0;
		this.currentLocalCursorLocation = Vector3f.ZERO;
		this.segments = new HashMap<Vector3f, Segment>(); 
		this.sideWidth = WaypointDiscovery.unitSize / 2;
	}

	@Override
	public void onEquip() {
		this.wd.getHud().setColorByName(ColorRGBA.Orange, this.getName());
	}

	@Override
	public void onDequip() {
		this.wd.getHud().setColorByName(ColorRGBA.Gray, this.getName());
		
		Iterator<Vector3f> it = segments.keySet().iterator();
		while ( it.hasNext() ) {
			segments.get( it.next() ).hideSides();
		}
		
	}

	@Override
	public void onActivate(Vector2f cursor ) {
		CollisionResult collision = this.wd.rayPicker( cursor, wd.getMapNode());
		if ( collision == null ) {
			System.out.println("no pick,   cursor: " + cursor );
			return;
		}
		Vector3f pick = Util.snapToGrid(collision.getContactPoint());
		
		Segment lastSegment = null;
		if ( segment != null ) {
			if ( segment.invalid ) return;
			lastSegment = segment;
			lastSegment.updateColor(new ColorRGBA(0.2f, 0.6f, 0.2f, 0.5f));
		}
		
		// New wall segment with startingpoint.
		segment = new Segment( pick );
		segment.setPriorSegment( lastSegment );
		segments.put(pick, segment);
		
//		This ability is now doing something and needs attention from mainloop.
//		Expect call thru onUpdate(..)
		needsUpdate = true;
		
	}

	@Override
	public void onDeactivate() {
		if ( segment.priorSegment != null ) {
			// Reset end to normal 90 edges
			segment.priorSegment.strutEnd = segment.priorSegment.end.add( segment.priorSegment.end.cross(Vector3f.UNIT_Y).normalize().mult( sideWidth ) );
			segment.priorSegment.updateLines();
		}
		this.wd.getMapNode().detachChild( segment.getNode() );
		
		needsUpdate = false;
		segment = null;
	}
	public void onUpdate( float tpf ) {
		// Lets find the cursor
		Vector3f stopW = this.wd.rayPicker( this.wd.getMapNode() ).getContactPoint();

		// Make sure end location is valid
		if (stopW != null ) {
			// Conform to node and grid
			segment.getNode().worldToLocal(stopW, stopW);
			segment.setEnd( Util.snapToGrid( stopW ) );
		
			// Only do work if the position has changed (new grid location)
			if( !segment.getEnd().equals( currentLocalCursorLocation )) {
				updateWorkCount += 1;
				wd.getHud().setDebug4("updateWorkCount:  ", "" + updateWorkCount );
				currentLocalCursorLocation = segment.getEnd();
				segment.updateSegment();
			}
		}
	}
		
	private class Segment {
		private Node node;
		private Vector3f strutSrt;
		private Vector3f strutEnd;
		private Vector3f end;

		private Line wall;
		private Line sideA;
		private Line sideB;
		private Line sideAE;
		private Line sideBE;
		private Line sideAPara;
		private Line sideBPara;
		private Geometry wallGeo;
		private boolean invalid;
		
		private Segment priorSegment;
		
		public Segment( Vector3f location ) {
			node = new Node("SegmentNode");
			node.setLocalTranslation( location );
			
			strutEnd = new Vector3f( Vector3f.ZERO );
			
			this.wall = new Line(Vector3f.ZERO, Vector3f.ZERO);
			wallGeo = new Geometry("WallGeo", this.wall);
			Material wallMat = new Material(wd.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			wallMat.setColor("Color", new ColorRGBA(0.4f, 0.8f, 0.4f, 0.5f));
			wallGeo.setMaterial( wallMat );
			node.attachChild( wallGeo );
			wallGeo.setCullHint( CullHint.Never );
			
			this.sideA = new Line(Vector3f.ZERO, Vector3f.ZERO);
			Geometry sideAGeo = new Geometry("SideAGeo", this.sideA);
			Material sideAMat = new Material(wd.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			sideAMat.setColor("Color", ColorRGBA.Yellow);
			sideAGeo.setMaterial( sideAMat );
			node.attachChild( sideAGeo );
			sideAGeo.setCullHint( CullHint.Never );

			this.sideB = new Line(Vector3f.ZERO, Vector3f.ZERO);
			this.sideB.setLineWidth( 3f );
			Geometry sideBGeo = new Geometry("SideBGeo", this.sideB);
			Material sideBMat = new Material(wd.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
			sideBMat.setColor("Color", ColorRGBA.Orange);
			sideBGeo.setMaterial( sideBMat );
			node.attachChild( sideBGeo );
			sideBGeo.setCullHint( CullHint.Never );
			
			this.sideAE = new Line(Vector3f.ZERO, Vector3f.ZERO);
			Geometry sideAEGeo = new Geometry("SideAEGeo", this.sideAE);
			sideAEGeo.setMaterial( sideAMat );
			node.attachChild( sideAEGeo );
			sideAEGeo.setCullHint( CullHint.Never );

			this.sideBE = new Line(Vector3f.ZERO, Vector3f.ZERO);
			this.sideBE.setLineWidth(3f);
			Geometry sideBEGeo = new Geometry("SideBEGeo", this.sideBE);
			sideBEGeo.setMaterial( sideBMat );
			node.attachChild( sideBEGeo );
			sideBEGeo.setCullHint( CullHint.Never );
			
			this.sideAPara = new Line(Vector3f.ZERO, Vector3f.ZERO);
			Geometry sideAParaGeo = new Geometry("SideAParaGeo", this.sideAPara);
			sideAParaGeo.setMaterial( sideAMat );
			node.attachChild( sideAParaGeo );
			sideAParaGeo.setCullHint( CullHint.Never );

			this.sideBPara = new Line(Vector3f.ZERO, Vector3f.ZERO);
			this.sideBPara.setLineWidth(3f);
			Geometry sideBParaGeo = new Geometry("SideBParaGeo", this.sideBPara);
			sideBParaGeo.setMaterial( sideBMat );
			node.attachChild( sideBParaGeo );
			sideBParaGeo.setCullHint( CullHint.Never );
			
			wd.getMapNode().attachChild( node );
		}

		public void updateSegment() {
			float angle;
            angle = 0;

            if ( this.end.length() == 0f ) {
				return;
			}
			
			this.strutSrt = this.end.cross(Vector3f.UNIT_Y).normalize().mult( sideWidth );
			this.strutEnd = this.end.add( strutSrt );
			
			if (this.priorSegment != null) {
				angle = priorSegment.end.negate().normalize().angleBetween( segment.end.normalize() ) * FastMath.RAD_TO_DEG;
				if ( angle >= 90 ) {
					wd.getHud().setDebug1("angle   ", "" + angle + " (" + angle / 2 + ")");
					this.invalid = false;
					

					
					if ( Math.round( angle ) != 180) {
						Vector3f strut = this.end.normalize();
						Vector3f backV = priorSegment.end.negate();
						wd.getHud().setDebug2("backV:  ", "" + backV );
						strut.interpolate( backV.normalize(), 0.5f );
						strut.normalizeLocal();
						wd.getHud().setDebug3("1/sin"+angle/2+ ":  ", "" + WaypointDiscovery.unitSize/FastMath.sin(angle/2 * FastMath.DEG_TO_RAD) );
						strut.multLocal( WaypointDiscovery.unitSize/FastMath.sin(angle/2 * FastMath.DEG_TO_RAD) );

						// Plane facing+
						Vector3f cross = this.priorSegment.end.cross( Vector3f.UNIT_Y );
						Vector3f normal = backV.cross( Vector3f.UNIT_Y );
						Vector3f displacement = this.end;
						float constant = displacement.dot(normal);
						Plane plane = new Plane( cross , constant);
						wd.getHud().setDebug6("plane:   ", "" + plane.whichSide( end ));
						
						if ( plane.whichSide( end ) == Side.Negative ) {
							strut.negateLocal();
						}
						this.strutSrt = strut.mult( sideWidth );
//						this.priorSegment.strutEnd = this.priorSegment.getNode().worldToLocal(this.node.localToWorld( this.strutSrt, null), null);
						
						
					}

					this.priorSegment.strutEnd = this.priorSegment.getNode().worldToLocal(this.node.localToWorld( this.strutSrt, null), null);
					this.priorSegment.updateLines();

				} else {
					wd.getHud().setDebug1("angle   ", "" + angle + " (" + angle / 2 + ") WARNING, limit exceeded");
					this.invalid = true;
				}
			}

			if ( !this.invalid ) {
				this.updateLines();
			}
		}
		
		public void updateLines() {
			this.wall.updatePoints( Vector3f.ZERO, this.end );
			
			if ( this.sideA != null )
				this.sideA.updatePoints( Vector3f.ZERO, this.strutSrt );
			if ( this.sideB != null )
				this.sideB.updatePoints( Vector3f.ZERO, this.strutSrt.negate() );

			if ( this.sideAE != null )
				this.sideAE.updatePoints( this.end, this.strutEnd );
			if ( this.sideBE != null )
				this.sideBE.updatePoints( this.end, this.end.add( this.end.subtract( this.strutEnd )) );

			this.sideAPara.updatePoints( this.strutSrt, this.strutEnd );
			this.sideBPara.updatePoints( this.strutSrt.negate(), this.end.add( this.end.subtract( this.strutEnd )) );
			
			
		}
		
		public void hideSides() {
			this.node.detachChildNamed( "SideAGeo" );
			this.node.detachChildNamed( "SideBGeo" );
			this.node.detachChildNamed( "SideAEGeo" );
			this.node.detachChildNamed( "SideBEGeo" );
			this.node.detachChildNamed( "SideAParaGeo" );
			this.node.detachChildNamed( "SideBParaGeo" );
		}
				
		public void updateColor(ColorRGBA color) {
			this.wallGeo.getMaterial().setColor("Color", color);
		}


		public Vector3f getEnd() {
			return end;
		}

		public void setEnd(Vector3f end) {
			this.end = end;
		}

		public Node getNode() {
			return node;
		}

		public void setPriorSegment(Segment priorSegment) {
			this.priorSegment = priorSegment;
		}
		
	
	}
}

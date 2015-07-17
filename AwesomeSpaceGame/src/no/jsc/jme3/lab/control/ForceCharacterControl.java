package no.jsc.jme3.lab.control;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
 
/**
 *  A special CharacterControl which can handle received forces.
 * <p>
 *  This class extends {@link CharacterControl} and emulates the application
 *  of central forces.
 *  Implements {@link PhysicsTickListener}, which is necessary to time the
 *  emulation with the rest of the physics system.
 *
 * @author nego
 * @version 1.3
 */
public class ForceCharacterControl extends CharacterControl implements PhysicsTickListener {
 
    /**
     * The desired direction, set by the user, updated on each PhysicsTick.
     *
     * Not to be mistaken with walk direction, which is set as the sum
     * of move and force direction.
     */
    protected Vector3f moveDirection;
    /**
     * The calculated linear velocity, handled internally, updated on each
     * Physicstick.
     */
    protected Vector3f forceDirection;
    /**
     * The linear damping, which is applied at each PhysicsTick, reduces
     * the linear velocity gradually, while the character is on the ground.
     *
     */
    protected float forceDamping;
    /**
     * The minimal force amount, expressed as velocity.length().
     * If the current velocitys length is lower as this value,
     * the current velocity is reset to zero.
     *
     */
    protected float minimalForceAmount;
    /**
     * The characters gravity.
     *
     */
    protected float gravity;
    /**
     * Identifies players current Enviroment. If in space Y-Axis is considered
     * when applying force/dampening.
     */
    protected boolean inSpace;
    /**
     * Temporary value, used for expressing the distance to move between
     * each PhysicsTick.
     */
    private Vector3f distanceToMove;
    /**
     * Temporary value, used for expressing the dampened linear velocity,
     * which will be applied on the next PhysicsTick.
     */
    private Vector3f updatedForceDirection;
    /**
     * Temporary value, identifies if this was already in the air before.
     * Useful for reseting the vertical component of the velocity upon
     * landing.
     *
     */
    protected boolean wasInAir;
 
    /**
     * The default constructor. Initializes basic values.
     *
     */
    public ForceCharacterControl() {
        super();
        this.moveDirection = new Vector3f(0, 0, 0);
        this.distanceToMove = new Vector3f(0, 0, 0);
        this.forceDamping = 0.9f;
        this.forceDirection = new Vector3f(0, 0, 0);
        this.updatedForceDirection = new Vector3f(0, 0, 0);
        this.wasInAir = false;
        this.minimalForceAmount = 2f;
        this.gravity = this.getControllerId().getGravity();
    }
 
    /**
     * Another constructor which invokes the superclasses constructor
     * with the specified arguments
     *
     * @param CollisionShape The CollisionShape to be used for physics calculations.
     * @param stepHeigt The Heigth a Character can step up.
     */
    public ForceCharacterControl(CollisionShape shape, float stepHeight) {
        super(shape, stepHeight);
        this.moveDirection = new Vector3f(0, 0, 0);
        this.distanceToMove = new Vector3f(0, 0, 0);
        this.forceDamping = 0.9f;
        this.forceDirection = new Vector3f(0, 0, 0);
        this.updatedForceDirection = new Vector3f(0, 0, 0);
        this.wasInAir = false;
        this.minimalForceAmount = 2f;
        this.gravity = this.getControllerId().getGravity();
    }
 
    /**
     * Another constructor which invokes the superclasses constructor
     * with the specified arguments and sets the linear damping
     *
     * @param CollisionShape The CollisionShape to be used for physics calculations.
     * @param stepHeigt The Heigth a Character can step up.
     * @param linearDamping The amount of linear damping to be applied.
     */
    public ForceCharacterControl(CollisionShape shape, float stepHeight, float linearDamping) {
        super(shape, stepHeight);
        this.moveDirection = new Vector3f(0, 0, 0);
        this.distanceToMove = new Vector3f(0, 0, 0);
        this.forceDamping = linearDamping;
        this.forceDirection = new Vector3f(0, 0, 0);
        this.updatedForceDirection = new Vector3f(0, 0, 0);
        this.wasInAir = false;
        this.minimalForceAmount = 2f;
        this.gravity = this.getControllerId().getGravity();
    }
 
    /**
     * Set the force damping being applied per second.
     * A value of 0.5f will reduce the force by a half each second.
     * Values greater than that will reduce the force even further,
     * values smaller than that will lower the force dampening.
     *
     * Defaults to 0.8f
     *
     * @return forceDamping The force Damping being applied, greater than 0f and less than 1f
     */
    public float getForceDamping() {
        return forceDamping;
    }
 
    /**
     * Set the force damping being applied per second.
     * A value of 0.5f will reduce the force by a half each second.
     * Values greater than that will reduce the force even further,
     * values smaller than that will lower the force dampening.
     *
     * Defaults to 0.8f
     *
     * @param forceDamping The force Damping being applied, greater than 0f and less than 1f
     */
    public void setForceDamping(float forceDamping) {
        this.forceDamping = forceDamping;
    }
 
    /**
     * Gets the minimal amount of force to be applied.
     * If the total current force length is under the minimal force amount,
     * the force will be reset to zero.
     * Useful for eliminating too small force movements.
     *
     * Defaults to 2f.
     *
     * @return  minimalForceAmount  The minimal amount of force to be applied.
     */
    public float getMinimalForceAmount() {
        return minimalForceAmount;
    }
 
    /**
     * Sets the minimal amount of force to be applied.
     * If the total current force length is under the minimal force amount,
     * the force will be reset to zero.
     * Useful for eliminating too small force movements.
     *
     * Defaults to 2f.
     *
     * @param minimalForceAmount The minimal amount of force to be applied.
     */
    public void setMinimalForceAmount(float minimalForceAmount) {
        this.minimalForceAmount = minimalForceAmount;
    }
 
    /**
     * Gets the force currently applied to this.
     *
     * @return forceDirection   The force that is currently applied to the character.
     */
    public Vector3f getForceDirection() {
        return forceDirection;
    }
 
    /**
     * Check if there is a force applied currently.
     * Note that the force must be above the minimal force amount.
     *
     * @return boolean  True if there is a force applied above the treshold, false otherwise.
     */
    public boolean isForceApplied() {
        return (forceDirection.length() >= minimalForceAmount);
    }
 
    /**
     * Utility method for cloning this object to another Spatial.
     * Useful for controlling many Spatials with the same setup.
     *
     * @param spatial   The Spatial which will receive the newly created Control.
     * @return control  The newly created Control, with the same field values as this.
     */
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        ForceCharacterControl control = new ForceCharacterControl(collisionShape, stepHeight, forceDamping);
        control.setCcdMotionThreshold(getCcdMotionThreshold());
        control.setCcdSweptSphereRadius(getCcdSweptSphereRadius());
        control.setCollideWithGroups(getCollideWithGroups());
        control.setCollisionGroup(getCollisionGroup());
        control.setFallSpeed(getFallSpeed());
        control.setGravity(getGravity());
        control.setJumpSpeed(getJumpSpeed());
        control.setMaxSlope(getMaxSlope());
        control.setPhysicsLocation(getPhysicsLocation());
        control.setUpAxis(getUpAxis());
        control.setApplyPhysicsLocal(isApplyPhysicsLocal());
 
        control.setForceDamping(getForceDamping());
        control.setMinimalForceAmount(getMinimalForceAmount());
 
        control.setSpatial(spatial);
        return control;
    }
 
    /**
     * Set the {@link PhysicsSpace} of this.
     *
     * Overriden to add/remove this as a PhysicsTickListener.
     *
     * @param physicsSpace The PhysicsSpace which this will be located in.
     */
    @Override
    public void setPhysicsSpace(PhysicsSpace physicsSpace) {
 
        if (physicsSpace == null) {
            if (this.getPhysicsSpace() != null) {
                this.getPhysicsSpace().removeTickListener(this);
            }
        } else {
            if (this.getPhysicsSpace() == physicsSpace) {
                return;
            }
            physicsSpace.addTickListener(this);
        }
 
        super.setPhysicsSpace(physicsSpace);
    }
 
    /**
     * Method wich applies a given velocity to this Control, which degrades on ground.
     * <p>
     * The given linear Velocity is gradually applied via PhysicsTickListener.
     * This operation is cummulative and increases the current velocity.
     * Note that the velocity is reset, when it reaches minimal force amount.
     * Note that the input velocity has to be greater than the minimal force amount.
     *
     * @param linearVelocity    The initial velocity to be applied, degrades due to linear damping on ground.
     */
    public void applyCentralForce(Vector3f linearVelocity) {
        if (linearVelocity.length() >= this.minimalForceAmount) {
            this.forceDirection.addLocal(linearVelocity);
            //System.out.println("[FCC.applyCentralForce] requested forceDirection: " + this.forceDirection + "    G-NOLLTOLERANZ: " + (gravity - FastMath.ZERO_TOLERANCE) );
            if (this.forceDirection.getY() >= this.gravity - FastMath.ZERO_TOLERANCE) {
                //this.forceDirection.setY ( this.gravity - FastMath.ZERO_TOLERANCE );
            }
            //System.out.println("[FCC.applyCentralForce] resulting forceDirection: " + this.forceDirection );
        } else {
        	System.out.println("[ !! PANIC !! ] something told me to move too little. shouldnt happen.");
        }
    }
 
    /**
     * Masking method, sets the walk direction for the character, as in {@link PhysicsCharacter}.
     * <p>
     * Internally is the desired user walk direction added to force direction
     * and is then called as an paramater (walkDirection + forceDirection) to
     * setWalkDirection from PhysicsCharacter.
     * Note that due to adding of "forces", the character is still able to counteract
     * an amount of force.
     *
     * @param vec The walk direction the user wants to go, applied continously, until canceled with a zero Vector3f.
     */
    @Override
    public void setWalkDirection(Vector3f vec) {
        this.moveDirection = vec;
    }
 
    /**
     * Masking method, gets the walk direction for the character, as in {@link PhysicsCharacter}.
     * <p>
     * Internally is the desired user walk direction added to force direction
     * and is then called as an paramater (walkDirection + forceDirection) to
     * setWalkDirection from PhysicsCharacter.
     * Note that due to adding of "forces", the character is still able to counteract
     * an amount of force.
     *
     * @return  walkDirection   The walk direction the user wants to go, applied continously, until canceled with a zero Vector3f.
     */
    @Override
    public Vector3f getWalkDirection () {
        return this.moveDirection;
    }
 
    /**
     * Internal method, should NOT be called, this method calculates the distance
     * to move within the next physics tick.
     * <br>
     * This distance is calculated with the formula distance = (initialVelocity +
     * finalVelocity) * timeDelta / 2. The force damping is applied as newVelocity =
     * oldVelocity * (1-forceDamping) ^ timeDelta.  TimeDelta is the rate at which
     * this PhysicsTick occurs, usually 1/60.
     * The desired move direction is added to this force direction and is then
     * applied via super.setWalkDirection (moveDirection+forceDirection).
     * <p>
     * Note that the force dampening is only applied on ground, as the underlying
     * {@link KinematicCharacterController} is handling vertical deacceleration while
     * this is in the air.
     * Note that this method checks for a minimal velocity, set by setMinimalForceAmount(..),
     * and sets the velocity to zero, once the velocity is smaller than that specified
     * treshold in order to avoid minimal movements (which can produce stuttering).
     * Note that once this reaches ground, and the vertical velocity is negative,
     * the vertical velocity is reset, to avoid some conflicts with the floor.
     * Note that once this reaches ground, after being in the air, the vertical velocity
     * is set to zero, to avoid bouncing.
     */
    public void prePhysicsTick(PhysicsSpace space, float f) {
        //if there is a force above the treshold, apply force+movement
        if (forceDirection.length() > 0) {
        	//System.out.println("inSpace: " + inSpace + "  onGround: " + onGround() );
        	
            //old check: if (onGround() || forceDirection.y<this.gravity*f)
            //eliminates odd behaviour on BoxCollisionShape floor
            //but prevents correct behaviour when an upforce and downforce
            //is applied simultaniously to this control
 
            //we are on ground, apply linear dampening
            if (onGround()) {
                //we have landed, reset vertical component to avoid bouncing
                if (wasInAir) {
                    forceDirection.setY(0f);
                    wasInAir = false;
                }
                
                //we are on ground, no use of negative vertical velocity
                if (!this.inSpace && forceDirection.getY() < 0f) {
                    forceDirection.setY(0f);
                }
 
                //calculate the final velocity for the current delta time
                float decreasingFactor = FastMath.pow(1f - forceDamping, f);
                //create new Vector for updatedForceDirection
                updatedForceDirection = forceDirection.mult(decreasingFactor);
 
                //calculate the force distance to move on next physic tick
                distanceToMove = forceDirection.add(updatedForceDirection);
                distanceToMove.multLocal(0.5f);
                distanceToMove.multLocal(f);
 
                //reset the new force direction if its < minimalForceamount
                if (updatedForceDirection.length() < minimalForceAmount) {
                    updatedForceDirection.set(0, 0, 0);
                }
                //update the force direction for the next calculations
                //point to the Vector of the (old) updatedForceDirection
                forceDirection = updatedForceDirection;
            } else {
                //jumping on ground, no dampening
                distanceToMove = forceDirection.mult(f);
            }
            super.setWalkDirection(distanceToMove.add(moveDirection));
            //else apply movement only
        } else {
            super.setWalkDirection(moveDirection);
        }
    }
 
    /**
     * Internal method, should NOT be called, this method checks if the character
     * has been in air.
     */
    public void physicsTick(PhysicsSpace space, float f) {
        //only do something if we have a sufficient force
        if (forceDirection.length() > 0) {
            if (!onGround()) {
                wasInAir = true;
            }
        }
    }
 
    /**
     * Sets the gravity for this, as in {@link PhysicsCharacter}.
     *
     * @param value The gravity to set.
     */
    @Override
    public void setGravity(float value) {
        super.setGravity(value);
        this.gravity = this.getControllerId().getGravity();
    }
    
    /**
     * Returns whether the character is in space or not.
     * Affects Y-AXIS calculation.
     *
     * @param boolean inSpace
     */
	public boolean isInSpace() {
		return inSpace;
	}
	
    /**
     * Sets whether the character is in space or not.
     * Affects Y-AXIS calculation.
     *
     * @param boolean inSpace
     */
	public void setInSpace(boolean inSpace) {
		this.inSpace = inSpace;
	}
    
    
}

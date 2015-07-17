/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.component;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author Karsten
 */
public class MovementComponent extends Component {
 
    private Vector3f movement;
    private Quaternion rotation;
    
    public MovementComponent(Vector3f movement, Quaternion rotation) {
        this.movement = movement;
        this.rotation = rotation;
    }
    
    public Vector3f getMovement() {
        return movement;
    }
    
    public Quaternion getRotation() {
        return rotation;
    }
    
}

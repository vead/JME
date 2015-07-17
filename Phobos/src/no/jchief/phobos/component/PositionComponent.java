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
public class PositionComponent extends Component {

    private Vector3f location;
    private Quaternion rotation;

    public PositionComponent(Vector3f location, Quaternion rotation) {
        this.location = location.clone();
        this.rotation = rotation.clone();
    }

    public Vector3f getLocation() {
        return location;
    }

    public Quaternion getRotation() {
        return rotation;
    }


}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.entityevent;

import no.jchief.phobos.component.Component;

/**
 *
 * @author Karsten
 */
public interface EntityEventListener {
    public void receiveEntityEvent(EntityEvent changeEvent);
    public Class<? extends Component>[] componentsIntrestedIn();
}

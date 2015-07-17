/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.component;


/**
 *
 * @author Karsten
 */
public class InSceneComponent extends Component {
    
    private boolean value;
    
    public InSceneComponent(boolean value)
    {
        this.value=value;
    }
       
    public boolean getValue()
    {
        return value;
    }
    
}

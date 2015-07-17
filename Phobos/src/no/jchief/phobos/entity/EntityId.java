/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.phobos.entity;

/**
 *
 * @author Karsten
 */
public class EntityId {
    
    private long id;
    
    public static EntityId emptyId = new EntityId(Long.MIN_VALUE);
    
    public EntityId(long id)
    {
        this.id=id;
    }
    
    public long getId()
    {
        return id;
    }
    
}

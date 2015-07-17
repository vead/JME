/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.jchief.akira.support;


/**
 *
 * @author Karsten
 */
public class IdGenerator {
    
    private long id;
    
    public IdGenerator()
    {
        id = Long.MIN_VALUE;
    }
    
    public synchronized long getNextId()
    {
        id++;
        return id;
    }
    
}

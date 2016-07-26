/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core;

import org.rt.core.math.ExtendedList;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class LightCache 
{
    //Full list of light used in scene
    ExtendedList<AbstractLight> lightList = null;    
        
    //Also includes the sunsky
    AbstractBackground backgroundLight = null;
  
    public LightCache()
    {
        lightList = new ExtendedList<>();         
    }
    
    public void clear()
    {
        lightList.removeAll();
    }
    
    public void init()
    {
        if(backgroundLight != null)
        {
            lightList.add(backgroundLight);
            
            if(backgroundLight.isCompound())
                lightList.addAll(backgroundLight.getLights());
        }
        
    }
        
    public void setBackgroundLight(AbstractBackground background)
    {
        this.backgroundLight = background;
    }
    
    public AbstractBackground getBackgroundLight()
    {
        return backgroundLight;
    }
              
    public boolean hasBackgroundLight()
    {
        return backgroundLight != null;
    }
    
    public void add(AbstractPrimitive primitive)
    {       
        if(!primitive.getMaterial().isEmitter())
            return;        
        
        if(primitive.canIntersect())
        {
            lightList.add(primitive.getAreaLight());
            
        }
        else
        {
            ArrayList<AbstractPrimitive> list = new ArrayList<>();
            primitive.refine(list);
        
            for(AbstractPrimitive prim : list)
                lightList.add(prim.getAreaLight());            
        }       
    }
    
    public AbstractLight getRandomLight()
    {
        return lightList.getRandom();
    }
    
    public float getLightsPdf()
    {
        return 1f/lightList.size();
    }
    
    public int getSize()
    {
        return lightList.size();
    }
}
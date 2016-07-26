/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rt.core.primitive;

import org.rt.core.AbstractBSDF;
import org.rt.core.AbstractPrimitive;
import org.rt.core.AbstractShape;
import org.rt.core.Intersection;
import org.rt.core.Material;
import org.rt.core.image.Texture;
import org.rt.core.coordinates.Normal3f;
import org.rt.core.coordinates.Vector3f;
import org.rt.core.light.AreaLight;
import org.rt.core.math.BoundingBox;
import org.rt.core.math.Ray;
import org.rt.core.math.Transform;
import java.util.ArrayList;

/**
 *
 * @author user
 */

public class GeometryPrimitive extends AbstractPrimitive
{
    
    private final AbstractShape shape;
    private final Material material;
    
    public GeometryPrimitive(AbstractShape shape, Material material)
    {
        this.shape = shape;
        this.material = material;
    }

    @Override
    public BoundingBox getWorldBounds() {
        return shape.getWorldBounds();
    }

    @Override
    public boolean intersect(Ray ray, Intersection isect) 
    {   
        //No intersection
        if(!shape.intersect(ray, isect.dg))
            return false;        
        
        //skyportal material false intersection
        if(material.skyportal)
            return false;
                    
        isect.bsdf = material.getBSDF(isect.dg.n, ray.d);
                
        //has texture, set texture color to bsdf
        if(material.hasTexture())
        {
            Texture texture = material.getTexture();            
            isect.bsdf.setColor(texture.getTexelUV(isect.dg.u, isect.dg.v));            
        }
        
        isect.primitive = this;        
        return true;
    }

    @Override
    public boolean intersectP(Ray ray) 
    {        
        return shape.intersectP(ray);
    }

    @Override
    public AreaLight getAreaLight() 
    {
        if(material.isEmitter())
            return new AreaLight(material, shape, new Transform());
        else
            return null;
    }

    @Override
    public Material getMaterial() 
    {
        return material;
    }

    @Override
    public AbstractBSDF getBSDF(Normal3f worldNormal, Vector3f worldWi) 
    {
        return material.getBSDF(worldNormal, worldWi);
    }

    @Override
    public void refine(ArrayList<AbstractPrimitive> refined) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString()
    {
        return shape.toString();
    }
}
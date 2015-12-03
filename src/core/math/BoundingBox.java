/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 *
 * @author user
 */
public class BoundingBox implements Cloneable
{
    public Point3f minimum;
    public Point3f maximum;
   
    public BoundingBox() 
    {
        minimum = new Point3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        maximum = new Point3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
    }
    
    public BoundingBox(Point3f p) 
    {
        minimum = new Point3f(p);
        maximum = new Point3f(p);
    }
    
    public BoundingBox(Point3f p1, Point3f p2) 
    {
        minimum = new Point3f(//
                min(p1.x, p2.x), min(p1.y, p2.y), min(p1.z, p2.z));
        maximum = new Point3f(//
                max(p1.x, p2.x), max(p1.y, p2.y), max(p1.z, p2.z));
    }
    
    public BoundingBox(float x1, float y1, float z1, float x2, float y2, float z2) {
        minimum = new Point3f(//
                min(x1, x2), min(y1, y2), min(z1, z2));
        maximum = new Point3f(//
                max(x1, x2), max(y1, y2), max(z1, z2));
    }
    
    public final Point3f getCenter() 
    {
        return Point3f.mid(minimum, maximum);
    }
    
    public boolean intersectP(Ray ray, float[] hitt) 
    {
        float t0 = ray.getMin(), t1 = ray.getMax();
        for (int i = 0; i < 3; ++i) 
        {
            // Update interval for _i_th bounding box slab, page 180
            float invRayDir = 1f / ray.d.get(i);
            float tNear = (minimum.get(i) - ray.o.get(i)) * invRayDir;
            float tFar = (maximum.get(i) - ray.o.get(i)) * invRayDir;

            // Update parametric interval from slab intersection $t$s
            if (tNear > tFar) 
            {
                float swap = tNear;
                tNear = tFar;
                tFar = swap;
            }
            if (tNear > t0) t0=tNear;
            if (tFar < t1) t1=tFar;
            if (t0 > t1) 
            {
                return false;
            }
        }
        if (hitt != null) 
        {
            hitt[0] = t0;
            hitt[1] = t1;
        }
        return true;
    }
        
    public int maximumExtent() {
        Vector3f diag = Point3f.sub(maximum, minimum);
        if (diag.x > diag.y && diag.x > diag.z) {
            return 0;
        } else if (diag.y > diag.z) {
            return 1;
        } else {
            return 2;
        }
    }
    
    public boolean inside(Point3f pt) 
    {
        return pt.x >= minimum.x && pt.x <= maximum.x && //
                pt.y >= minimum.y && pt.y <= maximum.y &&//
                pt.z >= minimum.z && pt.z <= maximum.z;
    }
    
    public Point3f bounds(int index)
    {
        if(index == 0) return minimum;
        else if(index == 1) return maximum;
        else return minimum;
    }
    
    public static BoundingBox union(BoundingBox b, Point3f p) 
    {
        BoundingBox ret = b.clone();
        ret.minimum.x = min(b.minimum.x, p.x);
        ret.minimum.y = min(b.minimum.y, p.y);
        ret.minimum.z = min(b.minimum.z, p.z);
        ret.maximum.x = max(b.maximum.x, p.x);
        ret.maximum.y = max(b.maximum.y, p.y);
        ret.maximum.z = max(b.maximum.z, p.z);
        return ret;
    }
    
    public final void include(Point3f p) 
    {
        if (p != null) {
            if (p.x < minimum.x)
                minimum.x = p.x;
            if (p.x > maximum.x)
                maximum.x = p.x;
            if (p.y < minimum.y)
                minimum.y = p.y;
            if (p.y > maximum.y)
                maximum.y = p.y;
            if (p.z < minimum.z)
                minimum.z = p.z;
            if (p.z > maximum.z)
                maximum.z = p.z;
        }
    }
    
    public final void include(BoundingBox b) {
        if (b != null) {
            if (b.minimum.x < minimum.x)
                minimum.x = b.minimum.x;
            if (b.maximum.x > maximum.x)
                maximum.x = b.maximum.x;
            if (b.minimum.y < minimum.y)
                minimum.y = b.minimum.y;
            if (b.maximum.y > maximum.y)
                maximum.y = b.maximum.y;
            if (b.minimum.z < minimum.z)
                minimum.z = b.minimum.z;
            if (b.maximum.z > maximum.z)
                maximum.z = b.maximum.z;
        }
    }
    
    public BoundingSphere getBoundingSphere()
    {
        Point3f c = getCenter();
        return new BoundingSphere(c, c.distanceTo(maximum));
    }
    
    @Override
    public BoundingBox clone() 
    {
        return new BoundingBox(minimum, maximum);
    }
    
    @Override
    public final String toString() {
        return String.format("(%.2f, %.2f, %.2f) to (%.2f, %.2f, %.2f)", minimum.x, minimum.y, minimum.z, maximum.x, maximum.y, maximum.z);
    }
}

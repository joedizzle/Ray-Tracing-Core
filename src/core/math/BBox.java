/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Point3f;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 *
 * @author user
 */
public class BBox 
{
    public Point3f minimum;
    public Point3f maximum;
   
    public BBox() 
    {
        minimum = new Point3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        maximum = new Point3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
    }
    
    public BBox(Point3f p) 
    {
        minimum = new Point3f(p);
        maximum = new Point3f(p);
    }
    
    public BBox(Point3f p1, Point3f p2) 
    {
        minimum = new Point3f(//
                min(p1.x, p2.x), min(p1.y, p2.y), min(p1.z, p2.z));
        maximum = new Point3f(//
                max(p1.x, p2.x), max(p1.y, p2.y), max(p1.z, p2.z));
    }
    
    public final Point3f getCenter() 
    {
        return Point3f.mid(minimum, maximum);
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
    
    public final void include(BBox b) {
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
    
    public void boundingSphere(Point3f c, FloatValue rad) 
    {
        c.set(getCenter());
        rad.value = c.distanceTo(maximum);        
    }
}

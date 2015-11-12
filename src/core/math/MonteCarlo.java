/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import static core.math.Utility.PI_F;
import static core.math.Utility.acosf;
import static core.math.Utility.cosf;
import static core.math.Utility.lerp;
import static core.math.Utility.sinf;
import static core.math.Utility.sphericalDirection;
import static core.math.Utility.sqrtf;

/**
 *
 * @author user
 */
public class MonteCarlo 
{
    public final static Vector3f uniformSampleCone(float u1, float u2, float costhetamax) 
    {
        float costheta = (1.f - u1) + u1 * costhetamax;
        float sintheta = sqrtf(1.f - costheta * costheta);
        float phi = u2 * 2.f * PI_F;
        return new Vector3f(cosf(phi) * sintheta, sinf(phi) * sintheta, costheta);
    }
    
    public final static Vector3f uniformSampleCone(float u1, float u2, float costhetamax,
            Vector3f x, Vector3f y, Vector3f z) 
    {
        float costheta = lerp(u1, costhetamax, 1.f);
        float sintheta = sqrtf(1.f - costheta * costheta);
        float phi = u2 * 2.f * PI_F;
        
        return x.mul(cosf(phi) * sintheta).addAssign(y.mul(sinf(phi) * sintheta)).addAssign(
                z.mul(costheta));
    }
    public final static Point3f uniformSampleSphere(float u1, float u2) 
    {
        float phi = 2 * PI_F * u1;
        float theta = acosf(1 - 2 * u2);
        
        Vector3f dir = sphericalDirection(theta, phi);
        
        return new Point3f(dir.x, dir.y, dir.z);
    }
    
    public static Point3f uniformSampleTriangle(float r1, float r2, Point3f p1, Point3f p2, Point3f p3)
    {
        float s = (float)(Math.sqrt(r1));
        float t = (float)r2;
        
        Point3f p = new Point3f();
        p.x = (1 - s)*p1.x + s*(1 - t)*p2.x + s*t*p3.x;
        p.y = (1 - s)*p1.y + s*(1 - t)*p2.y + s*t*p3.y;
        p.z = (1 - s)*p1.z + s*(1 - t)*p2.z + s*t*p3.z;
        
        return p;
    }
     
    public static float areaTriangle(Point3f p1, Point3f p2, Point3f p3)
    {
        float a = p1.distanceTo(p2);
        float b = p1.distanceTo(p3);
        float c = p2.distanceTo(p3);
        
        float s = 0.5f*(a + b + c);
        
        return (float)Math.sqrt(s*(s-a)*(s-b)*(s-c));
    } 
}

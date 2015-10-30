/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.math;

import core.coordinates.Point2f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;

/**
 *
 * @author user
 */
public class Utility {
    public static final float PI_F = (float)Math.PI;
    public static final float INV_PI_F = 1.f/PI_F;
    public static final float EPS_COSINE = 1e-6f;
    
    public static float clamp(float x, float min, float max)
    {
        if (x > max)
            return max;
        if (x > min)
            return x;
        return min;
    }
    
    public static int clamp(int x, int min, int max)
    {
        if (x > max)
            return max;
        if (x > min)
            return x;
        return min;
    }

    public static int max(int a, int b, int c) 
    {
        if (a < b)
            a = b;
        if (a < c)
            a = c;
        return a;
    }

    public static float max(float a, float b, float c) 
    {
        if (a < b)
            a = b;
        if (a < c)
            a = c;
        return a;
    }
    
    public static float sqr(float value)
    {
        return value * value;
    }
    
    public static float sqrt(float value)
    {
        return (float)Math.sqrt(value);
    }
    
    public static float fresnelSchlick(float n1, float n2, float cosThetaI, float cosThetaT)
    {
        float r0 = (n1 - n2)/(n1 + n2);
        r0 *= r0;
        float cosX = cosThetaI;
        
        if(n1 > n2)
        {
            float n = n1/n2;
            float sinT2 = n * n * (1 - cosX*cosX);
            
            if(sinT2 > 1f) return 1f; //TIR
            
            cosX = sqrt(1f - sinT2);
        }
        
        float x = 1f - cosX;
        return r0 + (1f - r0) * x * x * x * x * x;
    }
    
    public static Vector3f sampleCosineHemisphereW(Point2f rndTuple, FloatValue pdfW)
    {
        float term1 = 2.f * PI_F * rndTuple.x;
        float term2 = (float) Math.sqrt(1.f - rndTuple.y);
        
        Vector3f ret = new Vector3f(
            (float)Math.cos(term1) * term2,
            (float)Math.sin(term1) * term2,
            (float)Math.sqrt(rndTuple.y));
        
        if(pdfW != null)
        {
            pdfW.value = ret.z * INV_PI_F;
        }
        
        return ret;
    }
    
    public static float cosHemispherePdfW(
        Vector3f  aNormal,
        Vector3f  aDirection)
    {
        return Math.max(0.f, Vector3f.dot(aNormal, aDirection)) * INV_PI_F;
    }
        
    public static Vector3f samplePowerCosHemisphereW(float r1, float r2, float power)
    {
        float term1 = 2.f * PI_F * r1;
        float term2 = (float) Math.pow(r2, 1.f / (power + 1.f));
        float term3 = (float) Math.sqrt(1.f - term2 * term2);

        return new Vector3f(
            (float)(Math.cos(term1) * term3),
            (float)(Math.sin(term1) * term3),
            term2);
    }
    
    public static float pdfPowerCosHemisphereW(Vector3f n, Vector3f w, float power)
    {
        float cosTheta = Vector3f.dot(n, w);
        return (float) ((power + 1.f) * Math.pow(cosTheta, power) * (0.5f * INV_PI_F));
    }
    
    public static Point3f sampleUniformTriangle(float r1, float r2, Point3f p1, Point3f p2, Point3f p3)
    {
        float s = (float)(Math.sqrt(r1));
        float t = (float)r2;
        
        Point3f p = new Point3f();
        p.x = (1 - s)*p1.x + s*(1 - t)*p2.x + s*t*p3.x;
        p.y = (1 - s)*p1.y + s*(1 - t)*p2.y + s*t*p3.y;
        p.z = (1 - s)*p1.z + s*(1 - t)*p2.z + s*t*p3.z;
        
        return p;
    }
    
    public static Vector3f sampleUniformSphereW(Point2f sample, FloatValue pdfSA)
    {
        float term1 = 2.f * PI_F * sample.x;
        float term2 = (float) (2.f * Math.sqrt(sample.y - sample.y * sample.y));

        Vector3f ret = new Vector3f(
            (float)Math.cos(term1) * term2,
            (float)Math.sin(term1) * term2,
            1.f - 2.f * sample.y);

        if(pdfSA != null)
        {
            //*oPdfSA = 1.f / (4.f * PI_F);
            pdfSA.value = INV_PI_F * 0.25f;
        }

        return ret;
    }
    
    public static float concentricDiscPdfA()
    {
        return INV_PI_F;
    }
    
    public static float uniformSpherePdfW()
    {
        //return (1.f / (4.f * PI_F));
        return INV_PI_F * 0.25f;
    }
    
    public static Point2f sampleConcentricDisc(Point2f aSamples)
    {
        float phi, r;

        float a = 2*aSamples.x - 1;   /* (a,b) is now on [-1,1]^2 */
        float b = 2*aSamples.y - 1;

        if(a > -b)      /* region 1 or 2 */
        {
            if(a > b)   /* region 1, also |a| > |b| */
            {
                r = a;
                phi = (PI_F/4.f) * (b/a);
            }
            else        /* region 2, also |b| > |a| */
            {
                r = b;
                phi = (PI_F/4.f) * (2.f - (a/b));
            }
        }
        else            /* region 3 or 4 */
        {
            if(a < b)   /* region 3, also |a| >= |b|, a != 0 */
            {
                r = -a;
                phi = (PI_F/4.f) * (4.f + (b/a));
            }
            else        /* region 4, |b| >= |a|, but a==0 and b==0 could occur. */
            {
                r = -b;

                if (b != 0)
                    phi = (PI_F/4.f) * (6.f - (a/b));
                else
                    phi = 0;
            }
        }

        Point2f res = new Point2f();
        res.x = (float) (r * Math.cos(phi));
        res.y = (float) (r * Math.sin(phi));
        return res;
    }
    
    public static float areaTriangle(Point3f p1, Point3f p2, Point3f p3)
    {
        float a = p1.distanceTo(p2);
        float b = p1.distanceTo(p3);
        float c = p2.distanceTo(p3);
        
        float s = 0.5f*(a + b + c);
        
        return (float)Math.sqrt(s*(s-a)*(s-b)*(s-c));
    } 
    
    // reflect vector through (0,0,1)
    public static Vector3f reflectLocal(Vector3f w)
    {
        return new Vector3f(-w.x, -w.y, w.z);
    }
    
    public static float cosTheta(Vector3f w)
    {
        return w.z;
    }
    
    public static float absCosTheta(Vector3f w)
    {
        return Math.abs(cosTheta(w));
    }
    
    public static float sinTheta2(Vector3f w)
    {
        return Math.max(0f, 1.f - cosTheta(w) * cosTheta(w));
    }
    
    public static float sinTheta(Vector3f w)
    {
        return (float) Math.sqrt(sinTheta2(w));
    }
    
    public static float cosPhi(Vector3f w) 
    {
        float sintheta = sinTheta(w);
        if (sintheta == 0.f) return 1.f;
        return clamp(w.x / sintheta, -1.f, 1.f);
    }
    
    float sinPhi(Vector3f w) 
    {
        float sintheta = sinTheta(w);
        if (sintheta == 0.f) return 0.f;
        return clamp(w.y / sintheta, -1.f, 1.f);
    }
}
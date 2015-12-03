/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.shape;

import core.AbstractShape;
import core.coordinates.Normal3f;
import core.coordinates.Point3f;
import core.coordinates.Vector3f;
import core.math.BoundingBox;
import core.math.DifferentialGeometry;
import core.math.Ray;
import core.math.Transform;
import static core.math.MonteCarlo.areaTriangle;
import static core.math.MonteCarlo.uniformSampleTriangle;

/**
 *
 * @author user
 */
public class Triangle extends AbstractShape
{
    Point3f p1, p2, p3;
    public Normal3f n;
    
    public Triangle(Point3f p1, Point3f p2, Point3f p3, Normal3f n)
    {        
        super(new Transform(), new Transform());
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;  
        this.n = new Normal3f(n.normalize());
        
    }
    @Override
    public BoundingBox getObjectBounds() {
        BoundingBox bound = new BoundingBox();
        bound.include(p1);
        bound.include(p2);
        bound.include(p3);
        return bound;
    }

    @Override
    public boolean intersectP(Ray r) {
        Vector3f ao = p1.subV(r.o);
        Vector3f bo = p2.subV(r.o);
        Vector3f co = p3.subV(r.o);
        
        Vector3f v0 = Vector3f.cross(co, bo);
        Vector3f v1 = Vector3f.cross(bo, ao);
        Vector3f v2 = Vector3f.cross(ao, co);

        float v0d = Vector3f.dot(v0, r.d);
        float v1d = Vector3f.dot(v1, r.d);
        float v2d = Vector3f.dot(v2, r.d);
        
        if(((v0d < 0.f)  && (v1d < 0.f)  && (v2d < 0.f)) ||
           ((v0d >= 0.f) && (v1d >= 0.f) && (v2d >= 0.f)))
        {
            float distance = Vector3f.dot(n, ao) / Vector3f.dot(n, r.d);
            
            if((distance > r.getMin()) & (distance < r.getMax()))
            {              
                return true;
            }
        }
        
        return false;
    }

    @Override
    public boolean intersect(Ray r, DifferentialGeometry dg) {
        Vector3f e1, e2, h, s, q;
        double a, f, u, v;

        e1 = Point3f.sub(p2, p1);
        e2 = Point3f.sub(p3, p1);
        h = Vector3f.cross(r.d, e2);
        a = Vector3f.dot(e1, h);

        if (a > -0.00000000000001 && a < 0.0000000000001)
            return false;

        f = 1/a;
        s = Point3f.sub(r.o, p1);
	u = f * (Vector3f.dot(s, h));

        if (u < 0.0 || u > 1.0)
            return false;

        q = Vector3f.cross(s, e1);
	v = f * Vector3f.dot(r.d, q);

	if (v < 0.0 || u + v > 1.0)
            return false;

	float t = (float) (f * Vector3f.dot(e2, q));

        if (r.isInside(t))
        {   
            Normal3f nhit;
            if(Vector3f.dot(n, r.d) < 0) 
                nhit = n;   
            else
                nhit = n.neg();
                
            r.setMax(t);
            dg.p = r.getPoint();
            dg.n = nhit; 
            dg.u = (float) u;
            dg.v = (float) v;
            dg.shape = this;
                        
            return true;
        }        
        return false;
    }

    @Override
    public float getArea() 
    {
        return areaTriangle(p1, p2, p3);
    }
    
    @Override
    public Point3f sampleA(float u1, float u2, Normal3f n) 
    {
        if(n != null)
            n.set(this.n);
        
        return uniformSampleTriangle(u1, u2, p1, p2, p3);
    }

    @Override
    public Normal3f getNormal(Point3f p) {
        return n.clone();
    }
}

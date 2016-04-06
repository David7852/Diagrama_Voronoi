package diagrama_voronoi;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Polygoncell
{
    public Color c;
    private Polygon p;
    private List<Point2D> vertices;
    private Site center;
    public String name;

    public Polygoncell(Site s) 
    {
        c = s.c;
        name=s.name;
        center=s;
        vertices=new ArrayList();
        p=new Polygon();
    }
    public void addP(int x,int y) 
    {
        vertices.add(new Point2D.Double(x,y));
    }
    public void setP() 
    {
        vertices=QuickHull.quickHull(vertices);
        p=new Polygon();
        for(Point2D a:vertices)
            p.addPoint((int)a.getX(),(int)a.getY());
        vertices.clear();
    }
    public Polygon getP(){return p;}

    public Site getCenter(){return center;} 
}

package diagrama_voronoi;

import java.awt.Color;
import java.awt.geom.Point2D;

public class Site 
{
    private Point2D at;
    public Color c;
    public String name;

    public Site(int x,int y,Color cc,String n) 
    {
        name=n;
        at=new Point2D.Double(x, y);
        c=cc;
    }
    public Point2D getAt() 
    {
        return at;
    }
}

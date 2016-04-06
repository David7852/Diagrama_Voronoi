package diagrama_voronoi;

import java.awt.Point;

public class PuntoQ 
{
    public int value;
    public Point p;
    public String name;

    public PuntoQ(int x, int y,int value) 
    {
        p=new Point(x, y);
        this.value=value;
    }
    public void setPadre(Site padre) 
    {
        name=padre.name;
    } 
}

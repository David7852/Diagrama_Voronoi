package diagrama_voronoi;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class QuickHull{
    public static List<Point2D> quickHull(List<Point2D> nube)     {
        List<Point2D> cerradura = new ArrayList<Point2D>();
        Point2D min_x = new Point2D.Double(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        Point2D max_x = new Point2D.Double(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        for(Point2D aux:nube) {
            if(aux.getX()<min_x.getX()) {
                min_x = aux;
            }
            if(aux.getX()>max_x.getX()) {
                max_x = aux;
            }
        }
        List<Point2D> S1 = puntosLado(min_x, max_x, nube);
        List<Point2D> S2 = puntosLado(max_x, min_x, nube);
        cerradura.add(min_x);
        cerradura.addAll(findHull(min_x, max_x, S1));
        cerradura.add(max_x);
        cerradura.addAll(findHull(max_x, min_x, S2));
        return cerradura;
    }

    public static List<Point2D> findHull(Point2D a, Point2D b, List<Point2D> S) {
        List<Point2D> cerradura = new ArrayList<Point2D>();
        Point2D c = new Point2D.Double();
        if(!S.isEmpty()) {
            List<Point2D> A = new ArrayList<Point2D>();
            List<Point2D> B = new ArrayList<Point2D>();
            c = puntosMayorArea(a, b, S);
            A = puntosLado(a, c, S);
            B = puntosLado(c, b, S);
            cerradura.addAll(findHull(a, c, A));
            cerradura.add(c);
            cerradura.addAll(findHull(c, b, B));
        }
        return cerradura;
    }

    public static List<Point2D> puntosLado(Point2D a, Point2D b, List<Point2D> S) {
        List<Point2D> subset = new ArrayList<Point2D>();
        for(Point2D aux:S) {
            if(isLeft(a, b, aux)>0) {
                if(!aux.equals(a) && !aux.equals(b)) {
                    subset.add(aux);
                }
            }
        }
        return subset;
    }

    private static double isLeft(Point2D P0, Point2D P1, Point2D P2) {
        return (P1.getX() - P0.getX())*(P2.getY() - P0.getY()) -
                (P2.getX() - P0.getX())*(P1.getY() - P0.getY());
    }

    public static Point2D puntosMayorArea(Point2D a, Point2D b, List<Point2D> S) {
        Point2D punto = new Point2D.Double();
        double area = Double.MIN_VALUE;
        for(Point2D aux:S) {
            double area_aux = determinante(a, b, aux);
            if(area_aux>area) {
                area = area_aux;
                punto = aux;
            }
        }
        return punto;
    }

    public static double determinante(Point2D p1, Point2D p2, Point2D p3){
        double det = 0;
        det = p2.getY() * p3.getX() + p1.getY() * p2.getX() + p1.getX() * p3.getY();
        det = p1.getX() * p2.getY() + p2.getX() * p3.getY() + p1.getY() * p3.getX()-det;
        return det;
    }
}
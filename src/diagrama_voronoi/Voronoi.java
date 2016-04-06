package diagrama_voronoi;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

public class Voronoi extends JComponent implements MouseListener
{
    private File sites=new File("./Sites.in.txt");
    private File vorin=new File("./Voronoi.in.txt");
    private File vorout=new File("./Voronoi.out.txt");
    private LinkedList<Site> lisit=new LinkedList<Site>();
    private LinkedList<PuntoQ> liQ=new LinkedList<PuntoQ>();
    private LinkedList<Polygoncell> cell=new LinkedList<Polygoncell>();
    //constructor
    public Voronoi() throws IOException
    {
        fill();
        addMouseListener(this);
    }
    //lee archivos
    private void fill() throws FileNotFoundException, IOException
    {
        FileReader fr=new FileReader(sites);
        BufferedReader bfr=new BufferedReader(fr);
        int sizex=300, sizey=300;
        for(String s=bfr.readLine();s!=null;s=bfr.readLine())
        {
            int x,y;
            x= Integer.parseInt(s.substring(0,s.indexOf(",")));
            if(x>sizex)    
                sizex=x;
            s=s.substring(s.indexOf(",")+1);
            y= Integer.parseInt(s);
            if(y>sizey)    
                sizey=y;
            lisit.add(new Site(x,y,new Color((float)Math.random(), (float)Math.random(), (float)Math.random()),""+(lisit.size()+1)));
            cell.add(new Polygoncell(lisit.getLast()));
        }
        bfr.close();  
        fr=new FileReader(vorin);
        bfr=new BufferedReader(fr);
        for(String s=bfr.readLine();s!=null;s=bfr.readLine())
        {
            int x,y,val;
            x= Integer.parseInt(s.substring(0,s.indexOf(",")));
            if(x>sizex)    
                sizex=x;
            s=s.substring(s.indexOf(",")+1);
            y= Integer.parseInt(s.substring(0,s.indexOf(",")));
            if(y>sizey)    
                sizey=y;
            s=s.substring(s.indexOf(",")+1);
            val=Integer.parseInt(s);           
            liQ.add(new PuntoQ(x,y,val));
        }
        bfr.close();
        setSize(sizex+100, sizey+100);        
    }
    //calcula las celdas
    private void On2()
    {
        for(int x=0;x<=this.getWidth();x+=1)
            for(int y=0;y<=this.getHeight();y+=1)
            {
                int id=-1;
                Point2D at=new Point2D.Double(x,y);
                double lastmin=Double.POSITIVE_INFINITY;
                for(Site s: lisit)
                {      
                    double newmin=at.distance(s.getAt());
                    if(newmin<lastmin)
                    {
                        lastmin=newmin;
                        id=lisit.indexOf(s);
                    }
                }
                    cell.get(id).addP(x, y);
            }
    }
    //rellena el archivo de salida
    public void ans() throws IOException
    {
        FileWriter fr=new FileWriter(vorout);
        BufferedWriter br=new BufferedWriter(fr);
        String s="";
        for(PuntoQ q: liQ)
           s=s+(int)q.p.getX()+","+(int)q.p.getY()+","+q.name+","+q.value+"\r\n";
        br.append(s);
        br.close();
    }
    //modifica el archivo de entrada
    public void app(Site s) throws IOException
    {
        FileWriter fr=new FileWriter(sites,true);
        BufferedWriter br=new BufferedWriter(fr);
        br.append("\r\n"+(int)s.getAt().getX()+","+(int)s.getAt().getY());
        br.close();
    }
    public void app(PuntoQ q) throws IOException
    {
        FileWriter fr=new FileWriter(vorin,true);
        BufferedWriter br=new BufferedWriter(fr);
        br.append("\r\n"+(int)q.p.x+","+q.p.y+","+q.value);
        br.close();
    }
    
    //dibuja
    @Override
    public void paintComponent(Graphics g) 
    {
        On2();
        super.paintComponent(g);
        for(Polygoncell s: cell)
        {
            s.setP();
            g.setColor(s.c);
            g.fillPolygon(s.getP());
            g.setColor(Color.white);
            g.drawPolygon(s.getP());
        }
        for(Site x : lisit)
        {
            g.setColor(Color.white);
            g.drawRect((int)x.getAt().getX()-1, (int)x.getAt().getY()-1, 3, 3);
            g.setColor(Color.black);
            g.fillOval((int)x.getAt().getX()-1, (int)x.getAt().getY()-1, 3, 3);
            g.drawString(x.name, (int)x.getAt().getX()+5, (int)x.getAt().getY()+5);
        }
        for(PuntoQ q:liQ)
        {
            g.setColor(Color.red);
            g.fillOval((int)q.p.getX()-2,(int)q.p.getY()-2,4,4);
            g.setColor(Color.yellow);
            g.drawOval((int)q.p.getX()-2,(int)q.p.getY()-2,4,4);
            for(Polygoncell s: cell)
               if(s.getP().contains(q.p))
               {
                   q.setPadre(s.getCenter());
                   break;
               }
        }
        g.setColor(Color.white);
        g.drawRect(0, 0, getSize().width-1, getSize().height-1);
        try {
            ans();
        } catch (IOException ex) {
            Logger.getLogger(Voronoi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //lee nuevos sites por mouse
    
    @Override
    public void mousePressed(MouseEvent e) 
    {
        if(e.getButton()==MouseEvent.BUTTON1)
        {
            Point p=e.getPoint();
            lisit.add(new Site(p.x,p.y,new Color((float)Math.random(), (float)Math.random(), (float)Math.random()),""+(lisit.size()+1)));
            cell.add(new Polygoncell(lisit.getLast()));
            repaint();
            try {
                app(lisit.getLast());
            } catch (IOException ex) {
                Logger.getLogger(Voronoi.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else
            if(e.getButton()==MouseEvent.BUTTON3)
            {
                Point p=e.getPoint();
                liQ.add(new PuntoQ(p.x, p.y, 0));
                repaint();
                try {
                    app(liQ.getLast());
                } catch (IOException ex) {
                    Logger.getLogger(Voronoi.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else
                if(e.getButton()==MouseEvent.BUTTON2)
                {
                    Point p=e.getPoint();
                    for(Polygoncell c:cell)
                    {
                        if(c.getP().contains(p)&&lisit.size()>1)
                        {
                            //int id=Integer.parseInt(c.getCenter().name)+1;
                            Site d=c.getCenter();
                            cell.remove(c);
                            lisit.remove(d);
                            break;
                        }
                    }
                    repaint();
                }
    }
    
    @Override
    public void mouseClicked (MouseEvent e) 
    {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
         //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
         //To change body of generated methods, choose Tools | Templates.
    }
}

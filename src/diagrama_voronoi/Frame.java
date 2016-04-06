package diagrama_voronoi;

import java.awt.Container;
import java.awt.HeadlessException;
import java.io.IOException;
import javax.swing.JFrame;

public class Frame extends JFrame
{
    private Voronoi vor=new Voronoi();
    private Container c;
    public Frame() throws HeadlessException, IOException 
    {
        setTitle("Diagrama de Voronoi");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        c=getContentPane();
        c.add(vor);
        setSize(vor.getSize());
        setVisible(true);
    }    
}
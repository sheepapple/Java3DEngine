import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

public class PolygonObj{

  
    Polygon P;
    Color c;
    boolean inView = true;
	  double lighting = 1;
    
    public PolygonObj(double[] x, double[] y, Color color)
    {
        P = new Polygon();
        for(int i = 0; i < x.length; i++){
            P.addPoint((int)x[i], (int)y[i]);
        }
        this.c = color;
    }

    //Updates the polygon object
    void updatePolygonObj(double[] x, double[] y){
        P.reset();
		  for(int i = 0; i<x.length; i++)
  		{
  			P.xpoints[i] = (int) x[i];
  			P.ypoints[i] = (int) y[i];
  			P.npoints = x.length;
  		}
    }

    //Draws the polygon
    //this is where you could add outlines using a Graphics2D, when I did it, it looked quite good but it lagged the system drastically for whatever reasons so I opted to not include it
    void drawPolygon(Graphics g){


      //Uncomment this if you want outlines (WARNING: It will lag the system severely)
      
      // if(Display.Outlines){
      //   Graphics2D g2 = (Graphics2D) g;
      //   g2.setStroke(new BasicStroke(5));
      //   g2.setColor(new Color(0, 0, 0));
      //   g2.drawPolygon(P);
      //   g.setColor(new Color(0, 0, 0));     
      //   g.drawPolygon(P);
      // }
      
      if(inView){
        g.setColor(new Color((int)(c.getRed() * lighting), (int)(c.getGreen() * lighting), (int)(c.getBlue() * lighting)));
        g.fillPolygon(P);
      }
    }
}
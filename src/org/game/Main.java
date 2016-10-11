package org.game;
 
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame; 
 
public class Main {
    
    
    public static void main(String[] args) { 
        
        Game g = new Game(); 
        
        JFrame f = new JFrame();
        f.getContentPane().add(g.getCanvas()); 
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1024, 768);
        //f.setResizable(false);
        f.setVisible(true); 
        
        g.start();
    }
    public static BufferedImage draw(
            BufferedImage source,
            double x1, double y1, double x2, double y2,
            double beamWidth,
            Color beamColor, Color darknessColor) {
        RenderingHints hints = new RenderingHints(
              RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        BufferedImage bi = new BufferedImage(
                source.getWidth(), source.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = bi.createGraphics();
        g.setRenderingHints(hints);

        g.drawImage(source, 0, 0, null);

        // Create a conical shape to constrain the beam
        double distance = Math.sqrt(Math.pow(x1 - x2, 2d) + Math.pow(y1 - y2, 2d));
        double tangent = (y2 - y1) / (x2 - x1);
        double theta = Math.atan(tangent);
        System.out.println(
                "distance: " + distance
                + "  tangent: " + tangent
                + "  theta: " + theta);
        double minTheta = theta + beamWidth / 2;
        double maxTheta = theta - beamWidth / 2;
        double xMin = x1 + distance * Math.cos(minTheta);
        double yMin = y1 + distance * Math.sin(minTheta);

        double xMax = x1 + distance * Math.cos(maxTheta);
        double yMax = y1 + distance * Math.sin(maxTheta);

        Polygon beam = new Polygon();
        beam.addPoint((int) x1, (int) y1);
        beam.addPoint((int) xMax, (int) yMax);
        beam.addPoint((int) xMin, (int) yMin);

        g.setColor(beamColor);
        GradientPaint gp = new GradientPaint(
                (int)x1,(int)y1, beamColor,
                (int)x2,(int)y2, darknessColor);
        g.setClip(beam);
        g.setPaint(gp);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());

        // create an area the size of the image, but lacking the beam area
        Area darknessArea = new Area(new Rectangle(0, 0, bi.getWidth(), bi.getHeight()));
        darknessArea.subtract(new Area(beam));
        g.setColor(darknessColor);
        g.setClip(darknessArea);
        g.fillRect(0, 0, bi.getWidth(), bi.getHeight());

        // fill in the beam edges with black (mostly to smooth lines)
        g.setClip(null);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.draw(new Line2D.Double(x1,y1,xMin,yMin));
        g.draw(new Line2D.Double(x1,y1,xMax,yMax));

        g.dispose();

        return bi;
    }

}

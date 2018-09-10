package detection;
import java.awt.Color;
import java.awt.geom.*;
import java.io.Serializable;

public abstract class DetectionShape implements Serializable
{
    Point2D.Double center;
    boolean traversable = false;
    Color color = Color.blue;
    
    public abstract boolean collision( DetectionShape s );
    
    public boolean isTraversable()
    { return traversable; }
    
    public void setTraversable(boolean b)
    { traversable = b; }
    
    public void setColor(Color c)
    { color = new Color(c.getRed(), c.getGreen(), c.getBlue(), 100); }
    
    public Color getColor()
    { return color; }
    
    public abstract void setLocation(double x, double y);
    
    public abstract Point2D.Double getLocation();

	public void setLocation (Point2D.Double p) 
	{
			center = p;
	}
	
	public abstract double getMaxRadius();
}
package detection;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

public class DetectionCircle extends DetectionShape
{
    private Point2D.Double center;
    private int radius;
    private Ellipse2D circle;
    DetectionCircle( Point2D.Double c, int r )
    {
        center = c;
        radius = r;
        circle = new Ellipse2D.Double( c.getX()-r, c.getY()-r, 2*r, 2*r);
        setTraversable(false);
    }
    
    public DetectionCircle(int x, int y, int r)
    {
        center  = new Point2D.Double(x,y);
        radius = r;
        circle = new Ellipse2D.Double( x-r, y-r, 2*r, 2*r);
        setTraversable(false);
    }
    
    public boolean collision(DetectionShape s)
    {
        if( s instanceof DetectionRectangle )
        {
        	if(center.distance(((DetectionRectangle)s).getCenter()) > radius+((DetectionRectangle)s).getMaxRadius() )
        	{
        		return false;
        		
        	}
        	if( ((DetectionRectangle)s).getRect().contains(center))
        		return true;
        	else
        	{
	        	Point2D[] corners = ((DetectionRectangle)s).getCorners();
	        	double closestDist = Double.MAX_VALUE;
	        	double distToA, distToB, distAtoB;
	        	double angleA, angleB;
	        	Point2D cornerA, cornerB;
	        	for(int i = 0; i < 4; i++)
	        	{
	        		cornerA = corners[i];
	        		cornerB = corners[(i+1)%4];
	        		distToA = center.distance(cornerA);
	        		distToB = center.distance(cornerB);
	        		distAtoB = cornerA.distance(cornerB);
	        		angleA = Math.acos( (Math.pow(distToA, 2) + Math.pow(distAtoB, 2) - Math.pow(distToB, 2)) / (2*distToA*distAtoB) );
	        		angleB = Math.acos( (Math.pow(distToB, 2) + Math.pow(distAtoB, 2) - Math.pow(distToA, 2)) / (2*distToB*distAtoB) );
	        		if(Math.toDegrees(Math.max(angleA, angleB)) < 90.0)
	        		{
	        			closestDist = Math.min(closestDist, distToA*Math.sin(angleA));
	        		}
	        		else
	        		{
	        			closestDist = Math.min(closestDist, Math.min(distToA, distToB));
	        		}
	        	}
	        	return closestDist <= radius;
        	}
        }
        else if ( s instanceof DetectionCircle )
        {
            return (radius + ((DetectionCircle)s).getRadius() >= center.distance(((DetectionCircle)s).getCenter()));
        }
        else
        {
            System.out.println("Error, invalid detection type.");
            return false;
        }
    }
    
    public Point2D.Double getCenter()
    {return center;}
    
    public Point2D.Double getLocation()
	{
		return getCenter();
	}
    
    public int getRadius()
    { return radius; }
    
    public double getMaxRadius()
    { return radius; }
    
    
    public Ellipse2D getCircle()
    { return circle; }

	public void setLocation(double x, double y)
	{
		center = new Point2D.Double(x,y);
		circle = new Ellipse2D.Double( x-radius, y-radius, 2*radius, 2*radius); 
		
	}

	public void setLocation(int x, int y)
	{
		setLocation(x*1.0,y*1.0);
		
	}
    
}

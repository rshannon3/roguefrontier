package detection;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class DetectionRectangle extends DetectionShape
{
    private Rectangle2D.Double rect;
    
    
    public DetectionRectangle(double x, double y, int h, int w)
    {
        rect = new Rectangle2D.Double(x,y,w,h);
        setTraversable(false);
    }
    
    DetectionRectangle(Rectangle2D.Double r)
    {
        rect = r;
        setTraversable(false);
    }
    
    
    // ("\(@_@)/") 
    public Rectangle2D.Double getRect()
    { return rect; }
    
    public boolean collision(DetectionShape s)
    {
        if( s instanceof DetectionRectangle )
        {
            return rect.intersects( ((DetectionRectangle)(s)).getRect() ) ;
        }
        else if ( s instanceof DetectionCircle )
        {
            return s.collision(this);
        }
        else
        {
            System.out.println("Error, invalid detection type.");
            return false;
        }
    }
    
    /**
     * Returns an array containing the corners of the rectangle, starting with the top left and proceeding counter clockwise.
     */
    public Point2D[] getCorners()
    {
    	Point2D[] result = new Point2D[4];
    	result[0] = new Point2D.Double(rect.x,rect.y);
    	result[1] = new Point2D.Double(rect.x+rect.width,rect.y);
    	result[2] = new Point2D.Double(rect.x+rect.width,rect.y+rect.height);
    	result[3] = new Point2D.Double(rect.x,rect.y+rect.height);
    	return result;
    }

    /*
     * returns the length of the line from the center to a corner
     */
	public double getMaxRadius()
	{
		double w = rect.getWidth();
		double h = rect.getHeight();
		return Math.sqrt(w*w + h*h)/2;
	}

	public Point2D.Double getCenter()
	{
		return new Point2D.Double(rect.getCenterX(),rect.getCenterY());
	}
	
	public Point2D.Double getLocation()
	{
		return getCenter();
	}

	public Rectangle getDrawingRect()
	{
		return new Rectangle((int)(this.getRect().x), (int)(this.getRect().y), (int)(this.getRect().width), (int)(this.getRect().height));
	}
    
	public void setLocation(int x, int y)
	{
		rect.x = x;
		rect.y = y;
	}

	@Override
	public void setLocation(double x, double y)
	{
		rect.x = x;
		rect.y = y;
		
	}
    
}

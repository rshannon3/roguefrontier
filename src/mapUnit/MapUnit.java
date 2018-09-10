
package mapUnit;
import game.MapUnitList;
import detection.DetectionRectangle;
import detection.DetectionShape;

import java.awt.Image;
import java.io.Serializable;

/*
 * MapUnit
 * Anything that exists in the map
 */
public class MapUnit implements Serializable
{
	public enum Name{MAP_UNIT, ACTOR, PLAYER, SPAZ, DUMMY, ZOMBIE, ITEM_CONTAINER};
	 protected DetectionShape collisionBox;
	 protected MapUnitList obs;
	 protected static final int DEFAULT_SIZE = 25;
	 
	 public MapUnit( MapUnitList o, DetectionShape d)
	 {
		 obs = o;
		 collisionBox = d;
		 //obs.add(this);
	 }
	 
	 public MapUnit(double x, double y, MapUnitList o)
	 {
		 obs = o;
		 collisionBox = new DetectionRectangle(x, y, DEFAULT_SIZE, DEFAULT_SIZE);
		 //obs.add(this);
	 }
	 
	 public MapUnit(double x, double y, int h, int w, MapUnitList o)
	 {
		 obs = o;
		 collisionBox = new DetectionRectangle(x, y,h, w);
		 //obs.add(this);
	 }
    
    public int getX()
    { return (int)collisionBox.getLocation().x; }
    
    public int getY()
    { return (int)collisionBox.getLocation().y; }
    
    public DetectionShape getCollisionBox()
    { return collisionBox; }
    
    public void setCollisionBox(DetectionShape s)
    { collisionBox = s; }
    
    public void setLocation(int x,int y)
    {
    	collisionBox.setLocation(x, y);
    }
    
    public Image getImage()
    {
    	return MapUnitImageFlyweight.get(this);
    }
}

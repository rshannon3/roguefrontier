package game;

// Obstacles.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

import java.io.Serializable;
/* A collection of boxes which the worm cannot move over
 */
import java.util.ArrayList;

import detection.DetectionCircle;
import detection.DetectionShape;
import mapUnit.Actor;
import mapUnit.MapUnit;
import mapUnit.Player;
import mapUnit.Mob;

public class MapUnitList implements Serializable {
	private ArrayList<MapUnit> mapUnits; // arraylist of MapUnit objects
	// private ArrayList<DetectionShape> obs; // arrayList of DetectionShapes
	public static int deathCount = 0;

	public MapUnitList() {
		mapUnits = new ArrayList<MapUnit>();
	}

	synchronized public void add(MapUnit test) {
		if (test == null)
			return;
		/*
		if (!test.getCollisionBox().isTraversable()) {
			for (int i = 0; i < mapUnits.size(); i++) {
				if (test.getCollisionBox().collision(get(i))) {
					// System.out.println("COLLISION DETECTED!!");
					return;
				}
			}
		}*/
		mapUnits.add(test);
	}

	/*
	 * synchronized void addRect(int x, int y) { add(new DetectionRectangle(x,
	 * y, BOX_LENGTH, BOX_LENGTH)); }
	 * 
	 * synchronized public void addCircle(int x, int y) { add(new
	 * DetectionCircle(x, y, BOX_LENGTH)); }
	 */

	synchronized public void remove(DetectionShape s) {
		for (int i = 0; i < mapUnits.size(); i++) {
			if (mapUnits.get(i).getCollisionBox().collision(s)) {
				mapUnits.remove(i);
				break;
			}
		}
	}

	synchronized public void remove(MapUnit m) {
		if (m instanceof Mob && mapUnits.remove(m))
			deathCount++;
		mapUnits.remove(m);
		//mapUnits.remove(m); // TODO FIX THIS
	}

	/*
	 * synchronized public boolean hits(Point p, int size) // does p intersect
	 * with any of the obstacles? { Rectangle r = new Rectangle(p.x, p.y, size,
	 * size); Rectangle box; for(int i=0; i < obs.size(); i++) { box =
	 * (Rectangle) obs.get(i); if (box.intersects(r)) return true; } return
	 * false; } // end of intersects()
	 */
	/*
	 * synchronized public void draw(Graphics g) // draw a series of blue boxes
	 * { Rectangle box; g.setColor(Color.blue); for(int i=0; i < obs.size();
	 * i++) { box = (Rectangle) obs.get(i).getRect(); g.fillRect( box.x, box.y,
	 * box.width, box.height); } } // end of draw()
	 */

	synchronized public int getNumObstacles() {
		return mapUnits.size();
	}

	synchronized public ArrayList<MapUnit> getObstacleList() {
		return mapUnits;
	}

	synchronized public ArrayList<MapUnit> getObstaclesAt(double mx, double my) {
		DetectionCircle test = new DetectionCircle((int)mx, (int)my, 1);
		ArrayList<MapUnit> result = new ArrayList<MapUnit>(1);
		for(MapUnit mu : mapUnits) {
			if (mu.getCollisionBox().collision(test)) {
				result.add(mu);
			}
		}
		return result;
	}

	public DetectionShape get(int i) {
		return mapUnits.get(i).getCollisionBox();
	}

	public MapUnit getUnit(int i) {
		return mapUnits.get(i);
	}

	public Player getPlayer() // TODO Assumes there is onlyone player. Returns
								// the first found
	{
		for (MapUnit mu : mapUnits) {
			if (mu instanceof Player)
				return (Player) mu;
		}
		return null;
	}

	synchronized public void act() {
		// TODO Debug later
		for (int j = 0; j < mapUnits.size(); j++) {
			if (mapUnits.get(j) instanceof Actor)
				((Actor) mapUnits.get(j)).act();
		}
	}

	public int getNumActors() {
		int result = 0;
		for(MapUnit mu : mapUnits){
			if(mu instanceof Actor){
				result++;	
			}
		}
		return result;
	}

} // end of Obstacles class

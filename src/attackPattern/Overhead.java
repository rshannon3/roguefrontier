package attackPattern;
import java.awt.Color;

import detection.DetectionCircle;
import game.MapUnitList;
import mapUnit.MapUnit;
import mapUnit.Mob;
import mapUnit.Player;




public class Overhead extends AttackPattern
{
	private static int weaponLength = 3;
	private static int haftLength = 25;
	
	public Overhead(Mob p, MapUnitList o)
	{
		super(p, o, weaponLength, haftLength);
	}
	
	public Overhead(Mob mob, MapUnitList mapUnitList, int weaponLength, double haftLength, int radius,
			int startupFrames, int activeFrames, int recoveryFrames, int hitStun, double knockbackSpeed, double knockbackDistance, int damage)
	{
		super(mob, mapUnitList, weaponLength, haftLength, radius, startupFrames, activeFrames, recoveryFrames, hitStun, knockbackSpeed, knockbackDistance, damage);
	}

	//Places the hitboxes onto the map over time
	// TODO account for varying startup times and bigger weapon lengths
	public void startUp() 
	{
		if(attackFrame == 0)
		{
			double x = Math.cos(myMob.getFacingAngle()) ;
			double y = Math.sin(myMob.getFacingAngle()) ;
			createHitbox(x, y);
			super.startUp();
		}
		if(attackFrame < hitbox.length)
		{
			/*
			double hX = myPlayer.getX() + ( myPlayer.getCollisionBox().getRadius() + (2 * radius * (attackFrame + .5))) * x;
			double hY =  myPlayer.getY() - ( myPlayer.getCollisionBox().getRadius() + (2 * radius * (attackFrame + .5))) * y;
			hitbox[attackFrame] = new DetectionCircle( (int)(hX), (int)(hY), radius); //creates the hitbox
			hitbox[attackFrame].setTraversable(true);
			hitbox[attackFrame].setColor(Color.yellow);
			*/
			//obs.add(hitbox[attackFrame]); // adds the hitbox onto the map
		}

	}
	
	

}

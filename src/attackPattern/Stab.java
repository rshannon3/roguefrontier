package attackPattern;
import java.awt.Color;

import detection.DetectionCircle;
import game.MapUnitList;
import mapUnit.MapUnit;
import mapUnit.Mob;
import mapUnit.Player;




public class Stab extends AttackPattern
{
	
	private double stabDistance;
	

	public Stab(Mob mob, MapUnitList mapUnitList, int weaponLength, double haftLength, int radius,
			int startupFrames, int activeFrames, int recoveryFrames, int hitStun, double knockbackSpeed, double knockbackDistance, int damage, double stabDistance)
	{
		super(mob, mapUnitList, weaponLength, haftLength, radius, startupFrames, activeFrames, recoveryFrames, hitStun, knockbackSpeed, knockbackDistance, damage);
		this.stabDistance = stabDistance;
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
	}
	@Override
	public void updateHitboxLocation()
	{
	    	for(int k = 0; k < hitbox.length; k++)
			{
				double angleX = Math.cos(myMob.getFacingAngle());
				double angleY = Math.sin(myMob.getFacingAngle());
				double hX = myMob.getX() + ( ((DetectionCircle) myMob.getCollisionBox()).getRadius() + (2 * radius * (k + .5)) + haftLength ) *angleX;
				double hY =  myMob.getY() - ( ((DetectionCircle) myMob.getCollisionBox()).getRadius() + (2 * radius * (k + .5)) + haftLength) * angleY;
				if(active)
					hitbox[k].setLocation( (int)(hX + stabDistance*attackFrame/activeFrames*angleX), (int)(hY - stabDistance*attackFrame/activeFrames*angleY)); //updates the hitbox location
				else
					hitbox[k].setLocation( (int)(hX), (int)(hY)); //updates the hitbox location
			}
	}

	public double getStabDistance()
	{
		return stabDistance;
	}

	public void setStabDistance(double stabDistance)
	{
		this.stabDistance = stabDistance;
	}

}

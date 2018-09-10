package attackPattern;
import game.MapUnitList;
import detection.DetectionCircle;
import mapUnit.Mob;



/*
 * 
 * A swing is any attack pattern that creates an arc around the player. The attributes of the arc can be changed
 * in construction.
 * 
 */
public class Slash extends AttackPattern {
	
	private double slashWidth = Math.PI / 2; // the width of the slash attack
	private static int weaponLength = 2;
	
	public Slash(Mob p, MapUnitList o)
	{
		super(p, o, weaponLength, 10);
		
	}
	
	public Slash(Mob mob, MapUnitList mapUnitList, int weaponLength, double haftLength, int radius,
			int startupFrames, int activeFrames, int recoveryFrames, int hitStun, double knockbackSpeed, double knockbackDistance, int damage, double slashWidth)
	{
		super(mob, mapUnitList, weaponLength, haftLength, radius, startupFrames, activeFrames, recoveryFrames, hitStun, knockbackSpeed, knockbackDistance, damage);
		this.slashWidth = slashWidth;
	}
	
	public Slash(Mob p, MapUnitList o, int weaponLength, double haftLength, double slashWidth)
	{
		super(p, o, weaponLength, haftLength);
		this.slashWidth = slashWidth;
	}
	
	
	// Places the hitbox onto the map,
	// TODO SAME FIX AS STAB
	public void startUp()
	{
		if(attackFrame == 0)
		{
			double x = Math.cos(myMob.getFacingAngle() - slashWidth / 2);
			double y = Math.sin(myMob.getFacingAngle() - slashWidth / 2);
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
	
	/*
	 * Rotates each circle in the hitbox around the player according to the slash width
	 * 
	 * 
	 */
	public void updateHitboxLocation()
	{
		
		//Calculates the new position of each hitbox based on the angle and frame of the attack 
		if(starting)
        {
        	double moveX = Math.cos((myMob.getFacingAngle() )   - slashWidth / 2);
        	double moveY = Math.sin((myMob.getFacingAngle() )   - slashWidth / 2);


        	// Sets the new positions of each hitbox
        	for(int k = 0; k < hitbox.length; k++)
        	{
        		hitbox[k].setLocation((int)(myMob.getX() +( ((DetectionCircle) myMob.getCollisionBox()).getRadius() + (2 * radius * (k + .5)) + haftLength) * moveX), 
        				(int)(myMob.getY() - (((DetectionCircle) myMob.getCollisionBox()).getRadius()  + (2 * radius * (k + .5))+ haftLength) * moveY));
        	}
        }
		else if(active)
        {
        	double moveX = Math.cos((myMob.getFacingAngle() + attackFrame * slashWidth / activeFrames)  - slashWidth / 2);
        	double moveY = Math.sin((myMob.getFacingAngle() + attackFrame * slashWidth / activeFrames) - slashWidth / 2);


        	// Sets the new positions of each hitbox
        	for(int k = 0; k < hitbox.length; k++)
        	{
        		hitbox[k].setLocation((int)(myMob.getX() +( ((DetectionCircle) myMob.getCollisionBox()).getRadius() + (2 * radius * (k + .5))+ haftLength) * moveX), 
        				(int)(myMob.getY() - (((DetectionCircle) myMob.getCollisionBox()).getRadius()  + (2 * radius * (k + .5))+ haftLength) * moveY));
        	}
        }
        else if(recovering)
        {
        	double moveX = Math.cos((myMob.getFacingAngle() + slashWidth)   - slashWidth / 2);
        	double moveY = Math.sin((myMob.getFacingAngle() + slashWidth)   - slashWidth / 2);


        	// Sets the new positions of each hitbox
        	for(int k = 0; k < hitbox.length; k++)
        	{
        		hitbox[k].setLocation((int)(myMob.getX() +( ((DetectionCircle) myMob.getCollisionBox()).getRadius() + (2 * radius * (k + .5))+ haftLength) * moveX), 
        				(int)(myMob.getY() - (((DetectionCircle) myMob.getCollisionBox()).getRadius()  + (2 * radius * (k + .5))+ haftLength) * moveY));
        	}
        }
        else super.updateHitboxLocation();
        
	}

	public double getSlashWidth()
	{
		return slashWidth;
	}

	public void setSlashWidth(double slashWidth)
	{
		this.slashWidth = slashWidth;
	}
	
	
}

package attackPattern;

import game.MapUnitList;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

import detection.DetectionCircle;
import detection.DetectionShape;

import mapUnit.MapUnit;
import mapUnit.Mob;

/* 
 * The Weapon class is responsible for holding attack pattern types and creating hitboxes. This class may later be 
 * deprecated and moved into the player class.
 * 
 * 
 */
public abstract class AttackPattern implements Serializable
{
	
	
	protected Mob myMob; //access to player is needed for position and state reference
	protected MapUnit hitbox[]; // the size and shape of the hitbox will change based on the attack
	protected int attackFrame = 0; // used to keep track of how far into the attack the weapon is
	protected MapUnitList obs; // reference to the obstacles
	protected int radius = 8; // the radius of each hitbox
	
	//frame times for attack stages
	protected int startupFrames= 5;  // the amount of startup
	protected int activeFrames = 20; // the active frames of the attack
	protected int recoveryFrames = 10; // the amount of recovery s
	
	//booleans to keep track of attack state
	protected boolean starting = true;  
	protected boolean active = false;
	protected boolean recovering = false;
	
	protected int hitstun = 10; // how long the reel animation caused by this attack will be
	protected double knockbackSpeed = .5;
	protected double knockbackDistance = 5.0; // how far the attack will knock enemies back
	protected int damage = 1;
	
	protected int weaponLength = 2; // the length and consequently number of hitboxes of the weapon
	protected double haftLength = 0;  // TODO the length, in pixels, away from the character the hitboxes start drawing
	private ArrayList<Mob> mobsHit;
	
	public AttackPattern(Mob mob, MapUnitList mapUnitList)
	{
		myMob = mob;
		obs = mapUnitList;
	}
	public AttackPattern(Mob mob, MapUnitList mapUnitList, int weaponLength, double haftLength)
	{
		//super(p.getX(), p.getY(), o);
		this(mob,mapUnitList);
		this.weaponLength = weaponLength;
		this.haftLength = haftLength;
		hitbox = new MapUnit[weaponLength];
	}
	
	/**
	 * @param mob
	 * @param mapUnitList
	 * @param weaponLength
	 * @param haftLength
	 * @param radius
	 * @param startupFrames
	 * @param activeFrames
	 * @param recoveryFrames
	 */
	public AttackPattern(Mob mob, MapUnitList mapUnitList, int weaponLength, double haftLength, int radius, int startupFrames, int activeFrames, int recoveryFrames)
	{
		this(mob,mapUnitList,weaponLength,haftLength);
		this.startupFrames= startupFrames;
		this.activeFrames = activeFrames;
		this.recoveryFrames = recoveryFrames;
		this.radius = radius;
	}
	
	public AttackPattern(Mob mob, MapUnitList mapUnitList, int weaponLength, double haftLength, int radius,
			int startupFrames, int activeFrames, int recoveryFrames, int hitStun, double knockbackSpeed, double knockbackDistance, int damage)
	{
		this(mob,mapUnitList,weaponLength,haftLength,radius,startupFrames,activeFrames,recoveryFrames);
		this.hitstun = hitStun;
		this.knockbackSpeed = knockbackSpeed;
		this.knockbackDistance = knockbackDistance;
		this.damage = damage;
	}
	
	public void attack()
	{
			
		//------------------------------------------------Starting Phase---------------------------------------//
		if(starting && attackFrame < startupFrames)
		{
			//Delegates to subclasses specific attack pattern
			startUp();
			updateHitboxLocation();

			attackFrame++;
			attackFrame %= startupFrames;
			if(attackFrame == 0)
			{
				starting = false;
				active = true;
			}

		}
		//------------------------------------------------Active Phase---------------------------------------//
		else if( active && attackFrame < activeFrames )
		{
			//Delegates to subclasses specific attack pattern
			updateHitboxLocation();
			active();
			attackFrame++;
			attackFrame %= activeFrames;
			if(attackFrame == 0)
			{
				active = false;
				recovering= true;
			}
		}
		//------------------------------------------------Recovery Phase---------------------------------------//
		else if( recovering &&  attackFrame < recoveryFrames )
		{
			//Delegates to subclasses specific attack pattern
			recovery();
			updateHitboxLocation();
			attackFrame++;
			attackFrame %= recoveryFrames;
			if(attackFrame == 0) // if attack is over, stop attacking and remove hitbox from map
			{
				myMob.setAttacking(false);
				myMob.resetSpeed();
				recovering = false;
				starting = true;
				removeHitbox();
			}

		}
	}
	
	// Updates the hitbox location to follow the player during movement
	// should be customized for subclasses with specific hitbox placements
	public void updateHitboxLocation() 
	{
		for(int k = 0; k < hitbox.length; k++)
		{
			double angleX = Math.cos(myMob.getFacingAngle());
			double angleY = Math.sin(myMob.getFacingAngle());
			double hX = myMob.getX() + ( ((DetectionCircle) myMob.getCollisionBox()).getRadius() + (2 * radius * (k + .5)) + haftLength) *angleX;
			double hY =  myMob.getY() - ( ((DetectionCircle) myMob.getCollisionBox()).getRadius() + (2 * radius * (k + .5)) + haftLength) * angleY;
			hitbox[k].setLocation( (int)(hX), (int)(hY)); //updates the hitbox location
		}
	}

	/*
	 * Attack creates a hitbox for a set amount of frames and inflicts damage upon collision with an Actor
	 * TODO add damage infliction to players
	 * 
	 * 
	 */
	
	
	public void createHitbox(double angleX, double angleY)
	{
		for(int k = 0; k < hitbox.length; k++)
		{
			double hX = myMob.getX() + ( ((DetectionCircle) myMob.getCollisionBox()).getRadius() + (2 * radius * (k + .5)) +  haftLength) *angleX;
			double hY =  myMob.getY() - ( ((DetectionCircle) myMob.getCollisionBox()).getRadius() + (2 * radius * (k + .5)) + haftLength) * angleY;
			hitbox[k] = new MapUnit( obs, new DetectionCircle( (int)(hX), (int)(hY), radius)); //creates the hitbox
			hitbox[k].getCollisionBox().setTraversable(true);
			hitbox[k].getCollisionBox().setColor(Color.yellow);
			obs.add(hitbox[k]);
		}
	}
	
	
	//---------------------------------------------------------Default Attack Phase Methods------------------------------------//
	//  Color codes the hitbox based on the current Attack Phase
	
	public void startUp()
	{
		setHitboxColor(Color.yellow);
		this.mobsHit = new ArrayList<Mob>();
	}
	
	public void active()
	{
		setHitboxColor(Color.red);
		for(int j = 0; j < obs.getNumObstacles(); j++)
		{
			DetectionShape s = obs.get(j);
			if(obs.getUnit(j) instanceof Mob && !s.isTraversable() && !s.equals(myMob.getCollisionBox()) && !mobsHit.contains(obs.getUnit(j)))
			{
				for(int k = 0; k < hitbox.length; k++)
				{
					//Apply hitstun upon collision with a Mob
					if(hitbox[k].getCollisionBox().collision(s))
					{
						((Mob)obs.getUnit(j)).recieveAttack(((Mob)obs.getUnit(j)), damage);
						// If the knockback time is longer than the reel time, set the reel time to be equal to the knockback time
						((Mob)obs.getUnit(j)).startReel( (hitstun > (int)(knockbackDistance / knockbackSpeed)) ? hitstun : (int)(knockbackDistance / knockbackSpeed), 
								knockbackSpeed,  knockbackDistance, myMob.getFacingAngle());
						mobsHit.add((Mob)obs.getUnit(j));
					}

				} 
			}
		}
	}
	
	public void recovery()
	{
		setHitboxColor(Color.cyan);
	}

	public void setHitboxColor( Color c )
	{
		if( attackFrame == 0 )
		{
			for(int k = 0; k < hitbox.length; k++)
			{
				hitbox[k].getCollisionBox().setColor(c);
			}  
		}
	}
	
	//---------------------------------------------------------Default Attack Phase Methods------------------------------------//
	
	
	public void setWeaponLength(int l)
	{ 
		weaponLength = l;
		hitbox = new MapUnit[weaponLength];
	}

	public double getHaftLength()
	{
		return haftLength;
	}

	public void setHaftLength(double haftLength)
	{
		this.haftLength = haftLength;
	}

	public int getStartupFrames()
	{
		return startupFrames;
	}

	public void setStartupFrames(int startupFrames)
	{
		this.startupFrames = startupFrames;
	}

	public int getActiveFrames()
	{
		return activeFrames;
	}

	public void setActiveFrames(int activeFrames)
	{
		this.activeFrames = activeFrames;
	}

	public int getRecoveryFrames()
	{
		return recoveryFrames;
	}

	public void setRecoveryFrames(int recoveryFrames)
	{
		this.recoveryFrames = recoveryFrames;
	}

	public int getHitstun()
	{
		return hitstun;
	}

	public void setHitstun(int hitstun)
	{
		this.hitstun = hitstun;
	}

	public double getKnockbackDistance()
	{
		return knockbackDistance;
	}

	public int getRadius()
	{
		return radius;
	}

	public void setRadius(int radius)
	{
		this.radius = radius;
	}

	public void setKnockbackDistance(double knockback)
	{
		knockbackDistance = knockback;
	}

	/**
	 * @return the knockbackSpeed
	 */
	public double getKnockbackSpeed() {
		return knockbackSpeed;
	}

	/**
	 * @param knockbackSpeed the knockbackSpeed to set
	 */
	public void setKnockbackSpeed(double knockbackSpeed) {
		this.knockbackSpeed = knockbackSpeed;
	}

	public int getDamage()
	{
		return damage;
	}

	public void setDamage(int damage)
	{
		this.damage = damage;
	}

	/**
	 * @return the weaponLength
	 */
	public int getWeaponLength() {
		return weaponLength;
	}

	public void removeHitbox() 
	{
		for(int i = 0; i < hitbox.length; i++)
		{
			obs.remove(hitbox[i]);

		}
	}

	public void interruptAttack() 
	{
		starting = true;
		active = false;
		recovering = false;
		attackFrame = 0;
		
	}
	public boolean isStarting()
	{
		return starting;
	}
	public void setStarting(boolean starting)
	{
		this.starting = starting;
	}
	public boolean isActive()
	{
		return active;
	}
	public void setActive(boolean active)
	{
		this.active = active;
	}
	public boolean isRecovering()
	{
		return recovering;
	}
	public void setRecovering(boolean recovering)
	{
		this.recovering = recovering;
	}
}

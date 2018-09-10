package mapUnit;

import game.MapUnitList;
import item.Armor;
import item.Inventory;
import item.LootFactory;
import item.Weapon;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;

import attackPattern.AttackPattern;
import attackPattern.Slash;
import detection.DetectionCircle;


public abstract class Mob extends Actor
{
	
	private transient Image currentImage;
	private int updateSpeed;
	private int imageIndex = 0;
	private int dir = 0;
	private int facingDir = 0;
	private int framesSinceUpdate = 0;
	
	private static String IMAGE_DIR = System.getProperty("user.dir") + System.getProperty("file.separator") + "RogueFrontierImageSet" + 
			System.getProperty("file.separator");
	
	//----------------------------------------------Dashing Properties-----------------------------------------------//
	private double knockbackSpeed; // the movespeed during the dash
	private double knockbackDistance; // the distance of the knockback
	private int knockbackDuration; // the frame duration of the dash
	private int knockbackFrame = 0; // the current frame of the dash
	private boolean knockback = false; // is the player dashing
	private double knockbackAngle;
	//---------------------------------------------------------------------------------------------------------------//
	
	
	//------------------------------------------Attack Recovery----------------------------------------------------//
	private boolean recovering = false; // is the player recovering from an attack
	private int recoveryTime;
	private int recoveryFrame = 0;
	//--------------------------------------------------------------------------------------------------------------//
	
	private Inventory inventory;
	
	//----------------------------------------Health and Stamina----------------------------------//
	private int maxHealth = 3;
	private int maxStamina = 25;
	private int currentHealth = 3;
	private int currentStamina = 25;
	private double staminaRecoverySpeed = 1.4; // How many seconds it takes theplayer to recover one point of stamina
	private int staminaRecoveryTimer = 0;
	//-------------------------------------------------------------------------------------------//
	private boolean reeling = false;
	private int reelTime = 0;
	
	private AttackPattern currentAttack;
	private boolean attacking;
	
	private double walkSpeed = 2.0;
	private boolean walking;
	private int IMAGE_SET_SIZE = 12;
	private static final int INVENTORY_SIZE = 25;
	private static final double MAX_INTERACTION_DISTANCE = 100;
	
	
	public Mob(int x, int y, MapUnitList obs)
    {
		
		super(x, y, obs);
		//w = new Slash(this, obs);
		//st = new Stab(this, obs);
    	updateSpeed = 8 - (int)( 2 * getMoveSpeed());
    	imageIndex = 0;
    	currentAttack = new Slash(this, obs);
    	setMoveSpeed(walkSpeed);
    	updateCurrentImage();
    	getCollisionBox().setColor(Color.green);
    	
    	inventory = new Inventory(INVENTORY_SIZE);
    	
    }

    public int getImageIndex()
	{
		return imageIndex;
	}


	public void setImageIndex(int imageIndex)
	{
		this.imageIndex = imageIndex;
	}

	public void act()
	{
		if(checkDeath())
			die();
		determineAction();
		performAction();
	}
	
	private void die()
	{
		interruptAttack();
		getObs().remove(this);
		getObs().add(LootFactory.get(this));
		
	}

	public boolean checkDeath()
	{
		if(currentHealth <= 0)
		{
			
			return true;
		}
		else return false;
	}
	
	public void performAction()
	{
		//TODO TEST
		regen();
		//TODO TEST
		
		if(isMoving() || reeling || isTurning() || attacking)  //TODO add walking vs moving
		{
			if(reeling)
			{
				if(knockback)
				{
					knockback();
				}
				reel();
			}
			else
			{
				if(isMoving())
					move();
				if(attacking) // Mob can move while attacking
						attack();
				 // Mob cannot turn wile attacking
				if(isTurning())
				{
					if(!attacking)
						turn();
					else if(!currentAttack.isActive())
						turnWithTurnspeed();
				}
			}
			updateDir();
		}
		cycleImage();
	}

	public abstract void determineAction();
	
	public List<Interactable> getNearestInteractables()
	{
		LinkedList<Interactable> result = new LinkedList<Interactable>();
		for(int i = 0; i < getObs().getNumObstacles(); i++ )
		{
			MapUnit mu = getObs().getUnit(i);
			if(mu instanceof Interactable && collisionBox.getLocation().distance(mu.getCollisionBox().getLocation()) + collisionBox.getMaxRadius() + mu.getCollisionBox().getMaxRadius()  < MAX_INTERACTION_DISTANCE)
				result.add((Interactable) mu);
		}
		return result;
	}
    
	
	//--------------------------------------------------------------KNOCK BACK------------------------------------------------------//
	
	public void startKnockback(double angl, double spd, double distance)
	{
		knockback = true;
		knockbackAngle = angl;
		knockbackSpeed = spd;
		knockbackDistance = distance;
		knockbackDuration = (int)Math.ceil(distance / spd); // rounds the duration of the knockback in frames
		setMoveSpeed(spd); // Set your speed to the knockback speed
		setMoving(true);   // set Actor moving Flag
		setMoveAngle(angl);
	}
	
	public void knockback()
	{
		knockbackFrame++;
		knockbackFrame %= knockbackDuration;
		if( knockbackFrame == 0 || !canMove() ) // If the Mob is done being knocked back
		{
			stopKnockback();
		}
		else // get knocked back
		{
			move();
			// TODO DISPLAY A KNOCKBACK ANIMATION
		}
	}
	
	public void stopKnockback()
	{
		setMoveSpeed(walkSpeed);
		knockback = false;
		stopMoving();
		knockbackFrame = 0;
	}
	//------------------------------------------------------------END OF KNOCKBACK--------------------------------------------//
	
	
	
	//------------------------------------------------------------Reeling----------------------------------------------------//


	/*
	 * Called when hit by an attack
	 */
	public void startReel(int reelT, double knockBSpeed, double knockBDistance, double angl)
	{
		reelTime = reelT;
		reeling = true;
		interruptAttack();
		startKnockback(angl, knockBSpeed, knockBDistance);
		getCollisionBox().setColor(Color.black);
	}
	
	private void reel()
	{
		reelTime--;
		if(reelTime < 0)
		{
			reeling = false;
			getCollisionBox().setColor(Color.green);
		}
	}
	
	//---------------------------------------------------------END OF REELING--------------------------------------------//
	
	
	
	//---------------------------------------------------------ATTACKING-----------------------------------------------//
	
	public void startAttack()
	{ attacking = true; }
	
	// TODO trigger Mobs recovery once the attack is through the startup & active frames
	public void attack()
	{
		getCurrentAttack().attack();
	}
	
	public void interruptAttack()
	{
		if(attacking)
		{
			currentAttack.removeHitbox();
			currentAttack.interruptAttack();
			attacking = false;
			resetSpeed();
		}
	}
	
	
	//---------------------------------------------------------END OF ATTACKING---------------------------------------//
	
	
	//---------------------------------------------------------Image Handling----------------------------------------//
	public void cycleImage()
	{
		if( isMoving() )
		{
			framesSinceUpdate++;
			framesSinceUpdate %= updateSpeed;
			//Checks if end of animation, and resets to first frame
			if(currentImage == null)
			{
				setCurrentImageToFirstFrame();
			}
			else if(framesSinceUpdate == 0)
			{
				imageIndex++;
				imageIndex %= getImageSetSize();
			}
		}else{
			setCurrentImageToStandingFrame();
		}
		updateCurrentImage();
		
	}
	
	private void setCurrentImageToStandingFrame() {
		imageIndex = 3;
		updateCurrentImage();
		
	}

	private void updateCurrentImage()
	{
		currentImage = MapUnitImageFlyweight.get(this);	
	}


	private void setCurrentImageToFirstFrame()
	{
		imageIndex = 0;
		updateCurrentImage();
	}
	//-------------------------------------------------------End of Image Handling---------------------------------//
	
	//------------------------------------------------------Regen Methods--------------------------------------------------------//
	//TODO WARNING, TEST METHODS
	public void regen()
	{
		if(getCurrentStamina() >= maxStamina)
		{
			staminaRecoveryTimer = 0;
			return;
		}
		else
		{
			staminaRecoveryTimer++;
			if( staminaRecoveryTimer % ((int)(staminaRecoverySpeed * 60.0)) == 0)
				setCurrentStamina(getCurrentStamina() + 1);
		}
	}
	
	//-----------------------------------------------------End of Regen---------------------------------------------------------//

	private void updateDir()
	{
		double angle = Math.toDegrees(getFacingAngle());
		if(angle < 0)
			angle += 360.0;
		dir = (int)( ((angle + 22.5) % 360) / 45 );	
		
	}
	
	public double getWalkSpeed() 
	{
		return walkSpeed;
	}
	
	public void setWalkSpeed(double s)
	{ walkSpeed = s;  }
	

	@Override
	public Image getImage() 
	{
		return currentImage;
	}
    
	public void setSpeed(int s)
	{
		updateSpeed = s;
	}
	
	
	public int getDir()
	{
		return dir;
	}
	
	public void setDir(int d)
	{
		dir = d;
	}
	
	public boolean isKnockback()
	{ return knockback; }

	
	/**
	 * Heals player by h, up to max Health
	 * 
	 * @param h - The amount you want to heal
	 */
	public void healHealth(int h)
	{
		currentHealth = Math.min(maxHealth, currentHealth+h);
	}


	/**
	 * Sets player's currentStamina to s.
	 * Can increase a player's stamina past its max
	 * See healStamina(int s) to heal up to max.
	 * 
	 * @param s
	 */
	public void setStamina(int s)
	{
		setCurrentStamina(s);
	}

	/**
	 * Recovers player's stamina by s, up to max stamina
	 * 
	 * @param s - The amount you want to heal
	 */
	public void healStamina(int s)
	{
		setCurrentStamina(Math.min(maxStamina, getCurrentStamina()+s));
	}

	public int getMaxStamina()
	{
		return maxStamina;
	}


	public void setMaxStamina(int maxStamina)
	{
		this.maxStamina = maxStamina;
	}
	
	public void resetStaminaRecovery()
	{ staminaRecoveryTimer = 0; }


	public int getMaxHealth()
	{
		return maxHealth;
	}


	public void setMaxHealth(int maxHealth)
	{
		this.maxHealth = maxHealth;
	}


	public boolean isAttacking()
	{
		return attacking;
	}


	public void setAttacking(boolean attacking)
	{
		this.attacking = attacking;
	}


	public AttackPattern getCurrentAttack()
	{
		return currentAttack;
	}


	public void setCurrentAttack(AttackPattern currentAttack)
	{
		this.currentAttack = currentAttack;
	}
	
	public boolean isReeling()
	{ return reeling; }
	

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException
	{
		inputStream.defaultReadObject();
		updateCurrentImage();
	}
	
	public int getImageSetSize()
	{
		return IMAGE_SET_SIZE;
	}

	/**
	 * Called when Mob attacker attacks this Mob
	 * @param damage
	 */
	public void recieveAttack(Mob attacker, int damage)
	{
		//if(!reeling)
			inflictDamage(damage);
		
	}

	/**
	 * Deals damage to player subject to Armour mitigation
	 * If you want to directly decrease the player's health, see decreaseHealth(int damage)
	 * @param damage
	 */
	private void inflictDamage(int damage)
	{
		
		if(getInventory().getEquippedArmor() != null)
			decreaseHealth((int) (damage*getInventory().getEquippedArmor().getDamageScaling()));
		else
			decreaseHealth(damage);
		
	}

	private void decreaseHealth(int damage)
	{
		currentHealth -= damage;
	}
	/**	
	 * @return the currentHealth
	 */
	public int getCurrentHealth() {
		return currentHealth;
	}

	/**
	 * @param currentHealth the currentHealth to set
	 */
	public void setCurrentHealth(int currentHealth) {
		this.currentHealth = currentHealth;
	}

	public int getCurrentStamina() {
		return currentStamina;
	}

	public void setCurrentStamina(int currentStamina) {
		this.currentStamina = currentStamina;
	}
	
	public Weapon getEquippedWeapon()
	{
		return getInventory().getEquippedWeapon();
	}

	/**
	 * Will overwrite players equippedWeapon with weapon
	 * To prevent this, use equipWeapon(Weapon weapon)
	 * @param weapon
	 */
	public void setWeapon(Weapon weapon)
	{
		getInventory().setEquippedWeapon(weapon);
	}
	
	public boolean equipWeapon(Weapon weapon)
	{
		boolean result = getInventory().equipWeapon(weapon);
		return result;
	}
	
	public boolean unequipWeapon()
	{
		return getInventory().unequipWeapon();
	}
	
	public boolean hasWeaponEquipped()
	{
		return getEquippedWeapon() != null;
	}
	
	public Armor getEquippedArmor()
	{
		return getInventory().getEquippedArmor();
	}
	
	/**
	 * Will overwrite players equippedArmor with armor
	 * To prevent this, use equipArmor(Armor armor)
	 * @param armor
	 */
	public void setArmor(Armor armor)
	{
		getInventory().setEquippedArmor(armor);
	}
	
	public boolean equipArmor(Armor armor)
	{
		return getInventory().equipArmor(armor);
	}
	
	public boolean unequipArmor()
	{
		return getInventory().unequipArmor();
	}
	
	public void resetSpeed()
	{ setMoveSpeed(walkSpeed); }

	


}

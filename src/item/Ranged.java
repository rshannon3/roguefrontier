package item;

public class Ranged extends Weapon
{
	
	public static final String IMAGE_DIR = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "RogueFrontierImageSet"
			+ System.getProperty("file.separator") + "Item" + System.getProperty("file.separator") + "Ranged.gif";

	//---------------------------------------Stab Attributes----------------------------------------//
	public static final int
						STAB_STARTUP = 60, 
						STAB_ACTIVE = 30, 
						STAB_RECOVERY = 1,
						STAB_WEAPON_LENGTH = 4,
						STAB_RADIUS = 3,
						STAB_HITSTUN = 111;
	public static final double
						STAB_HAFT_LENGTH = 17,
						STAB_KNOCKBACK_SPEED = 10.0,
						STAB_KNOCKBACK_DISTANCE = 100.0,
						STAB_DAMAGE_MULTIPLIER = 1.0,
						STAB_DISTANCE = 400;
	
	public Ranged( int d)
	{
		super("Ranged", IMAGE_DIR, d);
	}
	
	public Ranged(String name, String imagePath, int d)
	{
		super(name, imagePath, d);
	}
	

	public int getStabStartup()
	{
		return STAB_STARTUP;
	}



	public int getStabActive()
	{
		return STAB_ACTIVE;
	}



	public int getStabRecovery()
	{
		return STAB_RECOVERY;
	}



	public int getStabWeaponLength()
	{
		return STAB_WEAPON_LENGTH;
	}



	public int getStabRadius()
	{
		return STAB_RADIUS;
	}



	public int getStabHitstun()
	{
		return STAB_HITSTUN;
	}



	public double getStabHaftLength()
	{
		return STAB_HAFT_LENGTH;
	}



	public double getStabKnockbackSpeed()
	{
		return STAB_KNOCKBACK_SPEED;
	}



	public double getStabKnockbackDistance()
	{
		return STAB_KNOCKBACK_DISTANCE;
	}

	public double getStabDamageMultiplier()
	{
		return STAB_DAMAGE_MULTIPLIER;
	}

	public double getStabDistance()
	{
		return STAB_DISTANCE;
	}

}

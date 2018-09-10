package item;

public class Sword extends Weapon
{
	public static final String IMAGE_DIR = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "RogueFrontierImageSet"
			+ System.getProperty("file.separator") + "Item" + System.getProperty("file.separator") + "Sword.gif";
	
	//-------------------------------------Slashing Attributes-----------------------------------------//
	public static final int
						SLASH_STARTUP = 10, 
						SLASH_ACTIVE = 20, 
						SLASH_RECOVERY = 62,
						SLASH_WEAPON_LENGTH = 2,
						SLASH_RADIUS = 7,
						SLASH_HITSTUN = 70;
	public static final double
						SLASH_HAFT_LENGTH = 7,
						SLASH_KNOCKBACK_SPEED = 8.0,
						SLASH_KNOCKBACK_DISTANCE = 60.0,
						SLASH_DAMAGE_MULTIPLIER = 1.0,
						SLASH_WIDTH = Math.PI / 1.0;
	
	//---------------------------------------Stab Attributes----------------------------------------//
	public static final int
						STAB_STARTUP = 15, 
						STAB_ACTIVE = 5, 
						STAB_RECOVERY = 70,
						STAB_WEAPON_LENGTH = 3,
						STAB_RADIUS = 7,
						STAB_HITSTUN = 70;
	public static final double
						STAB_HAFT_LENGTH = 17,
						STAB_KNOCKBACK_SPEED = 10.0,
						STAB_KNOCKBACK_DISTANCE = 50.0,
						STAB_DAMAGE_MULTIPLIER = 2.0,
						STAB_DISTANCE = 30;


	public Sword( int d)
	{
		super("Sword", IMAGE_DIR, d);
	}
	
	public Sword(String name, String imagePath, int d)
	{
		super(name, imagePath, d);
	}

	public int getSlashStartup()
	{
		return SLASH_STARTUP;
	}



	public int getSlashActive()
	{
		return SLASH_ACTIVE;
	}



	public int getSlashRecovery()
	{
		return SLASH_RECOVERY;
	}



	public int getSlashWeaponLength()
	{
		return SLASH_WEAPON_LENGTH;
	}



	public int getSlashRadius()
	{
		return SLASH_RADIUS;
	}



	public int getSlashHitstun()
	{
		return SLASH_HITSTUN;
	}


	public double getSlashHaftLength()
	{
		return SLASH_HAFT_LENGTH;
	}



	public double getSlashKnockbackSpeed()
	{
		return SLASH_KNOCKBACK_SPEED;
	}



	public double getSlashKnockbackDistance()
	{
		return SLASH_KNOCKBACK_DISTANCE;
	}



	public double getSlashWidth()
	{
		return SLASH_WIDTH;
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



	public double getSlashDamageMultiplier()
	{
		return SLASH_DAMAGE_MULTIPLIER;
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

package item;

public class Axe extends Weapon
{

	public static final String IMAGE_DIR = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "RogueFrontierImageSet"
			+ System.getProperty("file.separator") + "Item" + System.getProperty("file.separator") + "Axe.gif";

	//-------------------------------------Slashing Attributes-----------------------------------------//
	public static final int
						SLASH_STARTUP = 15, 
						SLASH_ACTIVE = 25, 
						SLASH_RECOVERY = 20,
						SLASH_WEAPON_LENGTH = 1,
						SLASH_RADIUS = 11,
						SLASH_HITSTUN = 20;
	public static final double
						SLASH_HAFT_LENGTH = 28,
						SLASH_KNOCKBACK_SPEED = 10.0,
						SLASH_KNOCKBACK_DISTANCE = 18.0,
						SLASH_DAMAGE_MULTIPLIER = 2.0,
						SLASH_WIDTH = Math.PI / 1.9;
	
	//---------------------------------------Overhead Attributes----------------------------------------//
	public static final int
						OVERHEAD_STARTUP = 45, 
						OVERHEAD_ACTIVE = 5, 
						OVERHEAD_RECOVERY = 30,
						OVERHEAD_WEAPON_LENGTH = 1,
						OVERHEAD_RADIUS = 13,
						OVERHEAD_HITSTUN = 25;
	public static final double
						OVERHEAD_HAFT_LENGTH = 28,
						OVERHEAD_KNOCKBACK_SPEED = 10.0,
						OVERHEAD_KNOCKBACK_DISTANCE = 18.0,
						OVERHEAD_DAMAGE_MULTIPLIER = 3.0;



	public Axe(int d)
	{
		super("Axe", IMAGE_DIR, d);
	}
	
	public Axe(String name, String imagePath, int d)
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



	public int getOverheadStartup()
	{
		return OVERHEAD_STARTUP;
	}



	public int getOverheadActive()
	{
		return OVERHEAD_ACTIVE;
	}



	public int getOverheadRecovery()
	{
		return OVERHEAD_RECOVERY;
	}



	public int getOverheadWeaponLength()
	{
		return OVERHEAD_WEAPON_LENGTH;
	}



	public int getOverheadRadius()
	{
		return OVERHEAD_RADIUS;
	}



	public int getOverheadHitstun()
	{
		return OVERHEAD_HITSTUN;
	}



	public double getOverheadHaftLength()
	{
		return OVERHEAD_HAFT_LENGTH;
	}



	public double getOverheadKnockbackSpeed()
	{
		return OVERHEAD_KNOCKBACK_SPEED;
	}



	public double getOverheadKnockbackDistance()
	{
		return OVERHEAD_KNOCKBACK_DISTANCE;
	}



	public double getSlashDamageMultiplier()
	{
		return SLASH_DAMAGE_MULTIPLIER;
	}



	public double getOverheadDamageMultiplier()
	{
		return OVERHEAD_DAMAGE_MULTIPLIER;
	}

}

package item;

import mapUnit.Mob;

public class Armor extends EquipableItem
{
	public static final String IMAGE_DIR = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "RogueFrontierImageSet"
			+ System.getProperty("file.separator") + "Item" + System.getProperty("file.separator") + "Armor.gif";
	double damageScaling;
	
	public Armor(double damageScaling)
	{
		super("Armor", IMAGE_DIR);
		this.damageScaling = damageScaling;
	}
	
	public Armor(String name, String imagePath, double damageScaling)
	{
		super(name,imagePath);
		this.damageScaling = damageScaling;
	}	
	

	public double getDamageScaling()
	{
		return damageScaling;
	}

	public void setDamageScaling(double damageScaling)
	{
		this.damageScaling = damageScaling;
	}


	@Override
	public void equip(Mob p)
	{
		p.equipArmor(this);
		
	}

	
}

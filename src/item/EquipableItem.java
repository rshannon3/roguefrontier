package item;
import mapUnit.Mob;
public abstract class EquipableItem extends Item
{
	public EquipableItem(String name, String imagePath)
	{
		super(name, imagePath);
	}

	@Override
	public void use(Mob p)
	{
		equip(p);
	}
	
	/**
	 * Have this item call players proper equipWeapon/ equipArmour method
	 * @param p
	 */
	public abstract void equip(Mob p);

}

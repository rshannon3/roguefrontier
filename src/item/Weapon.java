package item;

import mapUnit.Mob;

public abstract class Weapon extends EquipableItem 
{
	private int damage;
	
	public Weapon(String name, String imagePath, int d)
	{	
		super(name, imagePath);
		damage = d;
	}
	
	public int getDamage()
	{ return damage; }
	
	public void setDamage(int d)
	{ damage = d;  }
	
	@Override
	public void equip(Mob p)
	{
		p.equipWeapon(this);
	}

}

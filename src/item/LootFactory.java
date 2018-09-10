package item;

import mapUnit.ItemContainer;
import mapUnit.Mob;
import mapUnit.Spaz;
import mapUnit.Zombie;

public abstract class LootFactory
{
	public static ItemContainer get(Mob m)
	{
		ItemContainer result = null;
		
		if(m instanceof Zombie)
		{
			result = new ItemContainer(m.getX(), m.getY(), m.getObs());
			result.getInventory().add(PotionFactory.getHealthPotion(10));
		}
		return result;
		
	}
}

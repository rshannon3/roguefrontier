package item;

import mapUnit.Player;
import mapUnit.Mob;

public class PotionFactory
{
	public static Item getHealthPotion(final int healthIncrease)
	{
		return new Item("Health Potion", Item.IMAGE_DIR + "HealthPotion.gif", false, 1){
				public void use(Mob pl)
				{
					if(pl.getCurrentHealth() < pl.getMaxHealth() )
					{
						super.use(pl);
						pl.healHealth(healthIncrease);
					}
				}
		};
	}
	
	public static Item getStaminaPotion(final int staminaIncrease)
	{
		return new Item("Stamina Potion", Item.IMAGE_DIR + "StaminaPotion.gif", false, 1){
				public void use(Mob pl)
				{
					if(pl.getCurrentStamina() < pl.getMaxStamina() )
					{
						super.use(pl);
						pl.healStamina(staminaIncrease);
					}
				}
		};
	}
}

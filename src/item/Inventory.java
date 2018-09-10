package item;
import java.io.Serializable;
import java.util.ArrayList;

import mapUnit.Player;

public class Inventory implements Serializable
{
	ArrayList<Item> itemList;
	Weapon equippedWeapon;
	Armor equippedArmor; 
	int maxItems;
	double gold;
	
	public Inventory(int maxItems)
	{
		itemList = new ArrayList<Item>(maxItems);
		this.maxItems = maxItems;
		gold = 0.0;
	}

	
	public Item get(int index)
	{
		return getItem(index);
	}
	
	public Item getItem(int index)
	{
		if(itemList.size() > index && itemList.get(index) != null)
			return itemList.get(index);
		else
			return null;
	}
	
	/**
	 * If possible, transfer item at index in this inventory to Inventory destination
	 * @param index
	 * @param destination
	 * @return whether the transfer was successful; Returning false is usually due to destination being full;
	 */
	public boolean transferItemTo(int index, Inventory destination)
	{
		if(destination != null && !destination.isFull() && itemList.get(index) != null)
		{
			return destination.add(itemList.remove(index));
		}
		return false;
	}
	
	/**
	 * Transfer as many items as possible from this inventory to Inventory destination
	 * @param index
	 * @param destination
	 * @return number of items transferred
	 */
	public int transferAllItemsTo(Inventory destination)
	{
		int num = 0;
		while(destination != null && !destination.isFull() && !this.isEmpty() && itemList.get(0) != null)
		{
			destination.add(itemList.remove(0));
			num++;
		}
		return num;
	}
	
	public void useItem(int index, Player p)
	{
		itemList.get(index).use(p);
		if(index < itemList.size() && !itemList.get(index).isUnlimited() && itemList.get(index).getUses() <= 0)
		{
			itemList.remove(index);
		}
	}
	
	public boolean add(Item i)
	{	return addItem(i); 	}
	
	public boolean addItem(Item i)
	{
		if(i == null)
			return false;
		if(!isFull())
		{
			itemList.add(i);
			return true;
		}
		return false;
	}
	
	public void remove(int i)
	{	itemList.remove(i); }
	
	public void removeItem(Item i)
	{ itemList.remove(i); }
	
	public boolean contains(Item i)
	{ return itemList.contains(i); }
	
	public boolean isFull()
	{
		return itemList.size() == maxItems;
	}
	
	public Weapon getEquippedWeapon()
	{
		return equippedWeapon;
	}
	
	public void equip(Item i)
	{
		if(i instanceof Weapon)
			equipWeapon((Weapon)i);
		
		if(i instanceof Armor)
			equipArmor((Armor)i);
	}

	public boolean equipWeapon(Weapon w)
	{
		if(!this.contains(w))
		{
			if(equippedWeapon == null)
			{
				setEquippedWeapon(w);
				return true;
			}
			else if(!isFull())
			{
				Weapon temp = equippedWeapon;
				equippedWeapon = w;
				addItem(temp);
				return true;
			}
			else return false;
		}
		else
		{
			if(equippedWeapon == null)
			{
				setEquippedWeapon(w);
				removeItem(w);
				return true;
			}
			Weapon temp = equippedWeapon;
			equippedWeapon = w;
			removeItem(w);
			addItem(temp);
			return true;
		}
	}
	
	public boolean unequipWeapon()
	{
		if(addItem(equippedWeapon))
		{
			equippedWeapon = null;
			return true;
		}
		return false;
	}
	
	public boolean equipArmor(Armor a)
	{
		if(!this.contains(a))
		{
			if(equippedArmor == null)
			{
				setEquippedArmor(a);
				return true;
			}
			else if(!isFull())
			{
				Armor temp = equippedArmor;
				equippedArmor = a;
				addItem(temp);
				return true;
			}
			else return false;
		}
		else
		{
			if(equippedArmor == null)
			{
				setEquippedArmor(a);
				removeItem(a);
				return true;
			}
			Armor temp = equippedArmor;
			equippedArmor = a;
			removeItem(a);
			addItem(temp);
			return true;
		}
	}
	
	public boolean unequipArmor()
	{
		if(addItem(equippedArmor))
		{
			equippedArmor = null;
			return true;
		}
		return false;
	}
	
	public void setEquippedWeapon(Weapon equippedWeapon)
	{
		this.equippedWeapon = equippedWeapon;
	}

	public Armor getEquippedArmor()
	{
		return equippedArmor;
	}

	public void setEquippedArmor(Armor equippedArmor)
	{
		this.equippedArmor = equippedArmor;
	}

	public void addCopper()
	{	gold  = gold + .01; }
	
	public void addCopper(int a)
	{ gold += .01 * a; }
	
	public void addSilver()
	{	gold = gold +  0.1; }
	
	public void addSilver(int a)
	{ gold += .1 * a; }
	
	public void addGold()
	{ gold = gold + 1.0; }
	
	public void addGold(int a)
	{ gold += a; }
	
	public int getGold()
	{ return (int)gold; }
	
	public int getSilver()
	{ return (int)(gold - ((int)gold)) * 10; }
	
	public int getCopper()
	{ return ((int)(gold - ((int)gold)) * 100) % 10; }

	public int size()
	{
		return this.itemList.size();
	}


	public boolean isEmpty()
	{	return itemList.isEmpty(); }
}

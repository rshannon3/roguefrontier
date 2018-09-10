package mapUnit;

import game.MapUnitList;
import item.Inventory;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class ItemContainer extends Actor implements Interactable
{
	private static String IMAGE_DIR = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "RogueFrontierImageSet"
			+ System.getProperty("file.separator") + "Chest.gif";
	public final static int DEFAULT_SIZE = 25;
	
	
	private transient Image image;
	private Inventory inventory;
	boolean isPermanant;

	public ItemContainer(double x, double y, MapUnitList o, int size)
	{
		super((int)x, (int)y, o);
		inventory = new Inventory(size);
		this.getCollisionBox().setTraversable(true);
		isPermanant = false;
	}
	
	public ItemContainer(double x, double y, MapUnitList o, int size, boolean perma)
	{
		this(x,y,o,size);
		isPermanant = perma;
	}
	
	
	
	public ItemContainer(double x, double y, MapUnitList o)
	{
		this(x, y, o, DEFAULT_SIZE);
	}
	
	public ItemContainer(double x, double y, MapUnitList o, boolean perma)
	{
		this(x, y, o, DEFAULT_SIZE);
		isPermanant = perma;
	}
	
	public ItemContainer(double x, double y, MapUnitList o, Inventory itemList)
	{
		this(x, y, o, DEFAULT_SIZE);
		inventory = itemList;
	}
	
	@Override
	public Image getImage()
	{
		if(inventory.size() != 1||isPermanant)
		{
			//return super.getImage();
			if(image == null)
			{
				try
				{
					image = ImageIO.read(new File(IMAGE_DIR));
				} catch (IOException e)
				{	e.printStackTrace(); }
			}
			return image;
		}
			
		return inventory.get(0).getImage();
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}

	public void setInventory(Inventory inventory)
	{
		this.inventory = inventory;
	}
	
	public void act()
	{
		if(!isPermanant && inventory.isEmpty())
			getObs().remove(this);
	}



	@Override
	public void interact(Player p)
	{
		// TODO Auto-generated method stub
		
	}

}

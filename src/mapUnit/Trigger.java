package mapUnit;

import game.MapUnitList;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import mapUnit.*;
 
 //------------------------------------------Template class for triggers later in the game. Spawn points, doors, etc-----------------------------------------------------------//
 
public class Trigger extends MapUnit implements Interactable
{
	
	private static String IMAGE_DIR = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "RogueFrontierImageSet"
			+ System.getProperty("file.separator") + "Chest.gif";
	public final static int DEFAULT_SIZE = 25;
	private int interactionRange = 100;
	private transient Image image;
	boolean trigger;
	
	public Trigger(double x, double y, MapUnitList o)
	{
		super(x, y, o);
		trigger = false;
		this.getCollisionBox().setTraversable(true);
		// TODO Auto-generated constructor stub
	}
	
	public void interact(Player p)
	{
		trigger = true;
	}
	
	public boolean isActive()
	{ return trigger; }
	
	public void deactivate()
	{ trigger = false; }
	
	public void setTrigger(boolean b)
	{
		trigger = b;
	}
	
	public void trigger()
	{
	}
	
	@Override
	public Image getImage()
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
}

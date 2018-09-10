package item;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import mapUnit.Mob;

public class Item implements Serializable
{
	private String name;
	private transient Image image;
	private boolean unlimited;
	private int uses;
	
	private String imagePath;
	public static final String IMAGE_DIR = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "RogueFrontierImageSet"
			+ System.getProperty("file.separator") + "Item" + System.getProperty("file.separator");
	
	public Item(String name, String imagePath)
	{
		this.name = name;
		this.imagePath = imagePath;
		unlimited = true;
	}
	
	public Item(String name, String imagePath, boolean unlimited, int uses)
	{
		this(name,imagePath);
		this.unlimited = unlimited;
		this.uses = uses;
	}
	
	public void use(Mob player)
	{
		if( !unlimited )
			uses--;
	}

	/**
	 * @return the unlimited
	 */
	public boolean isUnlimited() {
		return unlimited;
	}

	/**
	 * @param unlimited the unlimited to set
	 */
	public void setUnlimited(boolean unlimited) {
		this.unlimited = unlimited;
	}

	/**
	 * @return the uses
	 */
	public int getUses() {
		return uses;
	}

	/**
	 * @param uses the uses to set
	 */
	public void setUses(int uses) {
		this.uses = uses;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Image getImage()
	{
		if(image == null)
		{
			File f = new File(imagePath);
			if (f.exists())
				try
				{
					image = ImageIO.read(f);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
		}
		return image;
	}
}

package mapUnit;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public final class MapUnitImageFlyweight
{
	private static String IMAGE_DIR = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "RogueFrontierImageSet"
			+ System.getProperty("file.separator");

	private MapUnitImageFlyweight()
	{

	}

	static List<List<Image>> playerImages = null,
			zombieImages = null, spazImages = null;

	final static String playerImagesetLocation = IMAGE_DIR + "Player"
			+ System.getProperty("file.separator");
	final static String zombieImagesetLocation = IMAGE_DIR + "Player"
			+ System.getProperty("file.separator");
	
	final static String spazImagesetLocation = IMAGE_DIR + "Spaz"
			+ System.getProperty("file.separator");

	public static Image get(MapUnit mu)
	{
		if (mu == null)
		{
			JOptionPane.showMessageDialog(null,
					"null mapUnit provided [MapUnitImageFlyweight]");
		}
		if (mu instanceof Mob)
			return getMob((Mob) mu);
		return null;

	}

	private static Image getMob(Mob mob)
	{
		String path = null;
		if (mob instanceof Player)
		{
			if (playerImages == null)
			{
				playerImages = instantiateMobImages(playerImagesetLocation);
			}
			return playerImages.get(mob.getDir()).get(mob.getImageIndex());

		}

		if (mob instanceof Zombie)
		{
			if (zombieImages == null)
			{
				zombieImages = instantiateMobImages(playerImagesetLocation);
			}
			return zombieImages.get(mob.getDir()).get(mob.getImageIndex());
		}
		
		if (mob instanceof Spaz)
		{
			if (spazImages == null)
			{
				spazImages = instantiateMobImages(spazImagesetLocation);
			}
			return spazImages.get(mob.getDir()).get(mob.getImageIndex());
		}
		
		return null;
	}

	private static String angleDirectory(int r)
	{
		switch (r)
		{
		case 0:
			return "EAST" + System.getProperty("file.separator");
		case 1:
			return "NORTH_EAST" + System.getProperty("file.separator");
		case 2:
			return "NORTH" + System.getProperty("file.separator");
		case 3:
			return "NORTH_WEST" + System.getProperty("file.separator");
		case 4:
			return "WEST" + System.getProperty("file.separator");
		case 5:
			return "SOUTH_WEST" + System.getProperty("file.separator");
		case 6:
			return "SOUTH" + System.getProperty("file.separator");
		case 7:
			return "SOUTH_EAST" + System.getProperty("file.separator");
		}
		return null;
	}

	private static List<List<Image>> instantiateMobImages(String path)
	{
		List<List<Image>> result = new ArrayList<List<Image>>(8);
		try
		{
			for (int r = 0; r < 8; r++)
			{
				result.add(r, new ArrayList<Image>());
				int c = 0;
				File f = new File(path + angleDirectory(r) + c + ".gif");
				while (f.exists())
				{
					result.get(r).add(c, ImageIO.read(f));
					c++;
					f = new File(path + angleDirectory(r) + c + ".gif");
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
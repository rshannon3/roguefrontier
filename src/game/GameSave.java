package game;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JOptionPane;


public class GameSave
{
	public static final String MAPS_PATH = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "maps" + System.getProperty("file.separator");
	private static String PERSISTENT_DATA_PATH = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "save.dat";
	
	public static void saveList(MapUnitList mul)
	{
		saveList(mul, PERSISTENT_DATA_PATH);
		
	}
	
	public static void saveList(MapUnitList obstacles, String filePath) {
		FileOutputStream file = null;
		ObjectOutputStream stream = null;
		try {
			file = new FileOutputStream(filePath, false);
			stream = new ObjectOutputStream(file);
			stream.writeObject(obstacles);
			stream.close();
			file.close();
		} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "ERROR: Could not write to "
						+ filePath);
				
			e.printStackTrace();	
		}
	}
	
	public static MapUnitList loadList() {
		return loadList(PERSISTENT_DATA_PATH);
	}
	
	public static MapUnitList loadList(String path) {
		FileInputStream file = null;
		ObjectInputStream stream = null;
		MapUnitList mul = null;

			try {
				file = new FileInputStream(path);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				stream = new ObjectInputStream(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				mul = (MapUnitList) stream.readObject();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				stream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				file.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return mul;
	}
	
	public static boolean exists()
	{
		return new File(PERSISTENT_DATA_PATH).exists();
	}
}

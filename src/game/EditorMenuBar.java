package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class EditorMenuBar extends JMenuBar {

	protected static final String DEFAULT_MAPS_DIR = System.getProperty("user.dir") + System.getProperty("file.separator") + "maps";
	protected static final String DEFAULT_FILE_PATH = DEFAULT_MAPS_DIR + System.getProperty("file.separator") +"defaultSave.dat";
	private JMenuItem newMap, openMap, saveMap, saveMapAs;
	private JMenu fileMenu;
	String currPath;

	public EditorMenuBar(final Model model) {
		currPath = null;
		final JFileChooser fc = new JFileChooser(DEFAULT_MAPS_DIR);
		fileMenu = new JMenu("File");
		newMap = fileMenu.add("New Map");
		openMap = fileMenu.add("Open Map");
		saveMap = fileMenu.add("Save Map");
		saveMapAs = fileMenu.add("Save Map As...");

		openMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc.setApproveButtonText("Open");
				int returnVal = fc.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		            String path = fc.getSelectedFile().getAbsolutePath();
		            model.setObstacles(GameSave.loadList(path));
		            model.updatePlayer();
		            currPath = path;
		            
		        } else {
		            System.out.println("Open command cancelled by user.");
		        }
			}
		});
		
		saveMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currPath == null){
					for(ActionListener a : saveMapAs.getActionListeners()){
						a.actionPerformed(e);
					}
				}else{
					GameSave.saveList(model.getObstacles(), currPath);
				}
			}
		});
		

		saveMapAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String path = fc.getSelectedFile().getAbsolutePath();
					if(fc.getSelectedFile().exists()){
						int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to overwrite '"+fc.getSelectedFile().getName()+"'?", "Saving to Default File", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			    		if(reply == JOptionPane.YES_OPTION)
			    		{
			    			GameSave.saveList(model.getObstacles(), path);
			    		}else{
			    			System.out.println("'Save As...' overwrite command cancelled by user.");
			    		}
					}else{
						GameSave.saveList(model.getObstacles(), path);
					}
		            
		            
		        } else {
		            System.out.println("'Save As...' command cancelled by user.");
		        } 
			}
		});

		fileMenu.addSeparator();
		add(fileMenu);
		setVisible(true);
	}

	
}

package game;

import item.PotionFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import mapUnit.Player;

public class LoadoutGUI extends JPanel 
{

		public static final int PANEL_HEIGHT = 208;
		public static final int PANEL_WIDTH = 250;
		private int numRows = 2;
		private int numCols = 2;
		private int tileWidth = PANEL_WIDTH / numCols;
		private int tileHeight= PANEL_HEIGHT / numRows;
		
		Image[][] itemImages = new Image[2][2];
		Player p;
		Image defaultImage;
		
		// off screen rendering
		  private Graphics dbg; 
		  private Image dbImage = null;
		  private Image backgroundImage;
		
		  public LoadoutGUI(Player p)
		  {
			this.p = p;
			setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
			setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
			
		    this.setLayout(new GridLayout(2, 2));
		    //this.setMaximumSize(new Dimension(300, 90));
		    try { 
		    
		    File k = new File(System.getProperty("user.dir")
					+ System.getProperty("file.separator") + "RogueFrontierImageSet"
					+ System.getProperty("file.separator")+ "BACKGROUND" 
					+ System.getProperty("file.separator") + "LoadoutBackground.gif");
		    backgroundImage = ImageIO.read(k);
		    
		    
		    //defaultImage = ImageIO.read(f);
		    } catch (IOException e)
			{
				e.printStackTrace();
			}
		    
			//icon = new ImageIcon(PotionFactory.getHealthPotion(0).getImage());

		    
		    addMouseListener( new MouseAdapter() {
		        public void mousePressed(MouseEvent e)
		        { testPress(e);}
		      });
		    
		  } 

		  
		  public void update()
		  {
			  if(p.getEquippedWeapon() != null)
				  itemImages[1][0] = p.getInventory().getEquippedWeapon().getImage();
			  else
				  itemImages[1][0] = null;
			  if(p.getEquippedArmor() != null)
				  itemImages[1][1] = p.getInventory().getEquippedArmor().getImage();
			  else
				  itemImages[1][1] = null;
			  gameRender();
			  paintScreen();
		  }
		  
		  
		  
		  private void gameRender()
		  {
		    // create an image buffer
		    if (dbImage == null){
		      dbImage = createImage(PANEL_WIDTH, PANEL_HEIGHT);
		      if (dbImage == null) {
		        System.out.println("dbImage is null");
		        return;
		      }
		      else
		        dbg = dbImage.getGraphics();
		    }

		    // clear the background
		    dbg.setColor(Color.white);
		    dbg.fillRect (0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		    dbg.drawImage(backgroundImage, 0, 0, PANEL_WIDTH,PANEL_HEIGHT, null);
		    drawItems();
		    
		  }
		  
		  public void drawItems()
		  {
			  for(int h = 0; h < numRows; h++ )
			  {
				  for(int w = 0; w < numCols; w++)
				  {
					  if(itemImages[h][w] != null)
						  dbg.drawImage(itemImages[h][w], w * tileWidth, h * tileHeight, tileWidth, tileHeight, null);
				  }
			  }
			  
		  }
		  
		  private void paintScreen()
		  // use active rendering to put the buffered image on-screen
		  { 
		    Graphics g;
		    try {
		      g = this.getGraphics();
		      if ((g != null) && (dbImage != null))
		        g.drawImage(dbImage, 0, 0, null);
		      Toolkit.getDefaultToolkit().sync();  // sync the display on some systems
		      g.dispose();
		    }
		    catch (Exception e)
		    { System.out.println("Graphics error: " + e);  }
		  } 
		  
		  public void testPress(MouseEvent e)
		  {
			  if(e.getButton() == MouseEvent.BUTTON3)
			  {
				  if(e.getY() > tileHeight)
				  {
					  switch(((int)e.getX()/tileWidth))
					  {
					  	case 0: p.unequipWeapon(); break;
					  	case 1: p.unequipArmor(); break;
					  }
				  }
			  }
		  }
		  
		  
		  
		  
	}

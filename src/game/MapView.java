package game;

//-------------------------------------------------------RogueFrontierView-------------------------------------------------//

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import mapUnit.Actor;
import mapUnit.ItemContainer;
import mapUnit.Mob;
import mapUnit.Player;
import detection.DetectionCircle;
import detection.DetectionRectangle;

@SuppressWarnings("serial")
public class MapView extends JPanel
{

  public static final int MAP_WIDTH = 800;   // size of MapPanel
  public static final int MAP_HEIGHT = 600; 



  private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp


  // used at game termination
  private volatile boolean gameOver = false;
  private Font font;
  private FontMetrics metrics;

  // off screen rendering
  private Graphics dbg; 
  private Image dbImage = null;

  //The game model
  private Model mod;
  
  //The View
  private View myView;
  
  //---------------------------------------------------------------------TODO REMOVE, ONLY TEST------------------------------------------------------------//
  private static String IMAGE_DIR = System.getProperty("user.dir") + System.getProperty("file.separator") + "RogueFrontierImageSet" + 
			System.getProperty("file.separator") + "BACKGROUND" + System.getProperty("file.separator") + "Space.png ";
  private Image backGroundImage;
  //------------------------------------------------------------------------------------------------------------------------------------------------------//
 
  
  //Create new view with game window reference and update period
  public MapView(View v, Model m)
  {
	myView = v;
    setBackground(Color.white);
    setPreferredSize( new Dimension(MAP_WIDTH, MAP_HEIGHT));

    setFocusable(true);
    requestFocus();    // the JPanel now has focus, so receives key events
    setupKeyListener();

    // Adds mouse detection for clicking and movement
    addMouseListener( new MouseAdapter() {
      public void mousePressed(MouseEvent e)
      { testPress(e);}
    });
    addMouseMotionListener( new MouseAdapter() { 
      public void mouseMoved(MouseEvent e)
      { testMove(e); }}
      );

    // set up message font
    font = new Font("SansSerif", Font.BOLD, 24);
    metrics = this.getFontMetrics(font);
    
    mod = m;
    
    try {

    	File f = new File(IMAGE_DIR);
    	if(f.exists())
    		backGroundImage = ImageIO.read(f);
	} catch (IOException e) {
		e.printStackTrace();}
    requestFocus();
  }  



  protected void testMove(MouseEvent e) 
  {
	  double x = mod.getPlayer().getCollisionBox().getLocation().getX()
				- (MapView.MAP_WIDTH / 2 - e.getX());
	  double y = mod.getPlayer().getCollisionBox().getLocation().getY()
				- (MapView.MAP_HEIGHT / 2 - e.getY());
	  if(mod.getCurrentMode() == Model.Mode.EDITOR)
	  {
		  myView.getEditorPanel().updateMousePosition((int)x, (int)y);
	  }
	  mod.mouseMoved((int)x, (int)y);
	  myView.requestFocus(); //reenable key input and update mapView
  }



private void setupKeyListener()
  {
    addKeyListener( new KeyAdapter() {
    // listen for esc, q, end, ctrl-c on the canvas to
    // allow a convenient exit from the full screen configuration
       public void keyPressed(KeyEvent e)
       { int keyCode = e.getKeyCode();
        	 mod.keyPressed((char)keyCode);
       }
       public void keyReleased(KeyEvent e)
       { int keyCode = e.getKeyCode();
        	 mod.keyReleased((char)keyCode);
       }
       public void keyTyped(KeyEvent e)
       {
       		mod.keyTyped(e.getKeyChar());
       }
     });
  }  // end of readyForTermination()

 public void testPress(MouseEvent e)
  // sends the key input to the model to be processed
  // needs multiple mouse click detection
  { 
	  double x = mod.getPlayer().getCollisionBox().getLocation().getX()
				- (MapView.MAP_WIDTH / 2 - e.getX());
	  double y = mod.getPlayer().getCollisionBox().getLocation().getY()
				- (MapView.MAP_HEIGHT / 2 - e.getY());
	  if(mod.getCurrentMode() == Model.Mode.EDITOR)
	  {
          //mod.mouseClicked((int)x, (int)y, e);
          
          if (e.getButton() == MouseEvent.BUTTON1)// Adds a rectangle on left
													// mouseclick
		   {
        	  myView.getEditorPanel().addMapUnit((int)x, (int)y);
			
			//obs.add(new Spaz((int) mx, (int) my, obs));
			} else if (e.getButton() == MouseEvent.BUTTON3) // deletes on object on
														// right click
			{
				mod.deleteMapUnit((int)x, (int)y);
			}
		
	  }
	  else
	  {
		  mod.mouseClicked((int)x, (int)y, e);
	  }
	  //myView.requestFocus(); //reenable key input
    
  } 

  public void step()
  {

	  gameRender( mod.getObstacles() ); // render the game to a buffer
	  //paintScreen();  // draw the buffer on-screen
  }


  private void gameRender(MapUnitList obs)
  {
    // create an image buffer
    if (dbImage == null){
      dbImage = createImage(MAP_WIDTH, MAP_HEIGHT);
      if (dbImage == null) {
        System.out.println("dbImage is null");
        return;
      }
      else
        dbg = dbImage.getGraphics();
    }

    // clear the background
    dbg.setColor(Color.white);
    dbg.fillRect (0, 0, MAP_WIDTH, MAP_HEIGHT);
    
    
    //TODO 
    drawBackground();
    
    dbg.setColor(Color.black);
    //update Game with number of obstacles
    //setObstacleNumber( obs.getNumObstacles() ); //TODO FIX
    
    
    
    drawGameElements(obs); 
    
    
    dbg.setColor(Color.green);
    dbg.setFont(font);

    // report frame count & average FPS and UPS at top left
    // dbg.drawString("Frame Count " + frameCount, 10, 25);
    dbg.drawString("Average FPS/UPS: " + df.format(myView.getAverageFPS()) + ", " +
                                df.format(myView.getAverageUPS()), 20, 25);  // was (10,55)
  }  
  
  public void drawGameElements(MapUnitList obs)
  {
	  // draw game elements
	    Rectangle2D.Double box;
	    Ellipse2D circle;
	    double centerX = mod.getPlayer().getCollisionBox().getLocation().getX();
	    double centerY = mod.getPlayer().getCollisionBox().getLocation().getY();
	    double panelCenterX = MAP_WIDTH / 2;  
	    double panelCenterY = MAP_HEIGHT / 2;
	    
	    for(int i=0; i < obs.getNumObstacles(); i++) //-------------------------------Draws each DetectionShape------------------------------//
	    {
	      if( obs.getUnit(i).getCollisionBox() instanceof DetectionRectangle )
	      {
	    	  dbg.setColor(obs.getUnit(i).getCollisionBox().getColor());
	          box = (Rectangle2D.Double) ((DetectionRectangle)obs.getUnit(i).getCollisionBox()).getRect();
	          dbg.fillRect( (int)(panelCenterX - (centerX - box.x)), (int)(panelCenterY - (centerY - box.y)), (int)box.width, (int)box.height);
	      }
	      else if ( obs.getUnit(i).getCollisionBox() instanceof DetectionCircle && (!(obs.getUnit(i) instanceof Player) || mod.getCurrentMode() != Model.Mode.EDITOR ))
	      {
	    	  dbg.setColor(obs.getUnit(i).getCollisionBox().getColor());
	          circle = (Ellipse2D) ((DetectionCircle)obs.getUnit(i).getCollisionBox()).getCircle();
	          dbg.fillOval((int)(panelCenterX - (centerX - circle.getX())), (int)(panelCenterY - (centerY - circle.getY())), (int)circle.getWidth(),  (int)circle.getWidth());
	      }
	    }
	    
	    for(int i=0; i < obs.getNumObstacles(); i++) //--------------------------------------Draws Each Mob-------------------------------------------//
	    {
	    	double imageScale = 2.5;
	    	
	    	if( obs.getUnit(i) instanceof ItemContainer)
	    	{
	    		//Draws the mob image
	    		//TODO FIX LATER, LEFT AS CONSTANT Y SHIFT TO GIVE 3D ILLUSION, SHOUDL BE VARIABLE BASED ON IMAGE SCALE AND COLLISON BOX SIZE
	    		dbg.drawImage((obs.getUnit(i)).getImage(), (int)( panelCenterX - (centerX - obs.getUnit(i).getX()) - (obs.getUnit(i).getCollisionBox()).getMaxRadius() * imageScale / 2) , 
	    				(int) ((int)(panelCenterY - (centerY - obs.getUnit(i).getY()) - ( obs.getUnit(i).getCollisionBox()).getMaxRadius() * imageScale / 2) - ( obs.getUnit(i).getCollisionBox()).getMaxRadius() / 2), (int)(( obs.getUnit(i).getCollisionBox()).getMaxRadius() * imageScale),
	    			(int)(( obs.getUnit(i).getCollisionBox()).getMaxRadius() * imageScale), null);
	    	}
	    	
	    	if( obs.getUnit(i) instanceof Mob)
	    	{
	    		if( obs.getUnit(i) instanceof Player && mod.getCurrentMode() == Model.Mode.EDITOR)
	    		{
	    			
	    		}
	    		else
	    		{
		    		//Draws the mob image
		    		//TODO FIX LATER, LEFT AS CONSTANT Y SHIFT TO GIVE 3D ILLUSION, SHOUDL BE VARIABLE BASED ON IMAGE SCALE AND COLLISON BOX SIZE
		    		dbg.drawImage(((Mob)obs.getUnit(i)).getImage(), (int)( panelCenterX - (centerX - obs.getUnit(i).getX()) - ((DetectionCircle) obs.getUnit(i).getCollisionBox()).getRadius() * imageScale / 2) , 
		    				(int)(panelCenterY - (centerY - obs.getUnit(i).getY()) - ((DetectionCircle) obs.getUnit(i).getCollisionBox()).getRadius() * imageScale / 2) - ((DetectionCircle) obs.getUnit(i).getCollisionBox()).getRadius() / 2, (int)(((DetectionCircle) obs.getUnit(i).getCollisionBox()).getRadius() * imageScale),
		    			(int)(((DetectionCircle) obs.getUnit(i).getCollisionBox()).getRadius() * imageScale), null);
		    		
		    		//Draws a sqaure to show where you are facing
		    		dbg.setColor(Color.WHITE);
		    		circle = (Ellipse2D) ((DetectionCircle)obs.getUnit(i).getCollisionBox()).getCircle();
		    		dbg.fillRect((int)(panelCenterX - (centerX - circle.getX()) + (circle.getWidth()/2) - 5  + (circle.getWidth() + 10) *  Math.cos(((Actor)obs.getUnit(i)).getFacingAngle()) / 2) , (int)(panelCenterY - (centerY - circle.getY()) + circle.getWidth()/2 - 5 - (circle.getWidth() + 10) *  Math.sin(((Actor)obs.getUnit(i)).getFacingAngle()) / 2),
		    					10, 10);
	    		}
	    	}
	    }
  }
  /*
   * 
   * Draws a star background 
   */
  public void drawBackground()
  {
	  int tileSize = 500;
	  int centerX = (int)mod.getPlayer().getCollisionBox().getLocation().getX();
	  int centerY = (int)mod.getPlayer().getCollisionBox().getLocation().getY();
	  for(int x = tileSize * 2 - (centerX / 2 % (tileSize * 2)) - tileSize * 5; x < MAP_WIDTH; x += tileSize )
	  {
		  for(int y = tileSize * 2 - (centerY / 2 % (tileSize*2)) - tileSize * 5; y < MAP_HEIGHT; y += tileSize)
		  {
			  dbg.drawImage(backGroundImage, x, y, tileSize, tileSize, null);
		  }
	  }
	  drawBackgroundGrid();
	 
	  
  }
  
  //
  public void drawBackgroundGrid()
  {
	  int gridSize = (mod.getCurrentMode() == Model.Mode.EDITOR ? myView.getEditorPanel().getSliderValue() : 100) ;

	  int absCenterX = (int)mod.getPlayer().getCollisionBox().getLocation().getX();
	  int absCenterY = (int)mod.getPlayer().getCollisionBox().getLocation().getY();
	  int absGridCenterX = absCenterX - absCenterX % gridSize;
	  int absGridCenterY = absCenterY - absCenterY % gridSize;
	  int numHorzTiles = MAP_WIDTH / gridSize + 4;
	  int numVertTiles = MAP_HEIGHT / gridSize + 4;
	  int[] viewCoords = modelCoordToViewCoord(absGridCenterX, absGridCenterY);
	  for(int x = viewCoords[0] - numHorzTiles/2*gridSize; x < MAP_WIDTH; x += gridSize )
	  {
		  for(int y = viewCoords[1] - numVertTiles/2*gridSize; y < MAP_HEIGHT; y += gridSize )
		  {
			  dbg.setColor(Color.GREEN);
			  dbg.drawLine(x,y, x + gridSize, y);
			  dbg.drawLine(x,y, x , y + gridSize);
		  }
	  }
  }
  
  public int[] modelCoordToViewCoord(int absoluteX, int absoluteY){
	  int absCenterX = (int)mod.getPlayer().getCollisionBox().getLocation().getX();
	  int absCenterY = (int)mod.getPlayer().getCollisionBox().getLocation().getY();
	  int cornerOfViewX = absCenterX - MAP_WIDTH/2;
	  int cornerOfViewY = absCenterY - MAP_HEIGHT/2;
	  int viewX = absoluteX - cornerOfViewX;
	  int viewY = absoluteY - cornerOfViewY;
	  int[] arr = new int[]{viewX,viewY};
	  return arr;
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
  
  public Image getImage()
  { return dbImage; }
  
}

package game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class GamePanel extends JPanel 
{
	MapView mapV;
	View myView;
	Model mod;
	
	  // off screen rendering
	  private Graphics dbg; 
	  private Image dbImage = null;
	
	public GamePanel(View v, Model m)
	{
		setLayout(null);
		setBackground(Color.white);
		setPreferredSize(new Dimension(MapView.MAP_WIDTH, MapView.MAP_HEIGHT));
		mapV = new MapView(v, m);
		myView = v;
		mod = m;
		/*
		addMouseListener( new MouseAdapter() {
		      public void mousePressed(MouseEvent e)
		      { mousePressedHelper(e);}
		    });
		    */
		add(mapV);
		
		mapV.setBounds(0,0, MapView.MAP_WIDTH, MapView.MAP_HEIGHT);

		
		mapV.setVisible(true);
		
		setVisible(true);
		
	
	}
	
	
	/*
	protected void mousePressedHelper(MouseEvent e) {
		System.out.println("You have clicked game panel");
		this.requestFocus();
		mapV.testPress(e);
	}*/

	public void step()
	{
		if(myView.hasFocus())
		{
			mapV.step();
			gameRender();
			paintScreen();
		}
	}

	public MapView getMapView() { 
		return mapV;
	}
	
	private void gameRender()
	  {
	    // create an image buffer
	    if (dbImage == null){
	      dbImage = createImage(MapView.MAP_WIDTH, MapView.MAP_HEIGHT);
	      if (dbImage == null) {
	        System.out.println("dbImage is null");
	        return;
	      }
	      else
	        dbg = dbImage.getGraphics();
	    }

	    // clear the background
	    dbg.setColor(Color.white);
	    dbg.fillRect (0, 0, MapView.MAP_WIDTH, MapView.MAP_HEIGHT);
	    dbg.drawImage(mapV.getImage(), 0, 0, MapView.MAP_WIDTH, MapView.MAP_HEIGHT, null);

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

}

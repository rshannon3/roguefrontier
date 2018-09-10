package game;
/* The game is responsible for control of the overall window
 * 
 * 
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


@SuppressWarnings("serial")
public class Game extends JFrame implements WindowListener
{
  private static int DEFAULT_FPS = 80;

  public HUD hud; // displays information for the player to use
  private View v;


  public Game(int period)
  { 
    super("Rogue Frontier");
    //makeGUI(period);
    v = new View(this, period);
    this.getContentPane().add(v, BorderLayout.CENTER );
    addWindowListener( this );
    pack();
    setResizable(false);
    setVisible(true);
  }  

  /*
   * makeGUI - Creates the GUI for the game
   * @param period - the target Frames per second of the game
   * 
   */
  

  // window listener methods 

  public void windowActivated(WindowEvent e) 
  { v.resumeGame();  }

  public void windowDeactivated(WindowEvent e) 
  {  v.pauseGame();  }


  public void windowDeiconified(WindowEvent e) 
  {  v.resumeGame();  }

  public void windowIconified(WindowEvent e) 
  {  v.pauseGame(); }


  public void windowClosing(WindowEvent e)
  {  v.stopGame();  }

  public void windowClosed(WindowEvent e) {}
  public void windowOpened(WindowEvent e) {}

  // main method

  public static void main(String args[])
  { 
    int fps = DEFAULT_FPS;
    if (args.length != 0)
      fps = Integer.parseInt(args[0]);

    int period = (int) 1000.0/fps;
    System.out.println("fps: " + fps + "; period: " + period + " ms");

    new Game(period);    // target fps
  }

} 



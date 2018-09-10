package game;
import item.PotionFactory;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;

import mapUnit.Player;

public class PlayerInput implements Serializable
{
	//Constants
    public static final double NORTH = Math.PI/2.0;
    public static final double WEST = Math.PI;
    public static final double SOUTH = -Math.PI/2.0;
    public static final double EAST = 0.0;
    public static final double NORTH_EAST = Math.PI/4.0;
    public static final double NORTH_WEST = 3.0 * Math.PI / 4.0;
    public static final double SOUTH_WEST = -3.0 * Math.PI / 4.0;
    public static final double SOUTH_EAST = -Math.PI / 4.0;

	private Player player;
	//private char[] inputHistory = new char[2];
	private ArrayList<Character> inputHistory;
	
	public PlayerInput(Player p)
	{
		player = p;
		player.setInput( this );
		//inputHistory[0] = 0;
		//inputHistory[1] = 0;
		inputHistory = new ArrayList<Character>();
	}

	synchronized void keyPressed(char e) 
	{
		//System.out.println("ADDING KEY " + (char)e.getKeyCode());
		if(e == KeyEvent.VK_SPACE)
		{
			//System.out.println("Dashing!");
			player.attemptDash();
		}
		else if(!inputHistory.contains(new Character(e)) && ( e == KeyEvent.VK_W || e == KeyEvent.VK_A || e == KeyEvent.VK_S || e == KeyEvent.VK_D) )
			inputHistory.add(0, new Character(e));
		//displayInput();
	}

	public void displayInput()
	{
		//TODO 
	}

	synchronized public void keyReleased(char e)
	{
		//System.out.println("REMOVING KEY " + (char)e.getKeyCode());
		//displayInput();
		
		if(inputHistory.size() > 0) // Avoid errors searching an empty list
			inputHistory.remove( new Character(e) ); // remove the key that has been released
		if(!player.isDashing())
		{
			switch(e) // Stop movement in the direction of the release, checks first for 4 way, then for 8 way to stop moving if a diagonal is released at the same time
			{

			case 'w':
			case 'W': if(player.getMoveAngle() == NORTH) player.stopMoving();
			else if( player.getMoveAngle() == NORTH_EAST && !inputHistory.contains(new Character('D')))
				player.stopMoving();
			else if( player.getMoveAngle() == NORTH_WEST && !inputHistory.contains(new Character('Q')))
				player.stopMoving();
			break;
			case 'a':
			case 'A': if(player.getMoveAngle() == WEST) player.stopMoving();
			else if( player.getMoveAngle() == NORTH_WEST && !inputHistory.contains(new Character('W')))
				player.stopMoving();
			else if( player.getMoveAngle() == SOUTH_WEST && !inputHistory.contains(new Character('S')))
				player.stopMoving();
			break;
			case 's':
			case 'S': if(player.getMoveAngle() == SOUTH) player.stopMoving();
			else if( player.getMoveAngle() == SOUTH_WEST && !inputHistory.contains(new Character('A')))
				player.stopMoving();
			else if( player.getMoveAngle() == SOUTH_EAST && !inputHistory.contains(new Character('D')))
				player.stopMoving();
			break;
			case 'd':
			case 'D': if(player.getMoveAngle() == EAST) player.stopMoving();
			else if( player.getMoveAngle() == SOUTH_EAST && !inputHistory.contains(new Character('S')))
				player.stopMoving();
			else if( player.getMoveAngle() == NORTH_EAST && !inputHistory.contains(new Character('W')))
				player.stopMoving();
			break;   

			}
		}
		//displayInput();
	}

	public synchronized void checkInput()
	{
		if( inputHistory.size() == 1 )   //----------------------------------------Single Key press-----------------------------------------//
		{
			switch (inputHistory.get(0)) 
			{
			case 'W' :
			{
				//player.setDir( 2 );
				player.setMoveAngle(NORTH);
				player.setMoving(true);
				break;
			}   
			case 'D' : 
			{
				//player.setDir( 0 );
				player.setMoveAngle(EAST);
				player.setMoving(true);
				break;
			}  
			case 'S' : 
			{
				//player.setDir( 6 );
				player.setMoveAngle(SOUTH);
				player.setMoving(true);
				break;
			}   
			case 'A' : 
			{
				//player.setDir( 4 );
				player.setMoveAngle(WEST);
				player.setMoving(true);
				break;
			}
			}
		}
		else if(inputHistory.size() > 1) /*-------------------------------------------------Two keys being pressed at once-------------------------------------------------*/
		{
			switch ( inputHistory.get(0) ) 
			{
			case 'W' : // NORTH
			{
				if( inputHistory.get(1) == 'D' ) //NORTH EAST
				{
					//player.setDir( 1 );
					player.setMoveAngle(NORTH_EAST);
					player.setMoving(true);
				}
				else if(inputHistory.get(1) == 'A') // NORTH WEST
				{
					//player.setDir( 3 );
					player.setMoveAngle(NORTH_WEST);
					player.setMoving(true);
				}
				break;
			}   
			case 'D' : // EAST
			{
				if( inputHistory.get(1) == 'W' ) // NORTH EAST
				{
					//player.setDir( 1 );
					player.setMoveAngle(NORTH_EAST);
					player.setMoving(true);
				}
				else if(inputHistory.get(1) == 'S') // SOUTH EAST
				{
					//player.setDir( 7 );
					player.setMoveAngle(SOUTH_EAST);
					player.setMoving(true);
				}
				break;
			}  
			case 'S' : // SOUTH
			{
				if( inputHistory.get(1) == 'D' ) //SOUTH EAST
				{
					//player.setDir( 7 );
					player.setMoveAngle(SOUTH_EAST);
					player.setMoving(true);
				}
				else if(inputHistory.get(1) == 'A') // SOUTH WEST
				{
					//player.setDir( 5 );
					player.setMoveAngle(SOUTH_WEST);
					player.setMoving(true);
				}
				break;
			}   
			case 'A' : // WEST
			{
				if( inputHistory.get(1) == 'W' )// NORTH WEST
				{
					//player.setDir( 3 );
					player.setMoveAngle(NORTH_WEST);
					player.setMoving(true);
				}
				else if(inputHistory.get(1) == 'S') // SOUTH WEST
				{
					//player.setDir( 5 );
					player.setMoveAngle(SOUTH_WEST);
					player.setMoving(true);
				}
				break;
			}   
			}
		
		}
	}

	public void keyTyped(char e)
	{
		
	}

	public void mouseMoved(MouseEvent e) 
	{
		//player.startTurn(e.getX(), e.getY());	
	}
	
	public void mouseMoved(int x, int y) 
	{
		player.startTurn(x, y);
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if( !player.isAttacking() )
		{	
			if(!player.hasWeaponEquipped())
				return;
			//Starts an attack
			if(e.getButton() == 3 && player.getAttackList().size() > 0)
			{

				player.setCurrentAttack(player.getAttackList().get(0));
				player.attemptAttack();
			}
			else if(e.getButton() == 1 && player.getAttackList().size() > 1)
			{
				
				player.setCurrentAttack(player.getAttackList().get(1));
				player.attemptAttack();
			}
		}
	}
}

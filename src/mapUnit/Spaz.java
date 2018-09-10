package mapUnit;

import java.awt.geom.Point2D;

import detection.DetectionCircle;
import attackPattern.Slash;
import attackPattern.Overhead;
import game.MapUnitList;

public class Spaz extends Mob
{
	private final double moveSpeed = 2.9;
	private final double turnSpeed = Math.PI/30;
	private final int reelTime = 30;
	private final int damage = 10;
	private final double knockbackSpeed = 10.0;
	private final double knockbackDistance = 20;
	
	private final int IMAGE_SET_SIZE = 1;
	
	private Player pl;

	
	public Spaz(int x, int y, MapUnitList obs)
	{
		super(x, y, obs);
		setMoveSpeed(moveSpeed);
		setWalkSpeed(moveSpeed);
		setTurnSpeed(turnSpeed);
		setCurrentHealth(25);
		pl = getPlayer();
	}
	
	public void determineAction()
	{
		if(!isMoving())
			setMoving(true);
		if(!canMove())
		{
			double angle = Math.random()*Math.PI*2 - Math.PI;
			startTurn(angle);
			setMoveAngle(angle);
		}
	}
	
	@Override
	protected void collidedWith(MapUnit mu)
	{
		if(mu instanceof Player)
		{
			((Mob) mu).recieveAttack(this, damage);
			((Mob)mu).startReel(reelTime, knockbackSpeed, knockbackDistance, getMoveAngle());
		}
	}
	
	private Player getPlayer()
	{
		for(int i = 0; i < getObs().getNumObstacles(); i++ )
		{
			if(getObs().getUnit(i) instanceof Player)
				return (Player)getObs().getUnit(i);
			
		}
		System.out.println("ERROR, COULD NOT FIND PLAYER.");
		return null;
	}
	@Override
	public int getImageSetSize()
	{
		return IMAGE_SET_SIZE;
	}
	
}

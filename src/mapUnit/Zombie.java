package mapUnit;

import game.MapUnitList;

import item.Ranged;

import java.awt.geom.Point2D;

import attackPattern.Slash;
import attackPattern.Stab;
import attackPattern.Overhead;

import detection.DetectionCircle;


public class Zombie extends Mob 
{
	private Player enemy;
	private final double moveSpeed = .9;
	private final double walkSpeed = .9;
	private final double turnSpeed = Math.PI/30;
	private Stab claw;
	private Slash w;
	
	public Zombie(int x, int y, MapUnitList obs)
	{
		super(x, y, obs);
		setMoveSpeed(moveSpeed);
		setWalkSpeed(moveSpeed);
		setTurnSpeed(turnSpeed);
		for(int i = 0; i < obs.getNumObstacles(); i++ )
		{
			if(obs.getUnit(i) instanceof Player)
				enemy = (Player)obs.getUnit(i);
			
		}
		if(enemy == null)
			System.out.println("ERROR, COULD NOT FIND PLAYER.");
		claw = new Stab(this, obs, 1, 0, 15,
				30, 15, 40, 30, 3.0, 20, 40, 15);;
		
		w = new Slash(this, obs);
		w.setWeaponLength(3);
		w.setRadius(8);
		w.setSlashWidth(Math.PI / 2);
		w.setStartupFrames(5);
		w.setActiveFrames(10);
		w.setRecoveryFrames(30);
		w.setKnockbackDistance(10);
		w.setKnockbackSpeed(10.0);
		w.setDamage(20);
		w.setHitstun(10);
		//setCurrentAttack(claw);
		setMaxHealth(100);
		setCurrentHealth(getMaxHealth());
		getInventory().equip(new Ranged(20));
		
	}
	
	public void determineAction()
	{
		
		if(enemy != null)
		{
			if(!isAttacking() && !isReeling())
			{
				if( (int)Point2D.Double.distance(getX(), getY(), enemy.getX(), enemy.getY()) < 
						((DetectionCircle)getCollisionBox()).getRadius() + getCurrentAttack().getRadius() * 2 * getCurrentAttack().getWeaponLength() + ((DetectionCircle)enemy.getCollisionBox()).getRadius() - 5)
				{
					stopMoving();
					startAttack();
				}
				else
				{
					startTurn(enemy.getX(), enemy.getY());
					setMoveAngle(enemy.getX(), enemy.getY());
					setMoving(true);
				}
			}
		}

	}
}

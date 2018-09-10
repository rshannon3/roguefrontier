package mapUnit;


import game.MapUnitList;
import game.PlayerInput;
import item.Armor;
import item.Axe;
import item.Inventory;
import item.PotionFactory;
import item.Ranged;
import item.Sword;
import item.Weapon;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import attackPattern.AttackPattern;
import attackPattern.AttackPatternFactory;
import attackPattern.Slash;
import attackPattern.Overhead;
import attackPattern.Stab;


public class Player extends Mob
{

	private Stab w; // IN PROGRESS
	private Overhead st; // IN PROGRESS
	private MasteryLevel swordMastery = MasteryLevel.EXPERT,
							maceMastery = MasteryLevel.ADEPT,
							spearMastery = MasteryLevel.ADEPT, 
							axeMastery = MasteryLevel.NOVICE,
							rangedMastery = MasteryLevel.ADEPT;
	
	public enum MasteryLevel{ NOVICE, ADEPT, JOURNEYMAN, EXPERT, MASTER };
	private List<AttackPattern> attackList;
	private PlayerInput input;
	
	private boolean dashing = false;
	private int dashFrame = 0;
	private double dashSpeed = 8.0;
	private int dashDuration = 8;
	private int dashCost = 0;
	
	private int attackCost = 0;
	
	private boolean dashRecovering = false;
	private int dashRecoveryFrame = 0;
	private int dashRecoveryDuration = 0;
	private Inventory inspectionInventory;
	
	
	
	
	public Player(int x, int y, MapUnitList obs)
    {
		//TODO ORGANIZE THIS
		super(x, y, obs);
		/*
		w = new Stab(null, obs, 0, 0 ,0 , 0 , 0 ,0 ,0 ,0, 0, 0, 0);
		w.setWeaponLength(3);
		w.setHaftLength(19);
		w.setRadius(2);
		//w.setSlashWidth(Math.PI / 1.0);
		w.setStartupFrames(15);
		w.setActiveFrames(5);
		w.setRecoveryFrames(60);
		w.setKnockbackDistance(9);
		w.setKnockbackSpeed(10.0);
		w.setDamage(1);
		w.setHitstun(20);
		w.setStabDistance(300);
		*/
		//st = new Stab(this, obs);
		//weapon = new Axe(1);
		inspectionInventory = new Inventory(16);
		getInventory().equip(new Ranged(20));

		getInventory().addItem(new Axe(10));
		getInventory().addItem(new Armor(0.9));
		getInventory().addItem(new Sword(10));
		getInventory().addItem(PotionFactory.getHealthPotion(5));
		getInventory().addItem(PotionFactory.getStaminaPotion(3));
		input = new PlayerInput(this);
		setWalkSpeed(2.0);
		setMaxHealth(100);
		setCurrentHealth(getMaxHealth());
		//attackList = new ArrayList<AttackPattern>();
		//attackList.add(st);
		updateAttackPatternList();
		//attackList.add(w);
		
		
    }
	
	@Override
	public void move(){
		super.move();
		if(!inspectionInventory.isEmpty()){
			ItemContainer dropContainer = new ItemContainer(this.getX(), this.getY(), obs);
			inspectionInventory.transferAllItemsTo(dropContainer.getInventory());
			this.obs.add(dropContainer);
		}
	}
	
	private void updateAttackPatternList()
	{
		attackList = AttackPatternFactory.getList(this);
	}

	public void act()
	{
		if(checkDeath())
			return;
		determineAction();
		if(dashing)
			dash();
		super.performAction();
	}
	
	public void determineAction()
	{
		//TODO INPUT SHOULD BE CHECKED BASED ON THE CURRENT STATE OF THE PLAYER
		//     ONLY ALLOWING AN INPUT TO ISSUE A NEW COMMAND IF THE PLAYER IS NOT
		//     IN A CONFLICTING STATE
		if(!dashing)
			input.checkInput();
	}
	
	
	//------------------------------------------------------------------DASHING------------------------------------------//
	public void dash()
	{
		dashFrame--;
		if(dashFrame < 0)
		{
			dashing = false; // stop dashing
			dashFrame = dashDuration; // reset dashFrame value
			dashRecovering = true; // TODO add dash recovery?
			stopMoving();
			setMoveSpeed(getWalkSpeed()); // reset your walkspeed to normal
		}
	}
	
	public void startDash()
	{ 
		dashing = true;
		setMoveSpeed(dashSpeed);
		this.setStamina(getCurrentStamina() - dashCost); // TODO REMOVE THIS, ADDED TO TEST STAMINA
		resetStaminaRecovery();
	}
	
	public void attemptDash() 
	{
		if( !dashing && getCurrentStamina() >= dashCost)
		{
			if(isAttacking() && getCurrentAttack().isRecovering()) //TODO TEST
				interruptAttack();
			startDash();
		}
		
	}
	
	//-------------------------------------------------------END OF DASHING-------------------------------------------------//
	
	
	//-------------------------------------------------------Attacking-----------------------------------------------------//
	
	public void attack()
	{
		AttackPattern p = getCurrentAttack();
		if(p!=null)
			p.attack();
		else
			setAttacking(false);
	}
	
	public void attemptAttack()
	{ if( !isAttacking() && getCurrentStamina() >= attackCost )
		startAttack();
	}
	
	public void startAttack()
	{
		setAttacking(true);
		setMoveSpeed( getWalkSpeed() /2 ); 
		this.setStamina(getCurrentStamina() - attackCost); // TODO REMOVE THIS, ADDED TO TEST STAMINA
		resetStaminaRecovery();
	}
	
	
	//-------------------------------------------------------End of Attacking-----------------------------------------------------//
	

	public List<AttackPattern>	getAttackList()
	{ return attackList; }

	public void setInput(PlayerInput playerInput) {
		input = playerInput;
	}
	
	public PlayerInput getInput() 
	{ return input; }

	
	public boolean isDashing()
	{ return dashing; }	
	
	public void setDashing(boolean b)
	{ dashing = b; }

	@Override
	public boolean equipWeapon(Weapon weapon)
	{
		boolean result = super.equipWeapon(weapon);
		updateAttackPatternList();
		return result;
	}
	
	public boolean unequipWeapon()
	{
		boolean result = super.unequipWeapon();
		updateAttackPatternList();
		return result;
		
	}
	
	public MasteryLevel getMaceMastery()
	{
		return maceMastery;
	}

	public void setMaceMastery(MasteryLevel maceMastery)
	{
		this.maceMastery = maceMastery;
	}
	
	public MasteryLevel getSpearMastery()
	{
		return spearMastery;
	}

	public void setSpearMastery(MasteryLevel spearMastery)
	{
		this.spearMastery = spearMastery;
	}

	public MasteryLevel getAxeMastery()
	{
		return axeMastery;
	}

	public void setAxeMastery(MasteryLevel axeMastery)
	{
		this.axeMastery = axeMastery;
	}

	public MasteryLevel getSwordMastery()
	{
		return swordMastery;
	}

	public void setSwordMastery(MasteryLevel swordMastery)
	{
		this.swordMastery = swordMastery;
	}

	public MasteryLevel getRangedMastery()
	{
		return rangedMastery;
	}

	public void setRangedMastery(MasteryLevel rangedMastery)
	{
		this.rangedMastery = rangedMastery;
	}

	public Inventory getInspectionInventory() {
		return inspectionInventory;
	}

}

package mapUnit;

import game.MapUnitList;

public class Dummy extends Mob {

	public Dummy(int x, int y, MapUnitList obs)
	{
		super(x,y, obs);
		setMaxHealth(10000000);
		setCurrentHealth(getMaxHealth());
	}
	
	public void determineAction() {
		// TODO Auto-generated method stub

	}

}

package attackPattern;

import item.Axe;
import mapUnit.Mob;
import item.Mace;
import item.Ranged;
import item.Spear;
import item.Sword;

import java.util.ArrayList;
import java.util.List;

import mapUnit.Player;
import mapUnit.Player.MasteryLevel;

public class AttackPatternFactory
{		
	
	public static List<AttackPattern> getList(Player pl)
	{
		if(pl.getEquippedWeapon() == null)
			return getUnarmedList(pl);
		if(pl.getEquippedWeapon() instanceof Sword)
			switch(pl.getSwordMastery())
			{
				case NOVICE: 		return getNoviceSwordList(pl);
				case ADEPT: 		return getAdeptSwordList(pl);
				case JOURNEYMAN:	return getJourneymanSwordList(pl);
				case EXPERT:		return getExpertSwordList(pl);
				case MASTER:		return getMasterSwordList(pl);
			}
		if(pl.getEquippedWeapon() instanceof Mace)
			switch(pl.getMaceMastery())
			{
				case NOVICE: 		return getNoviceMaceList(pl);
				case ADEPT: 		return getAdeptMaceList(pl);
				case JOURNEYMAN:	return getJourneymanMaceList(pl);
				case EXPERT:		return getExpertMaceList(pl);
				case MASTER:		return getMasterMaceList(pl);
			}
		if(pl.getEquippedWeapon() instanceof Spear)
			switch(pl.getSpearMastery())
			{
				case NOVICE: 		return getNoviceSpearList(pl);
				case ADEPT: 		return getAdeptSpearList(pl);
				case JOURNEYMAN:	return getJourneymanSpearList(pl);
				case EXPERT:		return getExpertSpearList(pl);
				case MASTER:		return getMasterSpearList(pl);
			}
		if(pl.getEquippedWeapon() instanceof Axe)
			switch(pl.getAxeMastery())
			{
				case NOVICE: 		return getNoviceAxeList(pl);
				case ADEPT: 		return getAdeptAxeList(pl);
				case JOURNEYMAN:	return getJourneymanAxeList(pl);
				case EXPERT:		return getExpertAxeList(pl);
				case MASTER:		return getMasterAxeList(pl);
			}
		if(pl.getEquippedWeapon() instanceof Ranged)
			switch(pl.getRangedMastery())
			{
				case NOVICE: 		return getNoviceRangedList(pl);
				case ADEPT: 		return getAdeptRangedList(pl);
				case JOURNEYMAN:	return getJourneymanRangedList(pl);
				case EXPERT:		return getExpertRangedList(pl);
				case MASTER:		return getMasterRangedList(pl);
			}
		return null;
		
	}
	
	private static List<AttackPattern> getUnarmedList(Player pl)
	{
		return null;
	}

	private static List<AttackPattern> getMasterRangedList(Player pl)
	{

		return getAdeptRangedList(pl);
	}

	private static List<AttackPattern> getExpertRangedList(Player pl)
	{
		return getAdeptRangedList(pl);
	}

	private static List<AttackPattern> getJourneymanRangedList(Player pl)
	{
		return getAdeptRangedList(pl);
	}

	private static List<AttackPattern> getAdeptRangedList(Player pl)
	{
		List<AttackPattern> result = new ArrayList<AttackPattern>();
		double masterySpeedMod = getSpeedModifier(pl.getRangedMastery());
		Ranged ranged = (Ranged)pl.getEquippedWeapon();
		//Add Stab
				Stab st = new Stab(pl, pl.getObs(), ranged.getStabWeaponLength(), ranged.getStabHaftLength(), ranged.getStabRadius(), (int)(ranged.getStabStartup()*masterySpeedMod), 
						(int)(ranged.getStabActive()*masterySpeedMod), (int)(ranged.getStabRecovery()*masterySpeedMod), ranged.getStabHitstun(), ranged.getStabKnockbackSpeed(),
						ranged.getStabKnockbackDistance(), (int)(pl.getEquippedWeapon().getDamage()*ranged.getStabDamageMultiplier()), ranged.getStabDistance());
				result.add(st);
		return result;
	}

	private static List<AttackPattern> getNoviceRangedList(Player pl)
	{
		return getAdeptRangedList(pl);
	}


	private static List<AttackPattern> getMasterAxeList(Player pl)
	{
		return getAdeptAxeList(pl);
	}

	private static List<AttackPattern> getExpertAxeList(Player pl)
	{
		return getAdeptAxeList(pl);
	}

	private static List<AttackPattern> getJourneymanAxeList(Player pl)
	{
		return getAdeptAxeList(pl);
	}

	private static List<AttackPattern> getAdeptAxeList(Player pl)
	{
		List<AttackPattern> result = new ArrayList<AttackPattern>();
		double masterySpeedMod = getSpeedModifier(pl.getAxeMastery());
		Axe axe = (Axe)pl.getEquippedWeapon();
		//Add Slash
		Slash sl = new Slash(pl, pl.getObs(), axe.getSlashWeaponLength(), axe.getSlashHaftLength(), axe.getSlashRadius(), (int)(axe.getSlashStartup()*masterySpeedMod), 
				(int)(axe.getSlashActive()*masterySpeedMod), (int)(axe.getSlashRecovery()*masterySpeedMod), axe.getSlashHitstun(), axe.getSlashKnockbackSpeed(),
				axe.getSlashKnockbackDistance(), (int)(pl.getEquippedWeapon().getDamage()*axe.getSlashDamageMultiplier()), axe.getSlashWidth());
		result.add(sl);
		
		//Add Overhead
		Overhead st = new Overhead(pl, pl.getObs(), axe.getOverheadWeaponLength(), axe.getOverheadHaftLength(), axe.getOverheadRadius(), (int)(axe.getOverheadStartup()*masterySpeedMod), 
				(int)(axe.getOverheadActive()*masterySpeedMod), (int)(axe.getOverheadRecovery()*masterySpeedMod), axe.getOverheadHitstun(), axe.getOverheadKnockbackSpeed(),
				axe.getOverheadKnockbackDistance(), (int)(pl.getEquippedWeapon().getDamage()*axe.getOverheadDamageMultiplier()));
		result.add(st);
		
		return result;
		
	}

	private static List<AttackPattern> getNoviceAxeList(Player pl)
	{
		return getAdeptAxeList(pl);
	}

	private static List<AttackPattern> getMasterSpearList(Player pl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private static List<AttackPattern> getExpertSpearList(Player pl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private static List<AttackPattern> getJourneymanSpearList(Player pl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private static List<AttackPattern> getAdeptSpearList(Player pl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private static List<AttackPattern> getNoviceSpearList(Player pl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private static List<AttackPattern> getMasterMaceList(Player pl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private static List<AttackPattern> getExpertMaceList(Player pl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private static List<AttackPattern> getJourneymanMaceList(Player pl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private static List<AttackPattern> getAdeptMaceList(Player pl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private static List<AttackPattern> getNoviceMaceList(Player pl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private static List<AttackPattern> getMasterSwordList(Player pl)
	{
		return getAdeptSwordList(pl);
	}

	private static List<AttackPattern> getExpertSwordList(Player pl)
	{
		return getAdeptSwordList(pl);
	}

	private static List<AttackPattern> getJourneymanSwordList(Player pl)
	{
		return getAdeptSwordList(pl);
	}

	private static List<AttackPattern> getAdeptSwordList(Player pl)
	{
		List<AttackPattern> result = new ArrayList<AttackPattern>();
		double masterySpeedMod = getSpeedModifier(pl.getSwordMastery());
		Sword sword = (Sword)pl.getEquippedWeapon();
		//Add Slash
		Slash sl = new Slash(pl, pl.getObs(), sword.getSlashWeaponLength(), sword.getSlashHaftLength(), sword.getSlashRadius(), (int)(sword.getSlashStartup()*masterySpeedMod), 
				(int)(sword.getSlashActive()*masterySpeedMod), (int)(sword.getSlashRecovery()*masterySpeedMod), sword.getSlashHitstun(), sword.getSlashKnockbackSpeed(),
				sword.getSlashKnockbackDistance(), (int)(pl.getEquippedWeapon().getDamage()*sword.getSlashDamageMultiplier()), sword.getSlashWidth());
		result.add(sl);
		
		//Add Stab
		Stab st = new Stab(pl, pl.getObs(), sword.getStabWeaponLength(), sword.getStabHaftLength(), sword.getStabRadius(), (int)(sword.getStabStartup()*masterySpeedMod), 
				(int)(sword.getStabActive()*masterySpeedMod), (int)(sword.getStabRecovery()*masterySpeedMod), sword.getStabHitstun(), sword.getStabKnockbackSpeed(),
				sword.getStabKnockbackDistance(), (int)(pl.getEquippedWeapon().getDamage()*sword.getStabDamageMultiplier()), sword.getStabDistance());
		result.add(st);
		
		return result;
	}

	private static List<AttackPattern> getNoviceSwordList(Player pl)
	{
		return getAdeptSwordList(pl);
	}
	
	private static double getSpeedModifier(MasteryLevel ml)
	{
		switch(ml)
		{
		case NOVICE: 		return 1.5;
		case ADEPT: 		return 1.0;
		case JOURNEYMAN: 	return 0.9;
		case EXPERT:		return 0.8;
		case MASTER:		return 0.7;
		default:			return 1.0;
		}
	}
}

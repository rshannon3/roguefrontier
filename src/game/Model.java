package game;

import item.*;

import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import mapUnit.*;
import mapUnit.MapUnit.Name;
import detection.*;

public class Model {
	private static final boolean AUTO_LOAD = true;
	MapUnitList obs;
	Player player;
	private int spawnTimer = 0;
	ItemContainer chest;
	InteractableMapUnit waveTrigger;
	private int waveNumber = 0;
	private boolean spawning = false;

	public enum Mode {
		STORY, TEST, EDITOR
	};

	private Mode currentMode;

	public Model(Mode mode) {
		currentMode = mode;
		switch (currentMode) {
		case STORY:
			if (AUTO_LOAD && GameSave.exists())// && GameSave.loadList() !=
												// null)
			{
				int reply = JOptionPane.showConfirmDialog(null,
						"Game save found. Would you like to load it?",
						"Load Game", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (reply == JOptionPane.YES_OPTION) {
					obs = GameSave.loadList();
					updatePlayer();
					spawnChest();
					fillChest();
					waveTrigger = new InteractableMapUnit(300, 25, obs){
						private static final long serialVersionUID = -6622949224066730094L;
						@Override
						public void interact(Player p){
							fillChest();
							spawning = true;
							waveNumber++;
						}
					};
				} else
					instantiateStoryMode();
			} else {
				instantiateStoryMode();
			}
			break;
		case EDITOR:
			instantiateEditorMode();
			break;
		default:
			System.err.println("Invalid Model Mode");
			System.exit(1);
		}
	}

	private void instantiateStoryMode() {
		//obs = GameSave.loadList(GameSave.MAPS_PATH+"Arena.rfd");
		//player = obs.getPlayer();
		obs = new MapUnitList();
		player = new Player(300,300,obs);
		player.getCollisionBox().setTraversable(false);
		obs.add(player);
		createArena();
		spawnChest();
		fillChest();
		waveTrigger = new InteractableMapUnit(300, 25, obs){
			/**
			 * 
			 */
			private static final long serialVersionUID = 8300237358901006149L;

			@Override
			public void interact(Player p){
				fillChest();
				spawning = true;
				waveNumber++;
			}
		};
		obs.add(waveTrigger);

	}

	private void instantiateEditorMode() {
		obs = new MapUnitList();
		player = new Player(0, 0, obs);
		player.getCollisionBox().setTraversable(true);
		obs.add(player);

	}

	public void update() {
		switch (this.currentMode) {
		case STORY:
			updateStoryMode();
			break;
		case EDITOR:
			updateEditorMode();
			break;
		}

	}

	private void updateEditorMode() {
		player.act();
	}

	private void updateStoryMode() {
		obs.act();
		//System.out.println("Num actors in obs: "+obs.getNumActors());
		// TEST: spawns zombies to attack the player
		if (spawning)
			spawnWave();
	}

	private void spawnChest() {
		chest = new ItemContainer(750, 475, obs, 25, true);
		obs.add(chest);

	}

	private void fillChest() {
		chest.getInventory().add(PotionFactory.getHealthPotion(20));
		chest.getInventory().add(PotionFactory.getStaminaPotion(10));
		switch (waveNumber) {
		case 2:
			chest.getInventory().add(new Axe(15));
			break;
		case 3:
			chest.getInventory().add(new Armor(.8));
			break;
		case 4:
			chest.getInventory().add(new Sword(15));
			break;
		case 10:
			chest.getInventory().add(new Sword(80));
			break;
		}
	}

	private void spawnWave() {
		switch (waveNumber) {
		case 1:
			spawnZombies();
			stopSpawning();
			break;
		case 2:
			spawnWaves(720, 2);
			break;
		default:
			spawnSpazWaves(720, waveNumber);
			break;
		}
	}

	private void spawnWaves(int waveLength, int numWaves) {
		if (spawnTimer % waveLength == 0) {
			spawnZombies();
		} else {
			if (spawnTimer > waveLength * numWaves)
				stopSpawning();
		}
		spawnTimer++;
	}

	// TODO REMOVE THIS, MAKE SPAWN MOB FUNCTION
	private void spawnSpazWaves(int waveLength, int numWaves) {
		if (spawnTimer % waveLength == 0) {
			spawnSpazzes();
		} else {
			if (spawnTimer > waveLength * numWaves)
				stopSpawning();
		}
		spawnTimer++;
	}

	private void stopSpawning() {
		spawnTimer = 0;
		spawning = false;
	}

	private void spawnSpazzes() {
		obs.add(new Spaz(50, 50, obs));
		obs.add(new Spaz(650, 50, obs));
		obs.add(new Spaz(50, 650, obs));
		obs.add(new Spaz(650, 650, obs));
	}

	public void spawnZombies() {
		Zombie z = new Zombie(50, 50, obs);
		z.setCollisionBox(new DetectionCircle(50,50, 10));
		obs.add(z);
		obs.add(new Zombie(650, 50, obs));
		obs.add(new Zombie(50, 650, obs));
		obs.add(new Zombie(650, 650, obs));
	}

	public void spawnDummies() {
		obs.add(new Dummy(150, 150, obs));
		obs.add(new Dummy(650, 50, obs));
		obs.add(new Dummy(50, 550, obs));
		obs.add(new Dummy(550, 550, obs));
	}

	public void createArena() {
		obs.add(new MapUnit(obs, new DetectionRectangle(-100, -100, 800, 100)));
		obs.add(new MapUnit(obs, new DetectionRectangle(0, -100, 100, 800)));
		obs.add(new MapUnit(obs, new DetectionRectangle(800, -100, 800, 100)));
		obs.add(new MapUnit(obs, new DetectionRectangle(-100, 700, 100, 1000)));
		obs.add(new MapUnit(obs, new DetectionRectangle(350, 350, 200, 200)));
	}

	public MapUnitList getObstacles() {
		return obs;
	}

	/**
	 * @return the currentMode
	 */
	public Mode getCurrentMode() {
		return currentMode;
	}

	public void setObstacles(MapUnitList mul) {
		obs = mul;
	}

	public void mouseClicked(int x, int y, MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON2) {
			spawnDummies();
		} else {
			player.getInput().mouseClicked(e);
		}
	}
	
	/*
	private void mouseClickedEditorMode(int x, int y, MouseEvent e) {
		double mx = player.getCollisionBox().getLocation().getX()
				- (MapView.MAP_WIDTH / 2 - e.getX());
		double my = player.getCollisionBox().getLocation().getY()
				- (MapView.MAP_HEIGHT / 2 - e.getY());
		if (e.getButton() == MouseEvent.BUTTON1)// Adds a rectangle on left
												// mouseclick
		{
			double size = 50.0;
			DetectionRectangle s = new DetectionRectangle(mx, my, (int) size,
					(int) size);

			//obs.add(new Spaz((int) mx, (int) my, obs));
		} else if (e.getButton() == MouseEvent.BUTTON3) // deletes on object on
														// right click
		{
			ArrayList<MapUnit> obsToDel = obs.getObstaclesAt(mx, my);
			for (MapUnit mu : obsToDel) {
				obs.remove(mu);
			}
		}

	}*/

	public Player getPlayer() {
		return player;
	}

	public void keyPressed(char c) {
		player.getInput().keyPressed(c); // TODO MOVE IMPLEMENTATION TO
											// PLAYERINPUT.KEYPRESSED
	}

	public void keyReleased(char c) {
		player.getInput().keyReleased(c);
	}

	public void keyTyped(char c) {
		player.getInput().keyTyped(c);

	}

	public void mouseMoved(int x, int y) {
		player.getInput().mouseMoved( x,  y);
	}

	public void updatePlayer() {
		player = this.obs.getPlayer();

		if(currentMode == Mode.EDITOR){
			player.getCollisionBox().setTraversable(true);
		}else if(currentMode == Mode.STORY){
			player.getCollisionBox().setTraversable(false);
		}

	}

	public void addMapUnit(Name mun, int x, int y) {
		obs.add(mapUnitFactory(mun, x, y, 50, 50));
	}
	
	public void addMapUnit(Name mun, int x, int y, int h, int w) {
		obs.add(mapUnitFactory(mun, x, y, h, w));
	}
	
	public void deleteMapUnit(int x, int y)
	{
		ArrayList<MapUnit> obsToDel = obs.getObstaclesAt(x, y);
		for (MapUnit mu : obsToDel) 
		{
			obs.remove(mu);
		}
	}
	
	private MapUnit mapUnitFactory(Name mun, int x, int y, int h, int w)
	{
		MapUnit m;
		switch(mun)
		{
		case MAP_UNIT: m = new MapUnit(x, y, h, w, obs);
		break;
		case ACTOR: m = new Actor(x, y, obs);
		break;
		//case MOB: m = null;
		//break;
		case PLAYER: m = new Player(x, y, obs);
		break;
		case SPAZ: m = new Spaz(x, y, obs);
		break;
		case DUMMY: m = new Dummy(x, y, obs);
		break;
		case ZOMBIE: m = new Zombie(x, y, obs);
		break;
		case ITEM_CONTAINER: m = new ItemContainer(x, y, obs);
		break;
		default:
		m = null;
		}
		return m;
	}

}

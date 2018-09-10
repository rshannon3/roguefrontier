package game;

import item.Inventory;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JTextField;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import detection.DetectionCircle;
import detection.DetectionRectangle;

import mapUnit.Actor;
import mapUnit.Interactable;
import mapUnit.ItemContainer;
import mapUnit.Mob;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

@SuppressWarnings("serial")
public class View extends JPanel implements Runnable {

	// private static long MAX_STATS_INTERVAL = 1000000000L;
	private static long MAX_STATS_INTERVAL = 1000L;
	// record stats every 1 second (roughly)

	private static final int NO_DELAYS_PER_YIELD = 16;
	/*
	 * Number of frames with a delay of 0 ms before the animation thread yields
	 * to other running threads.
	 */

	private static int MAX_FRAME_SKIPS = 5; // was 2;
	// no. of frames that can be skipped in any one animation loop
	// i.e the games state is updated but not rendered

	private static int NUM_FPS = 10;
	// number of FPS values stored to get an average

	// used for gathering statistics
	private long statsInterval = 0L; // in ms
	private long prevStatsTime;
	private long totalElapsedTime = 0L;
	private long gameStartTime;
	private int timeSpentInGame = 0; // in seconds

	private long frameCount = 0;
	private double fpsStore[];
	private long statsCount = 0;
	private double averageFPS = 0.0;

	private long framesSkipped = 0L;
	private long totalFramesSkipped = 0L;
	private double upsStore[];
	private double averageUPS = 0.0;

	private DecimalFormat df = new DecimalFormat("0.##"); // 2 dp
	private Thread animator; // the thread that performs the animation
	private volatile boolean running = false; // used to stop the animation
												// thread
	private volatile boolean isPaused = false;

	private int period; // period between drawing in _ms_

	private Game myGame; // reference to the game window

	// used at game termination
	private volatile boolean gameOver = false;
	private Font font;
	private FontMetrics metrics;

	// off screen rendering
	private Graphics dbg;
	private Image dbImage = null;

	// ---------------------------------------------------------------------TODO
	// REMOVE, ONLY
	// TEST------------------------------------------------------------//
	private static String IMAGE_DIR = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "RogueFrontierImageSet"
			+ System.getProperty("file.separator") + "BACKGROUND"
			+ System.getProperty("file.separator") + "CaveFloor.gif ";
	private Image menuBackgroundImage;
	// ------------------------------------------------------------------------------------------------------------------------------------------------------//
	
	private enum State{MENU,GAME,EDITOR};
	State currentState;
	private JButton play;
	private JButton editor;
	Image backgroundImage;
	JPanel menuPanel;

	private JTextField jtfBox; // displays number of obstacles
	private JTextField jtfTime; // displays time spent in game
	private JTextField jtfKillCount;
	JPanel itemPanel;

	public HUD hud; // displays information for the player to use
	private LoadoutGUI loadout;

	private GamePanel gameP; // TODO TEST
	private InventoryGUI playerInventory, inspectionInventoryGUI; // TODO TEST
	private EditorPanel editPanel;
	
	private Model mod;

	private DialogBox dialogBox; // -----------------------------IN PROGRESS
									// DIALOG BOX-----------------------------//
									// //TODO

	public View(Game g, int p) {
		
		currentState = State.MENU;
		String fs = System.getProperty("file.separator");
		String path = System.getProperty("user.dir") + fs
				+ "RogueFrontierImageSet" + fs + "BACKGROUND" + fs + "Menu.gif";
		try {

			File f = new File(path);
			if (f.exists())
				menuBackgroundImage = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// makeGUI(p);
		// //------------------------------------------------------TESTING MENU
		// SCREEN. TODO WIP --------------------------------//
		createMenu(); // TEST
		myGame = g;
		this.period = p;

		// setBackground(Color.white);
		// setPreferredSize( new Dimension(MAP_WIDTH, MAP_HEIGHT)); //TODO
		// change to game size

		setFocusable(true);
		requestFocus(); // the JPanel now has focus, so receives key events
		setupKeyInput();

		// set up message font
		font = new Font("SansSerif", Font.BOLD, 24);
		metrics = this.getFontMetrics(font);

		// initialise timing elements
		fpsStore = new double[NUM_FPS];
		upsStore = new double[NUM_FPS];
		for (int i = 0; i < NUM_FPS; i++) {
			fpsStore[i] = 0.0;
			upsStore[i] = 0.0;
		}
	}

	private void createMenu() {

		this.setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(1048, 708));
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(2, 1));
		play = new JButton("Play Mode");
		play.setFont(new Font("Tahoma", Font.BOLD, 12));
		play.setFocusPainted(false);
		play.setBackground(Color.black);
		play.setForeground(new Color(0, 210, 0));
		//play.setBorderPainted(false);
		//play.setBorder(BorderFactory.createLineBorder(new Color(0, 210, 0)));
		editor = new JButton("Edit Mode");
		editor.setFont(new Font("Tahoma", Font.BOLD, 12));
		editor.setFocusPainted(false);
		editor.setBackground(Color.black);
		editor.setForeground(new Color(0, 210, 0));
		//editor.setBorderPainted(false);
		//editor.setBorder(BorderFactory.createLineBorder(new Color(0, 210, 0)));
		play.setAlignmentX(Component.CENTER_ALIGNMENT);
		editor.setAlignmentX(Component.CENTER_ALIGNMENT);
		menuPanel.setBackground(Color.cyan);


		play.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("start playing game");
				removeMenu();
				setPreferredSize(new Dimension(3000, 3000));
				createGame();
				currentState = State.GAME;
				paintAll(getGraphics());
				
			}
		});
		editor.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				System.out.println("start editing game");
				removeMenu();
				//setPreferredSize(new Dimension(3000, 3000));
				createEditor();
				currentState = State.EDITOR;
				paintAll(getGraphics());
			}
		});
		menuPanel.add(play);
		menuPanel.add(editor);
		add(menuPanel);

	}
	
	private void removeMenu()
	{
		this.remove(menuPanel);
		menuPanel = null;
	}
	
	private void createEditor()
	{
		myGame.setSize(new Dimension(975, 650));
		setLayout(new BorderLayout());
		mod = new Model(Model.Mode.EDITOR);
		gameP = new GamePanel(this, mod);
		EditorMenuBar menuBar = new EditorMenuBar(mod);
		JPanel editPanelWrapper = new JPanel();
		editPanelWrapper.setLayout(new GridBagLayout());
		editPanel = new EditorPanel(mod);
		editPanelWrapper.add(editPanel);
		add(gameP, BorderLayout.WEST);
		
		add(editPanelWrapper, BorderLayout.EAST);
		add(menuBar, BorderLayout.NORTH);
		
	}
	
	private void createGame() {
		myGame.setSize(new Dimension(1048, 732));
		System.out.println("Creating the game!");
		mod = new Model(Model.Mode.STORY);
		gameP = new GamePanel(this, mod);

		JPanel layout = new JPanel();
		layout.setLayout(new BorderLayout());

		this.setLayout(new BorderLayout());

		layout.add(gameP, BorderLayout.CENTER);
		hud = new HUD();
		hud.setPlayer(mod.getPlayer());
		
		dialogBox = new DialogBox();
		//dialogBox = new JTextArea("Hello", 4, 60);
		//dialogBox.setEditable(false);
		//dialogBox.setBackground(Color.BLACK);
		//dialogBox.setForeground(Color.GREEN);
		//dialogBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

		JPanel bottomPanel = new JPanel(); // Create a panel to hold text filed
											// and HUD
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(dialogBox, BorderLayout.NORTH);
		bottomPanel.add(hud, BorderLayout.SOUTH);
		layout.add(bottomPanel, BorderLayout.SOUTH);

		// -------------------------------Test-------------------------------//
		itemPanel = new JPanel();
		itemPanel.setLayout(new BorderLayout());

		loadout = new LoadoutGUI(mod.getPlayer());
		itemPanel.add(loadout, BorderLayout.CENTER);

		inspectionInventoryGUI = new InspectionInventoryGUI(mod.getPlayer());
		itemPanel.add(inspectionInventoryGUI, BorderLayout.NORTH);

		playerInventory = new InventoryGUI(mod.getPlayer());
		itemPanel.add(playerInventory, BorderLayout.SOUTH);

		add(itemPanel, BorderLayout.EAST);
		// -----------------------------End Test-------------------------//

		JPanel ctrls = new JPanel(); // a row of textfields
		ctrls.setLayout(new BoxLayout(ctrls, BoxLayout.X_AXIS));

		jtfBox = new JTextField("Obstacles: 0"); // A textfield used for
													// debugging
		jtfBox.setEditable(false);
		ctrls.add(jtfBox);

		jtfTime = new JTextField("Time Spent: 0 secs"); // A textfield used for
														// debugginga
		jtfTime.setEditable(false);
		ctrls.add(jtfTime);

		jtfKillCount = new JTextField("Mobs Killed: "); // A textfield used for
														// debugginga
		jtfKillCount.setEditable(false);
		ctrls.add(jtfKillCount);

		layout.add(ctrls, BorderLayout.NORTH);
		add(layout, BorderLayout.CENTER);
		System.out.println("done creating the game!");
	}

	public MapView getMapView() {
		return gameP.getMapView();
	}

	public void setObstacleNumber(int n) {
		jtfBox.setText("Map Units: " + n);
	}

	public void setTimeSpent(long t) {
		jtfTime.setText("Time Spent: " + t + " secs");
	}

	public void setKillCount() {
		jtfKillCount.setText("Mobs Killed: " + mod.getObstacles().deathCount);
	}

	public void updateDebugFields() {
		setObstacleNumber(mod.getObstacles().getNumObstacles());
		setTimeSpent(timeSpentInGame);
		setKillCount();
	}

	// ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	private void setupKeyInput() {
		addKeyListener(new KeyAdapter() {
			// listen for esc, q, end, ctrl-c on the canvas to
			// allow a convenient exit from the full screen configuration
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if ((keyCode == KeyEvent.VK_ESCAPE)
						|| (keyCode == KeyEvent.VK_END)
						|| ((keyCode == KeyEvent.VK_C) && e.isControlDown())) {
					GameSave.saveList(mod.getObstacles());
					running = false;
				} else if (keyCode == KeyEvent.VK_E) {
					List<Interactable> interactables = mod.getPlayer()
							.getNearestInteractables();
					itemPanel.remove(inspectionInventoryGUI);
					
					for(Interactable inter : interactables){
						if (inter instanceof ItemContainer) {
							ItemContainer ic = (ItemContainer)inter;
							ic.getInventory().transferAllItemsTo(mod.getPlayer().getInspectionInventory());
						}
						inter.interact(mod.getPlayer());
					}
					itemPanel.add(inspectionInventoryGUI, BorderLayout.NORTH);
				} else {
					mod.keyPressed((char) keyCode);
				}
			}

			public void keyReleased(KeyEvent e) {
				int keyCode = e.getKeyCode();
				mod.keyReleased((char) keyCode);
			}

			public void keyTyped(KeyEvent e) {
				mod.keyTyped(e.getKeyChar());
			}
		});
	} // end of readyForTermination()

	public void addNotify()
	// wait for the JPanel to be added to the JFrame before starting
	{
		super.addNotify(); // creates the peer
		startGame(); // start the thread
	}

	private void startGame()
	// initialise and start the thread
	{
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	} // end of startGame()

	// -game life cycle methods
	// called by the JFrame's window listener methods

	public void resumeGame()
	// called when the JFrame is activated / deiconified
	{
		isPaused = false;
	}

	public void pauseGame()
	// called when the JFrame is deactivated / iconified
	{
		isPaused = true;
	}

	public void stopGame()
	// called when the JFrame is closing
	{
		running = false;
	}

	// ----------------------------------------------

	private void runGame()
	{
		 updateModel(); // update the model
		 updateGamePanel();
		 hud.repaint(); // update the HUD
		 playerInventory.update(); //TODO rename
		 inspectionInventoryGUI.update(); loadout.update();
		 updateDebugFields();
	}
	
	private void runEditor()
	{
		updateModel(); // update the model
		//if( gameP.isFocusOwner())
		//{
			//System.out.println("Game Panel has focus");
			updateGamePanel();
		//}
	}
	
	public void run()
	/* The frames of the animation are drawn inside the while loop. */
	{
		long beforeTime, afterTime, timeDiff, sleepTime;
		int overSleepTime = 0;
		int noDelays = 0;
		int excess = 0;
		gameStartTime = System.currentTimeMillis();
		prevStatsTime = gameStartTime;
		beforeTime = gameStartTime;
		//instantiateCurrentState();
		running = true;
		while (running) {/*
						 * updateGame(); // update the view & model
						 * updateGamePanel(); hud.repaint(); // update the HUD
						 * playerInventory.update(); //TODO rename
						 * otherInventory.update(); loadout.update();
						 * updateDebugFields();
						 */
			switch(currentState)
			{
				case GAME: runGame();
				break;
				case EDITOR: runEditor();
				break;
				case MENU: break;
				default: System.err.println("ERROR INVALID GAME STATE");
			}
			afterTime = System.currentTimeMillis();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;

			if (sleepTime > 0) { // some time left in this cycle
				try {
					Thread.sleep(sleepTime); // already in ms
				} catch (InterruptedException ex) {
				}
				overSleepTime = (int) ((System.currentTimeMillis() - afterTime) - sleepTime);
			} else { // sleepTime <= 0; the frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0;

				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield(); // give another thread a chance to run
					noDelays = 0;
				}
			}

			beforeTime = System.currentTimeMillis();

			/*
			 * If frame animation is taking too long, update the game state
			 * without rendering it, to get the updates/sec nearer to the
			 * required FPS.
			 */
			int skips = 0;
			while (currentState != State.MENU && (excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				updateModel(); // update state but don't render
				skips++;
			}
			framesSkipped += skips;

			storeStats();
		}

		printStats();
		System.exit(0); // so window disappears
	}

	private void updateModel() {
		// update the model
		if (!isPaused && !gameOver) {
			mod.update();
		}
	}

	private void updateGamePanel() {
		gameP.step();
	}
	
	@Override
	public void paintComponent(Graphics g){
		if (g == null) {
			System.out.println("NULL");
		}
		if (menuBackgroundImage != null && currentState == State.MENU) {
			System.out.println("Now drawing background");
			g.drawImage(menuBackgroundImage, 0, 0, null);
		} else {
			//System.out.println("Background is null");
			//super.paintComponent(g);
		}
		
	}

	private void gameOverMessage(Graphics g)
	// center the game-over message in the panel
	{
		// TODO ?? ADD TO MAPVIEW???
		return;
	}

	private void storeStats()
	/*
	 * The statistics: - the summed periods for all the iterations in this
	 * interval (period is the amount of time a single frame iteration should
	 * take), the actual elapsed time in this interval, the error between these
	 * two numbers;
	 * 
	 * - the total frame count, which is the total number of calls to run();
	 * 
	 * - the frames skipped in this interval, the total number of frames
	 * skipped. A frame skip is a game update without a corresponding render;
	 * 
	 * - the FPS (frames/sec) and UPS (updates/sec) for this interval, the
	 * average FPS & UPS over the last NUM_FPSs intervals.
	 * 
	 * The data is collected every MAX_STATS_INTERVAL (1 sec).
	 */
	{
		frameCount++;
		statsInterval += period;

		if (statsInterval >= MAX_STATS_INTERVAL) { // record stats every
													// MAX_STATS_INTERVAL
			long timeNow = System.currentTimeMillis();
			timeSpentInGame = (int) ((timeNow - gameStartTime) / 1000L); // ms
																			// -->
																			// secs
			// setTimeSpent( timeSpentInGame ); // TODO FIX

			long realElapsedTime = timeNow - prevStatsTime; // time since last
															// stats collection
			totalElapsedTime += realElapsedTime;

			totalFramesSkipped += framesSkipped;

			double actualFPS = 0; // calculate the latest FPS and UPS
			double actualUPS = 0;
			if (totalElapsedTime > 0) {
				actualFPS = (((double) frameCount / totalElapsedTime) * 1000L);
				actualUPS = (((double) (frameCount + totalFramesSkipped) / totalElapsedTime) * 1000L);
			}

			// store the latest FPS and UPS
			fpsStore[(int) statsCount % NUM_FPS] = actualFPS;
			upsStore[(int) statsCount % NUM_FPS] = actualUPS;
			statsCount = statsCount + 1;

			double totalFPS = 0.0; // total the stored FPSs and UPSs
			double totalUPS = 0.0;
			for (int i = 0; i < NUM_FPS; i++) {
				totalFPS += fpsStore[i];
				totalUPS += upsStore[i];
			}

			if (statsCount < NUM_FPS) { // obtain the average FPS and UPS
				averageFPS = totalFPS / statsCount;
				averageUPS = totalUPS / statsCount;
			} else {
				averageFPS = totalFPS / NUM_FPS;
				averageUPS = totalUPS / NUM_FPS;
			}
			/*
			 * System.out.println(timedf.format( (double) statsInterval/1000L) +
			 * " " + timedf.format((double) realElapsedTime/1000L) + "s " +
			 * df.format(timingError) + "% " + frameCount + "c " + framesSkipped
			 * + "/" + totalFramesSkipped + " skip; " + df.format(actualFPS) +
			 * " " + df.format(averageFPS) + " afps; " + df.format(actualUPS) +
			 * " " + df.format(averageUPS) + " aups" );
			 */
			framesSkipped = 0;
			prevStatsTime = timeNow;
			statsInterval = 0L; // reset
		}
	}

	private void printStats() {
		System.out.println("Frame Count/Loss: " + frameCount + " / "
				+ totalFramesSkipped);
		System.out.println("Average FPS: " + df.format(averageFPS));
		System.out.println("Average UPS: " + df.format(averageUPS));
		System.out.println("Time Spent: " + timeSpentInGame + " secs");
	}

	public double getAverageUPS() {
		return averageUPS;
	}

	public double getAverageFPS() {
		return averageFPS;
	}
	
	public EditorPanel getEditorPanel()
	{ return editPanel; }
	

}

package game;

import mapUnit.*;
import item.*;

import item.PotionFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class InventoryGUI extends JPanel {
	public static final int PANEL_HEIGHT = 250;
	public static final int PANEL_WIDTH = 250;
	protected int numRows = 5;
	private int numCols = 5;
	protected int tileSize = 50;
	private int tileWidth = tileSize;
	private int tileHeight = tileSize;

	Image[][] itemImages = new Image[5][5];
	Player p;
	Inventory inventory;
	Image defaultImage;

	// off screen rendering
	private Graphics dbg;
	private Image dbImage = null;
	private Image backgroundImage;

	public InventoryGUI(Player player) {
		this.p = player;
		setInventory();
		setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
		setSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

		this.setLayout(new GridLayout(6, 5));
		// this.setMaximumSize(new Dimension(300, 90));
		try {
			File f = new File(System.getProperty("user.dir")
					+ System.getProperty("file.separator")
					+ "RogueFrontierImageSet"
					+ System.getProperty("file.separator") + "Player"
					+ System.getProperty("file.separator") + "NORTH"
					+ System.getProperty("file.separator") + "0.gif");

			File k = new File(System.getProperty("user.dir")
					+ System.getProperty("file.separator")
					+ "RogueFrontierImageSet"
					+ System.getProperty("file.separator") + "BACKGROUND"
					+ System.getProperty("file.separator")
					+ "InventoryBackground.gif");
			backgroundImage = ImageIO.read(k);

			// defaultImage = ImageIO.read(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// icon = new ImageIcon(PotionFactory.getHealthPotion(0).getImage());
		setImages();

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				testPress(e);
			}
		});

	}

	protected void setInventory() {
		this.inventory = p.getInventory();
	}

	public void update() {
		setImages();
		gameRender();
		paintScreen();
	}

	private void setImages() {
		int i = 0;
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				Image temp;
				if (inventory.getItem(i) != null) // sets the Icon Image to the
													// item in the player's
													// inventory
					itemImages[r][c] = inventory.getItem(i).getImage();
				else
					itemImages[r][c] = null;
				i++;
			}
		}
	}

	private void gameRender() {
		// create an image buffer
		if (dbImage == null) {
			dbImage = createImage(PANEL_WIDTH, PANEL_HEIGHT);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			} else
				dbg = dbImage.getGraphics();
		}

		// clear the background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		dbg.drawImage(backgroundImage, 0, 0, 250, 250, null);
		drawItems();

	}

	public void drawItems() {
		for (int h = 0; h < numRows; h++) {
			for (int w = 0; w < numCols; w++) {
				if (itemImages[h][w] != null)
					dbg.drawImage(itemImages[h][w], w * tileWidth, h
							* tileHeight, tileWidth, tileHeight, null);
			}
		}

	}

	private void paintScreen()
	// use active rendering to put the buffered image on-screen
	{
		Graphics g;
		try {
			g = this.getGraphics();
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			Toolkit.getDefaultToolkit().sync(); // sync the display on some
												// systems
			g.dispose();
		} catch (Exception e) {
			System.out.println("Graphics error: " + e);
		}
	}

	public void testPress(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON3) {
			int index = e.getX() / tileSize + (e.getY() / tileSize) * numRows;
			if (inventory.getItem(index) != null) {
				inventory.useItem(index, p);
			}
		}

		if (e.getButton() == MouseEvent.BUTTON1) {
			int index = e.getX() / tileSize + (e.getY() / tileSize) * numRows;
			if (inventory.getItem(index) != null) {
				inventory.remove(index);
			}
		}
	}

	public Image getImage() {
		return dbImage;
	}

}

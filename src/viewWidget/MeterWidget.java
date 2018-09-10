package viewWidget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This widget shows a scalar meter showing a quantity out of a maximum. 
 * 
 * jobTarget: The space or container to put the tool in.
 * 
 * @author Samuel Ramirez, Jordan Wallet, Patrick Wilkening, Jonathan Wright
 * 
 */

@SuppressWarnings("serial")
public class MeterWidget extends JPanel {
	private JLabel meterLabel, scalarLabel;
	private JPanel meterPanel, meterBackground, meterActual;
	private int scalarMax, scalarActual;
	private boolean showMax;
	
	public MeterWidget(boolean showMax, String label, int amount, int max, Color foreground, Color background) {
		this.showMax = showMax;
		scalarActual = amount;
		scalarMax = max;
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(300, 20));
		
		
		JPanel labelPanel = new JPanel(new BorderLayout());
		meterPanel = new JPanel(null);
		JPanel scalarPanel = new JPanel(new BorderLayout());
		meterPanel.setPreferredSize(new Dimension(300, 50));
		labelPanel.setPreferredSize(new Dimension(75, 50));
		scalarPanel.setPreferredSize(new Dimension(75, 50));
		
	    
		this.add(labelPanel, BorderLayout.WEST);
		this.add(meterPanel, BorderLayout.CENTER);
		this.add(scalarPanel, BorderLayout.EAST);
		
		
		meterLabel = new JLabel(" " + label);
		labelPanel.add(meterLabel, BorderLayout.CENTER);
		scalarLabel = new JLabel();
		scalarPanel.add(scalarLabel, BorderLayout.CENTER);
		
		meterBackground = new JPanel(null);
		meterBackground.setBackground(background);
		meterPanel.add(meterBackground);
		meterActual = new JPanel(null);
		meterActual.setBackground(foreground);
		meterActual.setLocation(0,0);
		meterBackground.add(meterActual);
	}
	
	public MeterWidget(boolean showMax, String label, int amount, int max, Color foreground) {
		this(showMax, label, amount, max, foreground, Color.BLACK);
	}
	
	public MeterWidget(boolean showMax, String label, int max, Color foreground, Color background) {
		this(showMax, label, 0, max, foreground, background);
	}
	
	public MeterWidget(boolean showMax, String label, int max, Color foreground) {
		this(showMax, label, 0, max, foreground, Color.BLACK);
	}
	
	public MeterWidget(boolean showMax, String label, int amount, int max) {
		this(showMax, label, amount, max, Color.RED, Color.BLACK);
	}
	
	public MeterWidget(boolean showMax, String label, int max) {
		this(showMax, label, 0, max, Color.RED, Color.BLACK);
	}
	
	public MeterWidget(boolean showMax, String label, Color foreground) {
		this(showMax, label, 0, 100, foreground, Color.BLACK);
	}
	
	public MeterWidget(boolean showMax, String label) {
		this(showMax, label, 0, 100, Color.RED, Color.BLACK);
	}

	private void updateMeter() {
		if(scalarLabel == null) return;
		scalarLabel.setText(" " +  scalarActual + (showMax ? "/" + scalarMax : ""));
		int newWidth = meterPanel.getWidth();
		int newHeight = ((Double) (this.getHeight() * 0.8)).intValue();
		meterBackground.setSize(newWidth, newHeight);
		meterBackground.setLocation(0, ((Double) (this.getHeight() * 0.3)).intValue());
		
		double amtPerPixel = (scalarMax > 0 ? (1.0 * newWidth / scalarMax) : 0);
		int newMeter = ((Long) Math.round(scalarActual * amtPerPixel)).intValue();
		meterActual.setSize(newMeter, newHeight);
	}
	
	public void setAmount(int amount) {
		scalarActual = amount;
		updateMeter();
	}
	
	public void setMax(int max) {
		scalarMax = max;
		updateMeter();
	}
	
//	@Override
//	public void repaint() {
//		updateMeter();
//		super.repaint();
//	}
	
	@Override
	protected void paintComponent(Graphics g) {
		updateMeter();
		super.paintComponent(g);
	}
}

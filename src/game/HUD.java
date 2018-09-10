package game;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import mapUnit.Player;

import viewWidget.MeterWidget;

@SuppressWarnings("serial")
public class HUD extends JPanel
{
	private MeterWidget healthMeter, staminaMeter;
	private Player player;
	
	public void setPlayer(Player player)
	{
		this.player = player;
		updateMeters();
	}
	
	public HUD()
	{
		this.setLayout(new BorderLayout());
		healthMeter = new MeterWidget(true, "Health", Color.RED);
		staminaMeter = new MeterWidget(true, "Stamina", Color.GREEN);

		JPanel meterPanel = new JPanel(new BorderLayout());
		meterPanel.add(healthMeter, BorderLayout.WEST);
		meterPanel.add(staminaMeter, BorderLayout.EAST);
		this.add(meterPanel, BorderLayout.NORTH);
	}
	
	public HUD(Player player)
	{
		this.player = player;
		this.setLayout(new BorderLayout());
		healthMeter = new MeterWidget(true, "Health", player.getMaxHealth(), Color.RED);
		staminaMeter = new MeterWidget(true, "Stamina", player.getMaxStamina(), Color.GREEN);

		JPanel meterPanel = new JPanel(new BorderLayout());
		meterPanel.add(healthMeter, BorderLayout.WEST);
		meterPanel.add(staminaMeter, BorderLayout.EAST);
	}
	
	private void updateMeters()
	{
		if(player == null)
			return;
		healthMeter.setAmount(player.getCurrentHealth());
		healthMeter.setMax(player.getMaxHealth());
		staminaMeter.setAmount(player.getCurrentStamina());
		staminaMeter.setMax(player.getMaxStamina());
	}
	
	@Override
	public void repaint()
	{
		if(player != null)
			updateMeters();
		super.repaint();
		if(healthMeter != null)
		{
			healthMeter.repaint();
			staminaMeter.repaint();
		}
	}
	
	

}

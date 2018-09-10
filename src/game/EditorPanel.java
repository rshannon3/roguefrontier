package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import mapUnit.MapUnit;
import mapUnit.MapUnit.Name;

public class EditorPanel extends JPanel{
	
	JPanel spawnPanel;
	JLabel spawnXLabel, spawnYLabel, spawnWidthLabel, spawnHeightLabel, mousePosition;
	JTextField spawnXField, spawnYField, spawnWidthField, spawnHeightField;
	JButton placeObject;
	JComboBox<MapUnit.Name> selectObject;
	Model model;
	private JPanel sliderPanel;
	private JSlider slider;
	private boolean snap = true;
	private JCheckBox snapToggle;
	
	public EditorPanel(Model mod){
		model = mod;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		selectObject = new JComboBox<MapUnit.Name>(MapUnit.Name.values());
		mousePosition = new JLabel("X: . Y: .");
		mousePosition.setAlignmentX(CENTER_ALIGNMENT);
		add(mousePosition);
		add(selectObject);
		
		spawnPanel = new JPanel();
		spawnPanel.setLayout(new GridLayout(2,4));
		
		
		spawnXField = new JTextField();
		spawnXLabel = new JLabel("X: ");
		spawnXLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		spawnYField = new JTextField();
		spawnYLabel = new JLabel("Y: ");
		spawnYLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		
		spawnWidthField = new JTextField();
		spawnWidthLabel = new JLabel("Width: ");
		spawnHeightField = new JTextField();
		spawnHeightLabel = new JLabel("Height: ");
		
		spawnPanel.add(spawnXLabel);
		spawnPanel.add(spawnXField);
		spawnPanel.add(spawnYLabel);
		spawnPanel.add(spawnYField);
		spawnPanel.add(spawnHeightLabel);
		spawnPanel.add(spawnHeightField);
		spawnPanel.add(spawnWidthLabel);
		spawnPanel.add(spawnWidthField);
		
		add(spawnPanel);
		
		placeObject = new JButton("Place Object");
		placeObject.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				addMapUnit();
			}
		});
		placeObject.setAlignmentX(CENTER_ALIGNMENT);
		add(placeObject);
		
		slider = new JSlider(50,250,100);
		slider.setPreferredSize(new Dimension(10,50));
		slider.setMajorTickSpacing(50);
		slider.setPaintTicks(true);
		slider.setSnapToTicks(true);
		slider.setPaintLabels(true);
		
		JLabel slideLabel = new JLabel("Grid Size:");
		slideLabel.setAlignmentX(CENTER_ALIGNMENT);
		add(slideLabel);
		add(slider);
		
		//add(new JLabel("Snap to Grid:"));
		snapToggle = new JCheckBox("Snap to Grid:", true);
		snapToggle.setAlignmentX(CENTER_ALIGNMENT);
		snapToggle.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				snap = snapToggle.isSelected();
			}
		});
		add(snapToggle);
	}
	protected void addMapUnit() {

		
		
		MapUnit.Name mun = (Name) this.selectObject.getSelectedItem();
		if( !(spawnXField.getText().equals("") ||spawnYField.getText().equals("")) )
		{
			int xInput = Integer.parseInt(spawnXField.getText());
			int yInput = Integer.parseInt(spawnYField.getText());
			
			int xSnap = xInput - xInput % getSliderValue();
			int ySnap =  yInput - yInput % getSliderValue();
			
			if(!(spawnWidthField.getText().equals("") || spawnHeightField.getText().equals("")))
			{
				int hInput = Integer.parseInt(spawnHeightField.getText());
				int wInput = Integer.parseInt(spawnWidthField.getText());
				if(snap)
					model.addMapUnit(mun, xSnap, ySnap, hInput, wInput);
				else
					model.addMapUnit(mun, xInput, yInput, hInput, wInput);
			}
			else
			{
				if(snap)
					model.addMapUnit(mun, xSnap, ySnap);
				else
					model.addMapUnit(mun, xInput, yInput);
			}
		}

		
	}
	
	protected void addMapUnit(int x, int y) {
		MapUnit.Name mun = (Name) this.selectObject.getSelectedItem();
		int xSnap;
		int ySnap;
		if( x  > 0)
		{
			xSnap = x - x % getSliderValue();
		}
		else
		{
			xSnap = (int)(Math.floor(1.0 * x / getSliderValue() ) * getSliderValue());
		}
		if( y  > 0)
		{
			ySnap = y - y % getSliderValue();
		}
		else
		{
			ySnap = (int)(Math.floor(1.0 * y / getSliderValue() ) * getSliderValue());
		}
		if(!(spawnWidthField.getText().equals("") || spawnHeightField.getText().equals("")))
		{
			int hInput = Integer.parseInt(spawnHeightField.getText());
			int wInput = Integer.parseInt(spawnWidthField.getText());
			if(snap)
				model.addMapUnit(mun, xSnap, ySnap, hInput, wInput);
			else
				model.addMapUnit(mun, x, y, hInput, wInput);
		}
		else
		{

			if(snap)
				model.addMapUnit(mun, xSnap, ySnap);
			else
				model.addMapUnit(mun, x, y);
		}
		
	}
	protected int getSliderValue(){
		return slider.getValue();
	}
	
	public void updateMousePosition(int x, int y) 
	{
		// TODO Auto-generated method stub
		mousePosition.setText("X: " + x + ". Y: " + y + "."	);
		
	}
	
	

}

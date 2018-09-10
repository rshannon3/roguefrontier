package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class DialogBox extends JPanel
{
	JTextArea text;
	JTextArea choice;
	
	public DialogBox()
	{
		setLayout(new BorderLayout());
		this.setBackground(Color.GREEN);
		text = new JTextArea("Hello",4, 60);
		text.setEditable(false);
		text.setBackground(Color.BLACK);
		text.setForeground(Color.GREEN);
		text.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		add(text,BorderLayout.WEST);
		choice = new JTextArea(" Hi", 4, 50);
		choice.setEditable(false);
		choice.setBackground(Color.BLACK);
		choice.setForeground(Color.GREEN);
		choice.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		add(choice, BorderLayout.EAST);
		
	}
}

package ex5;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Tester {
	
	public static void main(String[] args) {
		
		//Credits Dialog
		JOptionPane.showConfirmDialog(null, "Created by Netanel Draiman \nTester v1.0", "FlightDatabase Tester",
				JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
		
		JFrame frame = new JFrame("FlightsDatabase");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		
		//Open Frame in middle of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		
		//Output Redirection - Yes = Frame, No = Console.
		int option = JOptionPane.showConfirmDialog(null, "Redirect Output to Frame? \n (No = Output to Console)",
				"Output Redirect", JOptionPane.YES_NO_OPTION);
		
		/*******************************************************************/
		/*** Put your SQL Username & Password into the Constructor below ***/
		/*******************************************************************/
		FlightsPanel fp = new FlightsPanel("system", "skywalker", option);
		frame.add(fp);
		
		frame.setVisible(true);
		
	}

}

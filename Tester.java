package ex5;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Tester {
	
	public static void main(String[] args) {
		
		JOptionPane.showConfirmDialog(null, "Tester Created by Netanel Draiman", "FlightDatabase Tester",
				JOptionPane.CLOSED_OPTION, JOptionPane.INFORMATION_MESSAGE);
		
		JFrame frame = new JFrame("FlightsDatabase");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int option = JOptionPane.showConfirmDialog(null, "Redirect Output to Frame? \n (No = Output to Console)",
				"Output Redirect", JOptionPane.YES_NO_OPTION);
		
		//Put your SQL Username & Password into the Constructor below
		FlightsPanel fp = new FlightsPanel("system", "skywalker", option);
		frame.add(fp);
		frame.setVisible(true);
		frame.setSize(500, 500);
		
		
	}

}

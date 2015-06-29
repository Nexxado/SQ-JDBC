package ex5;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Tester {
	
	public static void main(String[] args) {
		
		/************ NOTES ***********
		*
		* fixed method name from CheapastFlifht to CheapestFlight
		* 
		* COPY printTable() method at the bottom to FlightDatabase.java
		*
		*******************************/
		
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
		FlightsPanel fp = new FlightsPanel("system", "password", option);
		frame.add(fp);
		
		frame.setVisible(true);
		
	}

}

/*** COPY THE FOLLOWING METHOD TO FlightsDatabase.java TO ENABLE PRINT FUNCTION ***/


/*
public void printTable() {
		
		String query = "SELECT * FROM flights ORDER BY fno ASC";
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("--------------------------------------");
			System.out.println("FNO \t FFROM \t FTO \t COST\t");
			while(rs.next())
				System.out.println(rs.getInt("fno") + " \t " + rs.getString("ffrom") 
						+ " \t " + rs.getString("fto") + " \t " + rs.getInt("cost"));
			
			System.out.println("--------------------------------------");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
} 
*/


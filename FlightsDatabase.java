package ex5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class FlightsDatabase {
	
	/**************************************/
	/********** GLOBAL VARIABLES **********/
	/**************************************/
	
	private Connection con;
	private String table_name = "flights", trig_name = "flights_trig";
	final static String SELECT = "select ", CREATE_TABLE = "create table ", DROP_TABLE = "drop table ",
			INSERT = "insert into ", CREATE_TRIG = "create or replace trigger ", DROP_TRIG = "drop trigger ";
	
	
	
	/*********************************/
	/********** Constructor **********/
	/*********************************/
	/**
	* A constructor for the Class FlightsDatabase
	* @input login the login name of the user, in the database
	* @password the password of the user, in the database
	*/
	public FlightsDatabase(String login, String password) {
		con = createConnection(login, password);
		if(con == null)
			System.exit(-1);
	}
	
	public static Connection  createConnection(String login, String password) {
		System.out.println("-------- Oracle JDBC Establishing Connection ------");
		Connection  con = null;
		try {

			Class.forName("oracle.jdbc.driver.OracleDriver");

		} catch (ClassNotFoundException e) {

			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return con;

		}

		System.out.println("Oracle JDBC Driver Registered!");

		try {

			con = DriverManager
					.getConnection("jdbc:oracle:thin:@localhost:1521/xe",
							login, password);

		} catch (SQLException e) {

			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return con;

		}

		if (con != null) {

			System.out.println("Connection Successful");
			return con;
		}
		System.out.println("ERROR: Failed to make connection!");
		return con ;

	}
	
	/***********************************/
	/********** Create Method **********/
	/***********************************/

	/**
	 * The procedure creates a table in the Oracle database.
	 * The form of the table is:
	 * CREATE TABLE Flights(
	 *                  fno number(3) NOT NULL,
	 *                  ffrom varchar2(30) NOT NULL,
	 *                  fto varchar2(30) NOT NULL
	 *		     		cost number(4) NOT NULL);	 
	 * @return true if the table was created and else return false
	 */
	public boolean create() {
		
		System.out.println("Creating " + table_name + " Table...");
		
		String query = CREATE_TABLE + table_name + " ("
				+ " fno number(3) NOT NULL," 
				+  " ffrom varchar2(30) NOT NULL," 
				+ " fto varchar2(30) NOT NULL," 
				+ " cost number(4) NOT NULL"
				+ ")";
		
		try {
			
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			e.printStackTrace(); //DEBUG
			System.out.println("ERROR: Failed to Create Table");
			return false;
		}
		System.out.println(table_name + " Table Created");
		return true;
	}
	
	/***************************************/
	/********** addTrigger Method **********/
	/***************************************/

	/**
	 * The procedure creates a trigger on flights 
	* This trigger check for every insert –
	* That the pno number is less then 1000, if not it set the number to max(pno)+1
		
	 * @return true if the table was created and else return false
	 */
	public boolean addTrigger() {
		
		String query = CREATE_TRIG + trig_name 
				+ " BEFORE INSERT ON " + table_name 
				+ " FOR EACH ROW"
				+ " DECLARE maxfno NUMBER;"
				+ " BEGIN"
					+ " IF :NEW.fno < 1000 THEN"
						+ " SELECT MAX(fno)+1 INTO maxfno FROM Flights;"
						+ " IF maxfno IS NOT NULL THEN"
							+ " :NEW.fno := maxfno;"
					+ " END IF;"
					+ " END IF;"
				+ " END;";
		
		try {
			
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			
			e.printStackTrace(); //DEBUG
			System.out.println("ERROR: Failed to Create Trigger");
			return false;
		}
		
		System.out.println(trig_name + " Trigger On " + table_name + " Created");
		
		return true;
	}
	
	/***********************************/
	/********** Insert Method **********/
	/***********************************/

	/**
	 * The procedure inserts a flight to the table Flights in the database.
	 * @input route the flight’s number
	 * @input origin the name of the origin of the flight
	 * @input destination the name of the destination of the interval 
	 * @input price the flight’s cost
	 * @return true if the insertion succeeded and else false          
	 */
	public boolean insert(int route, String origin, String destination, int price) {
		String query = INSERT + table_name
				+ " values (" + route 
				+ "," + origin
				+ "," + destination
				+ "," + price + ")";
		
		//System.out.println(query); //DEBUG
		
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			
			e.printStackTrace(); //DEBUG
			System.out.println("ERROR: Failed to insert row");
			return false;
		}
		
		System.out.println("Row Inserted Successfuly");
		return true;
	}
	
	
	/**********************************/
	/********** Clean Method **********/
	/**********************************/

	/**
	 * The procedure drops the table Flights from the Oracle database.
	 * @return true if the table has been removed successfully and else false
	 */
	public boolean clean() {
		
		System.out.println("Dropping " + table_name + " Table...");
		
		String query = DROP_TABLE + " " + table_name;
		
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			
			e.printStackTrace(); //DEBUG
			System.out.println("ERROR: Failed to Drop Table");
			return false;
		}
		
		System.out.println(table_name + " Table Dropped");
		
		return true;
	}

	/*******************************************/
	/********** CheapestFlight Method **********/
	/*******************************************/

	/**
	 * The procedure tells if two given places are indirectly connected. 
	 * If there are no routes, in the database, that allow to get from
	 * place1 to place2 the procedure returns -1, else it returns the  cost of the cheapest path between place1 and place2. .
	 * @input place1 the name of the source of the path
	 * @input place2 the name of the destination of the path
	 * @return the cheapest cost of flights  (if the places are connected by a path) and else return -1
	 */
	public int CheapestFlight(String place1, String place2) {
		
		return 0;
	}
	
	
	/*********************************/
	/********** Main Method **********/
	/*********************************/

	/**
	 * The main function of the class.
	 * A graphical user interface is given and allows the user to do all the actions
	 * that were implemented here: create the database, clean the database, insert data
	 * and query connections in the data.
	 * 
	 */
	public static void main(String argv[]) {
		FlightsDatabase fd = new FlightsDatabase("system", "skywalker");
		fd.create();
		
		fd.insertRows();
		fd.addTrigger(); 
		
		fd.insert(777, "'Bora Bora'", "'Peru'", 4793);
		
		fd.clean();
		
	}
	
	
	/***********************************/
	/********** DEBUG METHODS **********/
	/***********************************/
	
	private boolean insertRows() {
//		String query1 = "INSERT INTO Flights values (123, 'Tel-Aviv', 'Brazil', 1754)";
//		String query2 = "INSERT INTO Flights values (456, 'Tel-Aviv', 'London', 482)";
//		String query3 = "INSERT INTO Flights values (514, 'London', 'Brazil', 750)";
//		String query4 = "INSERT INTO Flights values (793, 'Madrid', 'London', 1548)";
//		String query5 = "INSERT INTO Flights values (137, 'Madrid', 'Buenos Aires', 1486)";
//		String query6 = "INSERT INTO Flights values (943, 'Buenos Aires', 'Tel-Aviv', 2431)";
//		String query7 = "INSERT INTO Flights values (842, 'Chicago', 'Madrid', 749)";
//		String query8 = "INSERT INTO Flights values (543, 'Tel-Aviv', 'Chicago', 2187)";
		
		String query = "INSERT INTO Flights values (?, ?, ?, ?)";
		
		try {
			PreparedStatement pStatement = con.prepareStatement(query);
			
			//query 1
			pStatement.setInt(1, 123);
			pStatement.setString(2, "Tel-Aviv");
			pStatement.setString(3, "Brazil");
			pStatement.setInt(4, 1754);
			pStatement.addBatch();
			pStatement.clearParameters();
			
			//query 2
			pStatement.setInt(1, 456);
			pStatement.setString(2, "Tel-Aviv");
			pStatement.setString(3, "London");
			pStatement.setInt(4, 482);
			pStatement.addBatch();
			pStatement.clearParameters();
			
			//query 3
			pStatement.setInt(1, 514);
			pStatement.setString(2, "London");
			pStatement.setString(3, "Brazil");
			pStatement.setInt(4, 750);
			pStatement.addBatch();
			pStatement.clearParameters();
			
			//query 4
			pStatement.setInt(1, 793);
			pStatement.setString(2, "Madrid");
			pStatement.setString(3, "London");
			pStatement.setInt(4, 1548);
			pStatement.addBatch();
			pStatement.clearParameters();
			
			//query 5
			pStatement.setInt(1, 137);
			pStatement.setString(2, "Madrid");
			pStatement.setString(3, "Buenos Aires");
			pStatement.setInt(4, 1486);
			pStatement.addBatch();
			pStatement.clearParameters();
			
			//query 6
			pStatement.setInt(1, 943);
			pStatement.setString(2, "Buenos Aires");
			pStatement.setString(3, "Tel-Aviv");
			pStatement.setInt(4, 2431);
			pStatement.addBatch();
			pStatement.clearParameters();
			
			//query 7
			pStatement.setInt(1, 842);
			pStatement.setString(2, "Chicago");
			pStatement.setString(3, "Madrid");
			pStatement.setInt(4, 749);
			pStatement.addBatch();
			pStatement.clearParameters();
			
			//query 8
			pStatement.setInt(1, 543);
			pStatement.setString(2, "Tel-Aviv");
			pStatement.setString(3, "Chicago");
			pStatement.setInt(4, 2187);
			pStatement.addBatch();
			pStatement.clearParameters();
			
			pStatement.executeBatch();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR: Failed Random Insert");
			return false;
		}
		
		
		System.out.println("Random rows Inserted Successfuly");
		return true;
	}

	
	
	

}

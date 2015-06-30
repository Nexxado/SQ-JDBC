package ex5;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FlightsDatabase {
	
	/**************************************/
	/********** GLOBAL VARIABLES **********/
	/**************************************/
	
	private Connection con;
	private final String table_name = "flights", trig_name = "flights_trig";
	
	private final static String SELECT = "select ",
						CREATE_TABLE = "create table ",
						CREATE_VIEW = "create or replace view ",
						CREATE_TRIG = "create or replace trigger ",
						DROP_TABLE = "drop table ",
						INSERT = "insert into ";
		
	
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
	 *                  fno number(4) NOT NULL,
	 *                  ffrom varchar2(30) NOT NULL,
	 *                  fto varchar2(30) NOT NULL
	 *		     		cost number(4) NOT NULL);	 
	 * @return true if the table was created and else return false
	 */
	public boolean create() {
		
		System.out.println("Creating " + table_name + " Table...");
		
		String query = CREATE_TABLE + table_name + " ("
				+ " fno number(4) NOT NULL," 
				+  " ffrom varchar2(30) NOT NULL," 
				+ " fto varchar2(30) NOT NULL," 
				+ " cost number(4) NOT NULL"
				+ ")";
		
		try {
			
			Statement stmt = con.createStatement();
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			if(e.getErrorCode() == 955) {
				System.out.println("ERROR: Table Already Exists");
				return false;
			}
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
					+ " IF :NEW.fno > 999 THEN"
						+ " SELECT MAX(fno)+1 INTO maxfno FROM Flights;"
						+ " IF maxfno IS NOT NULL THEN"
							+ " :NEW.fno := maxfno;"
						+ " ELSE"
							+ " :NEW.fno := 1;"
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
				+ ",'" + origin + "'"
				+ ",'" + destination +"'"
				+ "," + price + ")";
		
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
			
			e.printStackTrace();
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
		
		if(place1.equals(place2))
			return 0;
		
		int cheapest = Integer.MAX_VALUE;
		
		String views[] = {"trip1", "trip2", "trip3"};
		
		String query1 = CREATE_VIEW + views[0] + " AS " +
						SELECT + "fto AS ffrom, cost AS total_cost " 
						+ "FROM flights "
						+ "WHERE ffrom = '" + place1 + "' "
						+ "ORDER BY total_cost ASC";
		
		String query2 = CREATE_VIEW + views[1] + " AS " +
						SELECT + "f.fto AS ffrom, f.cost + tp.total_cost AS total_cost "
						+ "FROM flights f JOIN trip1 tp ON f.ffrom = tp.ffrom "
						+ "ORDER BY total_cost ASC";
		
		String query3 = CREATE_VIEW + views[2] + " AS " +
						SELECT + "f.fto AS ffrom, f.cost + tp.total_cost AS total_cost "
						+ "FROM flights f JOIN trip2 tp ON f.ffrom = tp.ffrom "
						+ "ORDER BY total_cost ASC";
		
		String queries[] = {query1, query2, query3};
		
		for(int i = 0; i < 3; i++) {
			
			try {
				Statement stmt = con.createStatement();
				stmt.executeQuery(queries[i]);
				ResultSet rs = stmt.executeQuery("SELECT * FROM trip" + (i+1));
				
				
				while(rs.next()) {
					String connection = rs.getString("ffrom");
					if(connection.equals(place2)) {
						
						int cost = rs.getInt("total_cost");
						
						if(cheapest > cost)
							cheapest = cost;
					}
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
				return -1;
			}
			
		}
		
		
		if(cheapest == Integer.MAX_VALUE)
			cheapest = -1;
		
		return cheapest;
	}
		
	
	/***********************************/
	/********** DEBUG METHODS **********/
	/***********************************/
		
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
			e.printStackTrace();
		}
		
	}

	
	
	

}

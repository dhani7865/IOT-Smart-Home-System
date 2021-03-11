

// import java.sql to this class
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// import array list for all the rfid data
import java.util.ArrayList;

import com.phidget22.RFIDTag;

/**
 * import connection to this class and also creating new class called RFIDDao
 * creating public array list to get all getAllDoorLockStatus
 *  I have created a array to store all the sensor data into a array.
 * @author Dhanyaal Rashid
 */

// Creating public class RFIDDao class
public class RFIDDao {
	// Creating private static final string for the db connection and setting it to null
	private static final String dbConnection = null;

	// creating public static connection to get the db connection
	public static Connection getDBConnection() {
		// creating object for db connection and setting it to null
		Connection dbConnection = null;

		// Adding the mysql workbench username and assword, in order to connect to the database
		String user = "rashidd";
		String password = "rooSedef6";
		//  Usiing port 6306 instead of 3306 and connecting to the mudfoot server
		String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/rashidd";

		// creating try and catch to get the connection
		try {
			Class.forName("com.mysql.jdbc.Driver");//change string to mysql driver
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage()); // print message
		} // close ClassNotFoundException
		// creating try and catch to connect the db
		try {
			String dbURL = "jdbc:sqlite:DoorLockdb.sqlite"; // the url for the db
			// db connection
			dbConnection = DriverManager.getConnection(url, user, password); // return the db
			// connection
			// which comes
			// from the
			// drivermanager
			return dbConnection; // return the database connection
		} catch (SQLException e) { // creating catch sql exception for the
			// dbconnection
			System.out.println(e.getMessage()); // print the message to the
			// console
		} // close catch exception e
		return dbConnection; // return dbconnection
	} // close public connection


	// creating public array list for rfid sensor data and creating method called
	// getAllDoorLockStatus
	//Retrieve all RFID Data
	public static ArrayList<RFIDSensorData> getAllDoorLockStatus() throws SQLException {
		Connection dbConnection = null; // db connection object = null
		Statement statement = null; // statement object = null
		ResultSet resultset = null; // resultset object = null
		String query = "SELECT * FROM DoorLock;"; // select everything from the
		// DoorLock table
		RFIDSensorData temp = null; // creating object for temp and setting it to null

		ArrayList<RFIDSensorData> allRFIDData = new ArrayList<>(); // creating array
		// list for all
		// RFIDData - all
		// the rfid data will
		// be stored in the
		// RFIDSensorData array
		// list

		// creating try and catch to get the db connection
		try {
			dbConnection = getDBConnection(); // get db connection
			statement = dbConnection.createStatement(); // create statement for db connection
			System.out.println(query); // print the query result
			// execute sql query
			resultset = statement.executeQuery(query);
			// creating while loop for resultset
			while (resultset.next()) {
				// Creating string for all the results
				String TagID = resultset.getString("TagID");
				String TagValue = resultset.getString("TagValue");
				String SuccessFail = resultset.getString("SuccessFail");


				// creating temp for the RFIDSensorData
				temp = new RFIDSensorData(TagID, TagValue, SuccessFail);
				// adding the temporary allRFIDData to the array list
				allRFIDData.add(temp);
			} // close while loop for resultset
		} // close try
		// creating catch exception e and creating finally
		catch (SQLException e) {
			System.out.println(e.getMessage()); // print message
		} finally {
			// if resultset is not equal to null close resultset
			if (resultset != null) {
				resultset.close();
			} // close if statement for result set
			// if statement is not equal to null close statement
			if (statement != null) {
				statement.close();
			} // close if statement
			// if dbConnection is not equal to null close dbConnection
			if (dbConnection != null) {
				dbConnection.close();
			} // close if statment for db connection
		} // close finally
		return allRFIDData; // return all RFIDData
	} // close array list of rfid sensor data

	// Creating boolean to check if a valid user is trying to open the door, using specific tag id
	public boolean isUserValid(String TagId) {
		Connection dbConnection = null; // db connection object = null
		Statement statement = null; // statement object = null
		ResultSet resultset = null; // resultset object = null

		try {
			dbConnection = getDBConnection(); // get db connection
			statement = dbConnection.createStatement(); // create statement for db connection

			// select everything from the door lock table where the id is '1600ee15e9'
			String selectSQL = "select * from DoorLock where TagID='" + 
					TagId + "' order by timeinserted asc";
			// execute sql query
			resultset = statement.executeQuery(selectSQL);
			System.out.println(selectSQL); // print the query result
			System.out.println(">> Debug: All RFID data displayed");

			// tag id is 1600ee15e9 then it's valid user and returns true
			if (resultset.getString("TagID").equals("1600ee15e9"))
				return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} // CLose catch
		// Otherwise return false if valid user isn't openning the door
		return false;
	} // CLose try
	// Creating static update sensor table to insert the status of the door lock to the database
	public static void updateSensorTable(RFIDSensorData rfidtag){
		Connection dbConnection = null; // db connection object = null
		Statement statement = null; // statement object = null
		ResultSet resultset = null; // resultset object = null
		// Creating try to open the connection
		try {
			dbConnection = getDBConnection(); // get db connection
			statement = dbConnection.createStatement(); // create statement for db connection

			// Creating the INSERT statement from the parameters
			// set time inserted to be the current time on database server
			String updateSQL = 
					"insert into DoorLock(TagID,TagValue, SuccessFail) VALUES ('" +rfidtag.getTagID() +"','"+ rfidtag.getTagValue() + "', '"+ rfidtag.getSuccessFail() + "')";
			// Print message out onto the console
			System.out.println("DEBUG: Update: " + updateSQL);
			statement.executeUpdate(updateSQL);
			System.out.println("DEBUG: Update successful ");
		} catch (SQLException se) {
			// Problem with update, return failure message
			System.out.println(se);
			System.out.println("\nDEBUG: Update error - see error trace above for help. ");
			return;
		} // CLose catch

		// all ok,  return
		return;
	} // Close static void update sensor table
} // Close public class RFIDDao
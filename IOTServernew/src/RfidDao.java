
// Here we are importing java into the class
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// import array list for all the rfid data
import java.util.ArrayList;

import com.google.gson.Gson;

// Creating DAO class, to connect to the database
public class RfidDao {
	// Creating private static final serialVersionUID
	private static final long serialVersionUID = 1L;
	// Declaring GSON utility object
	// Connection = null and statement.
	Gson gson = new Gson();
	Connection conn = null;
	static Statement stmt;
	public static final String userid = "16038287"; // change this to be your student-id
	// Creating topic for the server
	public static final String TOPIC_SERVER = userid + "/server";
	public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
	//private MqttClient client;
	
	// Static string, connection being set to null 
	private static final String dbConnection = null;

	// getting db connection
	public static Connection getDBConnection() {
		// db connection and setting it to null
		Connection dbConnection = null;

		// Adding the mysql workbench username and assword, in order to connect to the database
		String user = "rashidd";
		String password = "rooSedef6";
		//  Usiing port 6306 instead of 3306 and connecting to the mudfoot server
		String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/rashidd";



		// trying to get connection, using the try and catch method
		try {
			Class.forName("com.mysql.jdbc.Driver");//string is altered to mysql driver
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage()); // message is being printed
		} // close, if class not found
		// using try to connect to the database
		try {
			String dbURL = "jdbc:sqlite:doorlock.sqlite"; // url which is being used to locate the database
			dbConnection = DriverManager.getConnection(url, user, password); // database connection is returned, this is located from the drive manager
			return dbConnection; // DB connection is brought back
		} catch (SQLException e) { // catch SQL
			System.out.println(e.getMessage()); // message is being printed
			// console
		} // close catch exception e
		return dbConnection; // database connection is being returned
	} // public connection closed
	
	/***
	 * Creating rfiddao method,
	 * Super is a method which is used inside a sub-class method, to call a method in the super class. 
	 * Private method of the super-class would not be called. 
	 * Only public and protected methods can be called by the super keyword.
	 * It is also used by class constructors to invoke constructors of its parent class.
	 */
	public RfidDao() {
		// Calling the super method
		super();

		/*try {
			client = new MqttClient(BROKER_URL, userid+"-server_publisher");
			// create mqtt session
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setWill(client.getTopic(userid + "/LWT"), "I'm gone :(".getBytes(), 0, false);
			client.connect(options);
		} catch (MqttException e) {
			System.out.println("mqtt");
			e.printStackTrace();
			System.exit(1);
		}*/
	} // Close public RFIDDao method

	// public array list is being created
	public static ArrayList<SensorData> getAllDoorLockStatus() throws SQLException {
		Connection dbConnection = null; // database connection = null
		Statement statement = null; // statement object 
		ResultSet resultset = null; // result set
		String query = "SELECT * FROM doorlock;"; // select query is being implemented to select all from the doorlock table
		SensorData temp = null; // sensor data temp being set to null

		ArrayList<SensorData> allRFIDData = new ArrayList<>(); // ArrayList<sensordata> gathering all rfid data

		// try and catch method is once again being executed to gain connection to the database
		try {
			dbConnection = getDBConnection(); // database connection
			statement = dbConnection.createStatement(); // statement for database connection
			System.out.println(query); // query result is shown
			// SQL query is then executed
			resultset = statement.executeQuery(query);
			// while loop
			while (resultset.next()) {
				// string is being created for all the results
				String sensorname = resultset.getString("sensorname");
				String sensorvalue = resultset.getString("sensorvalue");
				String userid = resultset.getString("userid");

				// creating temp for the RFIDSensorData
				temp = new SensorData(sensorname, sensorvalue, userid);
				// adding temporary RFIDDATA
				allRFIDData.add(temp);
			} // close while loop
		} // try method is being closed
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
	public boolean isUserValid(String sensorname) {
		Connection dbConnection = null; // db connection object = null
		Statement statement = null; // statement object = null
		ResultSet resultset = null; // resultset object = null

		try {
			dbConnection = getDBConnection(); // get db connection
			statement = dbConnection.createStatement(); // create statement for db connection

			// select everything from the door lock table where the id is '1600ee15e9'
			String sql = "SELECT * FROM doorlockValid WHERE sensorname = '"+sensorname+"'";

			// execute sql query
			resultset = statement.executeQuery(sql);
			System.out.println(sql); // print the query result
			System.out.println(">> Debug: All RFID data displayed");
		
			if (resultset.next()) {
				// tag id is 1600ee15e9 then it's valid user and returns true
				if (resultset.getString("sensorname").equals(sensorname)) {
					System.out.println(resultset.getString("sensorname"));
					return true;
				}
			}
				
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} // CLose catch
		// Otherwise return false if valid user isn't openning the door
		return false;
	} // CLose try
	// Creating static update sensor table to insert the status of the door lock to the database
	public static void updateSensorTable(SensorData oneSensor){
		Connection dbConnection = null; // db connection object = null
		Statement statement = null; // statement object = null
		ResultSet resultset = null; // resultset object = null
		// Creating try to open the connection
		try {
			dbConnection = getDBConnection(); // get db connection
			statement = dbConnection.createStatement(); // create statement for db connection

			// Creating the INSERT statement from the parameters
			// set time inserted to be the current time on database server
			// Creating the INSERT statement from the parameters
			// set time inserted to be the current time on database server
			String updateSQL = "insert into doorlock(userid, sensorname, sensorvalue, timeinserted) "
					+ "values('" + oneSensor.getUserid() + "','" + oneSensor.getSensorname() + "','"
					+ oneSensor.getSensorvalue() + "',"
					+ "now());";

			// Print message out onto the console
			System.out.println("DEBUG: Update: " + updateSQL);
			statement.executeUpdate(updateSQL);
			//publish_to_Android(oneSensor);
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
	
	/**
	 *  Creating private string to  retrieveSensorData
	 * Declaring a string to hold the sql select, set its value to a select
	 * statement that will retrieve all values from the doorlock table
	 *  where the sensorname equals the parameter supplied "where sensorname='" + sensorname + "'".
	 *  Then adding the qualifier " order by timeinserted asc" to the sql
	 * statement to ensure that the data is returned in ascending order
	 *  (i.e. newest last).
	 * http://localhost:8080/IOTServer/RFIDDao?getdata&sensorname=4d004a5587
	 * http://localhost:8080/IOTServer/RFIDDao?getdata&sensorname=1600ee15e9
	 * @param sensorname
	 * @return
	 */
	
	// Creating private string to retrieve sensor data
	private String retrieveSensorData(String sensorname) {
		Connection dbConnection = null; // db connection object = null
		Statement statement = null; // statement object = null
		ResultSet resultset = null; // resultset object = null
		Gson gson = new Gson();

		// Creating string for the sql statement to select everything from the door lock table where the sensoorname equal to sensorname and ordering the time inserted in ascending order
		String selectSQL = "select * from rashidd.doorlock where sensorname='" + 
				sensorname + "' order by timeinserted asc";
//		String selectSQL = "select * from rashidd.doorlock where sensorname = \"" + sensorname + "\"";
		ResultSet rs; // Return the result

		// Declaring ArrayList of rfid tags to hold the results
		ArrayList<SensorData> allRFIDTags = new ArrayList<SensorData>();

		try {	        
			// creating a result set of selected values
			rs = statement.executeQuery(selectSQL);

			// Declaring a RFIDSensorData object to hold individual values, 
			// initialised to defaults
			SensorData RFIDTag = new SensorData("unknown", "unknown"); // fill in statement
			// Iterating over the result set
			while (rs.next()) {
				// Getting the result of the tag id, if the tag was success or fail and the time
				RFIDTag.setSensorname(rs.getString("sensorname"));
				RFIDTag.setSensorvalue(rs.getString("sensorvalue"));
				RFIDTag.setUserid(rs.getString("userid"));
				RFIDTag.setSensordate(rs.getString("Timeinserted"));
				
				
				// add this sensor to ArrayList of Sensors
				allRFIDTags.add(RFIDTag); // Add the rfid sensor data to the database
				// Debug message print this sensor to console
				System.out.println(RFIDTag.toString());
			} // Close while loop for result.next
		} catch (SQLException ex) {
			// Print message out onto the console
			System.out.println("Error in SQL " + ex.getMessage());
		} // Close catch
		// Converting allRFIDTags to json array and sending it back to user
		String allRFIDTagsJson = gson.toJson(allRFIDTags);

		// Returning this String from method
		return allRFIDTagsJson;
	} // CLose private string retrieve sensor data
	
	// Creating public void publish to android to publish the sensor data to the application, in order to view the data on the appication
	//	public void publish_to_Android(SensorData a)throws Exception
	//	{
	//
	//		Gson g = new Gson();
	//		final MqttTopic motorTopic = client.getTopic(TOPIC_SERVER);
	//		motorTopic.publish(new MqttMessage(g.toJson(a).getBytes()));
	//
	//
	//	}
} // Close public class RFIDDao

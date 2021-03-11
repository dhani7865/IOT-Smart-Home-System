
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;


/**
 * Servlet implementation class sensorToDB
 * In this class i have created the insert statement which would insert the data to the database
 * I have also create good use of commenting and some form of validation.
 * The validation which has been used is; if valid user has been used "16038287" and valid tag it will open the door.
 */
// Creating web servlet for SensorServerDB and creating public class which extends HttpServlet
// http://localhost:8080/IOTServer/SensorServerDB?getdata&sensorname=Room105
@WebServlet("/SensorServerDB")
public class SensorServerDB extends HttpServlet {
	// Creating private static final serialVersionUID
	private static final long serialVersionUID = 1L;
	// Declaring GSON utility object
	// Connection = null and statement.
	Gson gson = new Gson();
	Connection conn = null;
	Statement stmt;
	public static final String userid = "16038287"; // change this to be your student-id
	// Creating topic for the server
	public static final String TOPIC_SERVER = userid + "/server";
	public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
	//private MqttClient client;



	// Creating public void init method for ServletConfig which throws ServletException
	public void init(ServletConfig config) throws ServletException {
		// init method is run once at the start of the servlet loading
		// This will load the driver and establish a connection
		super.init(config);

		// Adding the mysql workbench username and assword, in order to connect to the database
		String user = "rashidd";
		String password = "rooSedef6";
		//  Usiing port 6306 instead of 3306 and connecting to the mudfoot server
		String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/rashidd";



		// Loading the database driver
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			System.out.println(e);
		} // close catch exception e

		// Creating try to get a connection with the user/pass
		try {
			conn = DriverManager.getConnection(url, user, password);
			// Print messages out onto the console
			System.out.println("Sensor to DB  server is up and running\n");
			System.out.println("Upload sensor data with http://localhost:8080/IOTServer/SensorServerDB?sensorname=xxx&sensorvalue=nnn");
			System.out.println("View last sensor reading at  http://localhost:8080/IOTServer/SensorServerDB?getdata=true\n\n");

			// System.out.println("DEBUG: Connection to database successful.");
			stmt = conn.createStatement();
		} catch (SQLException se) {
			se.printStackTrace();
			System.out.println(se);
			System.out.println("\nDid you alter the lines to set user/password in the sensor server code?");
		} // Close catch sql exception
	} // init()

	// Creating destroy method
	public void destroy() {
		try {
			conn.close();
		} catch (SQLException se) {
			System.out.println(se);
		} // Close catch sql exception se
	} // destroy()

	//Creating SensorServerDB method
	public SensorServerDB() {
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
			System.out.println("bababaabbaba");
			e.printStackTrace();
			System.exit(1);
		}*/
	} // Close public SensorServerDB method

	// Creating do get method 
	// doGet is a method which supports the servlet HTTP GET requests 
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		// Declare a SensorData object to hold the incoming data
		SensorData oneSensor = new SensorData("unknown", "unknown");

		// Check to see whether the client is requesting data or sending it
		String getdata = request.getParameter("getdata");

		// if no getdata parameter, client is sending data
		if (getdata == null) {
			// getdata is null, therefore it is receiving data
			// Extracting the parameter data holding the sensordata
			String sensorJsonString = request.getParameter("sensordata");

			// Problem if sensordata parameter not sent, or is invalid json
			if (sensorJsonString != null) {
				// Converting the json string to an object of type SensorData
				// Also, calling the sensordata class
				oneSensor = gson.fromJson(sensorJsonString, SensorData.class);

				// Update sensor values and send back response
				PrintWriter out = response.getWriter();
				// Calling the updateSensorTable method
				try {
					updateSensorTable(oneSensor);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				out.close(); // close out
			} // endif sensorJsonString not null
		} // end if getdata is null
		else { // Otherwise retrieve and return data in json format
			// retrive data sensor by sensorname parameter
			PrintWriter out = response.getWriter();
			out.println(retrieveSensorData(request.getParameter("sensorname")));
			out.close();
		} // CLose else
	} // CLose protected void doget method

	// Creating do post method
	//doPost is used for HTTP POST requests 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Post is same as Get, so pass on parameters and do same
		doGet(request, response);
	} // CLose protected void dopost

	// Creating boolean to check if a valid user is trying to open the door, using specific user id = 16038287
	public boolean isUserValid(String userid) {
		try {
			// select everything from the door lock table where the id is '1600ee15e9'
			String sql = "SELECT * FROM doorlock WHERE sensorname = '1600ee15e9'";

			// execute sql query
			stmt.executeUpdate(sql);
			System.out.println(sql); // print the query result
			System.out.println(">> Debug: All RFID data displayed");

			ResultSet resultset = null;

			// If user id is 16038287 then it's valid user and returns true
			if (resultset.getString("sensorname").equals("1600ee15e9"))
				return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} // CLose catch
		// Otherwise return false if invalid user is trying to open the door
		return false;
	} // CLose buplic booelan is user valid method
	

	// Creating static update sensor table to insert the status of the door lock to the database
	private void updateSensorTable(SensorData oneSensor) throws Exception {
		try {
			// Creating the INSERT statement from the parameters
			// set time inserted to be the current time on database server
			String updateSQL = "insert into doorlock(userid, sensorname, sensorvalue, timeinserted) "
					+ "values('" + oneSensor.getUserid() + "','" + oneSensor.getSensorname() + "','"
					+ oneSensor.getSensorvalue() + "',"
					+ "now());";
			// Print out debug message to the console
			System.out.println("DEBUG: Update: " + updateSQL);
			// Execute query
			stmt.executeUpdate(updateSQL);
			//publish_to_Android(oneSensor);
			// Print debug message to the console
			System.out.println("DEBUG: Update successful ");
		} catch (SQLException se) {
			// Problem with update, return failure message
			System.out.println(se);
			System.out.println("\nDEBUG: Update error - see error trace above for help. ");
			return;
		} // CLose catch sql exception

		// all ok, return
		return;
	} // Close private void update sensor table method
	
	// Retrieve all data
	// Creating private string to  retrieveSensorData
	private String retrieveSensorData(String sensorname) {
		// Declaring a string to hold the sql select, set its value to a select
		// statement that will retrieve all values from the doorlock table
		// where the sensorname equals the parameter supplied (remember single
		// quotes around the compared value
		// ..where sensorname='" + sensorname + "'"
		// Adding the qualifier " order by timeinserted asc" to the sql
		// statement to ensure the data is returned in date ascending order
		// (i.e. newest last)

		//SELECT from mudfoot server where sensorname = the sensorname inserted
		String selectSQL = "select * from rashidd.doorlock where sensorname = \"" + sensorname + "\"";
		//String selectSQL = "select * from doorlock";

		ResultSet rs = null; // creating variable for result set
		// Creating array list of sensor data
		ArrayList<SensorData> s = new ArrayList<>();


		// Declaring a Result set called rs
		// rs = ((java.sql.Statement) statement).executeQuery(selectSQL);//RESULT FROM
		// QUERY

		// Creating try to declare ArrayList of SensorData called allSensors to hold results,
		// and initialise it
		try {
			// iterate over the result set created by the select for each of
			// the columns in the table, putting them into oneSensor with
			// the corresponding set method for sensorname, sensorvalue,
			// userid, timeinserted, using either
			// getString or getDouble
			// execute query
			rs = conn.createStatement().executeQuery(selectSQL);
			// Creating while resultset
			while (rs.next()) {
				// String sensorname, String sensorvalue, String userid, String sensordate
				SensorData oneSensor = new SensorData(rs.getString("sensorname"), rs.getString("sensorvalue"),
						rs.getString("userid"), rs.getString("timeinserted"));
				s.add(oneSensor); // add the data
			} // CLose while loop for rs.next
		} catch (SQLException ex) {
			System.out.println("Error in SQL " + ex.getMessage());
		} // Close catch sql exception e
		// Return the responce in json format
		return new Gson().toJson(s);
	} // Close private string retrieve sensor data

	// Creating private string to  retrieveSensorData
	private String retrieveSensorDataByTag(String sensorname) {
		// Declaring a string to hold the sql select, set its value to a select
		// statement that will retrieve all values from the doorlock table
		// where the sensorname equals the parameter supplied (remember single
		// quotes around the compared value
		// ..where sensorname='" + sensorname + "'"
		// Adding the qualifier " order by timeinserted asc" to the sql
		// statement to ensure the data is returned in date ascending order
		// (i.e. newest last)

		//SELECT from mudfoot server where sensorname = the sensorname inserted
		String selectSQL = "select * from rashidd.doorlock where sensorname = \"" + sensorname + "\"";
		//String selectSQL = "select * from doorlock";

		ResultSet rs = null; // creating variable for result set
		// Creating array list of sensor data
		ArrayList<SensorData> s = new ArrayList<>();


		// Declaring a Result set called rs
		// rs = ((java.sql.Statement) statement).executeQuery(selectSQL);//RESULT FROM
		// QUERY

		// Creating try to declare ArrayList of SensorData called allSensors to hold results,
		// and initialise it
		try {
			// iterate over the result set created by the select for each of
			// the columns in the table, putting them into oneSensor with
			// the corresponding set method for sensorname, sensorvalue,
			// userid, timeinserted, using either
			// getString or getDouble
			// execute query
			rs = conn.createStatement().executeQuery(selectSQL);
			// Creating while resultset
			while (rs.next()) {
				// String sensorname, String sensorvalue, String userid, String sensordate
				SensorData oneSensor = new SensorData(rs.getString("sensorname"), rs.getString("sensorvalue"),
						rs.getString("userid"), rs.getString("timeinserted"));
				s.add(oneSensor); // add the data
			} // CLose while loop for rs.next
		} catch (SQLException ex) {
			System.out.println("Error in SQL " + ex.getMessage());
		} // Close catch sql exception e
		// Return the responce in json format
		return new Gson().toJson(s);
	} // Close private string retrieve sensor data

//	public void publish_to_Android(SensorData a)throws Exception  //////////////////////////////
//	{
//
//		Gson g = new Gson();
//		final MqttTopic motorTopic = client.getTopic(TOPIC_SERVER);
//		motorTopic.publish(new MqttMessage(g.toJson(a).getBytes()));
//
//
//	} /////////////////////////////////////////////////
} // Close public class sensor server db
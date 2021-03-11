import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Model.RFIDDao;
import Model.RFIDSensorData;


/**
 * This class would convert different rfid tag details to the database in json format. 
 * This class would create connection to the database.
 *  It will then convert the data to json format.
 *  It will then retrieve data into the database using insert command.
 * @author dhanyaal
 */

//Web servlet for the SensorServerJson file
@WebServlet("/SensorServerJson")

// Creating public class sensor server json which extends the http ervlet
public class SensorServerJson extends HttpServlet {
	// Collects or returns data for TagID, Success/Fail parameters
	private static final long serialVersionUID = 1L;

	// Local variables holding last values stored for each parameter, in the RFIDSensorData class
	RFIDSensorData lastRFIDTag = new RFIDSensorData("unknown", "unknown");
	// Declaring gson object/variable
	Gson gson = new Gson();
	// Creating public Sensor Server Json method
	public SensorServerJson() {
		super();
	} // CLose public sensor server json method

	// Creating public void init method, which has paramters of ServletConfig confif, which throws ServletException 
	public void init(ServletConfig config) throws ServletException {
		// print message out onto the console
		System.out.println("Sensor server is up and running\n");	
		System.out.println("Upload sensor data with http://localhost:8080/PhidgetServer/SensorServerJsonSolution?sensordata=someSensorJson");
		System.out.println("View last sensor reading at  http://localhost:8080/PhidgetServer/SensorServerJsonSolution?getdata=true \n\n");		  
	} // Close publi void init method
	// Creating public do get method, which will request http servlet and respince
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
		// Creating new RFIDDao and calling the RFIDDao class
		// it asks the dao and then prints out the status of the door lock

		// creating array list for all rfid sensor data
		ArrayList<RFIDSensorData> allRFIDData = new ArrayList<>(); // creating array
		new RFIDDao(); // creating new RFIDDao
		new Gson(); // creating new gson
		// creating try and catch to get all RFIDData and catching sql exception which prints out sql exception
		try {
			allRFIDData = RFIDDao.getAllDoorLockStatus(); // get all rfud door lock statuses' 
		} // close try
		// creating catch exception e
		catch (SQLException e) {
			System.out.println("SQL Exception: " + e.getMessage()); // print message
		} // close catch sql exception e
		System.out.println(allRFIDData); // print all rfid data

		// Get all getAllDoorLockStatus and store them in array
		ArrayList<RFIDSensorData> RFIDSensorData = null;
		try {
			RFIDSensorData = RFIDDao.getAllDoorLockStatus();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// creating for loop for RFIDSensorData
		for (RFIDSensorData TagID : RFIDSensorData) {
			System.out.println(TagID.toString()); // print rfid door lock status
		} // close for loop
		/// print all rfid data 
		System.out.println("All data displayed successfully");

		// Creating variable for gson
		// Declaring gson object
		// Json is data format which would express data objects consisting of attribute value pairs. 
		Gson gson = new Gson();
		// Creating string for the json bject and then converting it to json format
		String allRFIDDataJson = gson.toJson(RFIDSensorData);
		// Print all rfid data in json format
		System.out.println(allRFIDDataJson);
		System.out.println("RFID data successfully in json format!"); // printing success message
		

		Object getdata = null;
		// getdata is null, therefore it is receiving data
		// Extracting the parameter data holding the RFIDSensorData
		String RFIDJsonString = request.getParameter("RFIDSensorData");

		// Problem if RFIDSensorData parameter not sent, or is invalid json
		if (RFIDJsonString != null) {
			// Convert the json string to an object of type RFIDSensorData and calling the RFIDSensorData class
			lastRFIDTag = gson.fromJson(RFIDJsonString, RFIDSensorData.class);
			// Saving the most recent Sensor object for later retrieval
			lastRFIDTag = lastRFIDTag;

			// Update RFIDTag values and send back response
			PrintWriter out1 = response.getWriter();
			out1.println(updateSensorValues(lastRFIDTag));
			out1.close();
		} // Endif statement for RFIDJsonString not null
	} // CLose doGet method

	// Creating private string to update the rfid values
	private String updateSensorValues(RFIDSensorData RFIDTag){
		// all ok, update last known values and return
		lastRFIDTag = RFIDTag;
		// Print message out onto the console and message
		System.out.println("DEBUG : Last rfid tag was " + RFIDTag.getTagID() + ", with value "+RFIDTag.getSuccessFail());
		return "RFID tag updated.";
	} // Close private string update sensor values	

	// Creating private void send json string method which throws HttpServletResponse
	private void sendJSONString(HttpServletResponse response) throws IOException {
		// Setting content type as json and converting the data to json format
		response.setContentType("application/json");  
		String json = gson.toJson(lastRFIDTag);
		// Printing the writer and getting the writer
		PrintWriter out = response.getWriter();
		// Print debug message out onto the console and calling the toString method
		System.out.println("DEBUG: sensorServer JSON: "+lastRFIDTag.toString());

		// Print data in json format
		out.println(json);
		out.close();
	} // Close send json string method


	/**
	 * Calling the doGet and post the responce
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	} // Close protected void doPost
} // CLose SensorServerJson class
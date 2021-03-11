

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Model.RFIDDao;
import Model.RFIDSensorData;

import java.util.*;
import java.io.*;
import java.sql.*;


/**
 * Servlet implementation class sensorToDB (database), which is linked to the SensorToServerDBTester
 * This class would create connection to the database.
 * It will then retrieve data into the database using insert command.
 */
// Web servlet for the SensorServerDB file
@WebServlet("/SensorServerDB")
// Creating public class sensor server db which extends the http ervlet
public class SensorServerDB extends HttpServlet {
	// Creating private static final long serialVersionUID
	private static final long serialVersionUID = 1L;
	// Declaring gson object/variable
	Gson gson = new Gson();
	// Declaring connection object/variable, equal to null
	Connection conn = null;
	// Declaring Statement variable/object
	Statement stmt;
	// Creating public void init method, which has paramters of ServletConfig confif, which throws ServletException 
	public void init(ServletConfig config) throws ServletException {
		// init method is run once at the start of the servlet loading
		// This will load the driver and establish a connection
		super.init(config);
		// Declaring the username and password for mySql and creating connection to the database, in order to retrieve the data.
		String user = "rashidd";
		String password = "rooSedef6";
		// 6306 port is used in order to access the mudfoot server
		String url = "jdbc:mysql://mudfoot.doc.stu.mmu.ac.uk:6306/"+user;

		// Loading the database driver
		try {  Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception e) {
			System.out.println(e);
		} // Close catch exception e

		// Getting a connection with the user/pass
		// ALso, printing message out onto the console
		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Sensor to DB  server is up and running\n");	
			System.out.println("Upload sensor data with http://localhost:8080/IOTServer/SensorServerDB?RFIDSensorData=some_sensor_data_in_json_format");
			System.out.println("View last sensor reading at  http://localhost:8080/IOTServer/SensorServerDB?getdata=true\n\n");		  
			// Print message out onto the console
			System.out.println("DEBUG: Connection to database successful.");
			stmt = conn.createStatement();
		} catch (SQLException se) {
			System.out.println(se);
			System.out.println("\nDid you alter the lines to set user/password in the sensor server code?");
		} // CLose catch
	} // Close init() method
	// Creating public void destroy method
	public void destroy() {
		try { conn.close(); } catch (SQLException se) {
			System.out.println(se);
		} // Close catch sql exception
	} // Close destroy() method

	// Creating public sensor server db method
	/** 
	 * Super is used inside a sub-class method, to call a method defined in the super class.
	 * Private methods of the super-class cannot be called. 
	 * Only public and protected methods can be called by the super keyword.
	 */
	public SensorServerDB() {
		super();
	} // Close SensorServerDB

	// Creating protected void ToGet, to create database connection, which throws ServletException and IOException
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
	} // CLose protected void to get

	// Creating protected void dopost, to post the data onto the sever
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Post is same as Get, so pass on parameters
		doGet(request, response);
	} // CLose protected void doPost
} // CLose public class sensor server db
package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Model.RFIDDao;
import Model.RFIDSensorData;

/**
 * Servlet implementation class Controller
 */
@WebServlet("/Control")
// Creating public class for the controller which extends http servlet
public class Control extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 * This controller class will display everything on the browser.
	 */
	// Creating public method for controller
	public Control() {
		super();
	} // Close controller method

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	// Creating protected void doGet to print out the list of films on tbe browser
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
		out.println(allRFIDData); // print all rfid data

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
			out.println(TagID.toString()); // print rfid door lock status
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
		
		
	} // CLose protected void doGet method

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	} // Close protected void doPost
} // Close public class conroller

package Controller;

//import java.sql to this class
import java.sql.ResultSet;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
//import array list for all the rfid data
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.gson.Gson;
import com.phidget22.PhidgetException;
import com.phidget22.RFID;
import com.phidget22.RFIDTagEvent;
import com.phidget22.RFIDTagListener;
import com.phidget22.RFIDTagLostEvent;
import com.phidget22.RFIDTagLostListener;

import Model.RFIDDao;
import Model.RFIDSensorData;
import Model.PhidgetPublisher;

public class RFIDDoorOpener  {
	// Creating variable for hte phidgetPublisher and calling the method
	PhidgetPublisher publisher = new PhidgetPublisher(); // source in PhidgetPublisher.java

	RFID rfid = new RFID(); // Avarable for the rfid


	// Creating variables for the connection and statement
	Connection conn = null;
	Statement stmt;

	// Creating main method and calling the rfid door opener method
	public static void main(String[] args) throws PhidgetException, SQLException {
		new RFIDDoorOpener();


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
		ArrayList<RFIDSensorData> RFIDSensorData = RFIDDao.getAllDoorLockStatus();

		// creating for loop for RFIDSensorData
		for (RFIDSensorData TagID : RFIDSensorData) {
			System.out.println(TagID.toString()); // print rfid door lock status
		} // close for loop
		/// print all rfid data 
		System.out.println("All data displayed successfully");

	} // close main method

	// Creating method for the rfid door opener which throws phidget exception
	public RFIDDoorOpener() throws PhidgetException {
		try {
			// Making the RFID Phidget able to detect loss or gain of an rfid card
			rfid.addTagListener(new RFIDTagListener() {
				// Creating public void for on tag for the rfid tag event
				public void onTag(RFIDTagEvent e) {
					// Creating string for the tag string
					String tagStr = e.getTag();
					try {
						RFIDDao.getAllDoorLockStatus();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// Creating try to check if its thecorrect tag
					try {
						// If thetag is "1600ee15e9", publish the message
						if (tagStr.equals("1600ee15e9") ) { 
							publisher.publishRfid(tagStr);
							publisher.publishMotor(tagStr);
						} // close if statement to check the tag string

						// If thetag is "1600ee15e9", move the motor
						if (tagStr.equals("1600ee15e9") ) { 
							publisher.publishMotor(tagStr);
							checkTag(tagStr);
						} // close if statement to check the tag string
						// Otherwise fail and print out invalid rfid tag to the console.
						else {
							checkTag(tagStr);
						}
					} catch (MqttException mqtte) {
						mqtte.printStackTrace();
					} // Close catch mqtt exception
				} // Close public on tag
			}); // Close rfid add tag listener

			// Creating rfid add tag lost listener
			rfid.addTagLostListener(new RFIDTagLostListener() {
				// Creating public void for on tag, which has parameter RFIDTagLostEvent e
				public void onTagLost(RFIDTagLostEvent e) {
					// Creating string for the tag
					String tagStr = e.getTag();
					// If the tag has been lost, print message to the console
					System.out.println("Tag lost: " + tagStr);
				} // Close public void on tag listener
			}); // Close rfid addTagLostListener

			// Open and starting to detect rfid cards
			rfid.open(10000);  // wait 5 seconds for device to respond

			// Display info on currently connected devices
			System.out.println("Device Name " + rfid.getDeviceName());
			System.out.println("Serial Number " + rfid.getDeviceSerialNumber());
			System.out.println("Device Version " + rfid.getDeviceVersion());

			// Set the rfid AntennaEnabled to true
			rfid.setAntennaEnabled(true);

			// Print message to the console
			System.out.println("\n\nGathering data for 15 seconds\n\n");
			// try to put the thread to sleep
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// Otherwise close the rfid, and print message to the console
			rfid.close();
			System.out.println("\nClosed RFID");


			// Attach to the sensor and start reading
			try {      	                                
				System.out.println("\n\nGathering data for 15 seconds\n\n");
				Thread.sleep(150000);
			} catch (Exception ex) {
				ex.printStackTrace();
			} // Close catch Exception e
		} finally { // Cewatting finally
			// Close the sensor
			// Print message to the console
			System.out.println("Closed and exiting...");
		} // Close finally
	} // Close RFIDDoorOpener method
	// checking the tag if its the correct tag or not
	// If its wrong tag, print message to the console saying UNKOWN TAG!
	private boolean checkTag(String tagStr) {
		if (tagStr.equals("1600ee15e9")) {
			// Print message, if it's the correct tag
			System.out.println("Tag read OPEN DOOR: " + tagStr);
			
			System.out.println("VALID TAG: OPEN DOOR!");
			Object he;
			// Calling the RFID sensor data class and inserting the status of the door lock to the database. 
			RFIDSensorData r= new RFIDSensorData("1600ee15e9", "Door Openned", "Success");
			// inserting rfid data to the database and printing message out onto the console
			RFIDDao.updateSensorTable(r);
			System.out.println("Success rrfid data have been added to the database!"); // printing success message
			// Creating variable for gson
			// Declaring gson object
			// Json is data format which would express data objects consisting of attribute value pairs. 
			Gson gson = new Gson();
			// Creating string for the json bject and then converting it to json format
			String allRFIDDataJson = gson.toJson(r);
			// Print all rfid data in json format
			System.out.println(allRFIDDataJson);
			System.out.println("RFID data successfully in json format!"); // printing success message
			
			return true;
			
			
		} // CLose if statement
		// Otherwise, if the tag id is not equal to 1600ee15e9
		else if(tagStr!="1600ee15e9") {
			System.out.println("Tag read: DOOR NOT OPEN!: " + tagStr);

			System.out.println("INVALID TAG!");
			System.out.println("Door not openned!");
			
			Object he;
			// Calling the RFID sensor data class and inserting the status of the door lock to the database. 
			RFIDSensorData r= new RFIDSensorData("4d004a5587", "Door Not Openned", "fail");
			// inserting rfid data to the database and printing message out onto the console
			RFIDDao.updateSensorTable(r);
			System.out.println("Success rrfid data have been added to the database!"); // printing success message
			// Creating variable for gson
			// Declaring gson object
			// Json is data format which would express data objects consisting of attribute value pairs. 
			Gson gson = new Gson();
			// Creating string for the json bject and then converting it to json format
			String allRFIDDataJson = gson.toJson(r);
			// Print all rfid data in json format
			System.out.println(allRFIDDataJson);
			System.out.println("RFID data successfully in json format!"); // printing success message
			
		} // CLose else
		return false;
		
	} // Close private void check tag method
	
} // Close public class RFIDDoorOpener
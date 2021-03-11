
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.phidget22.*;

/**
 * This class would send the data to the server.
 * @author Dhanyaal
 */
public class SensorToServer  {
	// CAlling the sensor data class and setting the sensor data
	SensorData oneSensor = new SensorData("Room105", "Open", "16038287" );

	// Declaring GSON utility object
	Gson  gson;

	// Creating oneSensorJson
	String oneSensorJson;
	// Setting the voltage ratio input for the slider and light
	VoltageRatioInput slider = new VoltageRatioInput();
	VoltageRatioInput light = new VoltageRatioInput();
	// setting the last sensor value
	int lastSensorValue = 0;

	// The address of the server which will receive sensor data
	public static String sensorServerURL = "http://localhost:8080/IOTServer new/SensorServer";
	// Creating main method, which calls the sensortoserver method
	public static void main(String[] args) throws PhidgetException {
		new SensorToServer();
	} // Close main method

	// Creating public sensor to server method which would send the server to the database
	public SensorToServer() throws PhidgetException {
		// This is the id of the PhidgetInterfaceKit (on back of device)
		// slider.setDeviceSerialNumber(319864);
		// This is the channel the slider is connected to on the interface kit
		light.setChannel(6);
		light.open(1000);

		// Creating add voltage ratio change listener for the light
		light.addVoltageRatioChangeListener(new VoltageRatioInputVoltageRatioChangeListener() {
			// Creating public void on voltage ratio change
			public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent e) {
				double sensorReading = e.getVoltageRatio();
				//System.out.println("Slider Voltage Ratio Changed: "+ sensorReading);

				// scale the sensor value from 0-1 to 0-1000
				int scaledSensorReading = (int) (1000 * sensorReading);

				// send value to server if changed since last reading
				if (scaledSensorReading != lastSensorValue ) {
					// print message to the console
					System.out.println("Sending new light value : " + scaledSensorReading);
					// Change sensor value to String and send to server
					String strSensorReading = "" + scaledSensorReading;
					sendToServer(strSensorReading);
					lastSensorValue = scaledSensorReading;
				} // CLose if stateemnt for if scaled sensor reading not equal to last sensor value
			} // Close public void on voltage ratio change
		}); // Close addVoltageRatioChangeListener

		// This is the id of the PhidgetInterfaceKit (on back of device)
		// slider.setDeviceSerialNumber(319864);
		// This is the channel the slider is connected to on the interface ki
		slider.setChannel(7);
		slider.open(5000);
		slider.addVoltageRatioChangeListener(new VoltageRatioInputVoltageRatioChangeListener() {
			public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent e) {
				double sensorReading = e.getVoltageRatio();
				//System.out.println("Slider Voltage Ratio Changed: "+ sensorReading);

				// scale the sensor value from 0-1 to 0-1000
				int scaledSensorReading = (int) (1000 * sensorReading);
				// send value to server if changed since last reading
				if (scaledSensorReading != lastSensorValue ) {
					// Print message to the console
					System.out.println("Sending new sensor value : " + scaledSensorReading);
					// Change sensor value to String and sending it to server
					String strSensorReading = "" + scaledSensorReading;
					// Creating strSensorReading to convert the sensor to json format
					strSensorReading = new Gson().toJson(oneSensor); 
					// send the sensor to the server
					sendToServer(strSensorReading);
					// last sensor value equal to scaled sensor reading
					lastSensorValue = scaledSensorReading;
				} // CLose if stateemnt for if scaled sensor reading not equal to last sensor value
			} // Close public void on voltage ratio change
		}); // Close addVoltageRatioChangeListener

		// attach to the sensor and start reading
		// try {                      
		System.out.println("\n\nGathering data for 15 seconds\n\n");
		while (true); 
	} // CLose public csensor to server class

	// DOOR LOCK FUNCTION CODE 
	// Creating public string send to server to send the satus of the door lock to the server
	public String sendToServer(String sensorValue){
		// Creating variables for url, http url connection and buffered reader
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		// Creating url for the sensor server and setting the sensor name as lock
		String fullURL = sensorServerURL + "?sensorname=Room105&sensorvalue="+sensorValue;
		System.out.println("Sending data to: "+fullURL);  // DEBUG confirmation message
		// Setting string variables for line and setting the result to empty string
		String line;
		String result = "";
		// Creating try for the url connection
		try {
			// Creating new url
			url = new URL(fullURL);
			// Creating http url connection to open the connection
			conn = (HttpURLConnection) url.openConnection();
			// Setting the request mehtod as get
			// Get is used to request data from a specified resource. 			
			conn.setRequestMethod("GET");
			// Creating buffered reader
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			// Requesting response from server to enable URL to be opened
			while ((line = rd.readLine()) != null) {
				result += line;
			} // CLose catch Exception e
			// Close buffered writer
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		} // Close catch excepion e
		// Return the reuslt
		return result;    	
	} // CLose public string send to server for the door lock



	//SUCCESSFUL SLIDER FUNCTION CODE START 
	// Creating public string send to server to send the slider informatiom to the server
	public String sendToServer1(String sensorValue){
		// Creating variables for url, http url connection and buffered reader
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;

		// Creating url for the sensor server and setting the sensor name as slider
		String fullURL = sensorServerURL + "?sensorname=slider&sensorvalue="+sensorValue;
		System.out.println("Sending data to: "+fullURL);  // DEBUG confirmation message
		// Setting string variables for line and setting the result to empty string
		String line;
		String result = "";

		// Creating try for the url connection
		try {
			// Creating new url
			url = new URL(fullURL);
			// Creating http url connection to open the connection
			conn = (HttpURLConnection) url.openConnection();
			// Setting the request mehtod as get
			// Get is used to request data from a specified resource.
			conn.setRequestMethod("GET");
			// Creating buffered reader
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// Request response from server to enable URL to be opened
			while ((line = rd.readLine()) != null) {
				result += line;
			} // CLose catch Exception e
			// Close buffered writer
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		} // CLose catch exception e
		// Otherwise, return the result
		return result;    	
	} // CLose public string send to server for the slider


	//LIGHT SENSOR FUNCTION 
	// Creating public string send to server to send the light informatiom to the server
	public String sendToServer2(String sensorValue) {
		// Creating variables for url, http url connection and buffered reader
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		// Creating url for the sensor server and setting the sensor name as light
		String fullURL = sensorServerURL + "?sensorname=Light&sensorvalue="+sensorValue;
		System.out.println("Sending data to: "+fullURL);  // DEBUG confirmation message
		// Setting string variables for line and setting the result to empty string
		String line;
		String result = "";

		// Creating try for the url connection
		try {
			// Creating new url
			url = new URL(fullURL);
			// Creating http url connection to open the connection
			conn = (HttpURLConnection) url.openConnection();
			// Setting the request mehtod as get
			// Get is used to request data from a specified resource.			
			conn.setRequestMethod("GET");
			// Creating buffered reader
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// Request response from server to enable URL to be opened
			while ((line = rd.readLine()) != null) {
				result += line;
			} // CLose catch Exception e
			// Close buffered writer
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		} // Close exception e
		// Otherwise, return the result
		return result;    	
	} // CLose public string send to server for the light

	//LIGHT SENSOR FUNCTION CODE
	//Creatingprivate void pause for the light, with int parameter for the seconds
	private void pause(int secs){
		// Creating try to put the threadto sleep
		try {
			Thread.sleep(secs*1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} // Close catch InterruptedException
	} // Close private void pause method
} // Close public class sensor to server
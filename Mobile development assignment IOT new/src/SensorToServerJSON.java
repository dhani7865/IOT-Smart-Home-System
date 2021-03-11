
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;
import com.phidget22.*;

/** 
 * This class would send the data to the server in json format
 * 
 * @author dhanyaal
 *
 */

// Creating public class SensorToServerJSON
public class SensorToServerJSON  {
	// Creating VoltageRatioInput for the slider
	VoltageRatioInput slider = new VoltageRatioInput();

	// Creating integer for the last sensor value
	int lastSensorValue = 0;

	// Declare a default sensor object (no location, name/value set later)
	SensorData oneSensor = 
			new SensorData("Room105", "Open", "16038287");

	// Declare GSON utility object
	Gson gson = new Gson();
	// Declare String to hold json representation of sensor object data
	String oneSensorJson = new String();
	// address of server which will receive sensor data
	// Altered to point to server to deal with incoming json parameters
	public static String sensorServerURL = "http://localhost:8080/IOTServernew/SensorServerJsonSolution";

	// Creating main method and calling the SensorToServerJSON method
	public static void main(String[] args) throws PhidgetException {
		new SensorToServerJSON();
	} // Close main method

	// Creating public SensorToServerJSON method which throws PhidgetException
	public SensorToServerJSON() throws PhidgetException {	
		// This is the id of your PhidgetInterfaceKit (on back of device)
		// slider.setDeviceSerialNumber(319864);
		// This is the channel your slider is connected to on the interface kit
		slider.setChannel(0);
		slider.open(5000);

		// Creating addVoltageRatioChangeListener for the slider
		slider.addVoltageRatioChangeListener(new VoltageRatioInputVoltageRatioChangeListener() {
			public void onVoltageRatioChange(VoltageRatioInputVoltageRatioChangeEvent e) {
				double sensorReading = e.getVoltageRatio();
				//System.out.println("Slider Voltage Ratio Changed: "+ sensorReading);

				// scale the sensor value from 0-1 to 0-1000
				int scaledSensorReading = (int) (1000 * sensorReading);
				// send value to server if changed since last reading
				if (scaledSensorReading != lastSensorValue ) {
					System.out.println("Sending new sensor value : " + scaledSensorReading);
					// Change sensor value to String and send to server
					String strSensorReading = "" + scaledSensorReading;

					// Place sensor values inside sensor object
					oneSensor.setSensorname("slider");
					oneSensor.setSensorvalue(strSensorReading);
					oneSensorJson = gson.toJson(oneSensor);

					sendToServer(oneSensorJson);
					lastSensorValue = scaledSensorReading;
				} // Close if stateement
			} // Close public void on voltage ratio change
		}); // Close slider add voltage ratio change

		// attach to the sensor and start reading
		try {      
			// Print message to the console
			System.out.println("\n\nGathering data for 15 seconds\n\n");
			pause(15);
			// CLose slider
			slider.close();
			// Print message to the console
			System.out.println("\nClosed slider Voltage Ratio Input");

		} catch (PhidgetException ex) {
			System.out.println(ex.getDescription());
		} // CLose catch PhidgetException ex
	} // CLose public sensor to server json method

	// Creating public string to the send the json data to the server
	public String sendToServer(String oneSensorJson){
		// Creatng variable for url, HttpURLConnection and BufferedReader
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		// Replacing invalid URL characters from json string
		try {
			oneSensorJson = URLEncoder.encode(oneSensorJson, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} // CLose catch UnsupportedEncodingException

		// Creating string full url for the sensor server url for the sensor data
		String fullURL = sensorServerURL + "?sensordata="+oneSensorJson;
		// Print message to the console
		System.out.println("Sending data to: "+fullURL);  // DEBUG confirmation message
		
		// Creating variable for line and result
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
			}// CLose while loop
			// Close buffered reader
			rd.close();
		} catch (Exception e) {
			e.printStackTrace();
		} // CLose catch Exception e
		// Otherwise, return the result
		return result;    	
	} // CLose public string send to server

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
} // Close public class SensorToServerJSON
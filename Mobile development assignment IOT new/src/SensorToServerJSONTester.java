import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;

/**
 * Testing sensor to server json tester and converting the data to json format.
 * @author dhanyaal.
 */

//Creating public class for SensorToServerJSONTester
public class SensorToServerJSONTester  {
	// Creating string for the last sensor value
	String lastSensorValue = "Open";


	// Declaring a default sensor object. has no location, name/value is set later.
	// This will only work when you have SensorData class developed
	// and a SensorServer developed that accepts json strings.
	// Also, creating set of strings from the sensor data class
	SensorData oneSensor = 
			new SensorData("Room105", "Open", "16038287");

	// Declaring GSON utility object
	Gson gson = new Gson();

	// Declaring String to hold json representation of sensor object data
	String oneSensorJson = new String();

	// The address of server which will receive sensor data
	// Altered to point to server to deal with incoming json parameters
	public static String sensorServerURL = "http://localhost:8080/IOTServernew/SensorServer";

	// Creating public main method and calling SensorToServerJSONTester method
	public static void main(String[] args) {
		new SensorToServerJSONTester();
	} // CLose main method

	// Creating public sensor to server json tester method, to send the data in json format
	public SensorToServerJSONTester() {
		// Convert to json format
		oneSensorJson = gson.toJson(oneSensor);
		// Sending the data to the server
		sendToServer(oneSensorJson);
	} // Close public SensorToServerJSONTester method

	// Creating public string sendToServer, to send the json string to the server
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
			} // CLose while loop
			rd.close(); // CLose buffered reader
		} catch (Exception e) {
			e.printStackTrace();
		} // CLose catch Exception e
		// Return the reuslt
		return result;    	
	} // Close public string send to server
} // Close public class sensor to server

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.Gson;
// Creating public class for SensorToServerJSONTester
public class SensorToServerJSONTester  {
	// Creating variable string for the last rfid tag which has been used to open the door
	String lastRFIDTag = "1600ee15e9";

	// Declaring a default rfid tag object (no location, name/value set later)
	// This will only work when you have RFIDSensorData class developed
	// and a SensorServer developed that accepts json strings, as in the lab
	RFIDSensorData RFIDTag = 
			new RFIDSensorData("1600ee15e9", "Door Openned", "Success");

	// Declaring GSON utility object, to convert object to json
	Gson gson = new Gson();

	// Declareing String to hold json representation of RFIDTag data and calling the toString method from the RFIDSensorData class
	String RFIDTagJson = new String();

	// Address of server which will receive the RFIDSensorData
	// Altered to point to server to deal with incoming json parameters from the IOTServer project and calling the SensorServer db file
	public static String sensorServerURL = "http://localhost:8080/IOTServer/SensorServerDB";

	// Main method
	public static void main(String[] args) {
		// Calling the SensorToServerJSONTester method
		new SensorToServerJSONTester();
	}  // Close main method
	// Creating Sensor To Server JSON Tester method
	public SensorToServerJSONTester() {
		RFIDTagJson = gson.toJson(RFIDTag);
		sendToServer(RFIDTagJson);
	} // Close public Sensor To Server JSON Tester method

	// Creating public String sendToServer method for the url connection
	public String sendToServer(String RFIDTagJson) {
		// Declaring bariables for the url, HttpURLConnection for the url connection and buffered reader
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		// Replacing the invalid URL characters from json string
		try {
			RFIDTagJson = URLEncoder.encode(RFIDTagJson, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} // Close catch Unsupported Encoding Exception
		// Declaring the url to access the json data
		String fullURL = sensorServerURL + "?RFIDSensorData="+RFIDTag;
		// DEBUG confirmation message		
		System.out.println("Sending data to: "+fullURL); 
		// Declaring variable for the string line and the result
		String line;
		String result = "";
		// Open connection
		try {
			// Creating new url to open the connection
			url = new URL(fullURL);
			conn = (HttpURLConnection) url.openConnection();
			//Requesting the get method for the connection
			conn.setRequestMethod("GET");
			// Creating new buffered reader for the connection
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// Requesting response from server to enable URL to be opened
			while ((line = rd.readLine()) != null) {
				result += line;
			} // CLose while loop
			rd.close();  // CLose buffered reader
		} catch (Exception e) {
			e.printStackTrace();
		} // CLose catch exception e
		return result;// Otherwise return the reesult	
	} // CLose public string send to server method
} // CLose public class sensor to server json tester
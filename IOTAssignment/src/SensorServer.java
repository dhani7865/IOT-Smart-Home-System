import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/SensorServer")

public class SensorServer extends HttpServlet {

	// Collects or returns data for TagID, SuccessFail parameters
	private static final long serialVersionUID = 1L;

	// Local variables holding last values stored for each parameter
	private String lastValidTagID  = "1600ee15e9";
	private String lastValidTagValueStr = "Door Openned";
	private String lastValidSuccessFailStr = "Success";

	// Creating public sensor server method
	public SensorServer() {
		super();
	} // CLose sensor server method
	// Creating public init method, which has paramters of ServletConfig config and throws ServletException
	public void init(ServletConfig config) throws ServletException {
		// Print message out onto the console
		System.out.println("Sensor server is up and running\n");	
		System.out.println("Upload sensor data with http://localhost:8080/IOTServer/SensorServer>TagID=xxx&TagValue=xxx&SuccessFail=xxx");
		System.out.println("View last sensor reading at  http://localhost:8080/IOTServer/SensorServer?getdata=true \n\n");		  
	} // Close init method

	// Creating protected void doGet method
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		// Check to see whether the client is requesting data or sending it
		String getdata = request.getParameter("getdata");

		// if no getdata parameter, client is then sending the data
		if (getdata == null){
			// if getdata is null, therefore it is receiving data
			String TagIDStr = request.getParameter("TagID");
			String TagValueStr = request.getParameter("TagValue");
			String SuccessFailStr = request.getParameter("SuccessFail");
			// If tag id string and successFail string is null send to the database
			if (!(TagIDStr==null) && !(TagValueStr==null) && !(SuccessFailStr==null)) {
				//COULD SEND OFF TO DATABASE HERE
				//USEFUL FOR ASSIGNMENT!!!
				// Creating variables or the url, http url connection and buffered reader
				URL url;
				HttpURLConnection conn;
				BufferedReader rd;
				String sensorServerURL = null;
				// String full url
				String fullURL = sensorServerURL + "?TagID=rfid&TagValue&SuccessFail="+TagIDStr;
				// Print message out onto the console
				System.out.println("Sending data to: "+fullURL);  // DEBUG confirmation message
				// String line and string for the result
				String line;
				String result = "";
				// Open the connection
				try {
					// Creating new url, http connection and the GET request method
					url = new URL(fullURL);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					// Requesting response from the server to enable URL to be opened.
					while ((line = rd.readLine()) != null) {
						result += line;
					} // Close while loop
					rd.close(); // Close buffered reader
				} catch (Exception e) {
					e.printStackTrace();
				} // Close catch

				// Updating the local variables and send confirmation back to the user for the tag id and success or fail
				PrintWriter out = response.getWriter();
				out.println(updateSensorValues(TagIDStr,TagValueStr, SuccessFailStr));
				out.close();

			} // Close if statement for TagIDStr not eqal to null and SuccessFailStr not equal to null
		} // end if getdata is null
		else {  // Display current data (JSON format)
			//This is the section that will deliver data back to a user who is not uploading sensor data
			sendJSONString(response);
		} // Close if statement for if getdata is equal to null
	} // Close protected void doGet

	// Creating private string to update the tag id and success or fail values
	private String updateSensorValues(String TagIDStr, String TagValueStr, String SuccessFailStr) {
		// all ok, update last known values and return the last tag id used and the last sucess or fail values which are used
		lastValidTagID = TagIDStr;
		lastValidTagValueStr = TagValueStr;
		lastValidSuccessFailStr = SuccessFailStr;
		// Print message to the console
		System.out.println("DEBUG : Last tag id which was used was: " + TagIDStr + ", with value "+SuccessFailStr);
		return "Rfid tag data updated."; // and return the result
	} // Close private string updateSensorValues method

	// Creating private void send json string in plain text, to the server
	private void sendJSONString(HttpServletResponse response) throws IOException{
		// Setting the content type as text/plain
		response.setContentType("text/plain");
		// Creating json string
		String json = "{\"RFID\": {\"" + lastValidTagID + 
				"\": \"" + lastValidTagValueStr
				+ "\"" + lastValidSuccessFailStr + "\"}}";
		// Creating string text
		String returnTextMessage = "Latest values for the rfid - RFID: "+lastValidTagID +
				", Value: Value: "+lastValidTagValueStr+", Value: "+lastValidSuccessFailStr;
		// Creating PrintWriter variable and calling the getWriter method
		PrintWriter out = response.getWriter();
		// Print message out onto the console
		// Print the data in json format and plain text
		System.out.println("DEBUG: sensorServer JSON: "+json);
		System.out.println("DEBUG: sensorServer TEXT: "+returnTextMessage);

		// Return the json string and in plain text
		// out.println(json);
		out.println(returnTextMessage);
		out.close();
	} // Close public void sendJSONString method


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	// Creating protected void doPost method and calling the doGet method
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	} // CLose protected void doPost method
} // Close public class senser server
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This class would add the sensor data to the server.
 * It would also allow you to upload sensor data to the server and view the last sensor which was uploaded on the server
 * @author Dhanyaal
 */
// Creating web servlet for the SensorServer
@WebServlet("/SensorServer")
//Creating Sensor server class which extends HttpServlet
public class SensorServer extends HttpServlet {

	// Collects or returns data for sensorname, sensorvalue parameters
	private static final long serialVersionUID = 1L;

	// Local variables holding last values stored for each parameter
	private String lastValidSensorNameStr  = "Room105";
	private String lastValidSensorValueStr = "Open";

	// Creating SensorServer method
	public SensorServer() {
		super();
	} // Close public SensorServer method

	// Creating public void init method and pritning messages out onto the console
	public void init(ServletConfig config) throws ServletException {
		System.out.println("Sensor server is up and running\n");	
		System.out.println("Upload sensor data with http://localhost:8080/IOTServernew/SensorServer?sensorname=xxx&sensorvalue=nnn");
		System.out.println("View last sensor reading at  http://localhost:8080/IOTServernew/SensorServer?getdata=true \n\n");		  
	} // Close public void init method

	// Creating do get method 
	// doGet is a method which supports the servlet HTTP GET requests 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_OK);
		// Check to see whether the client is requesting data or sending it
		String getdata = request.getParameter("getdata");

		// if there are no get data parameters, the client is then sending the cata.
		if (getdata == null){
			// getdata is null, therefore it is receiving data
			String sensorNameStr = request.getParameter("sensorname");
			String sensorValueStr = request.getParameter("sensorvalue");

			// Sending the data of to the database
			if (!(sensorNameStr==null) && !(sensorValueStr==null)) {
				// update local variables and sending confirmation back to user
				PrintWriter out = response.getWriter();
				// Calling the updateSensorValues method and updating the values
				out.println(updateSensorValues(sensorNameStr, sensorValueStr));
				out.close(); // Close printwriter
			} // CLose if statement for sensornameStr = null and SensorValue = null
		} // end if getdata is null
		else {  // Otherwise display current data in json format
			//This is the section that will deliver data back to a user who is not uploading sensor data
			sendJSONString(response);
		} // CLose else statement
	} // CLose protected void doGet

	// Creating updateSensorValues method to update the sensor name and sensor value
	private String updateSensorValues(String sensorNameStr, String sensorValueStr){
		// all ok, update last known values and return the last sensor
		lastValidSensorNameStr = sensorNameStr;
		lastValidSensorValueStr = sensorValueStr;
		System.out.println("DEBUG : Last sensor was " + sensorNameStr + ", with value "+sensorValueStr);
		return "Sensor value updated.";
	} // CLose private string updateSensorValues method

	// Creating private void to send json string and the responce type will be plain text
	private void sendJSONString(HttpServletResponse response) throws IOException{
		// Setting the content type as text/plain
		response.setContentType("text/plain");
		// Creating json string and plain text string
		String json = "{\"sensor\": {\"" + lastValidSensorNameStr + 
				"\": \"" + lastValidSensorValueStr + "\"}}";
		String returnTextMessage = "Latest values - Sensor: "+lastValidSensorNameStr +
				", Value: "+lastValidSensorValueStr;
		// Creating printwriter equal to responce.getwriter
		PrintWriter out = response.getWriter();
		// Printing message to the console
		System.out.println("DEBUG: sensorServer JSON: "+json);
		System.out.println("DEBUG: sensorServer TEXT: "+returnTextMessage);

		//out.println(json);
		// Return string
		out.println(returnTextMessage);
		out.close(); // Close print writer
	} // CLose private void send json string method


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * Creating do post method
	 * doPost is used for HTTP POST requests 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	} // CLose dopost method
} // Close public class sensor server
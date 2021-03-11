// importing java into the class
// server class
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson; // gson


@WebServlet("/Server")
// public class Server
public class Server extends HttpServlet {
	
	// collects or retrieves data
	private static final long serialVersionUID = 1L;
	
	// Local variables keeps the last values 
    SensorData lastSensor = new SensorData("unknown", "unknown");
    Gson gson = new Gson();
    
    public Server() {
        super();
    }
	  public void init(ServletConfig config) throws ServletException {
		  System.out.println("Sensor server is up and running\n"); // print message to show the server is up and running
		  System.out.println("Upload sensor data with http://localhost:8080/PhidgetServer/SensorServerJsonSolution?sensordata=someSensorJson"); // print message
		  System.out.println("View last sensor reading at  http://localhost:8080/PhidgetServer/SensorServerJsonSolution?getdata=true \n\n"); // print message		  
	  }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setStatus(HttpServletResponse.SC_OK);
	    SensorData oneSensor = new SensorData("unknown", "unknown");
	    // say that there is Sensordata object
	    // locate what data is being sent or taken
	    String getdata = request.getParameter("getdata");// string getdata
	    
	    if (getdata == null){
	    		// getdata is null, so it is receiving data
			String sensorJsonString = request.getParameter("sensordata");// getparameter which is holding the sensordata
			
			// if statement
			if (sensorJsonString != null) {
				oneSensor = gson.fromJson(sensorJsonString, SensorData.class);
			 // save data so most recent object appears
				lastSensor = oneSensor;
	
				// update values
				PrintWriter out = response.getWriter();
				out.println(updateSensorValues(oneSensor)); // print message
				out.close();
			} 
		} 
	    else {  // Display the data which is currently available in JSON format
	    	   sendJSONString(response);
	    }

	}

	private String updateSensorValues(SensorData oneSensor){
		lastSensor = oneSensor;
		System.out.println("DEBUG : Last sensor was " + oneSensor.getSensorname() + ", with value "+oneSensor.getSensorvalue()); // print message
		return "Sensor value updated.";
	} // values updated 
	
	
	private void sendJSONString(HttpServletResponse response) throws IOException{
		  response.setContentType("application/json");  
	      String json = gson.toJson(lastSensor);
	      
	      PrintWriter out = response.getWriter();
	      System.out.println("DEBUG: sensorServer JSON: "+lastSensor.toString()); // print message

	      out.println(json);
	      out.close();
	} //close sendJSON string
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    doGet(request, response);
	} // void do post

} //close Server class



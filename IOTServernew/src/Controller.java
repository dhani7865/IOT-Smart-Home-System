// importing java into the class
//CONTROLLER CLASS
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

@WebServlet("/controler-00")
public class Controller extends HttpServlet {
	RfidDao RfidDao; // controller class, which extends from the DAO class
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		System.out.println("Server has started successfully"); // print message
		RfidDao rfiddao = new RfidDao();
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setStatus(HttpServletResponse.SC_OK); // void method being implemented doGet method
		
		String reqString = req.getParameter("sensorname");
		SensorData data = null; // require string method, sensor data if it equals null
		
		System.out.println("request=" + reqString);
		
		if (reqString != null) {
			System.out.println("sensorname=" + reqString);
			boolean valid = RfidDao.isUserValid(reqString); // boolean valid check
			
			if (valid == true) {
				System.out.println("Valid tag/user"); // print message invalid
				data = new SensorData(reqString, "Door open", "16038287");
				// send command to open the door
				PrintWriter writer = new PrintWriter(resp.getOutputStream());
				writer.write("true"); // you command string there
				writer.close(); // if it is right the door will open
			} else {
				System.out.println("invalid tag/user"); // print message invalid
				data = new SensorData(reqString, "Door closed", "16038287"); // new data is shown if it is invalid, so in this case the door is closed
			}
			if (data != null)
				RfidDao.updateSensorTable(data);
		} else {
			System.out.println("nothing to check"); // print message, once it has been updated
		}
	}
}// close class
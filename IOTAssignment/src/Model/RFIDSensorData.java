package Model;

// Creating rfid RFIDSensorData class to create method for the sensor data and creating setters and getters
public class RFIDSensorData {
	// Declaring strings fr the tagid, success/fail and sensordate
	String TagID;
	String TagValue;
	String SuccessFail;
	String sensordate;

	// Creating method for RFIDSensorData and setting string paramters for the tag id, successfail and sensor data.
	public RFIDSensorData(String TagID, String TagValue, String SuccessFail, String sensordate) {
		super();
		this.TagID = TagID;
		this.TagValue = SuccessFail;
		this.SuccessFail = SuccessFail;
		this.sensordate = sensordate;
	} // Close RFIDSensorData method

	// Constructors depending on which parameters are known e.g. tagid and successfail
	public RFIDSensorData(String TagID, String TagValue, String SuccessFail) {
		super();
		this.TagID = TagID;
		this.TagValue = TagValue;
		this.SuccessFail = SuccessFail;
		// Defaults for when no location known
		this.sensordate = "unknown";
	} // Close constructor for RFIDSensorData
	
	// Creating constructor for the RFIDSensorData for the successfail
	public RFIDSensorData(String TagValue, String SuccessFail) {
		super();
		this.TagValue = TagValue;
		this.SuccessFail = SuccessFail;
		// Defaults for when no TagID or data known
		this.TagID = "unknown";
		this.TagValue = "unkown";
		this.SuccessFail = "unkown";
		this.sensordate = "unknown";
	} // Close RFIDSensorData constructor
	
	// Creating setter and getter for the Tag value
	public String getTagValue() {
		return TagValue;
	} // Close getter for TagValue
	
	public void setTagValue(String TagValue) {
		this.TagValue = TagValue;
	} // CLose setter for TagValue
	
	// Creating setter and getter for the success fail
	public String getSuccessFail() {
		return SuccessFail;
	} // Close getter for success/fail
	
	public void setSuccessFail(String SuccessFail) {
		this.SuccessFail = SuccessFail;
	} // CLose setter for sucess/fail
	
	// Creating getter and getter for the tag id
	public String getTagID() {
		return TagID;
	} // Cose getter for tag id
	
	public void setTagID(String TagID) {
		this.TagID = TagID;
	} // Close getter for tag id
	
	public String getSensordate() {
		return sensordate;
	} // Close getter for sensor date
	
	// Creating setter and getter for sensor date
	public void setSensordate(String Sensordate) {
		this.sensordate = Sensordate;
	} // Close setter sensor date
	/**
	 * The toString method is used in java when we want a object to represent string. 
	 * Overriding toString() method would return the specified values. 
	 * This method can be overridden to customize the String representation of the Object.
	 */
	@Override
	public String toString() {
		return "SensorData [TagID=" + TagID + ", TagValue=" + TagValue + ",SuccessFail=" + SuccessFail + ", sensordate=" + sensordate + "]";
	} // Close public toString
} // Close RFIDSensorData class
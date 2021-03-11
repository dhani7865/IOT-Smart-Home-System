package iotassignmentclient;

/**
 * Constructors initialize objects which are being created with the new operator and methods would perform operations on objects which already exist. 
 * The constructors cannot be called directly; they would be called indirectly.
 * @author dhanyaal
 */
// Creating public class for tag data
public class TagData {
	// Creating string for the sensor name
	String sensorname;
	String TagValid;
	String DoorName;

	// Creating method for TagData and setting string paramters for the sensorname, TagValid, userid and DoorName.
	public TagData() {
		super();
		this.TagValid = sensorname;
		this.TagValid = TagValid;
		this.DoorName = DoorName;
	} // Close RFIDSensorData method

	// Constructors depending on which parameters are known e.g. sensorname, TagValid and DoorName
	public TagData(String sensorname, String TagValid, String DoorName) {
		super();
		this.sensorname = sensorname;
		this.TagValid = TagValid;
		this.DoorName = DoorName;
	} // Close SensorData Constructors

	// Creating constructor for the TagData for the sensorname and TagValid
	public TagData(String sensorname, String TagValid) {
		super();
		this.sensorname = sensorname;
		this.TagValid = TagValid;
		// Defaults for when no userid or location known
		this.TagValid = "unknown";
	} // Close constructor for RFIDSensorData

	// Creating setter and getter for getSensorname and setSensorname
	public String getSensorname() {
		return sensorname;
	} // Close get sensorname

	public void setSensorname(String sensorname) {
		this.sensorname = sensorname;
	} // Close setter sensor name

	// Creating setter and getter for TagValid and setTagValid
	public String getTagValid() {
		return TagValid;
	} // Close getter sensor value

	public void setTagValid(String TagValid) {
		this.TagValid = TagValid;
	} // Close setter TagValid 

	// Creating setter and getter for DoorName
	public String getDoorName() {
		return DoorName;
	} // Close public get door name

	public void setDoorName(String DoorName) {
		this.DoorName = DoorName;
	} // CLose public set door name


	/**
	 * The toString method is used in java when we want a object to represent string. 
	 * Overriding toString() method would return the specified values. 
	 * This method can be overridden to customize the String representation of the Object.
	 */
	@Override
	public String toString() {
		return "TagData [sensorname=" + sensorname + ", TagValid=" + TagValid + ", DoorName=" + DoorName + "]";
	} // Close Tostring method
} // Close public class sensor data


package com.example.dh11a.mqttapp;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Constructors initialize objects which are being created with the new operator and methods would perform operations on objects which already exist.
 * The constructors cannot be called directly; they would be called indirectly.
 *
 * @author dhanyaal
 */

// Creating public class for sensor data and initializing the different strings
public class Sensordata {
    // Creating strings for the user id, sensor name, sensor value and sensor date
    String sensorname;
    String sensorvalue;
    String userid;
    String sensordate;

    // Creating method for SensorData and setting string paramters for the sensorname, sensorvalue, userid and sensor date.
    public Sensordata(String JsonString) {
        super();
        jsontoobj(JsonString);
    } // Close RFIDSensorData method

    // Creating method for SensorData and setting string paramters for the sensorname, sensorvalue, userid and sensor date.
    public Sensordata(String sensorname, String sensorvalue, String userid, String sensordate) {
        super();
        this.sensorname = sensorname;
        this.sensorvalue = sensorvalue;
        this.userid = userid;
        this.sensordate = sensordate;
    } // Close RFIDSensorData method

    // Constructors depending on which parameters are known e.g. sensorname, sensorvalue and userid
    public Sensordata(String sensorname, String sensorvalue, String userid) {
        super();
        this.sensorname = sensorname;
        this.sensorvalue = sensorvalue;
        this.userid = userid;
        // Defaults for when no sensordate is known
        this.sensordate = "unknown";
    } // Close SensorData Constructors

    // Creating constructor for the SensorData for the sensorname and sensorvalue
    public Sensordata(String sensorname, String sensorvalue) {
        super();
        this.sensorname = sensorname;
        this.sensorvalue = sensorvalue;
        // Defaults for when no userid or location known
        this.userid = "unknown";
        this.sensordate = "unknown";
    } // Close constructor for RFIDSensorData

    /**
     * A setter is a method which would update values of a variable.
     * And a getter is a method which would read values of a variable.
     *
     * @return
     */
    // Creating setter and getter for getSensorname and setSensorname
    public String getSensorname() {
        return sensorname;
    } // Close get sensorname

    public void setSensorname(String sensorname) {
        this.sensorname = sensorname;
    } // Close setter sensor name

    // Creating setter and getter for sensorvalue and setSensorvalue
    public String getSensorvalue() {
        return sensorvalue;
    } // Close getter sensor value

    public void setSensorvalue(String sensorvalue) {
        this.sensorvalue = sensorvalue;
    } // Close setter sensor value

    // Creating setter and getter for userid
    public String getUserid() {
        return userid;
    } // Close public get user id

    public void setUserid(String userid) {
        this.userid = userid;
    } // CLose public set user id

    // Creating setter and getter for usensor date
    public String getSensordate() {
        return sensordate;
    } // Close public string set sensor date

    public void setSensordate(String sensorvalue) {
        this.sensordate = sensorvalue;
    } // Close public set sensor date

    /**
     * The toString method is used in java when we want a object to represent string.
     * Overriding toString() method would return the specified values.
     * This method can be overridden to customize the String representation of the Object.
     */
    @Override
    public String toString() {
        return "Sensordata [sensorname=" + sensorname + ", sensorvalue=" + sensorvalue + ", userid=" + userid
                + ", sensordate=" + sensordate + "]";
    } // CLose public string toString method

    // Creating public string for turn to string and returning the sensorname, sensor value, user id and sensor data
    public String TurnTostring() {
        return new String(sensorname + " " + sensorvalue + " " + userid + " " + sensordate);
    } // Close public turn_to_string method

    // Creating public string toJSON method, which would convert the data to json format
    public String toJSON() {
        // Declaring new json object
        JSONObject jsonObject = new JSONObject();
        // Creating try to create a json object for the different fields.
        try {
            // Creting json objects
            jsonObject.put("sensorname", getSensorname());
            jsonObject.put("sensorvalue", getSensorvalue());
            jsonObject.put("userid", getUserid());
            jsonObject.put("sensordate", getSensordate());
            // Return the json object and calling the toString method
            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } // Close catch JSONException e
    } // CLose

    // Creating Public void jsontoobj and parameter
    public void jsontoobj(String jsonstring) {
        // Creating try for the json object  and calling the json string paramter
        try {
            JSONObject obj = new JSONObject(jsonstring);
            // Get string object for the sensorname, sensorvalue, userid and sensordate
            this.sensorname = obj.getString("sensorname");
            this.sensorvalue = obj.getString("sensorvalue");
            this.userid = obj.getString("userid");
            this.sensordate = obj.getString("sensordate");
        } catch (Throwable t) {
            /**
             *  Throw error message on the log.
             * This tag is used in places such as the catch statement.
             *  You would then know that an error has occurred and therefore you're logging an error.
             */
            Log.e("My App", "Could not parse malformed JSON: \"" + jsonstring + "\"");
        } // CLose catch Throwable t
    } // Close public void json to obj method
} // Close public class sensor data
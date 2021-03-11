package mqtt.publisher;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

// Creating PhidgetPublisher class
public class PhidgetPublisher {
	// Creating the broker url
	public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
	//     public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";

	// Setting the user id
	public static final String userid = "16038287";

	// Creating topics for the rfid and motor
	public static final String TOPIC_RFID       = userid + "/rfid";
	public static final String TOPIC_MOTOR     = userid + "/motor";


	public static final String TOPIC_GENERIC    = userid + "/";

	private MqttClient client;

	// Creating PhidgetPublisher method
	public PhidgetPublisher() {
		// Creating try which would call the broker url and user id
		try {
			client = new MqttClient(BROKER_URL, userid+"-publisher");
			// create mqtt session
			MqttConnectOptions options = new MqttConnectOptions();
			//Setting clean session and setting it to boolean value as false
			options.setCleanSession(false);
			options.setWill(client.getTopic(userid + "/LWT"), "I'm gone :(".getBytes(), 0, false);
			client.connect(options);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		} // Close catch mqtt exception e
	} // Close PhidgetPublisher method

	// Creating public void publish rfid method, which would then publish everything to the rfid topic
	public void publishRfid(String rfidTag) throws MqttException {
		// Creating final mqtt topic and getting the rfid topic and then print message out onto the console saying published data to the correct topic
		final MqttTopic rfidTopic = client.getTopic(TOPIC_RFID);
		final String rfid = rfidTag + "";
		rfidTopic.publish(new MqttMessage(rfid.getBytes()));
		System.out.println("Published data. Topic: " + rfidTopic.getName() + "   Message: " + rfid);
	} // Close publishRfid method

	// Creating public void publish motor method, which would then publish everything to the motor topic
	// If valid tag is used move the motor, otherwise if invalid tag is used motor won't move
	public void publishMotor(String tagStr) throws MqttException {
		// Creating final mqtt topic and getting the motor topic and then print message out onto the console saying published data to the correct topic
		final MqttTopic motorTopic = client.getTopic(TOPIC_MOTOR);
		// Print message out onto the console
		System.out.println("Publishing message : "+tagStr + " to topic: "+motorTopic.getName());
		final String motorMessage = tagStr + "";
		motorTopic.publish(new MqttMessage(motorMessage.getBytes()));
		System.out.println("Published data. Topic: " + motorTopic.getName() + "   Message: " + motorMessage);
	} // Close publishMotor method

	// Creating another publishMotor method which throws mqtt exception
	public void publishMotor() throws MqttException {
		// Creating final mqtt topic and getting the motor topic and then print message out onto the console saying published data to the correct topic
		final MqttTopic motorTopic = client.getTopic(TOPIC_MOTOR);
		// Print message out onto the console
		System.out.println("Publishing message : TESTER to topic: "+motorTopic.getName());
		// If valid tag is used this message will then be printed out onto the console
		final String motorMessage = "OPEN DOOR";
		motorTopic.publish(new MqttMessage(motorMessage.getBytes()));
		System.out.println("Published data. Topic: " + motorTopic.getName() + "   Message: " + motorMessage);
	} // CLose publish motor method

	// More generic publishing methods - avoids having to name every one
	public void publishSensor(String sensorValue, String sensorName) throws MqttException {
		final MqttTopic mqttTopic = client.getTopic(TOPIC_GENERIC + sensorName);
		final String sensor = sensorValue + "";
		mqttTopic.publish(new MqttMessage(sensor.getBytes()));
		System.out.println("Published data. Topic: " + mqttTopic.getName() + "   Message: " + sensor);
	}
	public void publishSensor(int sensorValue, String sensorName) throws MqttException {
		// same as string publisher, just convert int to String
		publishSensor(String.valueOf(sensorValue), sensorName);
	}
	public void publishSensor(float sensorValue, String sensorName) throws MqttException {
		// same as string publisher, just convert float to String
		publishSensor(String.valueOf(sensorValue), sensorName);
	}

	//    String jsonTag = gson.toJson(sensorname);
	//
	//    try {
	//        lockTopic.publish(new MqttMessage(jsonTag.getBytes()));
	//    } 
}

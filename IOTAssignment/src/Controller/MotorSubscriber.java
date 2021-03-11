package Controller;

import Model.MotorSubscribeCallback;
import Model.Utils;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;


public class MotorSubscriber {

	public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
	// public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";

	public static final String userid = "16038287"; // change this to be your student-id
	String clientId = userid + "-sub";


	private MqttClient mqttClient;

	public MotorSubscriber() {
		try {
			mqttClient = new MqttClient(BROKER_URL, clientId);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void start() {
		try {
			// setting the callback for the motor subscriberCallback
			mqttClient.setCallback(new MotorSubscribeCallback());
			mqttClient.connect(); // Connect to the mqtt client

			//Subscribe to correct topic
			final String topic = userid+"/motor";
			//  Subscribe to the mqtt client topic
			mqttClient.subscribe(topic);
			// print message onto the console
			System.out.println("Subscriber is now listening to "+topic);

		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		} // Close mqtt Exception
	} // Close public void start

	// Creating public main method
	public static void main(String... args) {
		// Calling the MotorSubscriber function
		final MotorSubscriber subscriber = new MotorSubscriber();
		subscriber.start(); // Starting the subscriber
	} // Close public static void main
} // Close public class motor subscriber

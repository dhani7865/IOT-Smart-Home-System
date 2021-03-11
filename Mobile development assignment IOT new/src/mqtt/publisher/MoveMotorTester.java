package mqtt.publisher;

import org.eclipse.paho.client.mqttv3.MqttException;

public class MoveMotorTester {

	public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
	// public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";

	public static final String userid = "16038287"; // change this to be your student-id
	public static final String TOPIC_MOTOR = userid + "/motor";

	static PhidgetPublisherMotorSolution publisher = new PhidgetPublisherMotorSolution(); // source in PhidgetPublisher.java

	public static void main(String[] args) {
		try {
			while (true) {
				System.out.println("Publishing to move motor");
				publisher.publishMotor(); // Calling the method publishMotor from the phidget ppublisher class
				// wait for motor to move
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Published and done.");
			}
		} catch (MqttException e) {
			System.out.println("Publish error");
			e.printStackTrace();
		}

	}

}

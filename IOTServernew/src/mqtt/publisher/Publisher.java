package mqtt.publisher;

import org.eclipse.paho.client.mqttv3.*;

import mqtt.utils.Utils;

// Creating publisher class to publish the messages to the topic
public class Publisher {
	// Setting the broaker url
	// public static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
	public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
	//public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";

	// Setting the user id
	public static final String userid = "16038287"; // change this to be your student-id

	// Creating topics for brightness, temperture, slider, rfid and motor
	public static final String TOPIC_BRIGHTNESS 	= userid + "/brightness";
	public static final String TOPIC_TEMPERATURE = userid +"/temperature";
	public static final String TOPIC_SLIDER     = userid + "/slider";
	public static final String TOPIC_RFID       = userid + "/rfid";
	public static final String TOPIC_MOTOR     = userid + "/motor";

	// Creating variable for the mqtt client
	private MqttClient client;

	// Creating public publisher method
	public Publisher() {
		// Creating try and calling the broker url and the user id
		try {
			client = new MqttClient(BROKER_URL, userid);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		} // CLose catch
	} // Close public publisher method

	// Creating private void start method to start publishing the messages to the topic
	private void start() {
		// Creating try to start publishing the messages
		try {
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setMaxInflight(1000);
			options.setAutomaticReconnect(true);
			options.setWill(client.getTopic(userid+"/LWT"), "I'm gone :(".getBytes(), 0, false);
			
			client.connect(options);

			// Publishing the data forever
			while (true) {
				// Calling the publish rfid ethod and setting the rfid tag
				publishRfid("1600ee15e9");
				Thread.sleep(1000);
				// Calling the publishMotor method
				publishMotor();
				Thread.sleep(2000);
			} // Close while loop
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // Close catch
	} // Close private void start method

	//    private void publishTemperature() throws MqttException {
	//        final MqttTopic temperatureTopic = client.getTopic(TOPIC_TEMPERATURE);
	//
	//        final int temperatureNumber = Utils.createRandomNumberBetween(20, 30);
	//        final String temperature = temperatureNumber + "°C";
	//
	//        temperatureTopic.publish(new MqttMessage(temperature.getBytes()));
	//
	//        System.out.println("Published data. Topic: " + temperatureTopic.getName() + "  Message: " + temperature);
	//    }
	//
	//    private void publishBrightness() throws MqttException {
	//        final MqttTopic brightnessTopic = client.getTopic(TOPIC_BRIGHTNESS);
	//
	//        final int brightnessNumber = Utils.createRandomNumberBetween(0, 100);
	//        final String brightness = "b"+brightnessNumber;
	//
	//        brightnessTopic.publish(brightness.getBytes(), 0, true);
	//        //.publish(new MqttMessage(brightness.getBytes()));
	//
	//        System.out.println("Published data. Topic: " + brightnessTopic.getName() + "   Message: " + brightness);
	//    }
	
	// Creating publish void for the publishrfid method
	public void publishRfid(String rfidTag) throws MqttException {
		// Getting the rfid topic
		final MqttTopic rfidTopic = client.getTopic(TOPIC_RFID);
		final String rfid = rfidTag + "";
		rfidTopic.publish(new MqttMessage(rfid.getBytes()));
		System.out.println("Published data. Topic: " + rfidTopic.getName() + "   Message: " + rfid);
	} // CLose publish rfid method

	// Creating publish motor method
	public void publishMotor(String tagStr) throws MqttException {
		// Creating topic to get the motor topic
		final MqttTopic motorTopic = client.getTopic(TOPIC_MOTOR);
		System.out.println("Publishing message : "+tagStr + " to topic: "+motorTopic.getName());
		final String motorMessage = tagStr + "";
		motorTopic.publish(new MqttMessage(motorMessage.getBytes()));
		System.out.println("Published data. Topic: " + motorTopic.getName() + "   Message: " + motorMessage);
	} // Close publish motor method
	
	public void publishMotor() throws MqttException {
		final MqttTopic motorTopic = client.getTopic(TOPIC_MOTOR);
		System.out.println("Publishing message : TESTER to topic: "+motorTopic.getName());
		final String motorMessage = "OPEN DOOR";
		motorTopic.publish(new MqttMessage(motorMessage.getBytes()));
		System.out.println("Published data. Topic: " + motorTopic.getName() + "   Message: " + motorMessage);
	}

	public static void main(String... args) {
		final Publisher publisher = new Publisher();
		publisher.start();
	}
}

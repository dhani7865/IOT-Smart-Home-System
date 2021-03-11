package mqtt.subscriber;
import com.google.gson.Gson;
import mqtt.utils.Utils;
import org.eclipse.paho.client.mqttv3.*;
import com.phidget22.PhidgetException;
import mqtt.utils.Utils;
/**
 * The motor subscriber callback class would then move the motor when valid tag has been used. 
 * MQTT would use the topic to determine which message goes to which client (subscriber). 
 * A topic is a structured string which can be used to filter and route messages.
 * Both publishers and subscribers are MQTT clients.
 * The publisher and subscriber would then refer to whether the client has published the messages or subsribed to messages.
 * @author dhanyaal.
 */

// Creating public class for the motor subscriber callback class which implements the mqtt callback.
public class MotorSubscribeCallback implements MqttCallback {
	/**
	 *  Setting the user id
	 *  Also, setting the correct broker url and the correct topic which the messages will subsribe to.
	 */
	public static final String userid = "16038287";
	// Setting the correct broker url
	public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
	//public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";
	public static final String TOPIC_MOTOR     = userid + "/motor_lock";
	private MqttClient client;


	/**
	 * Creating public MotorSubscribeCallback method and calling the client id and the broker url.
	 *  Once the messages have been subscribed to the correct topic, it will then move the motor. 
	 *  If the correct tag has been used, it will then move the motor. 
	 *  If invalid tag has been used it wouldn't move the motor. 
	 */
	public MotorSubscribeCallback()
	{
		try {
			client = new MqttClient(BROKER_URL, userid+"-motor_Publisher");
			// create mqtt session
			MqttConnectOptions options = new MqttConnectOptions();
			options.setCleanSession(false);
			options.setWill(client.getTopic(userid + "/LWT"), "I'm gone :(".getBytes(), 0, false);
			client.connect(options);
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(1);
		} // Close catch MqttException e
	} // lose MotorSubscribeCallback method

	@Override
	//This is called when the connection is lost. We could reconnect here.
	public void connectionLost(Throwable cause) {
	}

	@Override
	/**
	 * This is the message arrived method, which would then check if the correct message (Tag id)
	 *  has been received. If the correct tag id has been received, it will then move the motor.
	 *  Otherwise, invalid tag id (message), motor won't move.
	 *  The PhidgetMotorMover class been called as this is the class, which would then move the motor.
	 */
	public void messageArrived(String topic, MqttMessage message) throws MqttException{
		System.out.println("Message arrived. Topic: " + topic + "  Message: " + message.toString());
		// Move motor to open, then shut after pausing
		System.out.println("DEBUG: Trying to move motor");
		PhidgetMotorMover.moveServoTo(180.0);
		System.out.println("Waiting until motor at position 180");
		//Gson gson = new Gson();
		//SensorData temp = gson.fromJson(message.toString(), SensorData.class);

		// When the door is unlocked and door is open
		SensorData data = new SensorData(message.toString(),"OPEN DOOR","none","none");
		Gson gson = new Gson();
		final MqttTopic motorTopic = client.getTopic(TOPIC_MOTOR);
		motorTopic.publish(new MqttMessage(gson.toJson(data, SensorData.class).getBytes()));
		System.out.println("Publishing the locking sequence, to open the door");

		// When the door is locked
		Utils.waitFor(5);
		PhidgetMotorMover.moveServoTo(0.0);
		System.out.println("wait complete");
		Utils.waitFor(2);

		// When the door is ocked and door is not open
		SensorData data2 = new SensorData("motor","LOCKED","none","none");
		motorTopic.publish(new MqttMessage(gson.toJson(data2).getBytes()));
		System.out.println("Published locking sequence, door not open");

		// Creating if statement statement for the user id and then printing message out onto the console.
		if ((userid+"/LWT").equals(topic)) {
			// Print message out onto the console letting the user know the sensor has gone.
			System.err.println("Sensor gone!");
		} // CLose if statement
	} // Close public void message arrived method

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		//no-op
	} // Closing the deliveryComplete method
} // Close public void MotorSubscribeCallback class

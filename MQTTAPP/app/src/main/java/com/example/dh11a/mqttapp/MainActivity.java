package com.example.dh11a.mqttapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;

import com.google.gson.Gson;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created Door lock app, which would open the door, if a valid tag has been used.
 * Otherwise, if invalid tag has been used door won't open.
 * If valid tag has been used it would also move the motor.
 * Async task has been used to perform all the different tasks in the background.
 */

// Creating public class main activity, which extends the activity
public class MainActivity extends Activity {
    // Declaring variables for the lock button, message, rfid, door lock status, lv and image from the activity_main class
    Button lock;
    TextView Message;
    TextView Rfid;
    TextView Door_Lock_Status;
    ListView lv;
    TextView name;
    ImageView imagelock;


    // Setting the broker url
    public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
//public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";

    // Creating string variables
    // Student id
    String userid = "16038287";
    // Creating string for the client id
    String clientId = userid + "-sub_Android";
    // Setting the valid rfid tag
    String RFID = "1600ee15e9";
    // Creating mqtt client variable
    private MqttClient client;

    // Creating array list for the lock status (RFID TAGS)
    public List<String> rfidtags = new ArrayList<String>();
    // Creating topics for hte motor, server and all
    public final String TOPIC_MOTOR = userid + "/motor";
    public final String TOPIC_SERVER = userid + "/server";
    public final String TOPIC_ALL = userid + "/#";

    /**
     * Both publishers and subscribers are MQTT clients.
     * The publisher and subscriber labels refer to whether the client is currently publishing messages or
     * subscribing to messages
     */
    // Creating variable for hte mqtt client
    private MqttClient mqttClient;
    // Creating sensordata object and also calling the sensor data class and setting the message received
    Sensordata msg_recieved;
    // Creating variable for the array adapter
    ArrayAdapter adapter;

    @Override
    // Overriding is used to provide specific implementation of a method which is already provided by the super class.
    // Creating onCreate method, which calls the activity main class
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Setting the content view
        setContentView(R.layout.activity_main);
        lock = (Button) findViewById(R.id.lock);
        Rfid = (TextView) findViewById(R.id.rfid);
        Message = (TextView) findViewById(R.id.message);
        Door_Lock_Status = (TextView) findViewById(R.id.Door_Lock_Status);
        lv = (ListView) findViewById(R.id.lv);
        name = (TextView) findViewById(R.id.name);
        imagelock = (ImageView) findViewById(R.id.imageView);
        // Calling the add notification method to add the notification, for when the door is open/closed
        createNotification("Door lock status");

        // test data for listview
        rfidtags.add("CORRECT RFID TAG DOOR OPENED");
        // Creating new adapter for the simple text
        adapter = new ArrayAdapter<String>(this, R.layout.simple_text, rfidtags); //| Connects data to UI
        // Settng the adapter
        lv.setAdapter(adapter);                                                                                     //|
        // SUBSCRIBE AND EXECUTE
        new Subscribe().execute();
        // Setting on click listener for the lock and view
        lock.setOnClickListener(new View.OnClickListener() {
            // Creating public on click method
            public void onClick(View v) {
                // publish and execute
                new Publish().execute();
            } // Close on click method
        }); // Close lock set on click listener
    } // Close  public void on create method


    /**
     * Creating private void add notification method.
     * This method will then be called in the on create method.
     * Context is the context of current state of the application/object.
     * It lets newly-created objects understand what has been going on.
     * Typically you would call it to get information regarding another part of the program (main activity and package/application).
     */
    private void createNotification(String message) {
        // Creating new context for the main activity
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MainActivity.this)
                        // Setting the small icon
                        .setSmallIcon(R.drawable.notification_icon)
                        // Setting the content title for the door lock
                        .setContentTitle("MQTT Door Lock")
                        // Setting the context text
                        .setContentText(message);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    } // Close addNotification method

    // Creating private void for the status update
    private void status_update() {
        // Turn the ui thread on
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If the message has been received, get the user id and sensor value
                if (!msg_recieved.sensorname.equals("Server")) {
                    // getting the user id and sensor value
                    // Show data received from subscription
                    Rfid.setText(msg_recieved.getUserid());
                    Message.setText(msg_recieved.getSensorvalue());  //|

                    // Calling the lock image from the drawable folder for when the door is locked
                    imagelock.setImageResource(R.drawable.lock);

                    //OPEN DOOR OR NOT OPEN FROM RFID READER
                    name.setText("none");
                    // If the message has been received get the sensor value
                    if (msg_recieved.getSensorvalue().equals("OPEN DOOR")) {
                        // Door open
                        // Change UI incase lock was unlocked
                        name.setText(msg_recieved.getSensorname());

                        // Calling the unlock image from the drawable folder for when the door has opened
                        imagelock.setImageResource(R.drawable.unlock);

                        // Setting the rfid, message and name
                        Rfid.setText("16038287");
                        Message.setText("Unlocked");
                        name.setText("1600ee15e9");

                        // Setting the notification for when the door has opened
                        createNotification("Door Unlocked");

                    } else if (msg_recieved.getSensorvalue().equals("LOCKED")) // FROM MOTOR
                    {
                        // Setting the rfid, message and name
                        Rfid.setText("none");
                        Message.setText("Locked");
                        name.setText("Motor");
                        // Creating notification for when the door is locked
                        createNotification("Door locked");

                    } // Close else
                } // Close if statement for !received
                // Otherwise update the lost view
                else {
                    // add the data to the array
                    rfidtags.add(msg_recieved.TurnTostring());
                    // turn on ui thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();   // update listview
                        } // Close public void run
                    }); // Close runOnUiThread
                } // CLose else
            } // Close public void run method
        }); // Close turn on ui thread
    } // Close private void status update



    /**
     * Creating async task.
     * Async task is something  which runs everything in the background and the results are published on the ui thread
     * An Async task is defined by 3 different types, e.g.
     * Params, Progress and Result, and 4 different steps, e.g. onPreExecute, doInBackground, onProgressUpdate and onPostExecute.
     */
    // Creating class subscribe which extends the async task
    class Subscribe extends AsyncTask<Integer, Integer, String> {
        // Creating public void to start subscribing
        private void startSubscribing() {
            // CCreating try to connect to the client
            try {
                // Creating mqtt client and calling the connect method
                mqttClient.connect();
                // Creating final string for all topic
                final String topic = TOPIC_ALL;
                // Subscribe to the correct topic
                mqttClient.subscribe(topic);
                // Print message
                System.out.println("Subscriber is now listening to " + topic);
            } catch (MqttException e) {
                e.printStackTrace();
                System.exit(1);
            } // CLose catch MqttException e
        } // Close public void start subscribing method

        @Override
        /**
         * Perform all tasks in the background.
         * All the messages which have arrived will also be performed in the background
         */
        protected String doInBackground(Integer... params) {
            // Creating try for the connection and message arrived
            try {
                mqttClient = new MqttClient(BROKER_URL, clientId, null);
                mqttClient.setCallback(new MqttCallbackExtended() {
                    @Override
                    // Creating public void connection lost
                    //This is called when the connection is lost. We could reconnect here.
                    public void connectionLost(Throwable cause) {

                    } // Close public void cnnection lost

                    @Override
                    // Creating public void message arrived method
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        // Creating final string for the message string
                        final String messageStr = message.toString();
                        // Print message "message RECEIVED"
                        System.out.println("MESSAGE RECEIVED");
                        System.out.println(messageStr);
                        // message received and calling the sensor data class
                        msg_recieved = new Sensordata(messageStr);
                        // Publish the progress
                        publishProgress(0);
                        // Creating if statement for topic_all
                        if ((TOPIC_ALL + "/LWT").equals(topic)) {
                            // Sensor gone
                            System.err.println("Sensor gone!");
                        } // Close if statement
                    } // Close public void message arrived

                    @Override
                    // Creating public void delivery complete
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        //no-op
                    } // Close public void delivery complete

                    @Override
                    // Creating public void Connect complete
                    public void connectComplete(boolean b, String s) {
                        //no-op
                    } // Close public void connect complete
                });
            } catch (MqttException e) {
                e.printStackTrace();
                System.exit(1);
            } // Close catch exception e
            // Calling the start subscribing method and return task has been complete
            startSubscribing();
            return "Task Completed.";
        } // Close protected string do in background

        @Override
        // Creating public void on post execute
        protected void onPostExecute(String result) {
        } // Close protected void on post execute

        @Override
        // Creating protected void on pre execute
        protected void onPreExecute() {
        } // Close protected void on post execute

        @Override
        // Creating protected void on progress update
        protected void onProgressUpdate(Integer... values) {
            status_update();
        } // CLose protected void on progress update
    } // Close subscribe async task

    /**
     * Creating publish class which extends async task.
     * Perform all tasks in the background
     */
    class Publish extends AsyncTask<Integer, Integer, String> {
        @Override
        // Creating protected string for do in background
        protected String doInBackground(Integer... params) {
            // Publish door lock status and return task complete
            publish_Door_Lock_Status();
            return "Task Completed.";
        } // Close protected string do in background

        @Override
        // Creating protected void on post execute
        protected void onPostExecute(String result) {
        } // Close protected void onPostExecute

        @Override
        // Creating protected void for on pre execute
        protected void onPreExecute() {
        } // Close procted void on pre execute

        @Override
        // Creating protected void for progress update
        protected void onProgressUpdate(Integer... values) {
        } // Close protected void progress update

        // Creating private void to publish the status of door lock
        private void publish_Door_Lock_Status() {
            // Creating try to publish the messages
            try {
                // Creating final mqtt topic to get the motor topic
                final MqttTopic Topic = mqttClient.getTopic(TOPIC_MOTOR);
                // Print message
                System.out.println("Publish  message to:");
                // Publish the message to motor topic
                System.out.println(TOPIC_MOTOR);
                // Calling the sensor data class and creating the sensor name, value and user id
                Sensordata msg = new Sensordata("1600ee15e9", "OPEN DOOR", userid);
                // Convert the data to json format
                System.out.println(msg.toJSON());
                // Publish mqtt message to json
                Topic.publish(new MqttMessage(msg.toJSON().getBytes()));

            } catch (MqttException e) {
                e.printStackTrace();
                System.exit(1);
            } // Close catch exception e
        } // Close private void door lock status
    } // Close class publish which extends async task
} // Close public class main activity
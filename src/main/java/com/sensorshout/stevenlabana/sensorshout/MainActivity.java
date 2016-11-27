package com.sensorshout.stevenlabana.sensorshout;

import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import com.sensorshout.stevenlabana.sensorshout.R;
import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends Activity implements SensorEventListener {

    private SensorManager senSensorManager;
    private boolean isChecked;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;

    //originally set to 600
    //private static final int SHAKE_THRESHOLD = 600;
    private static final int SHAKE_THRESHOLD = 2400;
    //*******SL**Use button to stop/pause sound - need to
    // add more features for different lengths of alerts
    //button1 = (Button) findViewById(R.id.button);

    //Code for playing media sound @author - Steve Labana 2015
    //.wav
    MediaPlayer mySound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mySound = MediaPlayer.create(this, R.raw.sound);


        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        final Button v = (Button) findViewById(R.id.checkBox);
    }

        public void itemClicked(View v) {
            //Check to see if check box is checked
            boolean isChecked;
            CheckBox checkBox = (CheckBox) v;
            if (checkBox.isChecked()) {
                isChecked = true;
            }
        }


        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //   @Override
        //   public void onClick(View view) {
        //       Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //               .setAction("Action", null).show();
        //  }
        // });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                //If shaken greater than threshold and checkbox is checked display noise
                if (speed > SHAKE_THRESHOLD && isChecked) {
                    //Play the alarm sound when user drops phone
                    //******NEED to calculate and handle different thresholds here*******
                    mySound.start();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}

package example.meh.lab03;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    double sensitivity = 1;
    List<Double> readings;
    private SensorManager sensorManager;
    Sensor accelerometer;
    TextView peak, height;
    boolean canToss = true;
    SeekBar seekBar;

    boolean reachedTop = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        peak = findViewById(R.id.peak);
        height = findViewById(R.id.height);

        readings = new ArrayList<>();
        seekBar = findViewById(R.id.seekBar);
        seekBar.setMin(1);
        seekBar.setMax(15);
        seekBar.setProgress(5);



        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sensitivity = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    public  void onAccuracyChanged(Sensor sensor, int i){

    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){


        double x = sensorEvent.values[0];
        double y = sensorEvent.values[1];
        double z = sensorEvent.values[2];


        double ACC = Math.sqrt(x*x + y*y + z*z);


        if(ACC >= sensitivity && canToss) {
            readings.add(ACC);
            if (readings.size() >= 20) {
                canToss = false;
                throwBall(Collections.max(readings));
            }
        }
    }


    public void throwBall(double ACC){

        final double EarthGravity = -9.81;
        double timeToHighest = ACC / -EarthGravity;
        double maxHeight = Math.pow(ACC, 2) / (2 * -EarthGravity);
        Log.d("Main", "Acceleration: " + Double.toString(ACC) + " Max Height: " + maxHeight);

        new Throw(timeToHighest, MainActivity.this, maxHeight, peak, height, ACC).execute();

        reachedTop = false;

    }
}

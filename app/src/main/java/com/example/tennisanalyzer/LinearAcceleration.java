package com.example.tennisanalyzer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LinearAcceleration implements SensorEventListener {

    private Sensor sensor;
    private SensorManager sensorManager;
    private SensorCallback sensorCallback;

    public void setSensorCallback(SensorCallback sensorCallback) {
        this.sensorCallback = sensorCallback;
    }

    public LinearAcceleration(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private final int SWING_ACCELERATION = 10; // minimum acceleration size of a swing
    private double accelerationSize;

    @Override
    public void onSensorChanged(SensorEvent event) {
        accelerationSize = Math.pow(event.values[0], 2) + Math.pow(event.values[1], 2) + Math.pow(event.values[2], 2) ;
        accelerationSize = Math.sqrt(accelerationSize);
        if (accelerationSize > SWING_ACCELERATION)
            maxAccelerationOnly(accelerationSize);
    }

    private long millis; // current time
    private long lastMillis; //current time of last sensor callback
    private final int SECOND = 1000; //1000 milliseconds == 1 second
    private double maxAcceleration; // next max acceleration

    private void maxAccelerationOnly(double accelerationSize) {
        millis = System.currentTimeMillis();

        // if acceleration is slowing and a second has past since last callback
        if (accelerationSize < maxAcceleration && millis - lastMillis > SECOND) {
            sensorCallback.sendAccelerationSize(maxAcceleration);
            maxAcceleration = 0;
            lastMillis = millis;
        }
        // acceleration is getting faster
        else {
            maxAcceleration = accelerationSize;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void pause() {
        sensorManager.unregisterListener(this);
    }

    public void resume() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

    }
}

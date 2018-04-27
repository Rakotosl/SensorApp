package com.sl.rakoto.gyroacs;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DateService extends Service {
    static {
        System.loadLibrary("native-lib");
    }
    private List<Float> gyroX = new ArrayList<>();
    private List<Float> gyroY = new ArrayList<>();
    private List<Float> gyroZ = new ArrayList<>();

    private List<Float> acsX = new ArrayList<>();
    private List<Float> acsY = new ArrayList<>();
    private List<Float> acsZ = new ArrayList<>();

    private SensorManager sensorManager;
    private Sensor gyroSensor;
    private Sensor acsSensor;
    private SensorEventListener sensorEventListener;

    private boolean isSave = false;

    private SqlDB sqlDB;
    private Timer dateTimer = new Timer();

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (!isSave) {
                    if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                        gyroX.add(event.values[0]);
                        gyroY.add(event.values[1]);
                        gyroZ.add(event.values[2]);
                    }
                    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                        acsX.add(event.values[0]);
                        acsY.add(event.values[1]);
                        acsZ.add(event.values[2]);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        sensorManager.registerListener(sensorEventListener, acsSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);

        sqlDB = new SqlDB(this);

        startTimer();
    }

    private void startTimer(){
        dateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                saveDate();
            }
        }, 1000, 60000);
    }

    private void saveDate(){
        isSave = true;
        float averageGyroX = getAvaregaDate(getFloatArr(gyroX));
        float averageGyroY = getAvaregaDate(getFloatArr(gyroY));
        float averageGyroZ = getAvaregaDate(getFloatArr(gyroZ));

        float[] gyroDate = {averageGyroX, averageGyroY, averageGyroZ};

        float averageAcsX = getAvaregaDate(getFloatArr(acsX));
        float averageAcsY = getAvaregaDate(getFloatArr(acsY));
        float averageAcsZ = getAvaregaDate(getFloatArr(acsZ));

        float[] acsDate = { averageAcsX, averageAcsY, averageAcsZ };

        clearLists();

        sqlDB.addDate(SqlHelper.UNDEFIND_TABLE, gyroDate, acsDate);
        isSave = false;
    }

    private void clearLists(){
        gyroX.clear();
        gyroY.clear();
        gyroZ.clear();

        acsX.clear();
        acsY.clear();
        acsZ.clear();
    }

    private float[] getFloatArr(List<Float> enterArr){
        float[] exportArr = new float[enterArr.size()];

        for (int i = 0; i < enterArr.size(); i++)
            exportArr[i] = enterArr.get(i);

        return exportArr;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public native float getAvaregaDate(float[] date);
}

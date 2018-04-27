package com.sl.rakoto.gyroacs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private GraphView gyroGraph;
    private GraphView acsGraph;

    private LineGraphSeries<DataPoint> xGyroGraphDate = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> yGyroGraphDate = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> zGyroGraphDate = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> xAcsGraphDate = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> yAcsGraphDate = new LineGraphSeries<>();
    private LineGraphSeries<DataPoint> zAcsGraphDate = new LineGraphSeries<>();

    private int dateCount = 0;
    private int lastCount = 0;

    private SqlDB sqlDb;

    private Timer timer = new Timer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gyroGraph = (GraphView) findViewById(R.id.gyroGraph);
        acsGraph = (GraphView) findViewById(R.id.acsGraph);

        startService(new Intent(this, DateService.class));

        sqlDb = new SqlDB(this);

        yGyroGraphDate.setColor(R.color.colorPrimaryDark);

        addGyroGraph(xGyroGraphDate);
        addGyroGraph(yGyroGraphDate);
        addGyroGraph(zGyroGraphDate);

        addAcsGraph(xAcsGraphDate);
        addAcsGraph(yAcsGraphDate);
        addAcsGraph(zAcsGraphDate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dateCount = getDateCount();
        if (dateCount > 5) {
            startGyroDate();
            startAcsDate();
        }

        addDateToGraphs();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void addDateToGraphs(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dateCount = getDateCount();
                if (lastCount < dateCount) continueGraphs();
            }
        }, 1000, 10000);
    }

    private int getDateCount(){
        return sqlDb.count(SqlHelper.UNDEFIND_TABLE) - 1;
    }

    private void startGyroDate(){
        for (int i = dateCount - 5; i < 0; i--)
            addGyroDate(i);
    }

    private void startAcsDate(){

        for (int i = dateCount - 5; i < 0; i--)
            addAcsDate(i);
    }

    private void addGyroDate(int i){
        String[] gyroDate = sqlDb.getGyroDate(dateCount - i);
        xGyroGraphDate.appendData(new DataPoint(i, Float.valueOf(gyroDate[0])), true, 40);
        yGyroGraphDate.appendData(new DataPoint(i, Float.valueOf(gyroDate[1])), true, 40);
        zGyroGraphDate.appendData((new DataPoint(i, Float.valueOf(gyroDate[2]))), true, 40);
    }

    private void addAcsDate(int i){
        String[] acsDate = sqlDb.getAcsDate(dateCount - i);
        xAcsGraphDate.appendData(new DataPoint(i, Float.valueOf(acsDate[0])), true, 40);
        yAcsGraphDate.appendData(new DataPoint(i, Float.valueOf(acsDate[1])), true, 40);
        zAcsGraphDate.appendData((new DataPoint(i, Float.valueOf(acsDate[2]))), true, 40);
    }

    private void continueGraphs(){
        addGyroDate(1);
        addAcsDate(1);
    }

    private void addGyroGraph(LineGraphSeries<DataPoint> series){
        gyroGraph.addSeries(series);
    }

    private void addAcsGraph(LineGraphSeries<DataPoint> series){
        acsGraph.addSeries(series);
    }
}

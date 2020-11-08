package ud.example.acelerometro;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    private SensorManager sensores;
    private Sensor sensor;
    private float X, Y, Z;
    private TextView ValorX, ValorY, ValorZ, LogText;
    private ScrollView scrollView;
    private int slatigo = 0;

    SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        X = 0;
        Y = 0;
        Z = 0;

        LogText = findViewById(R.id.textView2);
        scrollView = findViewById(R.id.scrollView);
        ValorX = findViewById(R.id.textView4);
        ValorY = findViewById(R.id.textView6);
        ValorZ = findViewById(R.id.textView8);


        ValorX.setText("0");
        ValorY.setText("0");
        ValorZ.setText("0");



        sensores = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensores.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensores.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        List<Sensor> listSensors = sensores.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : listSensors) {
            log("Sensor: " + sensor.getName().toString());
        }

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float x = sensorEvent.values[0];
                System.out.println("valor giro " + x +" "+slatigo);
                if(x<-5 && slatigo ==0) {
                    slatigo++;
                    scrollView.setBackgroundColor(0X0000ff);
                } else if ( x >5 && slatigo == 1) {
                    slatigo++;
                    scrollView.setBackgroundColor(0Xff0000);
                }
                if(slatigo ==2){ sound(); slatigo = 0;}
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };

   }

    private void  sound(){
        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.latigo);
        mediaPlayer.start();
    };

    private void start (){sensores.registerListener(sensorEventListener,sensor,SensorManager.SENSOR_DELAY_NORMAL);}


    public void itemClicked(View v) {
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            start();
            sound();
        }
    }

        private void log(String s){
            LogText.append("\n" + s);
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            try{
                float Xa = sensorEvent.values[0];
                float Ya = sensorEvent.values[1];
                float Za = sensorEvent.values[2];
                if(Math.abs(Xa-X)>=1 || Math.abs(Ya-Y)>=1 || Math.abs(Za-Z)>=1){
                    ValorX.setText(String.valueOf(sensorEvent.values[0]));
                    ValorY.setText(String.valueOf(sensorEvent.values[1]));
                    ValorZ.setText(String.valueOf(sensorEvent.values[2]));
                }
                X = sensorEvent.values[0];
                Y = sensorEvent.values[1];
                Z = sensorEvent.values[2];
            } catch (Exception ex){}
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}





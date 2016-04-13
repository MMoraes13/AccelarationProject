package tests.lincare.acelerometro;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        long lastUpdate = 0;
        float last_x = 0, last_y = 0, last_z = 0;
        int i = 0, count = 0;
        double [] queda = {0, 0, 0};
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 800) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                TextView textQueda = (TextView) findViewById(R.id.queda);
                queda[i] = Math.sqrt(Math.pow((x - last_x), 2) + Math.pow((y - last_y),2) + Math.pow((z - last_z),2));
                if (((queda[0] - queda[1])*(queda[0] - queda[1])) > 2 ||( (queda[1] - queda[2])*(queda[1] - queda[2]) ) > 2 || ( (queda[0] - queda[2])*(queda[0] - queda[2]) ) > 2 ) {
                    count ++;
                    textQueda.setText("QUEDA DETECTADA: "+count);
                }
                else {
                    textQueda.setText("");
                }
                last_x = x;
                last_y = y;
                last_z = z;
                lastUpdate = curTime;
                FrameLayout ball1 = (FrameLayout) findViewById(R.id.ball1);
                ball1.setVisibility(View.VISIBLE);

                TextView textX = (TextView)findViewById(R.id.eixoX);
                textX.setText("Eixo X " + x + "\n");

                TextView textY = (TextView) findViewById(R.id.eixoY);
                textY.setText("Eixo Y " + y + "\n");

                //System.out.println ("Eixo Y: "+ y);
                TextView textZ = (TextView) findViewById(R.id.eixoZ);
                textZ.setText("Eixo Z " + z + "\n");


                if (i == 2) i = 0;
                else i++;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

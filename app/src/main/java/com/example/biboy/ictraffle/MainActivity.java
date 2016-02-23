package com.example.biboy.ictraffle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.DigitsKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BufferedReader reader;
    private JSONArray student;
    ListView lv;
    ArrayList<Student> list = new ArrayList<Student>();
    MyAdapter adapter;
    private ShakeDetector mShakeDetector;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private String result="ops";
    //Button btnGet;
    private int counter, setWin;
    private String ipaddress, campus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        lv = (ListView) findViewById(R.id.listView);
        String [] temporary = {"","",""};
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
        StrictMode.setThreadPolicy(policy);


        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mShakeDetector = new ShakeDetector(
                new ShakeDetector.OnShakeListener() {


                    @Override
                    public void onShake() {
                        // Do stuff!
                        Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                        long pattern[]={0,800,200,1200,300,2000};
                        v.vibrate(pattern,-1);

                        if(counter>0)
                            Toast.makeText(getBaseContext(), "Reset the app!", Toast.LENGTH_SHORT).show();
                        else{
                            if(counter == 0 && setWin != 0)
                                getWinner();
                            else if(counter == 0)
                                setWinner();
                        }
                        v.cancel();

                    }
                });


    }

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
        if (id == R.id.campus){
            final CharSequence[] items = {"BANILAD", "LM", "MAIN"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    switch(item){
                        case 0:
                            campus = "BANILAD";
                            break;
                        case 1:
                            campus = "LM";
                            break;
                        case 2:
                            campus = "MAIN";
                            break;


                    }
                    Toast.makeText(MainActivity.this, "Campus set to "+ campus, Toast.LENGTH_SHORT).show();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        }
        if (id == R.id.setip) {

//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setTitle("SET IP");
//            final EditText input = new EditText(this);
//            input.setHint("IP ADDRESS");
//            input.setHeight(100);
//            input.setWidth(340);
//            input.setGravity(Gravity.LEFT);
//
//            input.setImeOptions(EditorInfo.IME_ACTION_DONE);
//            alertDialogBuilder.setView(input);
//            alertDialogBuilder
//                    .setCancelable(true)
//                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                            Toast.makeText(getBaseContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//
//                                public void onClick(DialogInterface dialog, int id) {
//                                    ipaddress = input.getText().toString();
//                                    Toast.makeText(getBaseContext(), "IP was set! to" + ipaddress, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                    );
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
            getWinner();
            return true;
        }

        if (id == R.id.action_settings) {
            if(counter == 0)
                setWinner();
            else
                Toast.makeText(MainActivity.this, "Please reset the app.", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.reset) {
            counter = 0;
            list.clear();
            this.lv.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setWinner(){
        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("SET");
        input.setLayoutParams(lp);
        input.setKeyListener(new DigitsKeyListener());

        alertDialogBuilder.setView(input);
        alertDialogBuilder
                .setMessage("How many numbers of winners?")
                .setCancelable(false)

                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })

                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(input.getText().toString().equals(""))
                            setWinner();
                        else{
                            if(Integer.parseInt(input.getText().toString())>5 || Integer.parseInt(input.getText().toString())<1){
                                setWinner();
                                Toast.makeText(getBaseContext(), "Set 1-5 only", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                setWin = Integer.parseInt(input.getText().toString());
                                Toast.makeText(getBaseContext(), "Set to "+ setWin +" lucky winners.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public void getWinner(){

        try {
            //Toast.makeText(MainActivity.this, "jog2xging", Toast.LENGTH_SHORT).show();
            URL url = new URL("http://192.168.1.100/ictcongress2016/allRegistered/"+campus);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(4 * 1000);
            connection.connect();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();

        }
        catch(Exception e){}


        try{

            JSONObject jo= new JSONObject(result);
            student = jo.getJSONArray("students");
            int random = 0;
            int [] noRepeating = new int[setWin];
            int temp = 0, dup = 0, temp1 = 0;


            while(temp<setWin){
                //student.getLength()
                random =(int) (Math.random()*(student.length()));
                temp1++;


                for (int i = 0; i < noRepeating.length; i++){

                    if(random==noRepeating[i]){
                        dup=1; break;
                    }
                }

                if(dup==0)
                    noRepeating[temp++]=random;

            }




            for (int i = 0; i < noRepeating.length; i++) {

                JSONObject c = student.getJSONObject(noRepeating[i]);

                String qr = c.getString("ticketNo");
                String lname = c.getString("familyName");
                String fname = c.getString("firstName");
                String id = c.getString("idno");
                String campuss = c.getString("campus");
                String name =lname+", "+fname;
                this.list.add(new Student(qr,name,"ID:"+id+" "+campus));

                this.adapter=new MyAdapter(this,list);

                this.lv.setAdapter(adapter);

            }

            counter = 1;
            Toast.makeText(MainActivity.this, "Congratulations!", Toast.LENGTH_SHORT).show();

        }catch(Exception e){}

    }
}

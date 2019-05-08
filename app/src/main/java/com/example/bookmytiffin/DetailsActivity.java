package com.example.bookmytiffin;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class DetailsActivity extends AppCompatActivity {

    EditText displayer,name, mobile;
    String temp;
    ProgressBar progressBar;
    Button submit;
    Boolean setAlready=false;
    String OTP;

    double longitude = 0.0, latitude = 0.0;
    OrderData orderData;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(DetailsActivity.this,DecisionActivity.class);
        startActivity(a);



    }
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        temp = intent.getStringExtra("Signal");

        if (temp.equals("true")) {
            OrderData orderData = new OrderData();
            displayer.setText(orderData.getAddress());
            mobile.setText(orderData.getPhoneNo());
            name.setText(orderData.getName());}
        submit.setVisibility(View.VISIBLE);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Button myLocation = findViewById(R.id.location);
        progressBar = findViewById(R.id.progressBar);
        displayer = findViewById(R.id.editText2);
        submit = findViewById(R.id.location2);
        name = findViewById(R.id.name);
        progressBar.setVisibility(View.INVISIBLE);
        mobile = findViewById(R.id.mobile);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!name.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty()) {
                    orderData = new OrderData();
                    orderData.setName(name.getText().toString());
                    orderData.setPhoneNo(mobile.getText().toString());
                    if(checkAndRequestPermissions()){
                        SmsManager smsManager = SmsManager.getDefault();
                        OTP = "";
                        Random r = new Random();
                        int i1 = r.nextInt(8000 - 3000) + 3000;
                        OTP = "" + i1;
                        //sendTextMessage(destination number, ,"message", , );
                        if(!orderData.getPhoneNo().isEmpty())
                            smsManager.sendTextMessage(orderData.getPhoneNo(), null, OTP, null, null);
                        Toast toast = Toast.makeText(getApplicationContext(), "OTP sent", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    Intent intent = new Intent(DetailsActivity.this, SMSVerficiation.class);

                    setAlready = true;
                    intent.putExtra("OTPR",OTP);
                    Log.d("TESTING","OTP IS"+OTP);
                    startActivity(intent);
                }else
                {
                    Toast.makeText(DetailsActivity.this,"Please Fill all the details",Toast.LENGTH_SHORT).show();
                }
            }
        });

        myLocation.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty() && !mobile.getText().toString().isEmpty()) {
                    setAlready =true;
                    orderData.setName(name.getText().toString());
                    orderData.setPhoneNo(mobile.getText().toString());
                }
                FusedLocationProviderClient fusedLocationClient;
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(DetailsActivity.this);
                //This is for last known location
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(DetailsActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    Intent intent = new Intent(DetailsActivity.this,MapsActivity.class);
                                    intent.putExtra("LAT",location.getLatitude()+"");
                                    intent.putExtra("LONG",location.getLongitude()+"");
                                    startActivity(intent);


                                }
                                else {
                                    //Start async task and search for location
                                    GPSLocation gpsLocation = new GPSLocation();
                                    gpsLocation.execute();
                                }
                            }
                        });
            }
        });
    }



    public class GPSLocation extends AsyncTask<String,Integer,String> implements LocationListener {
        ProgressDialog progressDialog;
        boolean running = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetailsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }

            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 5, this);
            progressBar.setProgress(1);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);

            // Things to be done while execution of long running operation is in progress. For example updating ProgessDialog
        }

        @Override
        protected void onPostExecute(String s)
        {
            progressBar.setVisibility(View.GONE);
            Intent intent = new Intent(DetailsActivity.this,MapsActivity.class);
            intent.putExtra("LAT",latitude+"");
            intent.putExtra("LONG",longitude+"");
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... params) {
            boolean isDataSubmitted = false;

            while(!isDataSubmitted)
            {
                if(longitude !=0.0 && latitude!=0.0)
                {
                    isDataSubmitted = true;
                }
            }

            return "service completed";
        }

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);

        int receiveSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);

        int readSMS = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    201);
            return false;
        }
        return true;
    }






}
package com.example.bookmytiffin;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SMSVerficiation extends AppCompatActivity {

     EditText editText;
     Button verify;
    String OTPSENT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsverficiation);
        verify = findViewById(R.id.verify);
        editText = findViewById(R.id.editText3);
        Intent intent= getIntent();
       OTPSENT = intent.getStringExtra("OTPR");
        Log.d("TESTING","final"+OTPSENT);
                verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String OTPREC = editText.getText().toString();
                        Log.d("test","final"+OTPREC);

                        if(OTPREC.equalsIgnoreCase(OTPSENT)){
                            OrderData orderData = new OrderData();
                            SharedPreferences sharedpreferences = getSharedPreferences("MyDetails", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("PHONE",orderData.getPhoneNo() );
                            editor.commit();
                        Intent intent = new Intent(SMSVerficiation.this,InvoiceActivity.class);
                        startActivity(intent);}
                    }
                });


    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");


                editText.setText(message);
            }
        }
    };
    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).
                registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


}

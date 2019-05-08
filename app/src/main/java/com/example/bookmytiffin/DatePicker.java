package com.example.bookmytiffin;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DatePicker extends AppCompatActivity {
    AlertDialog alertDialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        Intent intent = getIntent();
        String planner = new String("");
        planner = intent.getStringExtra("plan");
        Log.d("TESTING", planner);

        final CalendarPickerView calendar_view = (CalendarPickerView) findViewById(R.id.calendar_view);
//getting current
        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        Date today = new Date();

//add one year to calendar from todays date
        calendar_view.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.RANGE);
        //fetch dates
        final List<Date> dates = calendar_view.getSelectedDates();

        calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                dates.add(date);
                // Toast.makeText(getApplicationContext(),"Selected Date is : " +date.toString(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDateUnselected(Date date) {
                dates.remove(date);
                //Toast.makeText(getApplicationContext(),"UnSelected Date is : " +date.toString(),Toast.LENGTH_SHORT).show();
            }
        });


        FloatingActionButton btn_show_dates = findViewById(R.id.btn_show_dates);
        btn_show_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date endDate = dates.get(dates.size() - 1);
                Date fromDate = dates.get(dates.size() - 2);
                //here you can fetch all dates
                //Toast.makeText(getApplicationContext(), dates.get(dates.size() - 1).toString() + " and " + dates.get(dates.size() - 2), Toast.LENGTH_SHORT).show();
                if (fromDate.toString().isEmpty() || endDate.toString().isEmpty()) {
                    Snackbar.make(findViewById(R.id.constraint), "Please select two dates", Snackbar.LENGTH_LONG).show();

                }
                int noOfDays = getDaysDifference(fromDate, endDate);
                OrderData orderData = new OrderData();
                orderData.setNoOfDays(String.valueOf(noOfDays));
                orderData.setStartDate(fromDate.toString());
                orderData.setEndDate(endDate.toString());
                //Toast.makeText(getApplicationContext(), "No of days are " + noOfDays, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(DatePicker.this, DetailsActivity.class);
                displayLocationSettingsRequest(DatePicker.this);
                intent.putExtra("Signal", "false");
                startActivity(intent);

            }
        });
    }

    public static int getDaysDifference(Date fromDate, Date toDate) {

        if (fromDate == null || toDate == null)
            return 0;



        return (int) (((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24)) + 1);
    }

        private void displayLocationSettingsRequest (final DatePicker context){
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Log.i("TEST", "All location settings are satisfied.");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i("TEST", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result
                                // in onActivityResult().
                                status.startResolutionForResult(context, 1);
                            } catch (IntentSender.SendIntentException e) {
                                Log.i("TEST", "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Log.i("TEST", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                }
            });
        }

}

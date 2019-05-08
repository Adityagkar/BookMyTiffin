package com.example.bookmytiffin;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.bookmytiffin.OrderData.mealType;

public class PlanNoDays extends AppCompatActivity {
    TextView tiffinCenter;
    long breakCost,lunchCost,dinnerCost,totalCost;
    String temp;
    DatabaseReference myRef;
    int i=0;
    CheckBox breakfast, lunch, dinner;
    ProgressBar progressBar;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(PlanNoDays.this,DecisionActivity.class);
        startActivity(a);
        finish();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_no_days);
        progressBar = findViewById(R.id.progressBar2);

        breakfast = findViewById(R.id.checkBox);
        lunch = findViewById(R.id.checkBox2);
        dinner = findViewById(R.id.checkBox4);

        breakCost=lunchCost=dinnerCost=totalCost= Long.valueOf(0);

        tiffinCenter = findViewById(R.id.tiffinCenterName);
        final Intent intent = getIntent();
         temp = intent.getStringExtra("TiffinName");
        tiffinCenter.setText(temp);


        Button confirm = findViewById(R.id.button5);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(dinner.isChecked() || lunch.isChecked() || breakfast.isChecked()){
                    Calculator calculator = new Calculator();
                    calculator.execute();

                }else {
                    Toast.makeText(getApplicationContext(),"Please Select a Meal Plan",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public class Calculator extends AsyncTask<String,Integer,String>{

        @Override
        protected String doInBackground(String... strings) {
            while (totalCost==0.0){
                progressBar.setProgress(i);
                i++;

            }

           return null;
        }

        @Override
        protected void onPreExecute() {
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            myRef = database.getReference("TiffinServices");
            progressBar.setVisibility(View.VISIBLE);
            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    String value = "temp";
                    HashMap<Object,Object> map = (HashMap<Object, Object>) dataSnapshot.child("Row-"+temp).getValue();
                    String rowValue = (String) map.get("Kitchen Name");
                    Long breakfastV = (long) map.get("BPlan");
                    breakCost=breakfastV;
                    Long lunchV = (Long) map.get("LPlan");
                    lunchCost = lunchV;
                    Long dinnerV = (Long) map.get("DPlan");
                    dinnerCost = dinnerV;
                    long sum = 0;

                    String mealTypeChosen = "";
                    if(dinner.isChecked()) {
                        sum += dinnerV;
                        mealTypeChosen = mealTypeChosen+"d";
                    }if(lunch.isChecked()) {
                        sum += lunchV;
                        mealTypeChosen = mealTypeChosen+"l";
                    }if(breakfast.isChecked()) {
                        sum += breakfastV;
                        mealTypeChosen = mealTypeChosen+"b";
                    }Log.d("TESTINGDB", "Value is: " + rowValue+ " "+sum);
                    OrderData orderData = new OrderData();
                    orderData.setMealType(mealTypeChosen);
                    totalCost = sum;
                    Log.d("TESTINGDB", "Value of totalCost is: " +totalCost );

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TESTINGDB", "Failed to read value.", error.toException());
                }
            });

        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            Intent intent1 = new Intent(PlanNoDays.this, DatePicker.class);
            intent1.putExtra("plan", totalCost+"");
            OrderData orderData = new OrderData();
            orderData.setMealPlanCost(totalCost+"");
            startActivity(intent1);

        }
    }


}

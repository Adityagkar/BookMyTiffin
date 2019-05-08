package com.example.bookmytiffin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InvoiceActivity extends AppCompatActivity {
    Double cost;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        Button payLater = findViewById(R.id.later);


        TextView textView = findViewById(R.id.invoice);
        TextView finalTotal = findViewById(R.id.total);
        final OrderData orderData = new OrderData();
        cost = Double.valueOf(orderData.getMealPlanCost())*Double.valueOf(orderData.getNoOfDays());

        String orderId = orderData.getId();
        String orderName = orderData.getName();
        String resNAme = orderData.getRestaurantName();
        String getNoOfDays = orderData.getNoOfDays();
        String StartDate = orderData.getStartDate();
        String EndDate = orderData.getEndDate();
        String Cost = orderData.getMealPlanCost();
        String address = orderData.getAddress();
        String total = cost+"";
        orderData.setTotalCost(cost+"");

        String display = "\n"+"Email id: " +orderId + "\n" +
                "Customer Name: "+ orderName+"\n"+
                "Restaurant Name: "+ resNAme+ "\n"+
                "No. of Days: "+getNoOfDays+ "\n"+
                "Cost of Meal per day: Rs. "+ Cost+"\n"
                +"Address: "+ address;

        display.toUpperCase();
        textView.setText(display);
        finalTotal.setText("\n Total Bill: Rs. "+cost);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("OrderTable");
        String key = myRef.child("USERS").child("").push().getKey();

        Log.d("TESTING","THIS WORKS YEAH !");
        myRef.child(orderId).child(key).child("Name").setValue(orderName);
        myRef.child(orderId).child(key).child("TCentre").setValue(resNAme);
        myRef.child(orderId).child(key).child("Address").setValue(address);
        myRef.child(orderId).child(key).child("StartDate").setValue(StartDate);
        myRef.child(orderId).child(key).child("EndDate").setValue(EndDate);
        myRef.child(orderId).child(key).child("Mobile").setValue(orderData.getPhoneNo());
        myRef.child(orderId).child(key).child("Cost of meal").setValue(cost);
        myRef.child(orderId).child(key).child("Meal").setValue(orderData.getMealType());
        myRef.child(orderId).child(key).child("Total Cost").setValue(total);
        myRef.child(orderId).child(key).child("TiffinsLeft").setValue(Integer.valueOf(orderData.getNoOfDays()));


        payLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvoiceActivity.this, MyProfileActivity.class);
                startActivity(intent);

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(InvoiceActivity.this,DecisionActivity.class);
        startActivity(a);



    }
}

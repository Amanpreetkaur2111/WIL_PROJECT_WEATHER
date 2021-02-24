package com.example.we_protectapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PaymentActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    EditText cardName,cardNumber,expDate,cvv;
    Button saveButton;


    SharedPreferences preff;
    public  static String PAYMENT_PREFF = "payment_preffs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        drawerLayout = findViewById(R.id.drawer_layout);

        cardName = findViewById(R.id.cardHolder);
        cardNumber = findViewById(R.id.cardNo);
        expDate = findViewById(R.id.expDate);
        cvv = findViewById(R.id.cvv);
        preff =getSharedPreferences(PAYMENT_PREFF,MODE_PRIVATE);

        saveButton = findViewById(R.id.saveData);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preff.edit();
                editor.putString("cardName",cardName.getText().toString());
                editor.putString("cardNumber",cardNumber.getText().toString());
                editor.putString("expDate",expDate.getText().toString());
                editor.putString("cvv",cvv.getText().toString());
                editor.commit();

                //here DbOperations class is called where
                DataBaseOperations dop  = new DataBaseOperations(PaymentActivity.this);
                int count =  dop.getCount(DataBaseOperations.PAYMENT_TABLE);

                    //here it puts all the information into database from the edit texts
                    dop.putPayment(dop,cardName.getText().toString(),cardNumber.getText().toString(),cvv.getText().toString(),expDate.getText().toString());


                cardName.setText(preff.getString("cardName",""));
                cardNumber.setText(preff.getString("cardNumber",""));
                expDate.setText(preff.getString("expDate",""));
                cvv.setText(preff.getString("cvv",""));
                startActivity(new Intent(PaymentActivity.this,MainActivity.class));
                finish();


            }
        });
        cardName.setText(preff.getString("cardName",""));
        cardNumber.setText(preff.getString("cardNumber",""));
        expDate.setText(preff.getString("expDate",""));
        cvv.setText(preff.getString("cvv",""));


    }



    // End of Create Method

    public void ClickMenu(View view){

        // open drawer
        MainActivity.openDrawer(drawerLayout);

    }

    public void ClickLogo(View view){

        MainActivity.closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view){

        MainActivity.redirectActivity(this,ProfileActivity.class);
    }

    public void ClickPayment(View view){
        MainActivity.redirectActivity(this,PaymentActivity.class);
    }

    public void ClickFeatures(View view){
        redirectActivity(this,FeaturesActivity.class);
    }



    public static void redirectActivity(Activity activity, Class aClass) {

        // Initialize Intent

        Intent intent = new Intent(activity,aClass);
        //Set Flag

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Start Activity
        activity.startActivity(intent);
    }



    @Override
    protected void onPause() {
        super.onPause();

        MainActivity.closeDrawer(drawerLayout);
    }
}

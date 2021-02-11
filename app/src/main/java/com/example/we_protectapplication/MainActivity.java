package com.example.we_protectapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    // initialize variables

    DrawerLayout drawerLayout;

    TextView tempText,descEd;
    EditText postalCodeEd;
    String req = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign Variables

        drawerLayout = findViewById(R.id.drawer_layout);


        tempText = (TextView) findViewById(R.id.tempText);
        descEd = (TextView) findViewById(R.id.desc);
        postalCodeEd = (EditText) findViewById(R.id.postalcode);
        final Button search = (Button) findViewById(R.id.button);
         postalCodeEd.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                 if(charSequence.length()==6){
                     search.setVisibility(View.VISIBLE);
                 } else {
                     search.setVisibility(View.GONE);
                 }

                 if(charSequence.length() == 3){
                     req = charSequence+"";

                 }



             }

             @Override
             public void afterTextChanged(Editable s) {

             }
         });







    }

    public void ClickMenu(View view){

        // Open Drawer

        openDrawer(drawerLayout);


    }

    public static void openDrawer(DrawerLayout drawerLayout) {

        // Open Drawer Layout

        drawerLayout.openDrawer(GravityCompat.START);

    }


    public void ClickLogo(View view){

        // Close Drawer

        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {

        // close drawer layout
        // check condition


        if(drawerLayout.isDrawerOpen(GravityCompat.START)){

            // when drawer is open
            //close drawer

            drawerLayout.closeDrawer(GravityCompat.START);

        }

    }


    public void ClickProfile(View view){

        // Recreate activity

        recreate();

    }


    public void ClickPayment(View view){
        // redirect Activity

        redirectActivity(this,PaymentActivity.class);
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
        // close drawer

        closeDrawer(drawerLayout);
    }
}

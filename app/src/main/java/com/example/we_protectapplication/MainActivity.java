package com.example.we_protectapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    // initialize variables

    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign Variables

        drawerLayout = findViewById(R.id.drawer_layout);


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

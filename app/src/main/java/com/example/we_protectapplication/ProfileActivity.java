package com.example.we_protectapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ProfileActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    private static final int CONTENT_REQUEST=1337;
    private File output=null;
    ImageView profile;
    EditText fname,lname;
    SharedPreferences preferences;
    public static String PROFILE_PREFF = "profilepreff";
    String imageDecode = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawer_layout);

       // getSupportActionBar().setTitle("Profile");
        profile = findViewById(R.id.profilePicture);
        fname = findViewById(R.id.firstName);
        lname = findViewById(R.id.lastName);
        preferences = getSharedPreferences(PROFILE_PREFF,MODE_PRIVATE);

   // when you click on the image to open the camera

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //this passes the intent to open open
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CONTENT_REQUEST);//this opens the camera window
                }
            }

        });

        fname.setText(preferences.getString("firstName","")); // setting the old data from shared preffs
        lname.setText(preferences.getString("lastName",""));///setting the old data from shared preffs
        if (preferences.getString("profilePic","").equals("")){


        }else {
            profile.setImageBitmap(decodeBase64(preferences.getString("profilePic",""))); //setting back the Image if data
            // is aleardy strored in sharedpreff
        }

        Button save = findViewById(R.id.save);
        //when you click on save button
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //in this 4 lines we are saving data into sharedprefferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("firstName",fname.getText().toString());
                editor.putString("lastName",lname.getText().toString());
                editor.putString("profilePic",imageDecode);
                editor.commit();
                //here below this we are call DbOperations class and saving data inside Sqlite Database
                DataBaseOperations dop  = new DataBaseOperations(ProfileActivity.this);
                int count =  dop.getCount(DataBaseOperations.PROFILE_TABLE);
                //database is empty then save data into it
                    dop.putProfile(dop,fname.getText().toString(),lname.getText().toString(),imageDecode);

                //after everything done go to main activity
                startActivity(new Intent(ProfileActivity.this,MainActivity.class));
                finish();

            }
        });


    }

    // End of Create Method


    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);// this is the converted string imageEncoded
        // which we will save into the database

        return imageEncoded;
    }



    private Bitmap decodeBase64(String input) {

        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CONTENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (requestCode == CONTENT_REQUEST && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data"); //here we get the image
                    profile.setImageBitmap(imageBitmap);// here we set that image to image view
                    imageDecode =   encodeTobase64(imageBitmap);

                }
            }
        }

    }

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

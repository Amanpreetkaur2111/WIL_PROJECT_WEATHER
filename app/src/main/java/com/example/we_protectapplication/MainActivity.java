package com.example.we_protectapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


//import com.android.volley.AuthFailureError;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.VolleyLog;
//import com.android.volley.toolbox.JsonObjectRequest;





import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.we_protectapplication.ProfileActivity.PROFILE_PREFF;


public class MainActivity extends AppCompatActivity {


    // initialize variables


    String Auth_key = "d74cffcd8b6d73ef616c923095e6a13d";  //Auth key for the api request
    TextView tempText,descEd;
    EditText postalCodeEd;
    ProgressBar progressBar;
    String req = "";

    DrawerLayout drawerLayout;

    LinearLayout navlayout;

    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign Variables

        drawerLayout = findViewById(R.id.drawer_layout);




        Log.i("ONCREATE CALLED", "onCreate: ");

        tempText = (TextView) findViewById(R.id.tempText);
        descEd = (TextView) findViewById(R.id.desc);
        postalCodeEd = (EditText) findViewById(R.id.postalcode);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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


        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String postalCode = postalCodeEd.getText().toString();
                char[] digs = postalCode.toCharArray();
                if (checkPostalCode(postalCode)){
                    progressBar.setVisibility(View.VISIBLE);
                    String upToNCharacters = postalCode.substring(0, Math.min(postalCode.length(), 3));
                   // makeJsonObjReq(upToNCharacters);
                    Log.d("check",upToNCharacters);
                }
                else {
                    postalCodeEd.setError("Wrong value");
                    progressBar.setVisibility(View.GONE);
                }


            }

        });


    }

    // End of Create Method

    boolean flag =false;

    // check Postal Method

    boolean checkPostalCode(String string){

        char[] letters = string.toCharArray(); //string is converted into char array like Sachin becomes [S,a,c,h,i,n]

        for (int i = 0;i<letters.length;i++){ //loop through the length of array letters[]
            Log.d("Letters",letters[i]+"..."); //Remaining

            if (i%2==0){    // checking odd places and mathching them for alphabets below
                String regexStr = "[a-zA-Z]";
                if (String.valueOf(letters[i]).matches(regexStr)){
                    Log.d("Test match","Mathched..."+letters[i]);
                    flag = true;

                }else {
                    Log.d("Test match","Not Mathched"+letters[i]);
                    flag = false;
                    break;
                }
                Log.d("Even","..."+letters[i]+"....."+i);
            }else {// checking even places and mathching them for numbers below
                String regex = "\\d+";

                if (String.valueOf(letters[i]).matches(regex)){
                    Log.d("Test match","Mathched..."+letters[i]);
                    flag = true;

                }else {
                    Log.d("Test match","Not Mathched"+letters[i]);
                    flag = false;
                    break;
                }
            }

        }


        return flag; //result is shown by this boolean flag if true code valid if not invalid

    }



    // json method

//    private void makeJsonObjReq(final String postalcode) {
//
//        String url = "http://api.openweathermap.org/data/2.5/weather?zip="+postalcode+",ca";// url on which request is done
//
//        Log.i("aaaa", "makeJsonObjReq: " + url);
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, // get request to fetch data
//                url, new JSONObject(),
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {// here we get the response after request
//                        Log.d("Response", response.toString());
//
//                        try {
//                            JSONObject main = response.getJSONObject("main");
//// parsing the json
//                            double tempr = main.getInt("temp");
//                            String temp  = String.valueOf(tempr - 273);
//                            double temp_min = main.getInt("temp_min");
//                            String min  = String.valueOf(temp_min - 273);
//                            double temp_max = main.getInt("temp_max");
//                            String max  = String.valueOf(temp_max - 273);
//
//                            tempText.setText(temp+" \u00b0C");
//
//
//                            progressBar.setVisibility(View.GONE);
//
//                            JSONArray weather = response.getJSONArray("weather");
//                            for (int i = 0;i < weather.length();i++){
//                                JSONObject object = weather.getJSONObject(i);
//                                descEd.setText(object.getString("main"));  //
//                            }
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override // if any error comes its caught here
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("error", "Error: " + error.getMessage());
//                Log.d("error", error.toString());
//                postalCodeEd.setError("Invalid Postal Code");
//                progressBar.setVisibility(View.GONE);
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
//                headers.put("charset", "utf-8");
//                headers.put("x-api-key", Auth_key);// we have to pass header to authenticate the request
//                return headers;
//            }
//
//
//        };
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq);
//
//        // Cancelling request
//        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
//    }









    public void ClickMenu(View view){

        // Open Drawer

        openDrawer(drawerLayout);
        SharedPreferences  preferences = getSharedPreferences(PROFILE_PREFF,MODE_PRIVATE);


        LayoutInflater inflater = this.getLayoutInflater();
        LinearLayout navlayout = (LinearLayout)inflater.inflate(R.layout.main_nav_drawer, null);
        TextView user_name = (TextView) drawerLayout.findViewById(R.id.username);
        ImageView user_image = (ImageView) drawerLayout.findViewById(R.id.userImage);
        user_name.setText(preferences.getString("firstName","")+" "+preferences.getString("lastName",""));
        user_image.setImageBitmap(decodeBase64(preferences.getString("profilePic","")));

    }


    private Bitmap decodeBase64(String input) {

        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);

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

        redirectActivity(this,ProfileActivity.class);

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

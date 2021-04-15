package com.example.we_protectapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeaturesActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageView cameraIcon;
    private static final int CONTENT_REQUEST=1337;
    public LocationManager locationManager;
    public MyLocationListener listener;
    static Location myLoc;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String Auth_key = "5927335b06c4d2d356d4adeba3889c03";
    Button compareButton;
    EditText et_location2_zip;
    ImageView alert_IV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);

        drawerLayout = findViewById(R.id.drawer_layout);

        cameraIcon = findViewById(R.id.image);
        compareButton = findViewById(R.id.compare);
        et_location2_zip = findViewById(R.id.location2_zip);
        alert_IV = findViewById(R.id.alerts);

        alert_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FeaturesActivity.this, NewsAlert.class);
                startActivity(intent);

            }
        });



        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        checkLocationPermission();

        cameraIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, CONTENT_REQUEST);
                }
            }

        });


        compareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String postalCode = et_location2_zip.getText().toString();
                char[] digs = postalCode.toCharArray();
                if (checkPostalCode(postalCode)){

                    String upToNCharacters = postalCode.substring(0, Math.min(postalCode.length(), 3));
                    makeJsonObjReq(upToNCharacters);
                    Log.d("check",upToNCharacters);
                }
                else {
                    et_location2_zip.setError("Wrong value");

                }


            }

        });




    }


    // End of Create Method
    boolean flag =false;

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




    private void makeJsonObjReq(final String postalcode) {

        String url = "http://api.openweathermap.org/data/2.5/weather?zip="+postalcode+",ca";// url on which request is done

        Log.i("aaaa", "makeJsonObjReq: " + url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, // get request to fetch data
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {// here we get the response after request
                        Log.d("Response", response.toString());

                        try {
                            JSONObject main = response.getJSONObject("main");
                            //coord
                            JSONObject coord = response.getJSONObject("coord");
                            double lon = coord.getDouble("lon");
                            double lat = coord.getDouble("lat");



                            myLoc = new Location("pincodelocation");
                            myLoc.setLongitude(lon);
                            myLoc.setLatitude(lat);
// parsing the json
                            double tempr = main.getInt("temp");
                            String temp  = String.valueOf(tempr - 273);
                            double temp_min = main.getInt("temp_min");
                            String min  = String.valueOf(temp_min - 273);
                            double temp_max = main.getInt("temp_max");
                            String max  = String.valueOf(temp_max - 273);


                            TextView tv_temp_loc2 = findViewById(R.id.location2_temp);
                            tv_temp_loc2.setText(temp+" \u00b0C");

                            TextView tv_mintemp_loc2 = findViewById(R.id.location2_mintemp);
                            tv_mintemp_loc2 .setText("Min "+min+" \u00b0C");

                            TextView tv_maxtemp_loc2 = findViewById(R.id.location2_maxtemp);
                            tv_maxtemp_loc2 .setText("Max "+max+" \u00b0C");

                            TextView tv_desc_loc2 = findViewById(R.id.location2_desc);


                            ImageView iv_loc2 = findViewById(R.id.location2_image);






                            JSONArray weather = response.getJSONArray("weather");

                            for (int i = 0;i < weather.length();i++){
                                JSONObject object = weather.getJSONObject(i);
                                tv_desc_loc2.setText(object.getString("main"));

                                String iconImg = object.getString("icon");
                                String iconURL = "https://openweathermap.org/img/wn/"+iconImg+"@4x.png";



                                new DownLoadImageTask(iv_loc2).execute(iconURL);


                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override // if any error comes its caught here
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                Log.d("error", error.toString());
                et_location2_zip.setError("Invalid Postal Code");

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                headers.put("x-api-key", Auth_key);// we have to pass header to authenticate the request
                return headers;
            }


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }






    @Override//again camera feature when camera window closes it returns the image
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == CONTENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (requestCode == CONTENT_REQUEST && resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");//here
                    cameraIcon.setImageBitmap(imageBitmap);//here set the image to imageview
                    if (myLoc !=  null){
                        makeJsonObjReq(myLoc);//and here when image is set. this line send request the api for current weather according to
                        //the location
                    }


                }
            }
        }
    }



    public class MyLocationListener implements LocationListener {


        public void onLocationChanged(final Location loc) {
            Log.i("*****************", "Location changed");
            Log.d("Location",loc.getLatitude()+"...."+loc.getLongitude());

            myLoc = loc;//here you get you location



        }

        public void onProviderDisabled(String provider) {
//            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
//            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }


    }



    //this method is checking if user has location permissio or not and if not then open a dialog to take location perission
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


                ActivityCompat.requestPermissions(FeaturesActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);



            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            }
            return false;
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, listener);

            return true;
        }
    }






    //this the method which takes the address from above method and returns the weather
    private void makeJsonObjReq(Location loc) {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+loc.getLatitude()+"&lon="+loc.getLongitude()+"";


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());

                        try {
                            JSONObject main = response.getJSONObject("main");

                            double tempr = main.getInt("temp");
                            String temp  = String.valueOf(tempr - 273);
                            double temp_min = main.getInt("temp_min");
                            String min  = String.valueOf(temp_min - 273);
                            double temp_max = main.getInt("temp_max");
                            String max  = String.valueOf(temp_max - 273);


                            // after amking UI we will set thiese

                            TextView tv_temp_loc1 = findViewById(R.id.location1_temp);
                            tv_temp_loc1.setText(temp+" \u00b0C");

                            TextView tv_mintemp_loc1 = findViewById(R.id.location1_mintemp);
                            tv_mintemp_loc1 .setText("Min "+min+" \u00b0C");

                            TextView tv_maxtemp_loc1 = findViewById(R.id.location1_maxtemp);
                            tv_maxtemp_loc1 .setText("Max "+max+" \u00b0C");

                            TextView tv_desc_loc1 = findViewById(R.id.location1_desc);


                            ImageView iv_loc1 = findViewById(R.id.location1_image);




                            Log.d("values",temp);
                            Log.d("values",min);
                            Log.d("values",max);

                            JSONArray weather = response.getJSONArray("weather");
                            for (int i = 0;i < weather.length();i++){
                                JSONObject object = weather.getJSONObject(i);
                                tv_desc_loc1.setText(object.getString("main"));



                                String iconImg = object.getString("icon");
                                String iconURL = "https://openweathermap.org/img/wn/"+iconImg+"@4x.png";



                                new DownLoadImageTask(iv_loc1).execute(iconURL);



                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("error", "Error: " + error.getMessage());
                Log.d("error", error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("charset", "utf-8");
                headers.put("x-api-key", Auth_key);
                return headers;
            }


        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
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

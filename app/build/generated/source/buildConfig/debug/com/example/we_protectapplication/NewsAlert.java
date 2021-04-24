package com.example.we_protectapplication;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


//this is highlights page it creates the list and show here in this page
public class NewsAlert extends AppCompatActivity {
   private RecyclerView mRecyclerview;
    private NewsModel model;
    private ArrayList<NewsModel> list = new ArrayList<>();
    MyAdapter mAdapter;
    String Auth_key = "d74cffcd8b6d73ef616c923095e6a13d";
    DrawerLayout drawerLayout;

    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsalert);
       mRecyclerview = findViewById(R.id.Reclist);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new MyAdapter(this);
        mRecyclerview.setAdapter(mAdapter);//setting the adapter to the list
       mRecyclerview.setHasFixedSize(false);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        drawerLayout = findViewById(R.id.drawer_layout);
    //createData();

        makeJsonObjReq();

    }


    //this is creating dummy data to show in that list

    void createData() {
        NewsModel model = new NewsModel();
        model.setTitle("freezing rain");
        model.setDescription("Freezing rain warnings are issued when rain falling in sub-zero temperatures creates ice build-up and icy surfaces");
        list.add(model);
        NewsModel model1 = new NewsModel();
        model1.setTitle("rainfall");
        model1.setDescription("Heavy downpours can cause flash floods and water pooling on roads. Localized flooding in low-lying areas is possible.Rainfall warnings are issued when significant rainfall is expected");
        list.add(model1);
        NewsModel model2 = new NewsModel();
        model2.setTitle("wind");
        model2.setDescription("Very strong damaging wind gusts up to 120 km/h beginning by Sunday morning.Easterly winds will increase late this evening to gusting 90 km/h by the overnight period. Winds will continue to strengthen early Sunday morning, possibly reaching up to 120 km/h.Conditions will improve Sunday night");
        list.add(model2);
        NewsModel model3 = new NewsModel();
        model3.setTitle("blizzard");
        model3.setDescription("Blizzard conditions with gusty winds and visibility frequently near zero in snow and blowing snow are expected or occurring");
        list.add(model3);

        mAdapter.setData(list);

    }


    private void makeJsonObjReq() {

        String url = "http://api.openweathermap.org/data/2.5/onecall?lat=52.3776&lon=-114.9139&callback";

        Log.i("aaaa", "makeJsonObjReq: " + url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, // get request to fetch data
                url, new JSONObject(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {// here we get the response after request
                        Log.d("Response", response.toString());

                        try {

                            JSONArray alert = response.getJSONArray("alerts");

                            for (int i = 0;i < alert.length();i++){
                                JSONObject object = alert.getJSONObject(i);

                                NewsModel model = new NewsModel();
                                model.setTitle(object.getString("event"));
                                model.setDescription(object.getString("description"));
                                list.add(model);

                            }


                            mAdapter.setData(list);

// parsing the json




                       } catch (JSONException e) {
                           e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override // if any error comes its caught here
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
                headers.put("x-api-key", Auth_key);// we have to pass header to authenticate the request
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
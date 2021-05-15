package com.example.davidlee1.whats_for_dinner;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;


//Main Page Functions
public class MainActivity extends AppCompatActivity{

    private Button btnptoDraw, btnptoSearch, btnptonearbyrest;    //button next page to ...
    //public int loginState = 0; // 0: log out; 1: logged in
    private TextView maintitletv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView locate
        maintitletv = (TextView)findViewById(R.id.maintv_title);
        maintitletv.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));


        //Button Functions in main page

        btnptoDraw = (Button)findViewById(R.id.mainbt_draw);
        btnptoDraw.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));
        btnptoSearch = (Button)findViewById(R.id.mainbt_restsearch);
        btnptoSearch.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));
        //btnptoLog = (Button)findViewById(R.id.mainbt_login);
        //btnptoLog.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));
        //btnptoFav = (Button)findViewById(R.id.mainbt_favlist);
        //btnptoFav.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));
        btnptonearbyrest = (Button)findViewById(R.id.mainbt_nearbyrest);
        btnptonearbyrest.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));


        btnptoDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , restaurant_draw.class);
                startActivity(intent);
            }
        });
/*
        btnptoLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , login.class);
                startActivity(intent);
            }
        });
*/
        btnptoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("asd34551");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , rest_searchbyspinner.class);
                startActivity(intent);
                //System.out.println("asd");
            }
        });
/*
        btnptoFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , MyFav.class);
                startActivity(intent);
            }
        });
*/

        btnptonearbyrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this , MapsActivity.class);
                startActivity(intent);
            }
        });

    }


    /*********************** Connect to Server ***********************/
    //main page 中需要向伺服器取得帳號是否登入

}

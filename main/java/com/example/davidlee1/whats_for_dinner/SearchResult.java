package com.example.davidlee1.whats_for_dinner;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class SearchResult extends AppCompatActivity {
    private ListView rest;
    String[] shopname;
    String[] web;
    int JAlength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle bundle0311 = this.getIntent().getExtras();
        String city, addr, food;
        city = bundle0311.getString("city").toString();
        addr = bundle0311.getString("addr").toString();
        food = bundle0311.getString("food").toString();

        getData(city , addr , food);

    }

    //向PHP取得資料，並在logcat中顯示成功或失敗
    private void getData(final String city, final String addr , final String type) {
        //PHP網址
        String urlRange = "http://140.136.148.202/search.php";
        StringRequest StringRequest = new StringRequest(Request.Method.POST,
                urlRange,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //成功時顯示回傳資料
                        Log.d("PHP_TAG", "response = " + response);
                        //將資料轉成JSONArray並傳到markJson
                        try {
                            JSONArray restaurantJSONArray = new JSONArray(response);
                            GETJson(restaurantJSONArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //失敗時顯示錯誤訊息
                        Log.e("PHP_ERROR_TAG", "error : " + error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("city", city);
                map.put("addr", addr);
                map.put("type", type);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(StringRequest);
    }

    //將取得的資料標記在地圖上
    private void GETJson(JSONArray jsonArray) {
        JAlength = jsonArray.length();
        shopname = new String[JAlength];
        web = new String[JAlength];
        Arrays.fill(shopname, null);
        Arrays.fill(web, null);
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                shopname[i] = o.getString("shop_name");
                web[i] = o.getString("web_addr");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        rest = (ListView)findViewById(R.id.rest);

        ArrayAdapter<String> arrayAdapterest;
        arrayAdapterest = new ArrayAdapter<>(
                this, android.R.layout.simple_expandable_list_item_1, shopname);

        rest.setAdapter(arrayAdapterest);

        rest.setOnItemClickListener(restListener);

    }

    private ListView.OnItemClickListener restListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent , View view , int position , long id){
            Uri uri = Uri.parse(web[position]);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(it);
        }
    };
}

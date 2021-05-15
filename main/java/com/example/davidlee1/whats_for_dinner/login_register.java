package com.example.davidlee1.whats_for_dinner;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class login_register extends AppCompatActivity {

    private Button btcreate;
    private EditText rid, rpassword,rpasswordcheck;
    AlertDialog.Builder builder;

    String server_url = "http://140.136.148.202/user.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        //Variables locate
        rid = (EditText) findViewById(R.id.lgret_ID);
        rid.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));
        rpassword = (EditText) findViewById(R.id.lgret_password);
        rpassword.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));
        rpasswordcheck = (EditText) findViewById(R.id.lgret_passwordcheck);
        rpasswordcheck.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));

        //Button locate
        btcreate = (Button) findViewById(R.id.lgrbt_create);
        btcreate.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));


        //Button function
        btcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String RID, RPASSWORD, RPASSWORDCHECK;
                RID = rid.getText().toString();
                RPASSWORD = rpassword.getText().toString();
                RPASSWORDCHECK = rpasswordcheck.getText().toString();

                if (RPASSWORD.equals(RPASSWORDCHECK)) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    builder.setTitle("Server Response");
                                    builder.setMessage("Response :" + response);
                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            rid.setText("");
                                            rpassword.setText("");
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }
                            , new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(login_register.this, "Error...", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("id", RID);
                            params.put("password", RPASSWORD);
                            return params;
                        }
                    };
                    MySingleton.getmInstance(login_register.this).addTorequestque(stringRequest);

                } else {
                    //產生視窗物件
                    new AlertDialog.Builder(login_register.this)
                            .setTitle("密碼錯誤")//設定視窗標題
                            .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                            .setMessage("請重新輸入密碼")//設定顯示的文字
                            .setPositiveButton("關閉視窗",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setClass(login_register.this , login_register.class);
                                    startActivity(intent);
                                }
                            })//設定結束的子視窗
                            .show();//呈現對話視窗

                }
            }

                /*換頁
                Intent intent = new Intent();
                intent.setClass(login.this, MainActivity.class);
                startActivity(intent);
                */

        });
/*
        btcreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login_register.this , MainActivity.class);
                startActivity(intent);


                //產生視窗物件
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("對話視窗")//設定視窗標題
                        .setIcon(R.mipmap.ic_launcher)//設定對話視窗圖示
                        .setMessage("這是一個對話視窗")//設定顯示的文字
                        .setPositiveButton("關閉視窗",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })//設定結束的子視窗
                        .show();//呈現對話視窗

                */

        /*********************** Connect to Server ***********************/
        //login_register 需先檢查兩次輸入的密碼是否相同，若相同則上傳ID, Password，並將登入狀態設為登入(Server端)
        //若兩次密碼輸入錯誤則顯示：密碼輸入錯誤

    }
}

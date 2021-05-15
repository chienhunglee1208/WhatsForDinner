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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private Button btlogin, btquit, btregister;
    private EditText id, password;
    //
    AlertDialog.Builder builder;

    String server_url = "http://140.136.148.202/log.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Variables locate
        id = (EditText)findViewById(R.id.loginet_id);
        id.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));
        password = (EditText)findViewById(R.id.loginet_password);
        password.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));

        //Button locate
        btlogin = (Button)findViewById(R.id.loginbt_login);
        btlogin.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));
        btquit = (Button)findViewById(R.id.loginbt_quit);
        btquit.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));
        btregister = (Button)findViewById(R.id.loginbt_register);
        btregister.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));

        //
        builder = new AlertDialog.Builder(login.this);

        //Button function

        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ID, PASSWORD;
                ID = id.getText().toString();
                PASSWORD = password.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                builder.setTitle("Server Response");
                                builder.setMessage("Response :"+ response);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        id.setText("");
                                        password.setText("");
                                    }
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                        }
                        , new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(login.this,"Error...", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError{
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("id", ID);
                        params.put("password", PASSWORD);
                        return params;
                    }
                };

                MySingleton.getmInstance(login.this).addTorequestque(stringRequest);

                /*
                Intent intent = new Intent();
                intent.setClass(login.this, MainActivity.class);
                startActivity(intent);
                */
            }
        });


        btquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(login.this, login_register.class);
                startActivity(intent);
            }
        });
    }
    /*********************** Connect to Server ***********************/
    //login 中需要上傳ID, Password, 並尋找伺服器中使否有此帳號（輸入錯誤或查無帳號都顯示：帳號或密碼錯誤或尚無帳號）
}

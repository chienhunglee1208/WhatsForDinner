package com.example.davidlee1.whats_for_dinner;

import android.support.v7.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class restaurant_draw extends AppCompatActivity implements LocationListener {

    private TextView restName ,title1 ,title2;
    public Button btdrawAgain;
    double lat , lng;
    private LocationManager locationManager;
    String bestProv;
    int JAlength;
    String[] ResArray;
    int[] DiscriArray;
    int ranint , ranint2 , sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_draw);

        //**************************請求定位授權***************************************************************/

        requestPermission();

        //**************************Post開啟時的經緯度至PHP並標記回傳餐廳*************************************/
        double latc, lngc;
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("ACCESS_ERROR_TAG" , "未取得定位權限");
            return;
        }
        assert lm != null;
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // 設定定位資訊由最佳提供者提供
        latc = location.getLatitude();  // 取得緯度
        lngc = location.getLongitude(); // 取得經度
        final String N = Double.toString(latc);
        final String E = Double.toString(lngc);
        getData(N , E);

        //Button locate
        btdrawAgain = (Button) findViewById(R.id.drawbt_redraw);
        btdrawAgain.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));

        restName = (TextView) findViewById(R.id.drawtv_restname);
        restName.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SentyTang.ttf"));

        title1 = (TextView) findViewById(R.id.drawtv_title1);
        title2 = (TextView) findViewById(R.id.drawtv_title2);
        title1.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SentyTang.ttf"));
        title2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/SentyTang.ttf"));
    }

    /**************************LocationListener方法**************************************************************/
    //當定位位置改變
    @Override
    public void onLocationChanged(Location location) {
        //取得經緯度
        lat = location.getLatitude();
        lng = location.getLongitude();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("ACCESS_ERROR_TAG" , "未取得定位權限");
        }
    }

    //當定位狀態改變
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Criteria criteria = new Criteria();
        bestProv = locationManager.getBestProvider(criteria, true);
    }

    //功能寫在onResume
    @Override
    public void onProviderEnabled(String provider) {

    }

    //功能寫在onPause
    @Override
    public void onProviderDisabled(String provider) {

    }

    //應用程式為開啟狀態
    @Override
    protected void onResume() {
        super.onResume();
        //取得定位服務
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //取得最佳定位方式
        Criteria criteria = new Criteria();
        bestProv = locationManager.getBestProvider(criteria, true);

        //若有定位功能則更新位置
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //確認ACCESS_FINE_LOCATION權限是否授權
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //取得定位
                locationManager.requestLocationUpdates(bestProv, 1000, 1, this);
            }
        }
        //若未開啟定位功能則要求開啟
        else {
            Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
        }
    }

    //應用程式關閉或手機待機
    @Override
    protected void onPause() {
        super.onPause();
        //確認ACCESS_FINE_LOCATION權限是否授權
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //停止定位服務
            locationManager.removeUpdates(this);
        }
    }

    /**************************請求GPS授權方法****************************************************************/
    //請求授權
    private void requestPermission() {
        //如果是Android 6.0以上判斷是否取得授權
        if (Build.VERSION.SDK_INT >= 23) {
            int hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            //未取得授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    //授權對話框
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            //若按允許按鈕則顯示地位圖層
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("GPS_TAG", "Allow");
            }
            //若按拒絕按鈕給予提示並關閉應用程式
            else {
                Toast.makeText(this, "未取得GPS授權", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**************************連接PHP取得資料與抽選功能*******************************************************/
    //向PHP取得資料，並在logcat中顯示成功或失敗
    private void getData(final String N, final String E) {
        //PHP網址
        String urlRange = "http://140.136.148.202/volley.php";
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
                map.put("N", N);
                map.put("E", E);
                return map;
            }
        };

        Volley.newRequestQueue(this).add(StringRequest);
    }

    //將取得的資料標記在地圖上
    private void GETJson(JSONArray jsonArray) {
        JAlength = jsonArray.length();
        ResArray = new String[JAlength];
        DiscriArray = new int[JAlength];
        Arrays.fill( ResArray, null );
        Arrays.fill( DiscriArray, 0 );
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                ResArray[i] = o.getString("shop_name") + "\n" + "地址：" + o.getString("area_city") + o.getString("area_addr") + "\n" + "電話：" + o.getString("telephone") + "\n" + o.getString("web_addr");
                DiscriArray[i] = 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Random ran = new Random();
        ranint = ran.nextInt(JAlength);
        restName.setText(ResArray[ranint]);
        restName.setAutoLinkMask(Linkify.WEB_URLS);
        restName.setMovementMethod(LinkMovementMethod.getInstance());
        DiscriArray[ranint] = 0;

        //Button function
        btdrawAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                while (true){
                    ranint2 = ran.nextInt(JAlength);
                    sum = 0;
                    if (DiscriArray[ranint2] == 1){
                        restName.setText(ResArray[ranint2]);
                        DiscriArray[ranint2] = 0;
                        break;
                    }
                    else {
                        for (int i = 0; i < JAlength; i++) {
                            sum += DiscriArray[i];
                        }

                        if (sum == 0){
                            Toast.makeText(restaurant_draw.this, "全部抽完了!!", Toast.LENGTH_SHORT).show();
                            for (int i = 0 ; i < JAlength; i++) {
                                DiscriArray[i] = 1;
                            }
                        }
                    }
                }
            }
        });
    }
}

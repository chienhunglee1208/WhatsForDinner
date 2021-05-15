package com.example.davidlee1.whats_for_dinner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnInfoWindowClickListener{

    //變數宣告

    private GoogleMap mMap;
    private LocationManager locationManager;
    String bestProv;

    //取得Google Map物件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /*
      Manipulates the map once available.
      This callback is triggered when the map is ready to be used.
      This is where we can add markers or lines, add listeners or move the camera. In this case,
      we just add a marker near Sydney, Australia.
      If Google Play services is not installed on the device, the user will be prompted to install
      it inside the SupportMapFragment. This method will only be triggered once the user has
      installed Google Play services and returned to the app.
     */

    //載入地圖時執行的動作
   @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //設定地圖的風格


        //隱藏商家與簡化地圖，若失敗在logcat顯示錯誤
       try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.d("STYLE_ERROR_TAG", "Set_Fail");
            }
        } catch (Resources.NotFoundException e) {
            Log.d("STYLE_ERROR_TAG", "Set_Fail");
        }

        //隱藏UI的小工具
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);

        //呼叫點擊資訊視窗的動作
        mMap.setOnInfoWindowClickListener(this);

        //讓資訊視窗可以換行與樣式設定
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @SuppressLint("RtlHardcoded")
            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.LEFT);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);

                return info;
            }
        });

        //請求GPS授權



        requestPermission();

        //應用程式開啟時的提示訊息




        Toast.makeText(this, "若所在位置有改變，請重新進入本頁面以取得當前位置的附近餐廳", Toast.LENGTH_LONG).show();

        //Post開啟時的經緯度至PHP並標記回傳餐廳


        double lat, lng;
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("ACCESS_ERROR_TAG" , "未取得定位權限");
            return;
        }
        assert lm != null;
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // 設定定位資訊由網路提供
        lat = location.getLatitude();  // 取得緯度
        lng = location.getLongitude(); // 取得經度
        final String N = Double.toString(lat);
        final String E = Double.toString(lng);
        getData(N , E);
        //將當前位置移置中心
        LatLng Self = new LatLng(lat , lng);
        moveMap(Self);
    }

    //移動地圖與標記的功能


    // 移動地圖到參數指定的位置定位的結果
    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(20)
                        .tilt(90)
                        .build();

        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    // 在地圖加入指定位置的標記與標題
    private void addMarker(LatLng place, String title, String snippet) {
        BitmapDescriptor icon =
                BitmapDescriptorFactory.fromResource(R.drawable.restaurant_red_24);//餐廳圖標

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place)
                .title(title)
                .snippet(snippet)
                .icon(icon);

        mMap.addMarker(markerOptions);
    }


   //LocationListener方法
    //當定位位置改變
    @Override
    public void onLocationChanged(Location location) {
        //若未授權定位顯示錯誤訊息
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("ACCESS_ERROR_TAG" , "未取得定位權限");
            return;
        }
        mMap.setMyLocationEnabled(true);
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

    //請求GPS授權方法



   //請求授權
    private void requestPermission() {
        //如果是Android 6.0以上判斷是否取得授權
        if (Build.VERSION.SDK_INT >= 23) {
            int hasPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            //未取得授權
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
        }
        //如果是Android 6.0以下或已授權則顯示定位圖層
        setMyLocation();
    }

    //授權對話框
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            //若按允許按鈕則顯示地位圖層
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setMyLocation();
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

   //顯示定位圖層


    private void setMyLocation() throws SecurityException {
        mMap.setMyLocationEnabled(true);
    }


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
                            markJson(restaurantJSONArray);
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
    private void markJson(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                addMarker(new LatLng(o.getDouble("loc_N"), o.getDouble("loc_E")), o.getString("shop_name") + "\n" + "菜式：" + o.getString("type") + "\n" + "均消：" + o.getString("avg_cost") + "元\n" + "地址：" + o.getString("area_city") + o.getString("area_addr") + "\n" + "電話：" + o.getString("telephone") + "\n\n" + "- 點擊查看網頁 -", o.getString("web_addr"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //設定資訊視窗點擊動作

    //點擊資訊視窗的動作
    @Override
    public void onInfoWindowClick(Marker marker) {
        //顯示字串
        Toast.makeText(this, "前往餐廳網頁",
                Toast.LENGTH_SHORT).show();
        //開啟網頁
        Uri uri = Uri.parse(marker.getSnippet());
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    //Class結尾

}

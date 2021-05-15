package com.example.davidlee1.whats_for_dinner;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class rest_searchbyspinner extends Activity {
    private String[] cityname = new String[] {"台北市", "新北市","桃園市","台中市","台南市","高雄市","基隆市","新竹市","嘉義市","新竹縣",
            "苗栗縣","彰化縣","南投縣","雲林縣","嘉義縣","屏東縣","宜蘭縣","花蓮縣","台東縣","澎湖縣","金門縣","連江縣"};//載入第一下拉選單
    private String[] area = new String[]{"中正區","大同區","中山區","松山區","大安區","萬華區","信義區","士林區","北投區","內湖區","南港區","文山區"};//起始畫面時預先載入第二下拉選單
    private String[] foodtype = new String[]{"全部","食補藥膳餐廳","義式料理","居酒屋","綜合日式料理","中式早餐","法式料理","其他日式料理","咖啡專賣","生魚片、壽司專賣","懷舊主題","甜品、甜湯","台菜餐廳","寵物餐廳","西點烘焙、麵包","海鮮餐廳","甜點專賣","美式料理","剉冰、豆花","麻辣火鍋","複合式","其他異國料理","炭烤串燒","涮涮鍋","中式糕餅","日式豬排專賣","其他小吃"};//起始畫面時預先載入第三下拉選單
    //第一下拉選取後載入第二下拉選單
    private String[][] area2 = new String[][]{{"中正區","大同區","中山區","松山區","大安區","萬華區","信義區","士林區","北投區","內湖區","南港區","文山區"},
            {"板橋區","新莊區","中和區","永和區","土城區","樹林區","三峽區","鶯歌區","三重區","蘆洲區","五股區","泰山區","林口區","八里區","淡水區","三芝區","石門區","金山區","萬里區","汐止區","瑞芳區","貢寮區","平溪區","雙溪區","新店區","深坑區","石碇區","坪林區","烏來區"},
            {"桃園區","中壢區","平鎮區","八德區","楊梅區","蘆竹區","大溪區","龍潭區","龜山區","大園區","觀音區","新屋區","復興區"},
            {"中區","東區","南區","西區","北區","北屯區","西屯區","南屯區","太平區","大里區","霧峰區","烏日區","豐原區","后里區","石岡區","東勢區","新社區","潭子區","大雅區","神岡區","大肚區","沙鹿區","龍井區","梧棲區","清水區","大甲區","外埔區","大安區","和平區"},
            {"中西區","東區","南區","北區","安平區","安南區","永康區","歸仁區","新化區","左鎮區","玉井區","楠西區","南化區","仁德區","關廟區","龍崎區","官田區","麻豆區","佳里區","西港區","七股區","將軍區","學甲區","北門區","新營區","後壁區","白河區","東山區","六甲區","下營區","柳營區","鹽水區","善化區","大內區","山上區","新市區","安定區"},
            {"楠梓區","左營區","鼓山區","三民區","鹽埕區","前金區","新興區","苓雅區","前鎮區","旗津區","小港區","鳳山區","大寮區","鳥松區","林園區","仁武區","大樹區","大社區","岡山區","路竹區","橋頭區","梓官區","彌陀區","永安區","燕巢區","田寮區","阿蓮區","茄萣區","湖內區","旗山區","美濃區","內門區","杉林區","甲仙區","六龜區","茂林區","桃源區","那瑪夏區"},
            {"仁愛區","中正區","信義區","中山區","安樂區","暖暖區","七堵區"},
            {"東區","北區","香山區"},
            {"東區","西區"},
            {"竹北市","竹東鎮","新埔鎮","關西鎮","湖口鄉","新豐鄉","峨眉鄉","寶山鄉","北埔鄉","芎林鄉","橫山鄉","尖石鄉","五峰鄉"},
            {"苗栗市","頭份市","竹南鎮","後龍鎮","通霄鎮","苑裡鎮","卓蘭鎮","造橋鄉","西湖鄉","頭屋鄉","公館鄉","銅鑼鄉","三義鄉","大湖鄉","獅潭鄉","三灣鄉","南庄鄉","泰安鄉"},
            {"彰化市","員林市","和美鎮","鹿港鎮","溪湖鎮","二林鎮","田中鎮","北斗鎮","花壇鄉","芬園鄉","大村鄉","永靖鄉","伸港鄉","線西鄉","福興鄉","秀水鄉","埔心鄉","埔鹽鄉","大城鄉","芳苑鄉","竹塘鄉","社頭鄉","二水鄉","田尾鄉","埤頭鄉","溪州鄉"},
            {"南投市","埔里鎮","草屯鎮","竹山鎮","集集鎮","名間鄉","鹿谷鄉","中寮鄉","魚池鄉","國姓鄉","水里鄉","信義鄉","仁愛鄉"},
            {"斗六市","斗南鎮","虎尾鎮","西螺鎮","土庫鎮","北港鎮","林內鄉","古坑鄉","大埤鄉","莿桐鄉","褒忠鄉","二崙鄉","崙背鄉","麥寮鄉","臺西鄉","東勢鄉","元長鄉","四湖鄉","口湖鄉","水林鄉"},
            {"太保市","朴子市","布袋鎮","大林鎮","民雄鄉","溪口鄉","新港鄉","六腳鄉","東石鄉","義竹鄉","鹿草鄉","水上鄉","中埔鄉","竹崎鄉","梅山鄉","番路鄉","大埔鄉","阿里山鄉"},
            {"屏東市","潮州鎮","東港鎮","恆春鎮","萬丹鄉","長治鄉","麟洛鄉","九如鄉","里港鄉","鹽埔鄉","高樹鄉","萬巒鄉","內埔鄉","竹田鄉","新埤鄉","枋寮鄉","新園鄉","崁頂鄉","林邊鄉","南州鄉","佳冬鄉","琉球鄉","車城鄉","滿州鄉","枋山鄉","霧臺鄉","瑪家鄉","泰武鄉","來義鄉","春日鄉","獅子鄉","牡丹鄉","三地門鄉"},
            {"宜蘭市","頭城鎮","羅東鎮","蘇澳鎮","礁溪鄉","壯圍鄉","員山鄉","冬山鄉","五結鄉","三星鄉","大同鄉","南澳鄉"},
            {"花蓮市","鳳林鎮","玉里鎮","新城鄉","吉安鄉","壽豐鄉","光復鄉","豐濱鄉","瑞穗鄉","富里鄉","秀林鄉","萬榮鄉","卓溪鄉"},
            {"台東市","成功鎮","關山鎮","長濱鄉","池上鄉","東河鄉","鹿野鄉","卑南鄉","大武鄉","綠島鄉","太麻里鄉","海端鄉","延平鄉","金峰鄉","達仁鄉","蘭嶼鄉"},
            {"馬公市","湖西鄉","白沙鄉","西嶼鄉","望安鄉","七美鄉"},
            {"金城鎮","金湖鎮","金沙鎮","金寧鄉","烈嶼鄉","烏坵鄉"},
            {"南竿鄉","北竿鄉","莒光鄉","東引鄉"}};
    //第二下拉選取後載入第三下拉選單

    private Spinner sp1;//第一個下拉選單
    private Spinner sp2;//第二個下拉選單
    private Spinner sp3;//第三個下拉選單
    private Context context;

    private Button final_ok;

    ArrayAdapter<String> adapter1;

    ArrayAdapter<String> adapter2;

    ArrayAdapter<String> adapter3;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_searchbyspinner);

        final_ok = findViewById(R.id.restsrchbt_ok);
        final_ok.setTypeface(Typeface.createFromAsset(getAssets() , "fonts/SentyTang.ttf"));

        final_ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent= new Intent(rest_searchbyspinner.this,SearchResult.class);

                String city = cityname[sp1.getSelectedItemPosition()];
                String addr=  area2[sp1.getSelectedItemPosition()][sp2.getSelectedItemPosition()];
                String food=  foodtype[sp3.getSelectedItemPosition()];
                Bundle bundle = new Bundle();
                bundle.putString("city",city);
                bundle.putString("addr",addr);
                bundle.putString("food",food);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


        context = this;

        //程式剛啟始時載入第一個下拉選單
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cityname);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1 = (Spinner) findViewById(R.id.type1);
        sp1.setAdapter(adapter1);
        sp1.setOnItemSelectedListener(selectListener);

        //因為下拉選單第一個為茶類，所以先載入茶類群組進第二個下拉選單
        adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, area);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2 = (Spinner) findViewById(R.id.type2);
        sp2.setAdapter(adapter2);

        //因為下拉選單第二個為紅茶，所以先載入紅茶群組進第三個下拉選單
        adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, foodtype);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp3 = (Spinner) findViewById(R.id.type3);
        sp3.setAdapter(adapter3);

    }

    //第一個下拉類別的監看式
    private OnItemSelectedListener selectListener = new OnItemSelectedListener(){
        public void onItemSelected(AdapterView<?> parent, View v, int position,long id){
            //讀取第一個下拉選單是選擇第幾個
            int pos = sp1.getSelectedItemPosition();
            //重新產生新的Adapter，用的是二維陣列type2[pos]
            adapter2 = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, area2[pos]);
            //載入第二個下拉選單Spinner
            sp2.setAdapter(adapter2);
        }

        public void onNothingSelected(AdapterView<?> arg0){

        }

    };

    /*********************** Connect to Server ***********************/
    //rest_searchbyspinner 中需要上傳spinner中的三項資料

}
package comp5216.finalAssessment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapPage extends AppCompatActivity {
    private ListView lv;
    private double longitude=0, latitude=0, toiletLongitude, toiletLatitude;
    private LocationManager locationManager;
    private ArrayList<String> toiletList;
    private ArrayAdapter<String> toiletAdapter;
    private String locationProvider, buildingName;
    private String token;
    private String toiletId;
    private Boolean isDamaged;
    private Location location;
    private Thread newThread;
    private JSONObject jsnobject;
    private ArrayList<String> idList;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mappage);
        lv = findViewById(R.id.listView);

        if (ContextCompat.checkSelfPermission(MapPage.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//no permission of location service
            //开启定位权限,200是标识码
            ActivityCompat.requestPermissions(MapPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            Toast.makeText(MapPage.this, "已开启定位权限", Toast.LENGTH_LONG).show();
            //getLocation(MapPage.this);
            try {
                getToiletList();

            }catch(IOException e){
                e.printStackTrace();
            }
        }
        System.out.println(longitude);
        System.out.println(latitude);
        System.out.println(token);
        System.out.println(idList);
        System.out.println(toiletList);
        token = getIntent().getStringExtra("token");



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MapPage.this, MapsActivity.class);
//                for (int length =0; length < idList.size(); length ++){
//                    if (length == i){
//                        toiletId = idList.get(length);
//                    }
//                }
                System.out.println(idList);
                System.out.println(toiletList);
                Bundle bundle = new Bundle();
                bundle.putString("toiletId", String.valueOf(i));
                bundle.putString("token", token);
                bundle.putString("oriLong", String.valueOf(longitude));
                bundle.putString("oriLat", String.valueOf(latitude));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200://刚才的识别码
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//用户同意权限,执行我们的操作
                  //  getLocation(MapPage.this);
                    try {
                        getToiletList();

                    }catch(IOException e){
                        e.printStackTrace();
                    }
                } else {//用户拒绝之后,当然我们也可以弹出一个窗口,直接跳转到系统设置页面
                    Toast.makeText(MapPage.this, "未开启定位权限,请手动到设置去开启权限", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private void getLocation(Context context) {
        //1.获取位置管理器
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        //3.获取上次的位置，一般第一次运行，此值为null
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }



    private void getToiletList() throws IOException {
        ArrayList<String> toiletList = new ArrayList<>();
        ArrayList<String> idList = new ArrayList<>();
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        String Uri = "http://81.68.198.152:7429/api/toilets?latitude=" + latitude + "&longitude=" + longitude;

       newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Uri)
                        .method("GET", null)
                        .addHeader("Authorization", token)
                        .build();

                try {

                    Response response = okHttpClient.newCall(request).execute();
                    String jsonString = response.body().string();
                    JSONArray jsonarray = new JSONArray(jsonString);
                    for(int i=0; i<jsonarray.length(); i++)
                    {
                        JSONObject obj=jsonarray.getJSONObject(i);
                        String id = String.valueOf(obj.getInt("toiletId"));
                        String value = obj.getString("name");
                        toiletList.add(value);
                        idList.add(id);
                    }

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toiletAdapter = new ArrayAdapter<String>(MapPage.this, android.R.layout.simple_list_item_1, toiletList);
                        lv.setAdapter(toiletAdapter);
                        System.out.println("id" + idList);
                        System.out.println("list" + toiletList);
                    }
                 });


                // for (Object object : jsonArray) {
                //    JSONObject jsonObject = (JSONObject)object;
                //    Student student = new Student(jsonObject.getString("name"), jsonObject.getInteger("age"), jsonObject.getString("gender"));
                //    studentList.add(student);
            }

        });
        newThread.start();


    }
}





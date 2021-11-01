package comp5216.finalAssessment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapPage extends AppCompatActivity {
    private ListView lv;
    private double longitude = 0, latitude = 0;
    private LocationManager locationManager;
    private volatile ArrayList<String> toiletList;
    private ArrayAdapter<String> toiletAdapter;
    private String locationProvider;
    private String token;
    private String toiletId;
    private Button signOutButton;

    private volatile ArrayList<String> idList;

    public MapPage() {
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mappage);
        lv = findViewById(R.id.listView);
        signOutButton = findViewById(R.id.SignOut);
        //connect with API server now
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        if (ContextCompat.checkSelfPermission(MapPage.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {//no permission of location service

            ActivityCompat.requestPermissions(MapPage.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        } else {
            getLocation(this);
            System.out.println("map" +longitude);
            System.out.println("map" + latitude);
            Toast.makeText(MapPage.this, "permission acquired", Toast.LENGTH_LONG).show();
            ArrayList<String> toiletList = new ArrayList<>();
            ArrayList<String> idList = new ArrayList<>();
            String Uri = "http://81.68.198.152:7429/api/toilets?latitude=" + latitude + "&longitude=" + longitude;
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
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    String id = String.valueOf(obj.getInt("toiletId"));
                    String value = obj.getString("name");
                    toiletList.add(value);
                    idList.add(id);
                }

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            toiletAdapter = new ArrayAdapter<String>(MapPage.this, android.R.layout.simple_list_item_1, toiletList);
            lv.setAdapter(toiletAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(MapPage.this, MapsActivity.class);
                    for (int length = 0; length < idList.size(); length ++){
                        if (length == i){
                            toiletId = idList.get(length);
                        }
                    }
                    System.out.println("id send" + toiletId);
                    System.out.println(toiletList);
                    Bundle bundle = new Bundle();
                    bundle.putString("toiletId", toiletId);
                    bundle.putString("token", token);
                    bundle.putString("oriLong", String.valueOf(longitude));
                    bundle.putString("oriLat", String.valueOf(latitude));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });


        }
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapPage.this, UserLogin.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//user

                } else {//user denied permission
                    Toast.makeText(MapPage.this, "We don't have your permissions, please change it on system setting", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    private void getLocation(Context context) {
        //1.acquire location manager
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        //the location manager provider could be GPS or NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //if is network location
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //if is gps location
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(this, "No available location provider", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return; //This method is useless, but we need it otherwise the program won't compile
        }
        Location location = locationManager.getLastKnownLocation(locationProvider); // get last know location
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
}





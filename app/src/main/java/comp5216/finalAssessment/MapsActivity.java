package comp5216.finalAssessment;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comp5216.finalAssessment.databinding.ActivityMapsBinding;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private double destLong;
    private double destLat;
    private double oriLong;
    private double oriLat;
    private String toiName;
    private String toiID;
    private String token;
    private Button btnComment;
    private Button btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setUpMapIfNeeded();

        Intent intent = getIntent();
        toiID = intent.getStringExtra("toiletId");
        token = intent.getStringExtra("token");
        oriLong = Double.parseDouble(intent.getStringExtra("oriLong"));
        oriLat = Double.parseDouble(intent.getStringExtra("oriLat"));
        System.out.println("toiid" + toiID);
        System.out.println("token: " + token);
        System.out.println("orilong" + oriLong);
        System.out.println("orilat" + oriLat);





        btnComment = findViewById(R.id.buttonGoToComment);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, UserComment.class);
                Bundle bundle = new Bundle();
                bundle.putString("toiletId", toiID);
                bundle.putString("token", token);
                bundle.putString("oriLong", String.valueOf(oriLong));
                bundle.putString("oriLat", String.valueOf(oriLat));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        btnMenu = findViewById(R.id.button2);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this, MapPage.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }
        });



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String urlSend = "http://81.68.198.152:7429/api/toilets/" + toiID;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(urlSend)
                .method("GET", null)
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response;
            response = client.newCall(request).execute();
            String resStr = response.body().string();
            JSONObject json = new JSONObject(resStr);
            destLong = json.getDouble("longitude");
            destLat = json.getDouble("latitude");
            toiName = json.getString("name");
            System.out.println(json);
            System.out.println(toiName);
            System.out.println(destLat);
            System.out.println(destLong);;
        }
        catch (JSONException J){
            J.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            // Map is ready
            Toast.makeText(this, "Map is ready to be used!", Toast.LENGTH_SHORT).show();

            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(oriLat, oriLong);
            LatLng destination = new LatLng(destLat, destLong);
            mMap.addMarker(new MarkerOptions().position(sydney).title("You are here!"));
            mMap.addMarker(new MarkerOptions().position(destination).title(toiName));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            List<LatLng> path = new ArrayList();


            //Execute Directions API request
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey("AIzaSyA-M5tJtvEM7daiicTVOmOm31nb2KBfgxI")
                    .build();
            String oriReq = oriLat + "," + oriLong;
            String destReq = destLat + "," + destLong;
            DirectionsApiRequest req = DirectionsApi.getDirections(context, oriReq, destReq);
            try {
                DirectionsResult res = req.await();

                //Loop through legs and steps to get encoded polylines of each step
                if (res.routes != null && res.routes.length > 0) {
                    DirectionsRoute route = res.routes[0];

                    if (route.legs !=null) {
                        for(int i=0; i<route.legs.length; i++) {
                            DirectionsLeg leg = route.legs[i];
                            if (leg.steps != null) {
                                for (int j=0; j<leg.steps.length;j++){
                                    DirectionsStep step = leg.steps[j];
                                    if (step.steps != null && step.steps.length >0) {
                                        for (int k=0; k<step.steps.length;k++){
                                            DirectionsStep step1 = step.steps[k];
                                            EncodedPolyline points1 = step1.polyline;
                                            if (points1 != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                for (com.google.maps.model.LatLng coord1 : coords1) {
                                                    path.add(new LatLng(coord1.lat, coord1.lng));
                                                }
                                            }
                                        }
                                    } else {
                                        EncodedPolyline points = step.polyline;
                                        if (points != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords = points.decodePath();
                                            for (com.google.maps.model.LatLng coord : coords) {
                                                path.add(new LatLng(coord.lat, coord.lng));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch(Exception ex) {
                Log.e("Error", ex.getLocalizedMessage());
            }

            //Draw the polyline
            if (path.size() > 0) {
                PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.RED).width(5);
                Log.i("draw", "line");
                mMap.addPolyline(opts);
            }

            // Enable zoom controls
            mMap.getUiSettings().setZoomControlsEnabled(true);
            // Animate the camera to zoom into the map
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 11));
        } else {
            Toast.makeText(this, "Error - Map was null!", Toast.LENGTH_SHORT).show();
        }
    }
}
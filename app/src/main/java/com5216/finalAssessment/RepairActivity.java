package com.toilet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepairActivity extends AppCompatActivity {

    private ListView repairToiletListview;
    private List<Toilet> toilets;
    private RepairToiletAdapter repairToiletAdapter;

    private Handler toiletHandle = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

        repairToiletListview = findViewById(R.id.repair_toilet_listview);
        toilets  = new ArrayList<>();
        repairToiletAdapter = new RepairToiletAdapter(this, toilets);
        repairToiletListview.setAdapter(repairToiletAdapter);




        new Thread(){
            @Override
            public void run() {
                String str = HttpUtil.Get("http://81.68.198.152:7429/admin/damagedtoilet");
                Logger.getLogger("Toilet").log(Level.INFO, "damagedtoilet response:"+str);
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Toilet toilet = new Toilet();

                        toilet.isDamage = jsonObject.getBoolean("isDamage");
                        toilet.name = jsonObject.getString("name");
                        toilet.toiletId = jsonObject.getInt("toiletId");

                        toilets.add(toilet);
                    }

                    toiletHandle.post(new Runnable() {
                        @Override
                        public void run() {
                            repairToiletAdapter.notifyDataSetChanged();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
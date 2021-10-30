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

public class CommentActivity extends AppCompatActivity {

    public static List<Comment> Comments;

    private List<String> Toilets;

    private ListView CommentToiletListView;

    private CommentToiletAdapter commentToiletAdapter;


    private Handler toiletHandle = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        CommentToiletListView = findViewById(R.id.comment_toilet_listview);

        Toilets = new ArrayList<>();
        Comments = new ArrayList<>();


        commentToiletAdapter = new CommentToiletAdapter(this,Toilets);

        CommentToiletListView.setAdapter(commentToiletAdapter);






        new Thread(){
            @Override
            public void run() {
                String str = HttpUtil.Get("http://81.68.198.152:7429/admin/comments");
                Logger.getLogger("Toilet").log(Level.INFO, "comments response:"+str);
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        Comment comment = new Comment();

                        comment.toiletName = jsonObject.getString("toiletName");
                        comment.content = jsonObject.getString("content");
                        comment.commentId = jsonObject.getInt("commentId");

                        Logger.getLogger("Toilet").log(Level.INFO, "comment:"+comment.content);

                        Comments.add(comment);

                        if(!Toilets.contains(comment.toiletName)){
                            Toilets.add(comment.toiletName);
                        }
                    }

                    toiletHandle.post(new Runnable() {
                        @Override
                        public void run() {
                            commentToiletAdapter.notifyDataSetChanged();
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();


    }
}
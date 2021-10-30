package com.toilet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CommentListActivity extends AppCompatActivity {


    public static List<Comment> Comments;
    private ListView CommentListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        CommentListView = findViewById(R.id.comment_listview);
//        Comments = new ArrayList<>();

        CommentAdapter commentAdapter = new CommentAdapter(this, Comments);

        CommentListView.setAdapter(commentAdapter);
        if(Comments != null && Comments.size()>0){
            setTitle(Comments.get(0).toiletName);
        }




    }
}
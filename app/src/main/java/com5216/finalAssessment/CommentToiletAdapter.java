package com.toilet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CommentToiletAdapter extends BaseAdapter {
    private List<String> data;
    private Context context;

    public CommentToiletAdapter(Context context, List<String> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data == null){
            return 0;
        }else {
            return data.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if(data == null){
            return null;
        }else {
            return data.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.comment_toilet_item,viewGroup,false);

        TextView nameTextView = view.findViewById(R.id.comment_toilet_name);
        Button viewCommentButton = view.findViewById(R.id.view_comment_button);
        nameTextView.setText(data.get(i));

        viewCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Comment> allComments = CommentActivity.Comments;
                List<Comment> slcComments = new ArrayList<>();
                for (Comment c :
                        allComments) {
                    if(c.toiletName.equals(data.get(i))){
                        slcComments.add(c);
                    }
                }
                CommentListActivity.Comments = slcComments;

                Intent intent = new Intent(context, CommentListActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }
}

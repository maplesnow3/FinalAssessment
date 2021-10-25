package comp5216.finalAssessment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserComment extends Activity {

    private RecyclerView commentListRecycle;
    private UserCommentListAdapter userCommentListAdapter;
    private Button commentAddButtonTop;
    private Button commentAddButtonBot;
    private List<Comment> commentList;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_comment);
        commentListRecycle = (RecyclerView) findViewById(R.id.commentListRecycle);
        commentAddButtonBot = (Button) findViewById(R.id.commentAddButtonBot);
        commentAddButtonTop = (Button) findViewById(R.id.commentAddButtonTop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserComment.this);
        commentListRecycle.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<Comment>();
        //use for testing
        Comment comment = new Comment("Good",3,"Pen");
        commentList.add(comment);
        userCommentListAdapter = new UserCommentListAdapter(UserComment.this,commentList);
        commentListRecycle.setAdapter(userCommentListAdapter);
    }

}

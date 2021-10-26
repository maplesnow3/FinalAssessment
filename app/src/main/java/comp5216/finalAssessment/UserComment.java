package comp5216.finalAssessment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserComment extends Activity {

    private RecyclerView commentListRecycle;
    private UserCommentListAdapter userCommentListAdapter;
    private static final int USER_COMMENT_ADDING_REQUEST_CODE = 101;
    private Button commentAddButtonTop;
    private Button commentAddButtonBot;
    private List<Comment> commentList;
    //use for testing
    private String username = "Renka";


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

    public void onCommentAddClick(View v){
        Intent intent = new Intent(UserComment.this, UserCommentAdding.class);
        // should put username obtained from DB
        intent.putExtra("username",username);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, USER_COMMENT_ADDING_REQUEST_CODE);
        }
    }

    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == USER_COMMENT_ADDING_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                //Extract value needed from result extra
                String userComment = data.getExtras().getString("userComment");
                int toiletRating = data.getIntExtra("toiletRating",-1);
                //handle the added item
                Comment newUserComment =  new Comment(userComment,toiletRating,username);
                commentList.add(newUserComment);
                userCommentListAdapter.notifyDataSetChanged();
            }
        }
    }
}

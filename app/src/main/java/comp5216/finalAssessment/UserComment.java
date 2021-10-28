package comp5216.finalAssessment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserComment extends Activity {

    private RecyclerView commentListRecycle;
    private UserCommentListAdapter userCommentListAdapter;
    private static final int USER_COMMENT_ADDING_REQUEST_CODE = 101;
    private Button commentAddButtonTop;
    private Button commentAddButtonBot;
    private List<Comment> commentList;
    //use for testing
    private String username = "PenUpload";
    private String toiID;
    private String token;



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_comment);
        commentListRecycle = (RecyclerView) findViewById(R.id.commentListRecycle);
        commentAddButtonBot = (Button) findViewById(R.id.commentAddButtonBot);
        commentAddButtonTop = (Button) findViewById(R.id.commentAddButtonTop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserComment.this);
        commentListRecycle.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<Comment>();
        Intent intent = getIntent();
        toiID = intent.getStringExtra("toiletID");
        token = intent.getStringExtra("token");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String toiID = "1";
        String urlSend = "http://81.68.198.152:7429/api/comments/" + toiID;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url("http://81.68.198.152:7429/api/comments/1")
                .method("GET", null)
                .addHeader("Authorization", token)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            JSONArray json = new JSONArray(resStr);
            for(int i=0;i<json.length();i++){
                JSONObject jsonObject1 = json.getJSONObject(i);
                String content = jsonObject1.optString("content");
                Integer rating = jsonObject1.optInt("rating");
                Comment comment = new Comment(content,rating,"Pen");
                commentList.add(comment);
            }
        }
        catch (JSONException J){
            System.out.println("happy"+J.toString());
            J.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void onCommentBackClick(View v){
        Intent intent = new Intent(UserComment.this, MapsActivity.class);
        startActivity(intent);
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
                uploadNewComment(userComment,toiletRating);

            }
        }
    }

    public void uploadNewComment(String content, int rating){
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "content="+content+"&rating="+rating);
        Request request = new Request.Builder()
                .url("http://81.68.198.152:7429/api/comments/"+ toiID)
                .method("POST", body)
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package comp5216.finalAssessment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;

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
    private List<ToiletComment> commentList;
    private String toiID;
    private String oriLong;
    private String oriLat;
    private String token;
    private boolean isDamage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set up the view for the User Comment Activity
        setContentView(R.layout.user_comment);
        commentListRecycle = (RecyclerView) findViewById(R.id.commentListRecycle);
        commentAddButtonTop = (Button) findViewById(R.id.commentAddButtonTop);
        // set up the layout manager for the recycle list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UserComment.this);
        commentListRecycle.setLayoutManager(linearLayoutManager);
        // create comment list to store the comment data for the particular toilet
        commentList = new ArrayList<ToiletComment>();
        //Create intent to get toiletId and token for the database
        Intent intent = getIntent();
        toiID = intent.getStringExtra("toiletId");
        token = intent.getStringExtra("token");
        oriLong = intent.getStringExtra("oriLong");
        oriLat = intent.getStringExtra("oriLat");

        // set up the connection of the database
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String urlSend = "http://81.68.198.152:7429/api/comments/" + toiID;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(urlSend)
                .method("GET", null)
                .addHeader("Authorization", token)
                .build();
        try {
            // obtain comment data from the cloud database
            Response response = client.newCall(request).execute();
            String resStr = response.body().string();
            JSONArray json = new JSONArray(resStr);
            /* get all comment content,rating and submitTime from the database
               and put it in the commentList*/
            for(int i=0;i<json.length();i++){
                JSONObject jsonObject1 = json.getJSONObject(i);
                String content = jsonObject1.optString("content");
                Integer rating = jsonObject1.optInt("rating");
                String submitTime = jsonObject1.optString("submitTime");
                // change submitTime format into readable format
                String fullSubmitTime = submitTime.substring(0,submitTime.lastIndexOf("."));
                String[] dateTime = fullSubmitTime.split("T");
                String date = dateTime[0];
                String Time = dateTime[1].substring( 0,dateTime[1].lastIndexOf(":"));
                ToiletComment comment = new ToiletComment(content,rating,date+" "+ Time);
                commentList.add(comment);
            }
        }
        catch (JSONException J){
            J.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // set up the adapter for the recycleList

        userCommentListAdapter = new UserCommentListAdapter(UserComment.this,commentList);
        commentListRecycle.setAdapter(userCommentListAdapter);
    }

    /**
     * Change the activity to UserCommentAdding view.
     * After user click on CommentAddButton
     */
    public void onCommentAddClick(View v){
        Intent intent = new Intent(UserComment.this, UserCommentAdding.class);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, USER_COMMENT_ADDING_REQUEST_CODE);
        }
    }

    /**
     * Change the activity to MapsActivity view.
     * After user click on CommentBackButton
     */
    public void onCommentBackClick(View v){
        Intent intent = new Intent(UserComment.this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("toiletId", toiID);
        bundle.putString("token", token);
        bundle.putString("oriLong", String.valueOf(oriLong));
        bundle.putString("oriLat", String.valueOf(oriLat));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Report damage to the cloud database.
     * After user click on ReportDmgButton
     */
    public void onReportDmgClick(View v){
        isDamage = true;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "isDamage="+isDamage+"&toiletId="+toiID);
        Request request = new Request.Builder()
                .url("http://81.68.198.152:7429/api/toilets/")
                .method("POST", body)
                .addHeader("Authorization", token)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        try {
            Response response = client.newCall(request).execute();
            AlertDialog.Builder builder = new AlertDialog.Builder(UserComment.this);
            builder.setTitle("Report Damage")
                    .setMessage("Thanks for you feedback!")
                    .setPositiveButton("Exit", new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(UserComment.this, MapPage.class);
                                    intent.putExtra("token", token);
                                    startActivity(intent);
                                }
                            });
            builder.create().show();

        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == USER_COMMENT_ADDING_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                //Extract value needed from result extra
                String userComment = data.getExtras().getString("userComment");
                int toiletRating = data.getIntExtra("toiletRating",-1);
                String submitTime = data.getExtras().getString("submitTime");
                //handle the added item
                ToiletComment newUserComment =  new ToiletComment(userComment,toiletRating,submitTime);
                commentList.add(newUserComment);
                // upload the data into database
                userCommentListAdapter.notifyDataSetChanged();
                uploadNewComment(userComment,toiletRating);
            }
        }
    }

    /**
     * upload the newly added comment into the cloud database
     * @param content the user comment in words
     * @param rating the toilet rating given by the user
     */
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

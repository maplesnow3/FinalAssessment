package comp5216.finalAssessment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserCommentAdding extends Activity {
    private Button commentSubButton;
    private RatingBar toiletRatingBar;
    private EditText etEditComment;
    private String username;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_comment_adding);
        //set up layout object
        toiletRatingBar = (RatingBar) findViewById(R.id.toiletRatingBar);
        commentSubButton = (Button) findViewById(R.id.commentSubButton);
        etEditComment = (EditText) findViewById(R.id.etEditComment);

    }

    /**
     * Put the added comment into the intent and pass it to the UserComment activity
     * After user click on CommentSubButton
     */
    public void onCommentSubClick(View v){
        // obtain user input in the comment input box
        String userComment = etEditComment.getText().toString();
        // ensure user type in comment
        if(userComment.equals("") ){
            AlertDialog.Builder builder = new AlertDialog.Builder(UserCommentAdding.this);
            builder.setTitle("Comment Required")
                    .setMessage("Please input your comment on the toilet")
                    .setPositiveButton("Ok", new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
            builder.create().show();
        }else {
            // get the toilet rating rated by user
            float ToiletRating = toiletRatingBar.getRating();
            int ItoiletRating = Math.round(ToiletRating);
            // get current time at String format
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String submitTime = sdf.format(timestamp);
            // Prepare data intent for sending it back
            Intent data = new Intent();
            // Pass relevant data back as a result
            data.putExtra("toiletRating", ItoiletRating);
            data.putExtra("userComment", userComment);
            data.putExtra("submitTime", submitTime);
            // Activity finishes OK, return the data
            setResult(RESULT_OK, data); // Set result code and bundle data for response
            finish(); // Close the activity, pass data to parent
        }
    }

    /**
     *  Discard the added comment by the user and change activity to UserComment Activity
     *  After user click on CommentCancelButton
     */
    public void onCommentCancelClick(View v) {
        // set up alert box to alert user that the added comment will be discarded
        AlertDialog.Builder builder = new AlertDialog.Builder(UserCommentAdding.this);
        builder.setTitle("Stop Adding Comment")
                .setMessage("Are you sure you want stop adding comment?" +
                        "All unsaved change will be discarded")
                .setPositiveButton("Yes", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setResult(1);
                                finish();
                            }
                        })
                .setNegativeButton("No", new
                        DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
        builder.create().show();
    }
}

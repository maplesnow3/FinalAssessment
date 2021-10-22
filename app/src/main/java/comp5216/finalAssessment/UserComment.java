package comp5216.finalAssessment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class UserComment extends Activity {
    private Button commentSubButton;
    private RatingBar toiletRatingBar;
    private EditText etEditComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set up layout object
        toiletRatingBar = (RatingBar) findViewById(R.id.toiletRatingBar);
        commentSubButton = (Button) findViewById(R.id.commentSubButton);
        etEditComment = (EditText) findViewById(R.id.etEditComment);
    }

    public void onCommentSubClick(View v){
        // obtain user input in the comment input box
        String userComment = etEditComment.getText().toString();
        // ensure user type in comment
        if(userComment.equals("") ){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Comment Required")
                    .setMessage("Please input your comment on the toilet")
                    .setPositiveButton("Ok", new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
            builder.create().show();
        }
        // get the toilet rating rated by user
        float toiletRating = toiletRatingBar.getRating();
    }

    public void onCommentCancelClick(View v) {

    }
}

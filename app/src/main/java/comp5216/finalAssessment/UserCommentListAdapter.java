package comp5216.finalAssessment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserCommentListAdapter extends RecyclerView.Adapter<UserCommentListAdapter.ViewHolder> {

    public Context mContext;
    public List<Comment> commentList;
    public UserCommentListAdapter(Context mContext, List<Comment> commentList){
        this.mContext= mContext ;
        this.commentList= commentList ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_list_recycle, viewGroup, false);
        return new UserCommentListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Comment comment = commentList.get(i);
        viewHolder.commentBox.setText(comment.getComment());
        viewHolder.postedBy.setText("Posted By: "+ comment.getPostedBy());
        viewHolder.userRatingBar.setRating(Long.valueOf(comment.getRating()));

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView postedBy;
        public RatingBar userRatingBar;
        public TextView commentBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postedBy = itemView.findViewById(R.id.textViewPostedBy);
            userRatingBar = itemView.findViewById(R.id.userRating);
            commentBox = itemView.findViewById(R.id.textViewComment);
        }
    }
}

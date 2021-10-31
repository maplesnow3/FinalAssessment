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
    public List<ToiletComment> commentList;
    public UserCommentListAdapter(Context mContext, List<ToiletComment> commentList){
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
        ToiletComment comment = commentList.get(i);
        viewHolder.commentBox.setText(comment.getComment());
        viewHolder.postedAt.setText("Posted At: "+ comment.getPostedAt());
        viewHolder.userRatingBar.setRating(Long.valueOf(comment.getRating()));

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView postedAt;
        public RatingBar userRatingBar;
        public TextView commentBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postedAt = itemView.findViewById(R.id.textViewPostedAt);
            userRatingBar = itemView.findViewById(R.id.userRating);
            commentBox = itemView.findViewById(R.id.textViewComment);
        }
    }
}

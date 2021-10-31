package comp5216.finalAssessment;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;



public class CommentAdapter extends BaseAdapter {
    private List<Comment> data;
    private Context context;

    public CommentAdapter(Context context, List<Comment> data){
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

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.comment_item,null);

        TextView nameTextView = view.findViewById(R.id.comment_content);
        Button viewCommentButton = view.findViewById(R.id.delete_comment_button);
        nameTextView.setText(data.get(i).content);

        viewCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int commentId = data.get(i).commentId;

//                ((View)v.getParent()).setVisibility(View.INVISIBLE);
                data.remove(i);
                notifyDataSetChanged();

//                Handler handler = new Handler();
                new Thread(){
                    @Override
                    public void run() {
                        String str = HttpUtil.Delete("http://81.68.198.152:7429/admin/comments?comment_ID="+commentId);
                        Logger.getLogger("Toilet").log(Level.INFO, "comments delete response:"+str);

//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                data.remove(i);
//                                notifyDataSetChanged();
//                            }
//                        });

                    }
                }.start();


            }
        });


        return view;
    }
}

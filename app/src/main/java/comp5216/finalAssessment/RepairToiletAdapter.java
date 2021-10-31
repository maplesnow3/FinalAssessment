package comp5216.finalAssessment;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepairToiletAdapter extends BaseAdapter {

    private List<Toilet> data;
    private Context context;

    public RepairToiletAdapter(Context context, List<Toilet> data){
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
        view = LayoutInflater.from(context).inflate(R.layout.repair_toilet_item,null);

        TextView nameTextView = view.findViewById(R.id.repair_toilet_name);
        Button repairButton = view.findViewById(R.id.repair_button);
        nameTextView.setText(data.get(i).name);

        repairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                repairButton.setVisibility(View.INVISIBLE);

                new Thread(){
                    @Override
                    public void run() {
                        String str = HttpUtil.Post("http://81.68.198.152:7429/admin/damagedtoilet?is_damage=false&toilet_ID="+data.get(i).toiletId,"{}");
                        Logger.getLogger("Toilet").log(Level.INFO, "damagedtoilet delete response:"+str);

                    }
                }.start();



            }
        });
        return view;
    }
}

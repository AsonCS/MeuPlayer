package br.com.acsgsa92.meuplayer.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.acsgsa92.meuplayer.R;

/**
 * Created by acsgs on 29/03/2017.
 */

public class GroupAdapter extends BaseAdapter {

    private Context context;
    private String[] groups;
    private GridViewItemClick gridViewItemClick;
    public GroupAdapter(Context context, String[] groups, GridViewItemClick gridViewItemClick){
        this.context = context;
        this.groups = groups;
        this.gridViewItemClick = gridViewItemClick;
    }

    @Override
    public int getCount() {
        return groups != null ? groups.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return groups != null ? groups[position] : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public interface GridViewItemClick{
        public void OnClickGroup (int idx, String tipo);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Infla o layout
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_group, parent, false);
        CardView cardView = (CardView) view.findViewById(R.id.adg_card);
        TextView text = (TextView) view.findViewById(R.id.adg_text);
        ImageView image = (ImageView) view.findViewById(R.id.adg_image);
        text.setText(String.valueOf(getItem(position)));
        if (gridViewItemClick != null){
            if (cardView != null){
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridViewItemClick.OnClickGroup(position, "card");
                    }
                });
            }
            if (image != null){
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gridViewItemClick.OnClickGroup(position, "image");
                    }
                });
            }
        }

        return view;
    }
}

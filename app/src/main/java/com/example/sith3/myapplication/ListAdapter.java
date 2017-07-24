package com.example.sith3.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Sith3 on 7/19/2017.
 */

public class ListAdapter extends RecyclerView.ViewHolder{

    TextView item_list;

    public ListAdapter(View itemView) {
        super(itemView);

        item_list = (TextView) itemView.findViewById(R.id.list);


    }
    public void bind(Model model , View.OnClickListener clickListener){

        item_list.setText(model.getName());
    }

}

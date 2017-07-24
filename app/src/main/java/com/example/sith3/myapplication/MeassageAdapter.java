package com.example.sith3.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Akshay on 7/24/2017.
 */

class MeassageAdapter extends RecyclerView.ViewHolder {
    TextView mesage;

    public MeassageAdapter(View itemView) {
        super(itemView);

        mesage= (TextView) itemView.findViewById(R.id.data);
    }

    public void bind(Messages messages, View.OnClickListener clickListener){

        mesage.setText(messages.getMeassage());
    }
}

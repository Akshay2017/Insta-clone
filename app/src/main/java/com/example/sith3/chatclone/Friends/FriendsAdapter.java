package com.example.sith3.chatclone.Friends;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sith3.chatclone.Models.FriendsModel;
import com.example.sith3.chatclone.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Akshay on 7/27/2017.
 */

class FriendsAdapter extends RecyclerView.ViewHolder {
    CircleImageView circleImageViewuserpic;
    TextView friends_list;
    public FriendsAdapter(View itemView) {
        super(itemView);
        circleImageViewuserpic= (CircleImageView) itemView.findViewById(R.id.friendspic);
        friends_list = (TextView) itemView.findViewById(R.id.chatusers);
    }

    public void bind(FriendsModel fmodel, View.OnClickListener clickListener) {
        friends_list.setText(fmodel.getName());
    }

}

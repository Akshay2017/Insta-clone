package com.example.sith3.chatclone.BottomNavigationView;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.example.sith3.chatclone.Friends.Friend_List;
import com.example.sith3.chatclone.Friend_Request.AcceptRequest;
import com.example.sith3.chatclone.TimeLine.TimeLine;
import com.example.sith3.chatclone.Me.Me;
import com.example.sith3.chatclone.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Akshay on 8/8/2017.
 */

public class BottomNavigationViewHelper {

        public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){

            bottomNavigationViewEx.enableAnimation(false);
            bottomNavigationViewEx.enableItemShiftingMode(false);
            bottomNavigationViewEx.enableShiftingMode(false);
            bottomNavigationViewEx.setTextVisibility(true);
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.timeline:
                        Intent intent1 = new Intent(context, TimeLine.class);//ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                        break;

                    case R.id.chat:
                        Intent intent2  = new Intent(context, Friend_List.class);//ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                        break;

                    case R.id.notification:
                        Intent intent3 = new Intent(context, AcceptRequest.class);//ACTIVITY_NUM = 2
                        context.startActivity(intent3);
                        break;

                    case R.id.me:
                        Intent intent4 = new Intent(context, Me.class);//ACTIVITY_NUM = 3
                        context.startActivity(intent4);
                        break;

                }


                return false;
            }
        });
    }

}

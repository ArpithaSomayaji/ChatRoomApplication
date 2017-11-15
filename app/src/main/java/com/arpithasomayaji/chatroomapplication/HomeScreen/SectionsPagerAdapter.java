package com.arpithasomayaji.chatroomapplication.HomeScreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.arpithasomayaji.chatroomapplication.FriendsListScreen.FriendsFragment;

/**
 * Created by arpitha.somayaji on 11/12/2017.
 */

class SectionsPagerAdapter extends FragmentPagerAdapter{

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

//            case 0 :
//                ChatsFragment chatsFragment=new ChatsFragment();
//                return  chatsFragment;
            case 0:
                FriendsFragment friendsFragment=new FriendsFragment();
                return  friendsFragment;

            default: return null;


        }

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

//            case 0:
//                return "CHATS";
            case 0:
                return "FRIENDS";
            default:
                return null;

        }

    }
}

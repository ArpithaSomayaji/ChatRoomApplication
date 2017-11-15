package com.arpithasomayaji.chatroomapplication.ChatsScreen;

import java.util.List;

/**
 * Created by arpitha.somayaji on 11/15/2017.
 */

public interface ChatScreenContract {

    interface viewActions{
        void setAppBarTitle(String chatUserName);
        void setLastSeen(String lastSeen);
        void computeLastSeen(String lastSeen);
        String getChatMessage();
        void setMessageFieldtoNull();
        void addToMessageList(Messages message);
        void adapterNotifyDatchanged();
        

    }

    interface userActions{

        void getLastSeenStatus(String chatUserName);

       
        void lastSeenUpdates(String chatUserID);
        void sendMessage(String chatUserID, String chatMessage);

        void loadMessages(String chatUserID);
    }
}

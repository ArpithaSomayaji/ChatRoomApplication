package com.arpithasomayaji.chatroomapplication.HomeScreen;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Arpitha.Somayaji on 11/8/2017.
 */

public interface HomeContract {

     interface UserActions{

        void isUserLoggedin();
        void userLogout();

     }


     interface ViewActions{
         void navigateWelcomScreen();
         void logoutUser();
         void navigateAllUsers();
         void navigateAccountSettings();
         void initializeFriendsList();
         void navigateToUserProfile(String list_user_id);
         void navigateToChatScreen(String listUserId, String list_user_id);
     }



}

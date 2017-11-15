package com.arpithasomayaji.chatroomapplication.HomeScreen;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Arpitha.Somayaji on 11/8/2017.
 */

public interface HomeContract {

     interface UserActions{

        public void isUserLoggedin(FirebaseAuth firebaseAuthService);


         void userLogout(FirebaseAuth firebaseAuthService);
     }


     interface ViewActions{
        public void navigateWelcomScreen();
             public void logoutUser();
         public void navigateAllUsers();
         public void navigateAccountSettings();



     }



}

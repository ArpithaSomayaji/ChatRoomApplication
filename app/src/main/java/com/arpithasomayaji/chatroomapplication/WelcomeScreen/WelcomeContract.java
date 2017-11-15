package com.arpithasomayaji.chatroomapplication.WelcomeScreen;

/**
 * Created by Arpitha.Somayaji on 11/8/2017.
 */
public interface WelcomeContract {


    interface viewActions{

        void clickLogin();

        void clickRegister();

    }

    interface userActions {

        void navigateLogin();

        void navigateRegister();

    }


}
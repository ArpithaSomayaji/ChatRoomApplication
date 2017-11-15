package com.arpithasomayaji.chatroomapplication.WelcomeScreen;

import com.arpithasomayaji.chatroomapplication.BasePresenter;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by Arpitha.Somayaji on 11/8/2017.
 */

public class WelcomePresenter implements BasePresenter<WelcomeContract.viewActions>,WelcomeContract.userActions {


    private WelcomeContract.viewActions viewActions;

    public WelcomePresenter(WelcomeContract.viewActions viewActions) {
        this.viewActions = checkNotNull(viewActions, "Login View cannot be null!");
}

    @Override
    public void bind(WelcomeContract.viewActions view) {
        viewActions=view;
    }

    @Override
    public void unbind() {
        viewActions=null;

    }

    @Override
    public void navigateLogin() {
        viewActions.clickLogin();

    }

    @Override
    public void navigateRegister() {
        viewActions.clickRegister();
    }
}

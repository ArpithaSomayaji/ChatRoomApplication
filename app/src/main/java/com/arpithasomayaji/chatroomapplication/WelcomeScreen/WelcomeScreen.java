package com.arpithasomayaji.chatroomapplication.WelcomeScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.arpithasomayaji.chatroomapplication.LoginScreen.LoginScreen;
import com.arpithasomayaji.chatroomapplication.R;
import com.arpithasomayaji.chatroomapplication.RegisterScreen.Login.RegisterScreen;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class WelcomeScreen extends AppCompatActivity implements WelcomeContract.viewActions {

    WelcomePresenter welcomepresenter;
    @InjectView(R.id.welcome_signup_button)
    Button signup_welcomescreen;
    @InjectView(R.id.welcome_login_button) Button login_welcomscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        ButterKnife.inject(this);
        welcomepresenter=new WelcomePresenter(this);



    }

    @OnClick(R.id.welcome_login_button)
    public void doLogin(){
        welcomepresenter.navigateLogin();
    }




    @Override
    public void clickLogin() {
        startActivity(new Intent(this, LoginScreen.class));

    }

    @OnClick(R.id.welcome_signup_button)
    public void doRegister(){
        welcomepresenter.navigateRegister();
    }

    @Override
    public void clickRegister() {
        startActivity(new Intent(this, RegisterScreen.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        welcomepresenter.unbind();
        welcomepresenter = null;
    }
}

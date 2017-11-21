package com.arpithasomayaji.chatroomapplication.LoginScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.arpithasomayaji.chatroomapplication.HomeScreen.HomeScreen;
import com.arpithasomayaji.chatroomapplication.R;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginScreen extends AppCompatActivity implements LoginContract.ViewActions {


    @InjectView(R.id.username_loginscreen)
    EditText loginUsername;
    @InjectView(R.id.password_loginscreen)
    EditText loginPassword;

    @Inject
    LoginPresenter loginPresenter;

    private FirebaseAuth firebaseAuthService;





    //ProgressDialog
    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuthService=FirebaseAuth.getInstance();

        mRegProgress = new ProgressDialog(this);
        ButterKnife.inject(this);

        loginPresenter=new LoginPresenter(this);
        loginPresenter.bind(this);
    }

    @OnClick(R.id.login_loginscreen)
    public void LoginTap(){

        if(loginPresenter!=null){
            if(loginPresenter.checkNullsForFields(getEmail(),getPassword())){


                mRegProgress.setTitle("Loginng in User");
                mRegProgress.setMessage("Please wait while we login to  your account !");
                mRegProgress.setCanceledOnTouchOutside(false);
                mRegProgress.show();

                loginPresenter.doLogin(getEmail(),getPassword(),firebaseAuthService);
            }


        }
        else {
            showToastMessage("Please Try Again!");
        }
    }

    @Override
    public void dismissDialog() {
        mRegProgress.dismiss();
    }
    @Override
    public String getEmail() {
        return loginUsername.getText().toString().trim();
    }

    @Override
    public void markErrorForEmail() {
        loginUsername.setError("E-mail cannot be empty");
    }

    @Override
    public String getPassword() {
        return loginPassword.getText().toString().trim();
    }

    @Override
    public void markErrorForPassword() {
        loginPassword.setError("Password cannot be empty");
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message,Toast.LENGTH_LONG).show();
    }


    @Override
    public void navigateAfterLogin() {

        Intent mainIntent = new Intent(this, HomeScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.unbind();
        loginPresenter = null;
    }
}

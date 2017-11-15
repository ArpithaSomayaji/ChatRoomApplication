package com.arpithasomayaji.chatroomapplication.RegisterScreen.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.arpithasomayaji.chatroomapplication.FirebaseAuthService;
import com.arpithasomayaji.chatroomapplication.HomeScreen.HomeScreen;
import com.arpithasomayaji.chatroomapplication.LoginScreen.LoginScreen;
import com.arpithasomayaji.chatroomapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterScreen extends AppCompatActivity implements RegisterContract.ViewActions {

    @InjectView(R.id.username_registerscreen)EditText nameEdit;
    @InjectView(R.id.email_registerscreen)EditText emailEdit;
    @InjectView(R.id.password_registerscreen)EditText passwordEdit;
    @InjectView(R.id.repassword_registerscreen)EditText confirmPasswordEdit;
    private FirebaseAuth firebaseAuthService;





    //ProgressDialog
    private ProgressDialog mRegProgress;


    @Inject
    RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        firebaseAuthService=new FirebaseAuthService().Initialize();

        mRegProgress = new ProgressDialog(this);
        ButterKnife.inject(this);
        presenter=new RegisterPresenter(this);
        presenter.bind(this);

    }


    @OnClick(R.id.register_registerscreen)
    public void signUpTap() {
        if (presenter != null) {

            if (presenter.checkNullsForFields(getName(),getEmail(),getPassword())) {

                if (presenter.validateConfirmPasswordField(getPassword(), getConfirmPassword())) {


                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("Please wait while we create your account !");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    presenter.registerNewUser(getName(),getEmail(),getPassword(),firebaseAuthService);
                }

            }
            else {
                showToastMessage("Please Try Again!");
            }
        }
    }


    @Override
    public void dismissDialog() {
        mRegProgress.dismiss();
    }

    @Override
    public String getName() {
        return nameEdit.getText().toString().trim();
    }

    @Override
    public void markErrorName() {

        nameEdit.setError("Name cannot be empty");

    }


    @Override
    public String getEmail() {
        return emailEdit.getText().toString().trim();
    }

    @Override
    public void markErrorForEmail() {
        emailEdit.setError("Email should be for the format : email@example.com");

    }


    @Override
    public String getPassword() {
        return passwordEdit.getText().toString().trim();
    }

    @Override
    public void markErrorForPassword() {
        passwordEdit.setError("Password cannot be empty");

    }


    @Override
    public String getConfirmPassword() {
        return confirmPasswordEdit.getText().toString().trim();
    }

    @Override
    public void markErrorForConfirmPassword() {
        confirmPasswordEdit.setError("Password mismatch. Please check again");
    }

    @Override
    public void showToastMessage(String message) {

        Toast.makeText(this, message,Toast.LENGTH_LONG).show();

    }


    @Override
    public void navigateAfterRegister() {

        //startActivity(new Intent(this, LoginScreen.class));

        Intent mainIntent = new Intent(this, HomeScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unbind();
        presenter = null;
    }
}

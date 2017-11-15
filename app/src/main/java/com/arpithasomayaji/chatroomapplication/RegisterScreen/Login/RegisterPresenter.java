package com.arpithasomayaji.chatroomapplication.RegisterScreen.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.arpithasomayaji.chatroomapplication.BasePresenter;
import com.arpithasomayaji.chatroomapplication.FirebaseAuthService;
import com.arpithasomayaji.chatroomapplication.ValidationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import static android.support.v4.util.Preconditions.checkNotNull;
import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Arpitha.Somayaji on 11/8/2017.
 */

public class RegisterPresenter implements BasePresenter<RegisterContract.ViewActions>,RegisterContract.UserActions {

    private DatabaseReference mDatabase;

    private RegisterContract.ViewActions viewActions;

    public RegisterPresenter(RegisterContract.ViewActions viewActions) {

        this.viewActions = checkNotNull(viewActions, "Register View cannot be null!");
    }

    @Override
    public void bind(RegisterContract.ViewActions view) {

        viewActions=view;

    }

    @Override
    public void unbind() {
        viewActions=null;

    }

    @Override
    public boolean checkNullsForFields(String name, String email, String password) {

        boolean noErrors = true;

        if (!validateNameField(name)) {
            noErrors = false;
        }



        if (!validateEmailField(email)) {
            noErrors = false;
        }


        if (!validatePasswordField(password)) {
            noErrors = false;
        }

        return noErrors;

    }

    @Override
    public void registerNewUser(final String name, String email, String password, FirebaseAuth firebaseAuthService) {

        firebaseAuthService.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("status", "Hi there I'm using Chat Room");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                viewActions.dismissDialog();
                              viewActions.navigateAfterRegister();

                            }

                        }
                    });


                } else {

                    viewActions.dismissDialog();
                    //Toast.makeText(RegisterActivity.this, "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();
                    viewActions.showToastMessage(task.getException().getMessage().toString());
                }

            }
        });






    }




    public boolean validateConfirmPasswordField(String password, String confirmPassword) {
        if (ValidationUtil.isTextEmpty(confirmPassword)) {
            viewActions.markErrorForConfirmPassword();
            return false;
        }
        else if (!password.equals(confirmPassword)) {
            viewActions.markErrorForConfirmPassword();
            return false;
        }

        return true;


    }

    public boolean validateNameField(String name) {
        if (ValidationUtil.isTextEmpty(name)) {
            viewActions.markErrorName();
            return false;
        }

        return true;
    }





    public boolean validateEmailField(String email) {
        if (ValidationUtil.isTextEmpty(email)) {
            viewActions.markErrorForEmail();
            return false;
        }
        else if (!ValidationUtil.isEmailFormatValid(email)) {
            viewActions.markErrorForEmail();
            return false;
        }

        return true;
    }



    public boolean validatePasswordField(String password) {
        if (ValidationUtil.isTextEmpty(password)) {
            viewActions.markErrorForPassword();
            return false;
        }
        else if (password.length() < 6) {
            viewActions.markErrorForPassword();
            return false;
        }

        return true;
    }



}

package com.arpithasomayaji.chatroomapplication.LoginScreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.arpithasomayaji.chatroomapplication.BasePresenter;
import com.arpithasomayaji.chatroomapplication.RegisterScreen.Login.RegisterContract;
import com.arpithasomayaji.chatroomapplication.ValidationUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by Arpitha.Somayaji on 11/8/2017.
 */

public class LoginPresenter  implements BasePresenter<LoginContract.ViewActions>,LoginContract.UserActions {

    private DatabaseReference databaseRefrence;

    private LoginContract.ViewActions viewActions;


    public LoginPresenter(LoginContract.ViewActions viewActions) {

        this.viewActions = checkNotNull(viewActions, "Login View cannot be null!");
        databaseRefrence= FirebaseDatabase.getInstance().getReference();
    }



    @Override
    public void bind(LoginContract.ViewActions view) {

        viewActions=view;

    }

    @Override
    public void unbind() {


        viewActions=null;

    }

    @Override
    public boolean checkNullsForFields(String email, String password) {
        boolean noErrors = true;



        if (!validateEmailField(email)) {
            noErrors = false;
        }


        if (!validatePasswordField(password)) {
            noErrors = false;
        }

        return noErrors;

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


        return true;
    }

    @Override
    public void doLogin(String email, String password, final FirebaseAuth firebaseAuthService) {
        firebaseAuthService.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                   viewActions.dismissDialog();
                   // viewActions.navigateAfterLogin();

                    String current_user_id = firebaseAuthService.getCurrentUser().getUid();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

                    databaseRefrence.child("Users").child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                           viewActions.navigateAfterLogin();

                        }
                    });




                } else {


                    viewActions.dismissDialog();

                    String task_result = task.getException().getMessage().toString();
                    viewActions.showToastMessage(task_result);

                }

            }
        });








    }
}

package com.arpithasomayaji.chatroomapplication.HomeScreen;

import com.arpithasomayaji.chatroomapplication.BasePresenter;
import com.arpithasomayaji.chatroomapplication.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by Arpitha.Somayaji on 11/8/2017.
 */

public class HomePresenter implements BasePresenter<HomeContract.ViewActions>,HomeContract.UserActions {

    private  HomeContract.ViewActions viewActions;
    private DatabaseReference userRef;

    public HomePresenter(HomeContract.ViewActions viewActions) {
        this.viewActions =checkNotNull(viewActions, "Login View cannot be null!");
    }


    @Override
    public void bind(HomeContract.ViewActions view) {

        viewActions=view;


    }

    @Override
    public void unbind() {
        viewActions=null;

    }

    @Override
    public void isUserLoggedin(FirebaseAuth firebaseAuthService) {

        FirebaseUser currentUser= firebaseAuthService.getCurrentUser();

        if(currentUser==null) {
            viewActions.navigateWelcomScreen();

        }

        else {




                userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseAuthService.getCurrentUser().getUid());



                userRef.child("online").setValue("true");

        }



    }

    @Override
    public void userLogout(FirebaseAuth firebaseAuthService) {
        FirebaseUser currentUser = firebaseAuthService.getCurrentUser();

        if(currentUser != null) {

            userRef.child("online").setValue(ServerValue.TIMESTAMP);

        }
    }
}

package com.arpithasomayaji.chatroomapplication.HomeScreen;

import com.arpithasomayaji.chatroomapplication.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;

import static android.support.v4.util.Preconditions.checkNotNull;

public class HomePresenter implements BasePresenter<HomeContract.ViewActions>,HomeContract.UserActions {

    private  HomeContract.ViewActions viewActions;
    private DatabaseReference userRef;
    private FirebaseAuth firebaseAuthService;
    private DatabaseReference database;

    public HomePresenter(HomeContract.ViewActions viewActions, FirebaseAuth firebaseAuthService, DatabaseReference database) {
        this.viewActions = checkNotNull(viewActions, "Login View cannot be null!");
        this.firebaseAuthService = firebaseAuthService;
        this.database = database;
    }


    @Override
    public void bind(HomeContract.ViewActions view) {
        viewActions = view;
    }

    @Override
    public void unbind() {
        viewActions = null;
    }

    @Override
    public void isUserLoggedin() {

        FirebaseUser currentUser = firebaseAuthService.getCurrentUser();

        if(currentUser == null) {
            viewActions.navigateWelcomScreen();
        }

        else {
                userRef = database.child("Users").child(firebaseAuthService.getCurrentUser().getUid());
                userRef.child("online").setValue("true");
                viewActions.initializeFriendsList();
        }
    }

    @Override
    public void userLogout() {
        FirebaseUser currentUser = firebaseAuthService.getCurrentUser();

        if(currentUser != null) {
            userRef.child("online").setValue(ServerValue.TIMESTAMP);
        }

        firebaseAuthService.signOut();
    }
}

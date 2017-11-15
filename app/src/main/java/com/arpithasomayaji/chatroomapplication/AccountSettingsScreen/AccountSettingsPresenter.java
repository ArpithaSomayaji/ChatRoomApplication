package com.arpithasomayaji.chatroomapplication.AccountSettingsScreen;

import com.arpithasomayaji.chatroomapplication.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by arpitha.somayaji on 11/12/2017.
 */

public class AccountSettingsPresenter implements BasePresenter<AccountSettingsContract.viewActions>,AccountSettingsContract.userActions {

    private AccountSettingsContract.viewActions viewActions;

    private DatabaseReference databaseReference;




    public AccountSettingsPresenter(AccountSettingsContract.viewActions viewActions) {
        //this.viewActions=checkNotNull(viewActions, "Account Settings View cannot be null!");

    }

    @Override
    public void bind(AccountSettingsContract.viewActions view) {

        viewActions=view;

    }

    @Override
    public void unbind() {
        databaseReference = null;
        viewActions=null;

    }

    @Override
    public void getUserProfileData(FirebaseAuth firebaseAuthService) {


        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid;
        if(current_user!=null){
            uid = current_user.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


            databaseReference.keepSynced(true);


        }

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(viewActions != null)
                {
                    if (dataSnapshot != null){

                        String name = dataSnapshot.child("name").getValue().toString();

                        String status = dataSnapshot.child("status").getValue().toString();

                        setFields(name, status);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    private void setFields(String name, String status) {

        viewActions.setNameField(name);
        viewActions.setStatusField(status);
    }
}

package com.arpithasomayaji.chatroomapplication.UpdateStatusScreen;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.arpithasomayaji.chatroomapplication.BasePresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by arpitha.somayaji on 11/13/2017.
 */

public class UpdateStatusPresenter implements BasePresenter<UpdateStatusContract.viewActions>,UpdateStatusContract.userActions {

   private UpdateStatusContract.viewActions viewActions;
    //Firebase
    private DatabaseReference statusDatabase;
    private FirebaseUser currentUser;

    public UpdateStatusPresenter(UpdateStatusContract.viewActions viewActions) {
        this.viewActions=checkNotNull(viewActions," Status Update View cannot be null!");
    }



    @Override
    public void bind(UpdateStatusContract.viewActions view) {
        viewActions=view;

    }

    @Override
    public void unbind() {

        viewActions=null;

    }

    @Override
    public void setUserStatusToFirebase(String updatedStatus) {

        //Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = currentUser.getUid();

        statusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);


        statusDatabase.child("status").setValue(updatedStatus).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    viewActions.moveBacktoAccountSettings();



                } else {
                    viewActions.showToastMessage("There was some error in saving Changes.Please Try again");


                }

            }
        });




    }

    @Override
    public void setUserStatus(String status_value) {
        viewActions.setUserStatusField(status_value);

    }
}

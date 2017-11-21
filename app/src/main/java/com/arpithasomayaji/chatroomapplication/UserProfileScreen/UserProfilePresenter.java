package com.arpithasomayaji.chatroomapplication.UserProfileScreen;

import com.arpithasomayaji.chatroomapplication.BasePresenter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by arpitha.somayaji on 11/13/2017.
 */

public class UserProfilePresenter implements BasePresenter<UserProfileContract.viewActions>,UserProfileContract.userActions {

   private UserProfileContract.viewActions viewActions;

    private DatabaseReference usersDatabase;

    private DatabaseReference friendReqDatabase;
    private DatabaseReference friendDatabase;
    private DatabaseReference notificationDatabase;

    private DatabaseReference rootRef;

    private FirebaseUser currentUser;
    private String current_state;




    public UserProfilePresenter(UserProfileContract.viewActions viewActions) {
        this.viewActions=checkNotNull(viewActions," User Profile View cannot be null!");
        rootRef = FirebaseDatabase.getInstance().getReference();
        friendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        friendDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        notificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void bind(UserProfileContract.viewActions view) {
        viewActions=view;
    }

    @Override
    public void unbind() {
        viewActions=null;

    }

    @Override
    public void getUserProfileDetailsFromFirebase(final String user_id) {
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        current_state = "not_friends";
        viewActions.setInvisibleDeleteRequestButton();
        viewActions.disableDeleteRequestButton();
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name=dataSnapshot.child("name").getValue().toString();
                String display_status=dataSnapshot.child("status").getValue().toString();

                viewActions.setUserProfileName(display_name);
                viewActions.setUserProfileStatus(display_status);

                if(currentUser.getUid().equals(user_id)){

                    viewActions.setInvisibleDeleteRequestButton();
                    viewActions.disableDeleteRequestButton();

                    viewActions.disableRequestButton();
                    viewActions.setInvisibleRequestButton();

                }

                //--------------- FRIENDS LIST / REQUEST FEATURE -----
                friendReqDatabase.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(user_id)) {

                            String req_type = dataSnapshot.child(user_id).child("request_type").getValue().toString();

                            if (req_type.equals("received")) {

                                current_state = "req_received";
                                viewActions.changeTextRequestButton("ACCEPT FRIEND REQUEST");
                                viewActions.setVisibleDeleteRequestButton();
                                viewActions.enableDeleteRequestButton();
                            } else if (req_type.equals("sent")) {
                                current_state = "req_sent";
                                viewActions.changeTextRequestButton("CANCEL FRIEND REQUEST");
                                viewActions.setInvisibleDeleteRequestButton();
                                viewActions.disableDeleteRequestButton();


                            }

                            viewActions.dismissDialog();
                        }
                            else {

                            friendDatabase.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.hasChild(user_id)) {

                                        current_state = "friends";
                                        viewActions.changeTextRequestButton("Unfriend this Person");

                                        viewActions.setInvisibleDeleteRequestButton();
                                        viewActions.disableDeleteRequestButton();
                                    }

                                    viewActions.dismissDialog();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                    viewActions.dismissDialog();

                                }
                            });


                            }


                        }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        viewActions.showToastMessage("Oops! Something went wrong! Please try again");
                    }
                });




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                viewActions.showToastMessage("Oops! Something went wrong! Please try again");

            }
        });



    }

    @Override
    public void sendRequesttoUser(final String user_id) {
        viewActions.disableRequestButton();
        // --------------- NOT FRIENDS STATE ------------
        if(current_state.equals("not_friends")){


            DatabaseReference newNotificationref = rootRef.child("notifications").child(user_id).push();
            String newNotificationId = newNotificationref.getKey();

            HashMap<String, String> notificationData = new HashMap<>();
            notificationData.put("from", currentUser.getUid());
            notificationData.put("type", "request");

            Map requestMap = new HashMap();
            requestMap.put("Friend_req/" + currentUser.getUid() + "/" + user_id + "/request_type", "sent");
            requestMap.put("Friend_req/" + user_id + "/" + currentUser.getUid() + "/request_type", "received");
            requestMap.put("notifications/" + user_id + "/" + newNotificationId, notificationData);

            rootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if(databaseError != null){

                        viewActions.showToastMessage("There was some error in sending request");

                    } else {

                        current_state = "req_sent";
                        viewActions.changeTextRequestButton("Cancel Friend Request");

                    }

                    viewActions.enableRequestButton();


                }
            });
        }


        if(current_state.equals("req_sent")){
            friendReqDatabase.child(currentUser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    friendReqDatabase.child(user_id).child(currentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            viewActions.enableRequestButton();
                            current_state="not_friends";
                            viewActions.changeTextRequestButton("SEND REQUEST");
                            viewActions.setInvisibleDeleteRequestButton();
                            viewActions.disableDeleteRequestButton();

                        }
                    });

                }
            });


        }

        if(current_state.equals("req_received")){
            final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

            Map friendsMap = new HashMap();
            friendsMap.put("Friends/" + currentUser.getUid() + "/" + user_id + "/date", currentDate);
            friendsMap.put("Friends/" + user_id + "/"  + currentUser.getUid() + "/date", currentDate);


            friendsMap.put("Friend_req/" + currentUser.getUid() + "/" + user_id, null);
            friendsMap.put("Friend_req/" + user_id + "/" + currentUser.getUid(), null);

            rootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                    if(databaseError == null){

                        viewActions.enableRequestButton();
                        current_state = "friends";
                        viewActions.changeTextRequestButton("Unfriend this Person");



                        viewActions.setInvisibleDeleteRequestButton();
                        viewActions.disableDeleteRequestButton();

                    } else {

                        String error = databaseError.getMessage();

                        viewActions.showToastMessage(error);


                    }

                }
            });



        }

        if(current_state.equals("friends")){

            Map unfriendMap = new HashMap();
            unfriendMap.put("Friends/" + currentUser.getUid() + "/" + user_id, null);
            unfriendMap.put("Friends/" + user_id + "/" + currentUser.getUid(), null);

            rootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                    if(databaseError == null){

                        current_state = "not_friends";
                        viewActions.changeTextRequestButton("Send Friend Request");

                        viewActions.setInvisibleDeleteRequestButton();
                        viewActions.disableDeleteRequestButton();



                    } else {

                        String error = databaseError.getMessage();

                       viewActions.showToastMessage(error);


                    }

                    viewActions.enableRequestButton();

                }
            });

        }




    }
}

package com.arpithasomayaji.chatroomapplication.ChatsScreen;

import android.text.TextUtils;
import android.util.Log;

import com.arpithasomayaji.chatroomapplication.BasePresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by arpitha.somayaji on 11/15/2017.
 */

public class ChatScreenPresenter implements BasePresenter<ChatScreenContract.viewActions>,ChatScreenContract.userActions {


    private ChatScreenContract.viewActions viewActions;
    private FirebaseAuth auth;
    private String currentUserId;

    private DatabaseReference rootRef;

    public ChatScreenPresenter(ChatScreenContract.viewActions viewActions, FirebaseAuth firebaseAuthService, DatabaseReference database, String currentUserID) {
        this.viewActions = checkNotNull(viewActions," Chat Screen View cannot be null!");
        rootRef = database;
        auth =firebaseAuthService;
        currentUserId = currentUserID;
    }

    @Override
    public void bind(ChatScreenContract.viewActions view) {
        viewActions=view;

    }

    @Override
    public void unbind() {

        viewActions=null;


    }

    @Override
    public void  getLastSeenStatus(String chatUserID) {


        rootRef.child("Users").child(chatUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String online = dataSnapshot.child("online").getValue().toString();
              //  String image = dataSnapshot.child("image").getValue().toString();

                if (online.equals("true")) {

                    viewActions.setLastSeen("Online");

                } else {

                    viewActions.computeLastSeen(online);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void lastSeenUpdates(final String chatUserID) {
        rootRef.child("Chat").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.hasChild(chatUserID)){

                    Map chatAddMap = new HashMap();
                    chatAddMap.put("seen", false);
                    chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                    Map chatUserMap = new HashMap();
                    chatUserMap.put("Chat/" + currentUserId + "/" + chatUserID, chatAddMap);
                    chatUserMap.put("Chat/" + currentUserId + "/" + chatUserID, chatAddMap);

                    rootRef.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if(databaseError != null){

                                Log.d("CHAT_LOG", databaseError.getMessage().toString());

                            }

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void sendMessage(String chatUserID, String chatMessage) {

         if(!TextUtils.isEmpty(chatMessage)) {

             String current_user_ref = "messages/" + currentUserId + "/" + chatUserID;
             String chat_user_ref = "messages/" + chatUserID + "/" + currentUserId;

             DatabaseReference user_message_push = rootRef.child("messages")
                     .child(currentUserId).child(chatUserID).push();

             String push_id = user_message_push.getKey();

             Map messageMap = new HashMap();
             messageMap.put("message", chatMessage);
             messageMap.put("seen", false);
             messageMap.put("type", "text");
             messageMap.put("time", ServerValue.TIMESTAMP);
             messageMap.put("from", currentUserId);

             Map messageUserMap = new HashMap();
             messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
             messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

             viewActions.setMessageFieldtoNull();

             rootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                 @Override
                 public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                     if(databaseError != null){

                         Log.d("CHAT_LOG", databaseError.getMessage().toString());

                     }

                 }
             });


         }
    }

    @Override
    public void loadMessages(String chatUserID) {


        rootRef.child("messages").child(currentUserId).child(chatUserID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = dataSnapshot.getValue(Messages.class);

                //messagesList.add(message);
               // mAdapter.notifyDataSetChanged();

                viewActions.addToMessageList(message);
                viewActions.adapterNotifyDatchanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

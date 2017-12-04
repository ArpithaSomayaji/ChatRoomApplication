package com.arpithasomayaji.chatroomapplication.HomeScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arpithasomayaji.chatroomapplication.AccountSettingsScreen.AccountSettingsScreen;
import com.arpithasomayaji.chatroomapplication.ChatsScreen.ChatScreen;
import com.arpithasomayaji.chatroomapplication.ListUsers.ListUserScreen;
import com.arpithasomayaji.chatroomapplication.R;
import com.arpithasomayaji.chatroomapplication.UserProfileScreen.UserProfileScreen;
import com.arpithasomayaji.chatroomapplication.WelcomeScreen.WelcomeScreen;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeScreen extends AppCompatActivity implements HomeContract.ViewActions {

    private HomePresenter homePresenter;
    private FirebaseAuth firebaseAuthService;
    private DatabaseReference database;
    private DatabaseReference friendsDatabase;
    private String currentUserId;
    private DatabaseReference usersDatabase;



    @InjectView(R.id.main_page_toolbar)Toolbar home_page_toolbar;

    @InjectView(R.id.friends_list) RecyclerView friendsList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //---Firebase Initializations------
        firebaseAuthService = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        //--Home presenter---
        homePresenter = new HomePresenter(this, firebaseAuthService, database);
        homePresenter.bind(this);

        //--- Butterknife----
        ButterKnife.inject(this);

        //--Toolbar ----
        setSupportActionBar(home_page_toolbar);
        getSupportActionBar().setTitle("FRIENDS");
    }

    @Override
    public void onStart(){
        super.onStart();
        //-- Check if User is Logged in --
        homePresenter.isUserLoggedin();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        //--Menu---
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

        //-- Select Menu --
        switch (item.getItemId()) {

       case R.id.main_page_logout:
                logoutUser();
                break;
            case R.id.main_page_all_users:
                navigateAllUsers();
                break;
            case R.id.main_page_account_settings:
                navigateAccountSettings();
                break;
        }

        return true;
    }

    @Override
    public void logoutUser() {
        //--Logotu User---
        homePresenter.userLogout();
        sendUsertoWelcomePage();
    }

    @Override
    public void navigateAllUsers() {
        Intent allUSersIntent = new Intent(this, ListUserScreen.class);
        startActivity(allUSersIntent);
    }

    @Override
    public void navigateAccountSettings() {
        Intent settingsIntent = new Intent(this, AccountSettingsScreen.class);
        startActivity(settingsIntent);

    }

    @Override
    public void initializeFriendsList() {

        currentUserId =  firebaseAuthService.getCurrentUser().getUid();
        usersDatabase = database.child("Users");
        friendsDatabase = database.child("Friends").child(currentUserId);

        //Initialize- List
        friendsList.setHasFixedSize(true);
        friendsList.setLayoutManager(new LinearLayoutManager(this));

        //set up
        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerViewAdapter= getFirebaseRecyclerAdapter(friendsDatabase,usersDatabase);
        friendsList.setAdapter(friendsRecyclerViewAdapter);
    }

    @Override
    public void navigateToUserProfile(String list_user_id) {

        Intent profileIntent = new Intent(HomeScreen.this, UserProfileScreen.class);
        profileIntent.putExtra("from_user_id", list_user_id);
        startActivity(profileIntent);

    }

    @Override
    public void navigateToChatScreen(String friend_user_id, String friend_user_name ) {

        Intent chatIntent = new Intent(HomeScreen.this, ChatScreen.class);
        chatIntent.putExtra("friend_user_id", friend_user_id);
        chatIntent.putExtra("friend_user_name", friend_user_name);
        chatIntent.putExtra("current_user_id", currentUserId);

        startActivity(chatIntent);

    }

    private FirebaseRecyclerAdapter<Friends,FriendsViewHolder> getFirebaseRecyclerAdapter(DatabaseReference friendsDatabase, final DatabaseReference users) {
        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends,FriendsViewHolder>(
                Friends.class,
                R.layout.single_user_layout,
                FriendsViewHolder.class,
                friendsDatabase)
        {
            @Override
            protected void populateViewHolder(final FriendsViewHolder viewHolder,final Friends model, int position) {
                final String list_user_id = getRef(position).getKey();

                users.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        final String userStatus=dataSnapshot.child("status").getValue().toString();


                        if(dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            viewHolder.setUserOnline(userOnline);

                        }

                        //friendsViewHolder.setStatus(friends.getDate());

                        viewHolder.setName(userName);
                        viewHolder.setStatus(userStatus);

                        viewHolder.userNameView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                navigateToChatScreen(list_user_id, userName);

                            }
                        });

                        viewHolder.userStatusView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                navigateToChatScreen(list_user_id,userName);

                            }
                        });

                        viewHolder.userOnlineView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                navigateToChatScreen(list_user_id,userName);

                            }
                        });

                        viewHolder.singleUserImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                navigateToUserProfile(list_user_id);

                            }
                        });



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }


        };

       return  friendsRecyclerViewAdapter;

    }



    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView userStatusView;
        TextView userNameView;
        ImageView userOnlineView;

        ImageView singleUserImage;
        public FriendsViewHolder(View itemView) {
            super(itemView);

            view = itemView;
             userStatusView = (TextView) view.findViewById(R.id.single_user_status);
             userNameView = (TextView) view.findViewById(R.id.single_user_name);
             userOnlineView = (ImageView) view.findViewById(R.id.user_single_online_icon);
             singleUserImage = (ImageView) view.findViewById(R.id.single_user_image);

        }

        public void setStatus(String status){


            userStatusView.setText(status);

        }

        public void setName(String name){


            userNameView.setText(name);

        }



        public void setUserOnline(String online_status) {



            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }
    @Override
    public void navigateWelcomScreen() {
        sendUsertoWelcomePage();
    }

    private void sendUsertoWelcomePage() {
        Intent startIntent = new Intent(this, WelcomeScreen.class);
        startActivity(startIntent);
        finish();
    }



}

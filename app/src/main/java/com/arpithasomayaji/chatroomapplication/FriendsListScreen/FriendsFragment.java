package com.arpithasomayaji.chatroomapplication.FriendsListScreen;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arpithasomayaji.chatroomapplication.ChatsScreen.ChatScreen;
import com.arpithasomayaji.chatroomapplication.R;
import com.arpithasomayaji.chatroomapplication.UserProfileScreen.UserProfileScreen;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {



    private RecyclerView friends_list;
    private View mainView;


    private DatabaseReference friendsDatabase;
    private DatabaseReference usersDatabase;

    private FirebaseAuth auth;

    private String current_user_id;



    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView= inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.inject(mainView);
        friends_list = (RecyclerView) mainView.findViewById(R.id.friends_list);
        friends_list.setHasFixedSize(true);

        friends_list.setLayoutManager(new LinearLayoutManager(getContext()));

        auth = FirebaseAuth.getInstance();

        current_user_id = auth.getCurrentUser().getUid();

        friendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(current_user_id);
        friendsDatabase.keepSynced(true);
        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        usersDatabase.keepSynced(true);



        return mainView;

    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> friendsRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>(

                Friends.class,
                R.layout.single_user_layout,
                FriendsViewHolder.class,
                friendsDatabase


        ) {
            @Override
            protected void populateViewHolder(final FriendsViewHolder friendsViewHolder, final Friends friends, int i) {

                //friendsViewHolder.setStatus(friends.getDate());

                final String list_user_id = getRef(i).getKey();

                usersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String userName = dataSnapshot.child("name").getValue().toString();
                        final String userStatus=dataSnapshot.child("status").getValue().toString();


                        if(dataSnapshot.hasChild("online")) {

                            String userOnline = dataSnapshot.child("online").getValue().toString();
                            friendsViewHolder.setUserOnline(userOnline);

                        }

                        friendsViewHolder.setName(userName);
                        friendsViewHolder.setStatus(userStatus);


                        friendsViewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                CharSequence options[] = new CharSequence[]{"Open Profile", "Send message"};

                                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        //Click Event for each item.
                                        if(i == 0){

                                            Intent profileIntent = new Intent(getContext(), UserProfileScreen.class);
                                            profileIntent.putExtra("user_id", list_user_id);
                                            startActivity(profileIntent);

                                        }

                                        if(i == 1){

                                            Intent chatIntent = new Intent(getContext(), ChatScreen.class);
                                            chatIntent.putExtra("user_id", list_user_id);
                                            chatIntent.putExtra("user_name", userName);
                                            startActivity(chatIntent);

                                        }

                                    }
                                });

                                builder.show();

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        friends_list.setAdapter(friendsRecyclerViewAdapter);



    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View view;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            view = itemView;

        }

        public void setStatus(String status){

            TextView userStatusView = (TextView) view.findViewById(R.id.single_user_status);
            userStatusView.setText(status);

        }

        public void setName(String name){

            TextView userNameView = (TextView) view.findViewById(R.id.single_user_name);
            userNameView.setText(name);

        }



        public void setUserOnline(String online_status) {

            ImageView userOnlineView = (ImageView) view.findViewById(R.id.user_single_online_icon);

            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }


    }

}

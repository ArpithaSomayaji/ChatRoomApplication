package com.arpithasomayaji.chatroomapplication.ListUsers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.arpithasomayaji.chatroomapplication.R;
import com.arpithasomayaji.chatroomapplication.UserProfileScreen.UserProfileScreen;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ListUserScreen extends AppCompatActivity   {


    @InjectView(R.id.list_users_page_toolbar)
    Toolbar list_user_page_toolbar;
    @InjectView(R.id.user_list_recyclerview)
    RecyclerView user_list_recyclerview;

    private LinearLayoutManager layoutManager;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_screen);

        ButterKnife.inject(this);
        setSupportActionBar(list_user_page_toolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        layoutManager = new LinearLayoutManager(this);

        user_list_recyclerview.setHasFixedSize(true);
        user_list_recyclerview.setLayoutManager(layoutManager);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<SingleUser,userViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<SingleUser, userViewHolder>(
                SingleUser.class,
                R.layout.single_user_layout,
                userViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(userViewHolder viewHolder, SingleUser model, int position) {
            viewHolder.setUserName(model.getName());
                viewHolder.setUserStatus(model.getStatus());


                final String user_id = getRef(position).getKey();

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(ListUserScreen.this, UserProfileScreen.class);
                        profileIntent.putExtra("user_id", user_id);
                        startActivity(profileIntent);

                    }
                });
            }
        };
        user_list_recyclerview.setAdapter(firebaseRecyclerAdapter);

    }

    public static class userViewHolder extends RecyclerView.ViewHolder {




        View view;
        public userViewHolder(View itemView){
            super(itemView);
            view=itemView;
        }

        public void setUserName(String name) {

            TextView single_user_name=(TextView) view.findViewById(R.id.single_user_name);
            single_user_name.setText(name);

        }

        public void setUserStatus(String status) {


            TextView single_user_status=(TextView)view.findViewById(R.id.single_user_status);


            single_user_status.setText(status);
        }
    }
}

package com.arpithasomayaji.chatroomapplication.ListUsers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.test.InstrumentationRegistry.getContext;

public class ListUserScreen extends AppCompatActivity   {


    @InjectView(R.id.list_users_page_toolbar)
    Toolbar list_user_page_toolbar;
    @InjectView(R.id.user_list_recyclerview)
    RecyclerView user_list_recyclerview;

    private LinearLayoutManager layoutManager;

    private DatabaseReference databaseReference;
    String currentUserID;
    FirebaseAuth firebaseAuth;



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
        firebaseAuth= FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();

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
            protected void populateViewHolder(final userViewHolder viewHolder, SingleUser model, int position) {
            viewHolder.setUserName(model.getName());
                viewHolder.setUserStatus(model.getStatus());



                final String user_id = getRef(position).getKey();


                // == check if the user is friend===
                DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference().child("Friends").child(currentUserID);
                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(user_id)){
                            viewHolder.setUserFriendStatus();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






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

        public void setUserFriendStatus() {


            TextView single_user_status=(TextView)view.findViewById(R.id.friend_or_notification_status);
            single_user_status.setVisibility(View.VISIBLE);
            ShapeDrawable sd = new ShapeDrawable();

            // Specify the shape of ShapeDrawable
            sd.setShape(new RectShape());

            // Specify the border color of shape
            sd.getPaint().setColor(Color.GREEN);

            // Set the border width
            sd.getPaint().setStrokeWidth(5f);

            // Specify the style is a Stroke
            sd.getPaint().setStyle(Paint.Style.STROKE);



            single_user_status.setText("FRIENDS");
            single_user_status.setBackground(sd);
        }
    }
}

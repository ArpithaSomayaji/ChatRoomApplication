package com.arpithasomayaji.chatroomapplication.ChatsScreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arpithasomayaji.chatroomapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by arpitha.somayaji on 11/15/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MESSAGE_FROM_OTHERUSER = 0;
    public static final int MESSAGE_FROM_ME = 1;
    FirebaseAuth firebaseAuth;
    String currentUserID;


    private List<Messages> messagesList;
    private DatabaseReference userDatabase;

    public MessageAdapter(List<Messages> messagesList) {

        this.messagesList = messagesList;
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        switch (viewType) {
            case MESSAGE_FROM_OTHERUSER:
                View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.message_single_layout, viewGroup, false);
                MessageViewHolder viewHolder = new MessageViewHolder(v);
                return viewHolder;
            case MESSAGE_FROM_ME:
                View v1 = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.message_single_layout_me, viewGroup, false);
                MessageViewHolder viewHolder1 = new MessageViewHolder(v1);
                return viewHolder1;
        }
        return null;

    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        Messages singleMessage = messagesList.get(i);
        String from_user = singleMessage.getFrom();

            final MessageViewHolder messageViewHolder = (MessageViewHolder) viewHolder;

            userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

            userDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String name = dataSnapshot.child("name").getValue().toString();


                    messageViewHolder.displayName.setText(name);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            messageViewHolder.messageText.setText(singleMessage.getMessage());


    }

    @Override
    public int getItemViewType(int position) {

        Messages singleMessage = messagesList.get(position);

        String fromUser = singleMessage.getFrom();

        if (fromUser.equals(currentUserID.toString())) {

            return MESSAGE_FROM_ME;
        } else {
            return MESSAGE_FROM_OTHERUSER;
        }


    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;

        public TextView displayName;

        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            displayName = (TextView) view.findViewById(R.id.name_text_layout);

        }
    }


}

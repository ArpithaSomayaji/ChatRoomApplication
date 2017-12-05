package com.arpithasomayaji.chatroomapplication.ChatsScreen;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arpithasomayaji.chatroomapplication.GetTimeAgo;
import com.arpithasomayaji.chatroomapplication.R;
import com.arpithasomayaji.chatroomapplication.UserProfileScreen.UserProfileScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatScreen extends AppCompatActivity implements ChatScreenContract.viewActions{


    @InjectView(R.id.chat_page_toolbar)
    Toolbar chatPageToolbar;

    @InjectView(R.id.user_chats_view_recycler)
    RecyclerView userChatsViewRecycler;

    @InjectView(R.id.chat_add_btn)ImageButton chatAddButton;
    @InjectView(R.id.chat_message_view)EditText chatMessageView;
    @InjectView(R.id.chat_send_btn)ImageButton chatSendBtn;

    @InjectView(R.id.single_user_image)
    CircleImageView singleUserImage;

    @InjectView(R.id.appbar_username)
    TextView appbarUsername;


    @InjectView(R.id.appbar_lastseen)
    TextView appbarLastseen;

    private String friendUserName;
    private String friendUserID;
    private String currentUserID;

    @Inject
    ChatScreenPresenter chatScreenPresenter;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private MessageAdapter messageAdapter;

    private FirebaseAuth firebaseAuthService;
    private DatabaseReference database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);


        //---Firebase--
        firebaseAuthService = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        //------------Get Name and ID from previous Intent------------
        //TODO Handle App Proccess death

        Bundle bundle = getIntent().getExtras();
        // Handle possible data accompanying notification message.
        if (bundle != null) {

            friendUserName =getIntent().getStringExtra("friend_user_name");
            friendUserID =getIntent().getStringExtra("from_user_id");
            currentUserID=getIntent().getStringExtra("current_user_id");

        }

        ButterKnife.inject(this);

        // -------------Presenter----------------------------------
        chatScreenPresenter=new ChatScreenPresenter(this,firebaseAuthService,database,currentUserID);
        chatScreenPresenter.bind(this);

        initActionBar(friendUserName);
        initChatList();

        //--------------------Set USername and Last Seen Status----------------
        chatScreenPresenter.getLastSeenStatus(friendUserID);
        chatScreenPresenter.loadMessages(friendUserID);
        chatScreenPresenter.lastSeenUpdates(friendUserID);


    }

    private void initChatList() {
        messageAdapter = new MessageAdapter(messagesList);

        linearLayout = new LinearLayoutManager(this);
        linearLayout.setStackFromEnd(true);

        userChatsViewRecycler.setHasFixedSize(true);
        userChatsViewRecycler.setLayoutManager(linearLayout);
        userChatsViewRecycler.setAdapter(messageAdapter);
    }

    private void initActionBar(String friendUserName) {
        setSupportActionBar(chatPageToolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);


        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.custom_chat_bar, null);

        actionBar.setCustomView(action_bar_view);

        setAppBarTitle(friendUserName);
    }

    @Override
    public void setAppBarTitle(String chatUserName) {

        appbarUsername.setText(chatUserName);


    }

    @Override
    public void setLastSeen(String lastSeen) {
        appbarLastseen.setText(lastSeen);

    }

    @Override
    public void computeLastSeen(String lastSeen) {


        GetTimeAgo getTimeAgo = new GetTimeAgo();

        long lastTime = Long.parseLong(lastSeen);

        String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, getApplicationContext());

        setLastSeen(lastSeenTime);

}

    @Override
    public String getChatMessage() {
        return chatMessageView.getText().toString();
    }

    @Override
    public void setMessageFieldtoNull() {
        chatMessageView.setText("");
    }

    @Override
    public void addToMessageList(Messages message) {
        messagesList.add(message);

    }

    @Override
    public void adapterNotifyDatchanged() {
        messageAdapter.notifyDataSetChanged();

        }



    @OnClick(R.id.chat_send_btn)
    public void sendMessage(){
    chatScreenPresenter.sendMessage(friendUserID,getChatMessage(),friendUserName);


}

    @OnClick(R.id.chat_page_toolbar)
    public void gotoUserProfile(){

        Intent userProfileIntent = new Intent(this, UserProfileScreen.class);
        userProfileIntent.putExtra("from_user_id", friendUserID);
        startActivity(userProfileIntent);

            }



}

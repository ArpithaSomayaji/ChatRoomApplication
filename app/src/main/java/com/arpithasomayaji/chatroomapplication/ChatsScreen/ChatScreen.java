package com.arpithasomayaji.chatroomapplication.ChatsScreen;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatScreen extends AppCompatActivity implements ChatScreenContract.viewActions{


    @InjectView(R.id.chat_page_toolbar)
    Toolbar chat_page_toolbar;

    @InjectView(R.id.user_chats_view_recycler)
    RecyclerView user_chats_view_recycler;

    @InjectView(R.id.chat_add_btn)ImageButton chat_add_btn;
    @InjectView(R.id.chat_message_view)EditText chat_message_view;
    @InjectView(R.id.chat_send_btn)ImageButton chat_send_btn;

    @InjectView(R.id.single_user_image)
    CircleImageView single_user_image;

    @InjectView(R.id.appbar_username)
    TextView appbar_username;


    @InjectView(R.id.appbar_lastseen)
    TextView appbar_lastseen;

    private String chatUserName;
    private String chatUserID;

    @Inject
    ChatScreenPresenter chatScreenPresenter;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);


        //------------Get Name and ID from previous Intent------------

        chatUserName=getIntent().getStringExtra("user_name");
        chatUserID=getIntent().getStringExtra("user_id");

        ButterKnife.inject(this);

        // -------------Presenter----------------------------------

        chatScreenPresenter=new ChatScreenPresenter(this);
        chatScreenPresenter.bind(this);


        //---------------Custom Action Bar---------------
        setSupportActionBar(chat_page_toolbar);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);




        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view = inflater.inflate(R.layout.custom_chat_bar, null);

        actionBar.setCustomView(action_bar_view);

        //--------------Recycler View ---------------------------
        messageAdapter = new MessageAdapter(messagesList);

        linearLayout = new LinearLayoutManager(this);

        user_chats_view_recycler.setHasFixedSize(true);
        user_chats_view_recycler.setLayoutManager(linearLayout);

        user_chats_view_recycler.setAdapter(messageAdapter);




        //--------------------Set USername and Last Seen Status----------------

         setAppBarTitle(chatUserName);
         chatScreenPresenter.getLastSeenStatus(chatUserID);
         chatScreenPresenter.loadMessages(chatUserID);
        chatScreenPresenter.lastSeenUpdates(chatUserID);
    }

    @Override
    public void setAppBarTitle(String chatUserName) {

        appbar_username.setText(chatUserName);


    }

    @Override
    public void setLastSeen(String lastSeen) {
        appbar_lastseen.setText(lastSeen);

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
        return chat_message_view.getText().toString();
    }

    @Override
    public void setMessageFieldtoNull() {
        chat_message_view.setText("");
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
    chatScreenPresenter.sendMessage(chatUserID,getChatMessage());


}




}

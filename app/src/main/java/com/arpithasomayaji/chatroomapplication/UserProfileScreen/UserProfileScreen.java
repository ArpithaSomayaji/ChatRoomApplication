package com.arpithasomayaji.chatroomapplication.UserProfileScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arpithasomayaji.chatroomapplication.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserProfileScreen extends AppCompatActivity implements UserProfileContract.viewActions {

    @InjectView(R.id.user_profile_name)
    TextView user_profile_name;
    @InjectView(R.id.user_profile_status)
    TextView user_profile_status;
    @InjectView(R.id.user_profile_page_toolbar)
    Toolbar user_profile_page_toolbar;

    @InjectView(R.id.send_request_button)
    Button send_request_button;

    @InjectView(R.id.delete_request_button)
    Button delete_request_button;
    private ProgressDialog progressDialog;
     String user_id;

    UserProfilePresenter userProfilePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_screen);

        ButterKnife.inject(this);
        setSupportActionBar(user_profile_page_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        // Handle possible data accompanying notification message.
        if (bundle != null) {
            user_id=bundle.getString("from_user_id").toString();
        }



        userProfilePresenter=new UserProfilePresenter(this);
        userProfilePresenter.bind(this);
        getUserProfileData(user_id);


    }

    private void getUserProfileData(String user_id) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading User Data");
        progressDialog.setMessage("Please wait while we load the user data.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        userProfilePresenter.getUserProfileDetailsFromFirebase(user_id);
    }




    @Override
    public void setUserProfileName(String display_name) {
        user_profile_name.setText(display_name);

    }


    @OnClick(R.id.send_request_button)
    public void sendRequestToUser()
    {
        userProfilePresenter.sendRequesttoUser(user_id);

    }

    @Override
    public void setUserProfileStatus(String display_status) {
        user_profile_status.setText(display_status);

    }

    @Override
    public void dismissDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void disableRequestButton() {
        send_request_button.setEnabled(false);
    }

    @Override
    public void enableRequestButton() {
        send_request_button.setEnabled(true);
    }

    @Override
    public void changeTextRequestButton(String buttonText) {
        send_request_button.setText(buttonText);

    }

    @Override
    public void enableDeleteRequestButton() {
        delete_request_button.setEnabled(true);

    }

    @Override
    public void disableDeleteRequestButton() {
        delete_request_button.setEnabled(false);

    }

    @Override
    public void setVisibleDeleteRequestButton() {
        delete_request_button.setVisibility(View.VISIBLE);

    }

    @Override
    public void setInvisibleDeleteRequestButton() {
        delete_request_button.setVisibility(View.INVISIBLE);

    }

    @Override
    public void setInvisibleRequestButton() {
        send_request_button.setVisibility(View.INVISIBLE);
    }
}

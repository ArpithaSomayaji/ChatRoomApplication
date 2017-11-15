package com.arpithasomayaji.chatroomapplication.AccountSettingsScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.arpithasomayaji.chatroomapplication.FirebaseAuthService;
import com.arpithasomayaji.chatroomapplication.R;
import com.arpithasomayaji.chatroomapplication.UpdateStatusScreen.UpdateStatusScreen;
import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettingsScreen extends AppCompatActivity implements AccountSettingsContract.viewActions {

    @InjectView(R.id.display_name)
    TextView display_name;
    @InjectView(R.id.user_status)
    TextView user_status;
    @InjectView(R.id.acount_setting_image)
    CircleImageView account_setting_image;
    @InjectView(R.id.account_settings_page_toolbar)
    Toolbar account_settings_page_toolbar;
    @InjectView(R.id.change_status)
    Button change_status;


    private FirebaseAuth firebaseAuthService;




    @Inject
    AccountSettingsPresenter accountSettingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings_screen);
        ButterKnife.inject(this);

        accountSettingsPresenter=new AccountSettingsPresenter(this);
        accountSettingsPresenter.bind(this);

        firebaseAuthService=new FirebaseAuthService().Initialize();

        setSupportActionBar(account_settings_page_toolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        accountSettingsPresenter.getUserProfileData(firebaseAuthService);

    }



    @Override
    public void setNameField(String Name) {
        display_name.setText(Name);

    }

    @Override
    public void setStatusField(String Status) {
        user_status.setText(Status);

    }

    @Override
    public void navigateUpdateStatus(String statusValue) {
        Intent updateStatusIntent= new Intent(this, UpdateStatusScreen.class);
        updateStatusIntent.putExtra("user_status",statusValue);
        startActivity(updateStatusIntent);

    }

    @Override
    public String getStatusValue() {
       return user_status.getText().toString();
    }

    @OnClick(R.id.change_status)
    public void changeUserStatus(){

        navigateUpdateStatus(getStatusValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accountSettingsPresenter.unbind();
    }
}

package com.arpithasomayaji.chatroomapplication.UpdateStatusScreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.Toast;

import com.arpithasomayaji.chatroomapplication.AccountSettingsScreen.AccountSettingsScreen;
import com.arpithasomayaji.chatroomapplication.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UpdateStatusScreen extends AppCompatActivity implements UpdateStatusContract.viewActions {


    @InjectView(R.id.update_status_field) EditText update_status_field;
    @InjectView(R.id.update_status_page_toolbar)
    Toolbar update_status_page_toolbar;
    public String status_value;
    UpdateStatusPresenter updateStatusPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status_screen);

         status_value = getIntent().getStringExtra("user_status");


        ButterKnife.inject(this);
        setSupportActionBar(update_status_page_toolbar);
        getSupportActionBar().setTitle("Account User Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateStatusPresenter=new UpdateStatusPresenter(this);
        updateStatusPresenter.bind(this);
        updateStatusPresenter.setUserStatus(status_value);


    }

    @Override
    public void setUserStatusField(String status_value) {
        update_status_field.setText(status_value);

    }

    @Override
    public String getUpdatedStatus() {
        return update_status_field.getText().toString();
    }

    @Override
    public void moveBacktoAccountSettings() {
        Intent accountSettingsIntent=new Intent(this, AccountSettingsScreen.class);
        startActivity(accountSettingsIntent);

    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message,Toast.LENGTH_LONG).show();
    }


    @OnClick(R.id.update_status_button)
    public void onClickUpdateStatus(){
        updateStatusPresenter.setUserStatusToFirebase(getUpdatedStatus());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateStatusPresenter.unbind();
        updateStatusPresenter = null;
    }
}

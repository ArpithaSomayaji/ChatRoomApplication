package com.arpithasomayaji.chatroomapplication.HomeScreen;

import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.arpithasomayaji.chatroomapplication.AccountSettingsScreen.AccountSettingsScreen;
import com.arpithasomayaji.chatroomapplication.FirebaseAuthService;
import com.arpithasomayaji.chatroomapplication.ListUsers.ListUserScreen;
import com.arpithasomayaji.chatroomapplication.R;
import com.arpithasomayaji.chatroomapplication.WelcomeScreen.WelcomeScreen;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class HomeScreen extends AppCompatActivity implements HomeContract.ViewActions {

    public HomePresenter homePresenter;
    FirebaseAuth firebaseAuthService;



    @InjectView(R.id.main_page_toolbar) Toolbar home_page_toolbar;
    @InjectView(R.id.main_tabPager)
    ViewPager main_tabPager;
    @InjectView(R.id.main_tabs)
    TabLayout main_tabs;
    private SectionsPagerAdapter sectionsPagerAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firebaseAuthService=new FirebaseAuthService().Initialize();
        homePresenter=new HomePresenter(this);
        homePresenter.bind(this);
        ButterKnife.inject(this);

        setSupportActionBar(home_page_toolbar);

        sectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager());
        main_tabPager.setAdapter(sectionsPagerAdapter);
        main_tabs.setupWithViewPager(main_tabPager);




    }

    @Override
    public void onStart(){
        super.onStart();
        homePresenter.isUserLoggedin(firebaseAuthService);


    }

    @Override
    protected void onStop() {
        super.onStop();

        homePresenter.userLogout(firebaseAuthService);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

        System.out.println("test"+item.getItemId());

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

        homePresenter.userLogout(firebaseAuthService);
        firebaseAuthService.getInstance().signOut();
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
    public void navigateWelcomScreen() {

        sendUsertoWelcomePage();



    }

    private void sendUsertoWelcomePage() {

        Intent startIntent = new Intent(this, WelcomeScreen.class);
        startActivity(startIntent);
        finish();
    }



}

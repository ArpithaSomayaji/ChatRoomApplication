package com.arpithasomayaji.chatroomapplication.AccountSettingsScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by arpitha.somayaji on 11/12/2017.
 */

public interface AccountSettingsContract {


    interface viewActions{
      public void setNameField(String nameField);
      public void setStatusField(String statusField);
      public void navigateUpdateStatus(String statusValue);
      public String getStatusValue();


    }

    interface userActions{

        public void getUserProfileData(FirebaseAuth firebaseUser);



    }


}

package com.arpithasomayaji.chatroomapplication.UserProfileScreen;

/**
 * Created by arpitha.somayaji on 11/13/2017.
 */

public interface UserProfileContract {

    interface viewActions{

        void setUserProfileName(String display_name);
        void setUserProfileStatus(String display_status);
        void dismissDialog();
        void showToastMessage(String message);
        void disableRequestButton();
        void enableRequestButton();
        void changeTextRequestButton(String buttonText);
        void enableDeleteRequestButton();
        void disableDeleteRequestButton();
        void setVisibleDeleteRequestButton();
        void setInvisibleDeleteRequestButton();
        void setInvisibleRequestButton();




    }

    interface userActions{

        void getUserProfileDetailsFromFirebase(String user_id);
        void sendRequesttoUser(String user_id);
    }
}

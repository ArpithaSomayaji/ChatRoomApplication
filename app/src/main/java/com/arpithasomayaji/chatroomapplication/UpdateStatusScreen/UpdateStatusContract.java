package com.arpithasomayaji.chatroomapplication.UpdateStatusScreen;

/**
 * Created by arpitha.somayaji on 11/13/2017.
 */

public interface UpdateStatusContract {

    interface viewActions{

        public void setUserStatusField(String status_value);
        public String getUpdatedStatus();
        public void moveBacktoAccountSettings();
        void showToastMessage(String message);




    }

    interface userActions{

        public void setUserStatusToFirebase(String updatedStatus);
        public void setUserStatus(String status_value);


    }


}

package smartdev.bzzhub.repository.chatmodel;

import com.google.firebase.firestore.DocumentSnapshot;

import static smartdev.bzzhub.util.FBStoreKey.KEY_FLAG;
import static smartdev.bzzhub.util.FBStoreKey.KEY_USER_ID;
import static smartdev.bzzhub.util.FBStoreKey.KEY_USER_NAME;
import static smartdev.bzzhub.util.FBStoreKey.KEY_USER_PROFILE_IMAGE;

public class Users {

    int userID;
    int userFlag;
    String userName;
    String userProfileURL;
    String uid;

    public Users(DocumentSnapshot snapshot) {
        uid = snapshot.getId();

        if (snapshot.get(KEY_USER_ID) != null) {
            try {
                this.userID = Integer.parseInt(snapshot.get(KEY_USER_ID).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (snapshot.get(KEY_FLAG) != null) {
            try {
                this.userFlag = Integer.parseInt(snapshot.get(KEY_FLAG).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (snapshot.get(KEY_USER_NAME) != null) {
            try {
                this.userName = (snapshot.get(KEY_USER_NAME).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (snapshot.get(KEY_USER_PROFILE_IMAGE) != null) {
            try {
                this.userProfileURL = (snapshot.get(KEY_USER_PROFILE_IMAGE).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getUserID() {
        return userID;
    }

    public int getUserFlag() {
        return userFlag;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfileURL() {
        return userProfileURL;
    }

    public String getUid() {
        return uid;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUserFlag(int userFlag) {
        this.userFlag = userFlag;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserProfileURL(String userProfileURL) {
        this.userProfileURL = userProfileURL;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

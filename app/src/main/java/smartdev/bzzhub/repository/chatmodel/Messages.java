package smartdev.bzzhub.repository.chatmodel;

import android.graphics.Bitmap;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import static smartdev.bzzhub.util.FBStoreKey.KEY_FLAG;
import static smartdev.bzzhub.util.FBStoreKey.KEY_MESSAGE_CONTENT;
import static smartdev.bzzhub.util.FBStoreKey.KEY_SENDER_ID;
import static smartdev.bzzhub.util.FBStoreKey.KEY_TIME;

public class Messages {
    private static final String TAG = "GroupMessages";
    private int senderID;
    private String message_content = "";
    private Timestamp message_time;
    private String uid;
    private int flag;

    public Messages(DocumentSnapshot snapshot){
        uid = snapshot.getId();
        if (snapshot.get(KEY_SENDER_ID) != null) {
            try {
                this.senderID = Integer.parseInt(snapshot.get(KEY_SENDER_ID).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (snapshot.get(KEY_MESSAGE_CONTENT) != null) {
            try {
                this.message_content = snapshot.get(KEY_MESSAGE_CONTENT).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (snapshot.get(KEY_TIME) != null) {
            try {
                this.message_time = snapshot.getTimestamp(KEY_TIME);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (snapshot.get(KEY_FLAG) != null) {
            try {
                this.flag = Integer.parseInt(snapshot.get(KEY_FLAG).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getSenderID() {
        return senderID;
    }

    public String getMessage_content() {
        return message_content;
    }

    public Timestamp getMessage_time() {
        return message_time;
    }

    public String getUid() {
        return uid;
    }

    public int getFlag() {
        return flag;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public void setMessage_time(Timestamp message_time) {
        this.message_time = message_time;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}

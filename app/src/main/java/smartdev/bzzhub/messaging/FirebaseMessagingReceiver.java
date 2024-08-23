package smartdev.bzzhub.messaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.MyPreferenceManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Notification.DEFAULT_LIGHTS;
import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;
import static smartdev.bzzhub.repository.PreferenceKey.ARG_FIREBASE_TOKEN;

public class FirebaseMessagingReceiver  extends FirebaseMessagingService {
    private static final String STATUS_CHANNEL_ID = "Bz Notification Channel";
    private static final int STATUS_CHANEL_NOTIFICATION_ID = 412;
    private static final String TAG = "FCMService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d("notification_body",remoteMessage.getData().toString());

        JSONObject payloadJson = null;
        String body ="Bzhub", title = "Bzhub";
        if (remoteMessage.getData().size() > 0){

            body = remoteMessage.getData().get("body").toString();
            title = remoteMessage.getData().get("title").toString();
        }

/*
        if (remoteMessage.getData().size() > 0) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, STATUS_CHANNEL_ID);
            String messageType = "";
            PendingIntent contentIntent;
            try {
                messageType = remoteMessage.getData().get("order_status_type");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (messageType != null && messageType.equalsIgnoreCase("new_message")) {
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra(Arg.ORDER_ID, Integer.parseInt(remoteMessage.getData().get("orders_outlets_id")));
                intent.putExtra(Arg.ARG_ORDER_STATUS, 7);
                contentIntent =
                        PendingIntent.getActivity(this, 0, intent, 0);
            } else {
                contentIntent =
                        PendingIntent.getActivity(this, 0, new Intent(this, SelectCategoryActivity.class), 0);
            }

            String content = remoteMessage.getData().get("message");
*/

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, STATUS_CHANNEL_ID);
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setContentTitle(title).setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setDefaults(DEFAULT_SOUND | DEFAULT_LIGHTS | DEFAULT_VIBRATE)
                    .setAutoCancel(true);

            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(STATUS_CHANNEL_ID, "BzHub", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
            manager.notify(STATUS_CHANEL_NOTIFICATION_ID, builder.build());

    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("FIREBASETOEKN", "NEW TOKEN STRING !!!!!====== " + s);
        MyPreferenceManager.getInstance(getApplicationContext()).put(ARG_FIREBASE_TOKEN,s);
    }
}

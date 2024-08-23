package smartdev.bzzhub;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;

import smartdev.bzzhub.repository.MyPreferenceManager;
import smartdev.bzzhub.repository.network.BzHubRepository;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

public class BzzApp extends Application {
    private static Context context;
    private static AppCompatActivity currentActivity;
    private static MyPreferenceManager preferenceManager;
    private static BzHubRepository bzHubRepository;
    private static FirebaseFirestore firestore;

    @Override
    public void onCreate() {
        super.onCreate();
        // the following line is important
        Fresco.initialize(getApplicationContext());
        context = getApplicationContext();
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        preferenceManager = MyPreferenceManager.getInstance(context);
        bzHubRepository = new BzHubRepository(preferenceManager);
        initFirebase();

    }

    public static AppCompatActivity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(AppCompatActivity currentActivity) {
        BzzApp.currentActivity = currentActivity;
    }

    public static MyPreferenceManager getPreferenceManager(){
        return preferenceManager;
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            try {
                netInfo = cm.getActiveNetworkInfo();
            }catch (Exception e){

            }
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static BzHubRepository getBzHubRepository() {
        return bzHubRepository;
    }

    private void initFirebase() {
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
        System.out.println("Token: " + instanceId.getToken());

        firestore = FirebaseFirestore.getInstance();

    }

    public static FirebaseFirestore getFirestore() {
        return firestore;
    }

    public static void setAppContext(Context mContext){
        context =mContext;
    }

    public static Context getAppContext() {
        return context;
    }

}

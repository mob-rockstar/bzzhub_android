package smartdev.bzzhub.ui.activity;

import android.net.LinkAddress;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import smartdev.bzzhub.R;
import smartdev.bzzhub.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import smartdev.bzzhub.util.Validator;

import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_VIDEO;

public class VideoActivity extends BaseActivity {
    String URL = "";

    @BindView(R.id.layout_parent)
    LinearLayout layout;
    YouTubePlayerView  youtube_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);
     //   youtube_view = findViewById(R.id.youtube_view);
        youtube_view = new YouTubePlayerView(this);

        layout.addView(youtube_view);
        initCurrentActivity();
        extractArguments();

        loadVideo();
    }


    private void initCurrentActivity(){
        currentActivity = VideoActivity.this;
        setCurrentActivity();
    }

    private void extractArguments(){
        Bundle bundle = getIntent().getExtras().getBundle(this.getClass().getName());
        if (bundle != null)
            URL = bundle .getString(ARG_URL_VIDEO);
        Log.d("mediaPath",URL);
    }

    private void loadVideo(){
    //     webView.getSettings().setJavaScriptEnabled(true);
   //     webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        getLifecycle().addObserver(youtube_view);
  //      youtube_view.setId(Integer.parseInt( Validator.getActualURL(URL)));
        youtube_view.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
         //       String videoId = "S0Q4gqBUs7c";

                youTubePlayer.loadVideo( ( Validator.getActualURL(URL) ) , 0);
                youTubePlayer.play();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youtube_view.release();
    }

    @OnClick(R.id.iv_close)
    void onCloseClicked(){
        finish();
    }
}

package smartdev.bzzhub.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import smartdev.bzzhub.R;
import smartdev.bzzhub.ui.base.BaseActivity;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_IMAGE;

public class ImageActivity extends BaseActivity {
    String URL = "";
    @BindView(R.id.imageView)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        initCurrentActivity();
        extractArguments();
        loadImage();
    }

    private void initCurrentActivity(){
        currentActivity = ImageActivity.this;
        setCurrentActivity();
    }

    private void extractArguments(){
        Bundle bundle = getIntent().getExtras().getBundle(this.getClass().getName());
        if (bundle != null)
            URL = bundle .getString(ARG_URL_IMAGE);
        Log.d("mediaPath",URL);
    }

    private void loadImage(){
        Glide.with(currentActivity).load(URL).centerCrop().into(imageView);
    }

    @OnClick(R.id.iv_close)
    void onCloseClicked(){
        finish();
    }
}

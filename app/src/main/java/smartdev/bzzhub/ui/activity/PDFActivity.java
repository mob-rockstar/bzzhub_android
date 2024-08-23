package smartdev.bzzhub.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.OnClick;
import smartdev.bzzhub.R;
import smartdev.bzzhub.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;

import static smartdev.bzzhub.util.navigation.Arg.ARG_URL_PDF;

public class PDFActivity extends BaseActivity implements DownloadFile.Listener{

    private String url = "";
    @BindView(R.id.pdfView)
    PDFViewPager pdfView;

    @BindView(R.id.root)
    LinearLayout root;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    ProgressDialog progressDialog;
    LinearLayout.LayoutParams lp;
    RemotePDFViewPager remotePDFViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f);
        ButterKnife.bind(this);
        initLayoutParams();
        progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Loading...");
        extractArguments();
        loadURL();
    }

    private void initLayoutParams(){
         lp = new LinearLayout.LayoutParams(84, 84);
         lp.setMargins(48, 48, 48, 48);
    }

    private void extractArguments(){
        url =  getIntent().getExtras().getBundle(this.getClass().getName()).getString(ARG_URL_PDF);
    }

    private void loadURL(){
        progressDialog.show();
        remotePDFViewPager =  new RemotePDFViewPager(this, url, this);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {

        PDFPagerAdapter  adapter = new PDFPagerAdapter(this, destinationPath);
        remotePDFViewPager.setAdapter(adapter);
        root.removeAllViews();

        ivBack.setLayoutParams(lp);
        root.addView(ivBack,lp);
        root.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        progressDialog.hide();
    }

    @Override
    public void onFailure(Exception e) {
        progressDialog.hide();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }

    @OnClick(R.id.iv_back) void onBackClicked(){
        finish();
    }
}

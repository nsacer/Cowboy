package cn.live9666.cowboy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_web_view)
public class WebViewActivity extends BaseActivity {

    public static final String WEB_URL = "web_url";
    public static final String SHARE_ABLE = "share";

    @ViewInject(R.id.toolbar_web_view)
    private Toolbar toolbar;

    @ViewInject(R.id.clpb)
    private ProgressBar progressBar;

    @ViewInject(R.id.web_view)
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        String url = getIntent().getStringExtra(WEB_URL);
//        webView.loadUrl(getIntent().getStringExtra(WEB_URL));
        webView.loadUrl(url);
    }

    private void initView() {

        initToolbar();

        initWebView();
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {

        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        webView.addJavascriptInterface(this, "web_view");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (view.getContentHeight() != 0)
                    progressBar.setVisibility(View.GONE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                progressBar.setProgress(newProgress);
                progressBar.postInvalidate();
            }
        });


    }

    private void initToolbar() {

        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}

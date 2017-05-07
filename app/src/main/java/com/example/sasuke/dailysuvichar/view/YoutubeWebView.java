package com.example.sasuke.dailysuvichar.view;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.sasuke.dailysuvichar.DailySuvicharApp;

/**
 * Created by Sasuke on 5/7/2017.
 */

public class YoutubeWebView extends FrameLayout implements View.OnLongClickListener {

    private WebView webView;
    private ProgressBar progressBar;

    public YoutubeWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        webView = new WebView(context, attributeSet);
        progressBar = new ProgressBar(context);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setOnLongClickListener(this);
        addView(webView);
        addView(progressBar, new LayoutParams(65, 65, Gravity.CENTER));

    }

    public void loadYoutubeVideo(Activity activity, String videoId) {
        if (activity == null)
            return;
        webView.setWebChromeClient(new MyWebChromeClient(activity));
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        String layoutwidth = String.valueOf(size.x / getResources().getDisplayMetrics().density);
        String layoutheight = String.valueOf(getLayoutParams().height / getResources().getDisplayMetrics().density);

        String frameVideo = "<html><body style='margin:0px;padding:0px;'><script " +
                "type='text/javascript' src='http://www.youtube.com/iframe_api'>" +
                "</script><script type='text/javascript'>function onYouTubeIframeAPIReady()" +
                "{ytplayer=new YT.Player('playerId',{events:{onReady:onPlayerReady}})}function onPlayerReady(a){}" +
                "</script><iframe id='playerId' " +
                "type='text/html' width=" + layoutwidth + " height=" + layoutheight +
                " src='http://www.youtube.com/embed/" + videoId + "?enablejsapi=1&rel=0&playsinline=1&showinfo=0'" +
                " frameborder='0'></body></html>";

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!DailySuvicharApp.hasNetwork()) {
                    view.loadData("<html><body style='background: black;'>" +
                            "<p style='color: white;'>Unable To Load Video." +
                            " Please check if your network connection is working properly " +
                            "or try again later.</p></body></html>", "text/html", null);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(GONE);
            }
        });

        webView.loadData(frameVideo, "text/html", "utf-8");
    }

    public void onPause() {
        if (webView != null)
            webView.onPause();
    }

    public void onResume() {
        if (webView != null)
            webView.onResume();
    }

    public void onHiddenStateChanged(boolean hidden) {
        if (webView != null)
            if (hidden)
                webView.onPause();
            else
                webView.onResume();
    }

    @Override
    public boolean onLongClick(View view) {
        return true;
    }

    public class MyWebChromeClient extends WebChromeClient {

        private View customView;
        private WebChromeClient.CustomViewCallback customViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int originalOrientation;
        private int originalSystemUiVisibility;
        private Activity activity;

        public MyWebChromeClient(Activity activity) {
            this.activity = activity;
        }

        public Bitmap getDefaultVideoPoster() {
            if (activity == null) {
                return null;
            }
            return BitmapFactory.decodeResource(activity.getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            if (activity != null) {
                ((FrameLayout) activity.getWindow().getDecorView()).removeView(this.customView);
                this.customView = null;
                activity.getWindow().getDecorView().setSystemUiVisibility(this.originalSystemUiVisibility);
                activity.setRequestedOrientation(this.originalOrientation);
                this.customViewCallback.onCustomViewHidden();
                this.customViewCallback = null;
            }
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paracustomViewCallback) {
            if (this.customView != null) {
                onHideCustomView();
                return;
            }
            this.customView = paramView;
            this.originalSystemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
            this.originalOrientation = activity.getRequestedOrientation();
            if (activity != null) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
            }
            this.customViewCallback = paracustomViewCallback;
            ((FrameLayout) activity.getWindow().getDecorView()).addView(this.customView, new FrameLayout.LayoutParams(-1, -1));
            activity.getWindow().getDecorView().setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
        }

    }
}

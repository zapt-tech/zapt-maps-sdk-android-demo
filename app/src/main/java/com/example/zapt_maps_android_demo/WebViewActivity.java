package com.example.zapt_maps_android_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        /*webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl("https://www.google.com");
        //webView.loadUrl("https://app.zapt.tech/#/map?placeId=-ldbpd2phzmsfhb9-9on");
        //setContentView(webView);*/


        this.webView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        WebViewClientImpl webViewClient = new WebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);

        webView.loadUrl("https://app.zapt.tech/#/map?placeId=-ldbpd2phzmsfhb9-9on");
        //webView.loadUrl("https://www.google.com");
        //webView.loadUrl("https://www.journaldev.com");



        // Set back button to mainActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

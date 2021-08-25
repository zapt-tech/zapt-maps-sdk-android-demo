package com.example.android;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends AppCompatActivity {

	private static final int REQUEST_FINE_LOCATION = 0;

    public WebView webView;

	private String mGeolocationOrigin;
	private GeolocationPermissions.Callback mGeolocationCallback;

	public class GeoWebChromeClient extends WebChromeClient {

		@Override
		public void onGeolocationPermissionsShowPrompt(String origin,
													   GeolocationPermissions.Callback callback) {
			// Geolocation permissions coming from this app's Manifest will only be valid for devices with
			// API_VERSION < 23. On API 23 and above, we must check for permissions, and possibly
			// ask for them.
			String perm = Manifest.permission.ACCESS_FINE_LOCATION;
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
					ContextCompat.checkSelfPermission(WebViewActivity.this, perm) == PackageManager.PERMISSION_GRANTED) {
				// we're on SDK < 23 OR user has already granted permission
				callback.invoke(origin, true, false);
			} else {
				if (!ActivityCompat.shouldShowRequestPermissionRationale(WebViewActivity.this, perm)) {
					// ask the user for permission
					ActivityCompat.requestPermissions(WebViewActivity.this, new String[]{perm}, REQUEST_FINE_LOCATION);

					// we will use these when user responds
					mGeolocationOrigin = origin;
					mGeolocationCallback = callback;
				}
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case REQUEST_FINE_LOCATION:
				boolean allow = false;
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// user has allowed this permission
					allow = true;
				}
				if (mGeolocationCallback != null) {
					// call back to web chrome client
					mGeolocationCallback.invoke(mGeolocationOrigin, allow, false);
				}
				break;
		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webView);

        String url = "https://app.zapt.tech/#/map?placeId=-mmh1wypssqmqdgtkhj0-floor0&bottomNavigation=false&splash=false";
        startWebView(url);

        // Set back button to mainActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    // Set settings for webView
    private void startWebView(String url) {
		webView.setWebChromeClient(new GeoWebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setGeolocationEnabled(true);

        webView.loadUrl(url);
    }

}

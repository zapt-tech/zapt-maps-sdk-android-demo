package com.example.android;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

	private static final int REQUEST_FINE_LOCATION = 0;
	private static final int REQUEST_AUDIO_CAPTURE = 1;

    public WebView webView;

	private String mGeolocationOrigin;
	private GeolocationPermissions.Callback mGeolocationCallback;
	private PermissionRequest audioPermissionRequest;

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

		@Override
		public void onPermissionRequest(PermissionRequest request) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				final String[] resources = request.getResources();
				for(String resource : resources) {
					if(resource.equals(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
						boolean audioPermissionGranted = checkAudioPermissions();
						if(audioPermissionGranted) {
							request.grant(new String[]{resource});
						} else {
							audioPermissionRequest = request;
							requestAudioPermissions();
						}
						break;
					}
				}
			} else {
				super.onPermissionRequest(request);
			}
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		boolean permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
		switch (requestCode) {
			case REQUEST_FINE_LOCATION:
				boolean allow = false;
				if (permissionGranted) {
					// user has allowed this permission
					allow = true;
				}
				if (mGeolocationCallback != null) {
					// call back to web chrome client
					mGeolocationCallback.invoke(mGeolocationOrigin, allow, false);
				}
				break;
			case REQUEST_AUDIO_CAPTURE:
				if(
					audioPermissionRequest != null &&
					permissionGranted &&
					Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
				) {
					audioPermissionRequest.grant(new String[]{PermissionRequest.RESOURCE_AUDIO_CAPTURE});
				}
				break;
		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webView);

        String url = "https://app.zapt.tech/#/map?placeId=-mmh1wypssqmqdgtkhj0-floor0&bottomNavigation=false&splash=false&enableMicSearch=true";
        startWebView(url);

        // Set back button to mainActivity
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    // Set settings for webView
	@SuppressLint("SetJavaScriptEnabled")
    private void startWebView(String url) {
		webView.setWebChromeClient(new GeoWebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
		webView.getSettings().setAppCacheEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.getSettings().setGeolocationEnabled(true);

        webView.loadUrl(url);
    }

	private boolean checkAudioPermissions() {
		boolean permissionGranted = false;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			permissionGranted =
				checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
		} else {
			// we're on SDK < 23 OR user has already granted permission
			permissionGranted = true;
		}
		return permissionGranted;
	}

	private void requestAudioPermissions() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			requestPermissions(
				new String[]{Manifest.permission.RECORD_AUDIO},
				REQUEST_AUDIO_CAPTURE
			);
		}
	}

}

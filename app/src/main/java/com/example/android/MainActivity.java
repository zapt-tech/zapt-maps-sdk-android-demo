package com.example.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public boolean clicked = false;
    public Button openMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openMap = (Button) findViewById(R.id.button);

        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!clicked) { // prevent multi click in MapButton
                    clicked = true;

                   // Open new activity
                   Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                   startActivity(intent);
               }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        clicked = false;
    }

}

package com.example.zapt_maps_android_demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openMap = (Button) findViewById(R.id.button);

        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (!clicked) { // prevent multi click in button
                    clicked = true;

                   // Open activity
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

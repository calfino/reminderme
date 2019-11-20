package com.example.reminderme;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

private Handler handler1;
private Runnable runnable1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runnable1 = new Runnable() {
            @Override
            public void run() {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        };

        handler1 = new Handler();

        handler1.postDelayed(runnable1, 2000);
    }



//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if(handler1 != null && runnable1 !=null)
//        handler1.removeCallbacks(runnable1);
//    }
}

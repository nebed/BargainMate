package com.example.android.bargainmate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashhActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent =new Intent(SplashhActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}

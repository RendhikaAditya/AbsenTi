package com.example.absenti.actifity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absenti.R;

public class QuisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quis);
        getSupportActionBar().setTitle("Questioner");
    }
}
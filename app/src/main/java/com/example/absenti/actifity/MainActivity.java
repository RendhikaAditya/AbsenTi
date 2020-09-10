package com.example.absenti.actifity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.absenti.R;
import com.example.absenti.util.SharedPrefManager;

public class MainActivity extends AppCompatActivity {
    CardView cardLecture, cardStudent;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardLecture = findViewById(R.id.cardLecture);
        cardLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lecIntent = new Intent(MainActivity.this, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(lecIntent);
                finish();
            }
        });

        cardStudent = findViewById(R.id.cardStudent);
        cardStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent studIntent = new Intent(MainActivity.this, LoginMhsActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(studIntent);
                finish();
            }
        });

        sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.getMhsLoginStatus()) {
            startActivity(new Intent(MainActivity.this, MhsActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }else if (sharedPrefManager.getDsnLoginStatus()){
            startActivity(new Intent(MainActivity.this, DosenActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }

}
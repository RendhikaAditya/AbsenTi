package com.example.absenti.actifity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.absenti.R;
import com.example.absenti.server.UtilsApi;
import com.example.absenti.util.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class DosenActivity extends AppCompatActivity {
    CardView cardGenerate, cardAdd, cardReport, cardQuis;
    TextView nama, nip;
    SharedPrefManager sharedPrefManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Intent setIntent = new Intent(DosenActivity.this, AboutActivity.class);
            startActivity(setIntent);
        }else if (item.getItemId() == R.id.action_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Perhatian")
                    .setMessage("Apakah anda yakin akan Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sharedPrefManager.setDsnLoginStatus(false);
                            sharedPrefManager.setMhsLoginStatus(false);
                            sharedPrefManager.setIdUser("");

                            Intent setIntent = new Intent(DosenActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(setIntent);
                            finish();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosen);
        getSupportActionBar().setTitle("Dosen");
        AndroidNetworking.initialize(this);

        nama = findViewById(R.id.name);
        nip = findViewById(R.id.nip);


//        nip.setText("1");
        sharedPrefManager = new SharedPrefManager(this);
        getDosen();
        cardGenerate = findViewById(R.id.cardGenerate);
        cardGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent g = new Intent(DosenActivity.this, GenerateActivity.class);
                startActivity(g);
            }
        });

        cardAdd = findViewById(R.id.cardAdd);
        cardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(DosenActivity.this, AddActivity.class);
                startActivity(a);
            }
        });

        cardReport = findViewById(R.id.cardReport);
        cardReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent r = new Intent(DosenActivity.this, ReportActivity.class);
                startActivity(r);
            }
        });

        cardQuis = findViewById(R.id.cardQuis);
        cardQuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent q = new Intent(DosenActivity.this, QuisActivity.class);
                startActivity(q);
            }
        });

    }

    private void getDosen() {
        AndroidNetworking.post(UtilsApi.BASE_URL+"get_dsn.php")
                .addBodyParameter("id", sharedPrefManager.getIdUser())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            nama.setText(response.getString("nama"));
                            nip.setText(response.getString("nim"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
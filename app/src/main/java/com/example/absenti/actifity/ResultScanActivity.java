package com.example.absenti.actifity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.absenti.R;
import com.example.absenti.server.UtilsApi;
import com.example.absenti.util.LoadingDialog;
import com.example.absenti.util.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class ResultScanActivity extends AppCompatActivity {
    TextView txtMatkul, txtTgl, txtJam0, txtJam1, txtDosen, txtRuang;

    Context context;
    LoadingDialog loadingDialog;
    SharedPrefManager sharedPrefManager;
    LinearLayout layoutGagal;
    ConstraintLayout layoutSukses;
    Button btnUlang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_scan);
        getSupportActionBar().hide();
        AndroidNetworking.initialize(this);
        txtMatkul = findViewById(R.id.matkul);
        txtTgl = findViewById(R.id.time);
        txtJam0 = findViewById(R.id.time0);
        txtJam1 = findViewById(R.id.time1);
        txtDosen = findViewById(R.id.dosen);
        txtRuang = findViewById(R.id.ruang);
        layoutGagal = findViewById(R.id.scanGagal);
        layoutSukses = findViewById(R.id.scanSukses);
        btnUlang = findViewById(R.id.ulang);

        btnUlang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultScanActivity.this, MhsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        loadingDialog = new LoadingDialog(context);

        Intent intent = new Intent(getIntent());
        String idsc = intent.getStringExtra("IDSCN");
//        sharedPrefManager = new SharedPrefManager(this);
//        txtJam0.setText(sharedPrefManager.getIdUser());
        fetchData(idsc);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ResultScanActivity.this, MhsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void fetchData(final String idscn) {
//        loadingDialog.startLoading();
        sharedPrefManager = new SharedPrefManager(this);

        AndroidNetworking.post(UtilsApi.BASE_URL+"get_data.php")
                .addBodyParameter("id", idscn)
                .addBodyParameter("idMhs", sharedPrefManager.getIdUser())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            loadingDialog.dismissLoading();
                            if (response.getString("code").equalsIgnoreCase("1")){
                                layoutSukses.setVisibility(View.VISIBLE);
                                layoutGagal.setVisibility(View.GONE);
                                Log.d("sikses", "suksen id : "+ response);
                                txtMatkul.setText(response.getString("matkul"));
                                txtTgl.setText(response.getString("tgl"));
                                txtJam0.setText(response.getString("jamM"));
                                txtJam1.setText(response.getString("jamS"));
                                txtDosen.setText(response.getString("dosen"));
                                txtRuang.setText(response.getString("ruangan"));
                            }else {
                                layoutSukses.setVisibility(View.GONE);
                                layoutGagal.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            loadingDialog.dismissLoading();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("eror", "eror:"+anError);
//                        loadingDialog.dismissLoading();
                    }
                });




//        baseApiService.get_data(idscn).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()){
//                    try {
//                        loadingDialog.dismissLoading();
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        txtMatkul.setText(jsonObject.getString("jsMatkul"));
//                        txtDosen.setText(jsonObject.getString("jsDosen"));
//                        txtRuang.setText(jsonObject.getString("jsRuang"));
//                        txtTgl.setText(jsonObject.getString("jsTgl"));
//                        String jamAwal = jsonObject.getString("jsJamAwal").substring(0, 5);
//                        String jamAkhir = jsonObject.getString("jsJamSelesai").substring(0, 5);
//                        txtJam0.setText(jamAwal);
//                        txtJam1.setText(jamAkhir);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(context, "No Internet Access", Toast.LENGTH_SHORT).show();
//                loadingDialog.dismissLoading();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Intent setIntent = new Intent(ResultScanActivity.this, AboutActivity.class);
            startActivity(setIntent);
        }else if (item.getItemId() == R.id.action_logout) {
            startActivity(new Intent(ResultScanActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
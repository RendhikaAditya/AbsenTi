package com.example.absenti.actifity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.absenti.R;
import com.example.absenti.server.UtilsApi;
import com.example.absenti.util.LoadingDialog;
import com.example.absenti.util.SharedPrefManager;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MhsActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    Button btnScan;
    TextView nama, nim;

    SharedPrefManager sharedPrefManager;
    LoadingDialog loading;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhs);
        getSupportActionBar().setTitle("Student");
        AndroidNetworking.initialize(this);

        scannerView = new ZXingScannerView(getApplicationContext());

        btnScan = findViewById(R.id.btnScan);
        nama = findViewById(R.id.nama);
        nim = findViewById(R.id.nim);

        sharedPrefManager = new SharedPrefManager(this);
        loading = new LoadingDialog(this);

        SetUser();

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(scannerView);
                scannerView.setResultHandler(MhsActivity.this);
                scannerView.startCamera();
            }
        });
    }

    private void SetUser() {
        AndroidNetworking.post(UtilsApi.BASE_URL+"get_mhs.php")
                .addBodyParameter("id", sharedPrefManager.getIdUser())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            nama.setText(response.getString("nama"));
                            nim.setText(response.getString("nim"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public void handleResult(Result rawResult) {
        Intent intent = new Intent(MhsActivity.this, ResultScanActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("IDSCN", rawResult.getText());
        finish();
        startActivity(intent);
        scannerView.resumeCameraPreview(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Intent setIntent = new Intent(MhsActivity.this, AboutActivity.class);
            startActivity(setIntent);
        }else if (item.getItemId() == R.id.action_logout) {
//            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
            new AlertDialog.Builder(this)
                    .setTitle("Perhatian")
                    .setMessage("Apakah anda yakin akan Logout?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            sharedPrefManager.setMhsLoginStatus(false);
                            sharedPrefManager.setDsnLoginStatus(false);
                            sharedPrefManager.setIdUser("");
                            startActivity(new Intent(MhsActivity.this, MainActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();

        }
        return super.onOptionsItemSelected(item);
    }

}
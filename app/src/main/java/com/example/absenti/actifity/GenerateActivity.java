package com.example.absenti.actifity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

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

public class GenerateActivity extends AppCompatActivity {

    EditText edtMatkul;

    EditText edtDosen;
    EditText edtKelas;
    EditText edtRuang;
    EditText edtJamAwal;
    EditText edtJamSelesai;
    Button btnGenerate;

    LoadingDialog loading;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        getSupportActionBar().setTitle("Generate QR Code");

        btnGenerate = findViewById(R.id.btnGenerate);
        edtMatkul = findViewById(R.id.editMk);
        edtDosen = findViewById(R.id.editDosen);
        edtKelas = findViewById(R.id.editKelas);
        edtRuang = findViewById(R.id.editRuang);
        edtJamAwal = findViewById(R.id.editJam0);
        edtJamSelesai = findViewById(R.id.editJam1);
        AndroidNetworking.initialize(this);

        loading = new LoadingDialog(this);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtMatkul.getText().toString())){
                    edtMatkul.setError("Subject is still empty!");
                }else if (TextUtils.isEmpty(edtDosen.getText().toString())){
                    edtDosen.setError("Lecture's Name is still empty!");
                }else if (TextUtils.isEmpty(edtKelas.getText().toString())){
                    edtKelas.setError("Class is still empty!");
                }else if (TextUtils.isEmpty(edtRuang.getText().toString())){
                    edtRuang.setError("Room is still empty!");
                }else if (TextUtils.isEmpty(edtJamAwal.getText().toString())){
                    edtJamAwal.setError("Start time is still empty!");
                }else if (TextUtils.isEmpty(edtJamSelesai.getText().toString())){
                    edtJamSelesai.setError("Time's up is still empty!!");
                }else {
                    generate();
                }
            }
        });
    }

    private void generate() {
        loading.startLoading();
        sharedPrefManager = new SharedPrefManager(this);
        AndroidNetworking.post(UtilsApi.BASE_URL+"generate.php")
                .addBodyParameter("id", sharedPrefManager.getIdUser())
                .addBodyParameter("matkul", edtMatkul.getText().toString())
                .addBodyParameter("dosen", edtDosen.getText().toString())
                .addBodyParameter("kelas", edtKelas.getText().toString())
                .addBodyParameter("ruang", edtRuang.getText().toString())
                .addBodyParameter("jamAwal", edtJamAwal.getText().toString())
                .addBodyParameter("jamSelesai", edtJamSelesai.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            loading.dismissLoading();
                            String id = response.getString("gen");
                            Intent intent = new Intent(GenerateActivity.this, QrActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("IDQR", id);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
//        mApiService.generateRequest(edtMatkul.getText().toString(),
//                edtDosen.getText().toString(),
//                edtKelas.getText().toString(),
//                edtRuang.getText().toString(),
//                edtJamAwal.getText().toString(),
//                edtJamSelesai.getText().toString())
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()){
//                            try {
//                                loading.dismissLoading();
//                                JSONObject jsonObject = new JSONObject(response.body().string());
//                                String txtQr = jsonObject.getString("gen");
//                                Log.i("debug", "onResponse : Success");
//                                Toast.makeText(mContext, "SUCCESSFUL", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(GenerateActivity.this, QrActivity.class);
//                                intent.putExtra("IDQR", txtQr);
//                                startActivity(intent);
//                                finish();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
//                        Toast.makeText(mContext, "No Internet Access", Toast.LENGTH_SHORT).show();
//
//                    }
//                });
    }
}
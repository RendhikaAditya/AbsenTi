package com.example.absenti.actifity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.absenti.R;
import com.example.absenti.adapter.AdapterKelas;
import com.example.absenti.model.ModelKelas;
import com.example.absenti.server.UtilsApi;
import com.example.absenti.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    RecyclerView rvKelas;
    private List<ModelKelas> kelas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setTitle("Attendance Report");
        AndroidNetworking.initialize(this);
        rvKelas = findViewById(R.id.rv_reportkls);
        rvKelas.setHasFixedSize(true);
        rvKelas.setLayoutManager(new LinearLayoutManager(this));
        kelas = new ArrayList<>();
        getKelas();
    }

    private void getKelas() {

        sharedPrefManager = new SharedPrefManager(this);
        AndroidNetworking.post(UtilsApi.BASE_URL+"get_kelas.php")
                .addBodyParameter("id", sharedPrefManager.getIdUser())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length()>0){
                            for (int i = 0; i< response.length(); i++) {
                                try {
                                    Log.d("syakses", "sukses"+response);
                                    JSONObject data = response.getJSONObject(i);
                                    kelas.add(new ModelKelas(
                                            data.getString("id"),
                                            data.getString("dosen"),
                                            data.getString("kelas"),
                                            data.getString("tgl"),
                                            data.getString("matkul"),
                                            data.getString("code")
                                    ));
                                    AdapterKelas adapterKelas = new AdapterKelas(kelas);
                                    rvKelas.setAdapter(adapterKelas);
                                    adapterKelas.notifyDataSetChanged();
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", "error : "+anError);
                    }
                });
    }
}
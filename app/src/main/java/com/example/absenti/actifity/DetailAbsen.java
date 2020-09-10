package com.example.absenti.actifity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.absenti.R;
import com.example.absenti.adapter.AdapterAbsen;
import com.example.absenti.adapter.AdapterKelas;
import com.example.absenti.model.ModelKelas;
import com.example.absenti.model.ModelMhs;
import com.example.absenti.server.UtilsApi;
import com.google.android.gms.common.api.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailAbsen extends AppCompatActivity {

    RecyclerView rv_absen;

    private List<ModelMhs> mhs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_absen);
        AndroidNetworking.initialize(this);
        rv_absen = findViewById(R.id.rv_mhs);
        rv_absen.setHasFixedSize(true);
        rv_absen.setLayoutManager(new LinearLayoutManager(this));
        mhs = new ArrayList<>();
        getStatus();
    }

    private void getStatus() {
        Intent intent = getIntent();
        final String id = intent.getStringExtra("IDK");
        AndroidNetworking.post(UtilsApi.BASE_URL+"absen_mhs.php")
                .addBodyParameter("id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mhs.clear();
                        if (response.length()>0){
                            Log.d("sukses", "sukses :"+response);
                            for (int i = 0; i< response.length(); i++) {
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    mhs.add(new ModelMhs(
                                            data.getString("id"),
                                            data.getString("nama"),
                                            data.getString("stat")
                                    ));
                                    AdapterAbsen adapterAbsen = new AdapterAbsen(mhs);
                                    rv_absen.setAdapter(adapterAbsen);
                                    adapterAbsen.notifyDataSetChanged();
                                }
                                catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}

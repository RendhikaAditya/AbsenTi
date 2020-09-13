package com.example.absenti.actifity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.absenti.R;
import com.example.absenti.adapter.AdapterStat;
import com.example.absenti.model.ModelStatus;
import com.example.absenti.server.UtilsApi;
import com.example.absenti.util.LoadingDialog;
import com.example.absenti.util.PreferencesKat;
import com.example.absenti.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class AddActivity extends AppCompatActivity {
    EditText nimMhs;
    Button btnAdd;

    LoadingDialog loading;
    Context mContext;

    SharedPrefManager sharedPrefManager;
    PreferencesKat preferencesKat;

    RecyclerView rv_stat;
    List<ModelStatus> status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("Add Student");

        nimMhs = findViewById(R.id.editNim);
        btnAdd = findViewById(R.id.btnAdd);

        mContext = this;
        loading = new LoadingDialog(this);
        AndroidNetworking.initialize(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nimMhs.getText().toString())) {
                    nimMhs.setError("NIM is still empty!");
                } else {
                    requestAdd();
                }
            }
        });

        rv_stat=findViewById(R.id.rvStat);
        rv_stat.setHasFixedSize(true);
        rv_stat.setLayoutManager(new GridLayoutManager(this,2 ));
        status = new ArrayList<>();
        getDataStat();

    }

    private void getDataStat() {
        AndroidNetworking.get(UtilsApi.BASE_URL+"get_status.php")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                        for (int i=0; i<response.length(); i++){

                                JSONObject data = response.getJSONObject(i);
                                status.add(new ModelStatus(
                                        data.getString("id"),
                                        data.getString("nama")
                                ));
                            AdapterStat adapterStat = new AdapterStat(status);
                            rv_stat.setAdapter(adapterStat);
                            adapterStat.notifyDataSetChanged();

                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void requestAdd() {
        sharedPrefManager = new SharedPrefManager(this);
        AndroidNetworking.post(UtilsApi.BASE_URL + "add_student.php")
                .addBodyParameter("iddsn", sharedPrefManager.getIdUser())
                .addBodyParameter("nim", nimMhs.getText().toString())
                .addBodyParameter("status", PreferencesKat.getId_kategori(getBaseContext()))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("debug", "onResponse : Success");
//                        loading.dismissLoading();
                        try {
                            Toast.makeText(mContext, "SUCCESSFUL", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(mContext, LoginActivity.class));
                            if (!response.getString("response").equalsIgnoreCase("Success!")) {
                                String error_msg = response.getString("response");
                                Toast.makeText(mContext, error_msg, Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(AddActivity.this, AddActivity.class);
                                startActivity(intent);
                            }else {
                                String error_msg = response.getString("response");
                                Toast.makeText(mContext, error_msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", "error :"+anError);
                    }
                });
//        mApiService.addRequest(nimMhs.getText().toString(),
//                namaMhs.getText().toString(),
//                statusMhs.getText().toString())
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()) {
//                            Log.i("debug", "onResponse : Success");
//                            loading.dismissLoading();
//                            try {
//                                JSONObject jsonRESULT = new JSONObject(response.body().string());
//                                Toast.makeText(mContext, "SUCCESSFUL", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(mContext, LoginActivity.class));
//                                if (!jsonRESULT.getString("error").equals("false")) {
//                                    String error_msg = jsonRESULT.getString("error_msg");
//                                    Toast.makeText(mContext, error_msg, Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            Log.i("debug", "onResponse: FAILED");
//                            loading.dismissLoading();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
//                        Toast.makeText(mContext, "No Internet Access", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

}
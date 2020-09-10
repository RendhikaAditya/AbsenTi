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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.absenti.R;
import com.example.absenti.server.UtilsApi;
import com.example.absenti.util.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class AddActivity extends AppCompatActivity {
    EditText nimMhs;
    EditText statusMhs;
    Button btnAdd;

    LoadingDialog loading;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().setTitle("Add Student");

        nimMhs = findViewById(R.id.editNim);
        statusMhs = findViewById(R.id.editStatus);
        btnAdd = findViewById(R.id.btnAdd);

        mContext = this;
        loading = new LoadingDialog(this);
        AndroidNetworking.initialize(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(nimMhs.getText().toString())) {
                    nimMhs.setError("NIM is still empty!");
                } else if (TextUtils.isEmpty(statusMhs.getText().toString())) {
                    statusMhs.setError("attendance status is still empty!");
                } else {
                    requestAdd();
                }
            }
        });
    }

    private void requestAdd() {
        AndroidNetworking.post(UtilsApi.BASE_URL + "add_student.php")
                .addBodyParameter("nim", nimMhs.getText().toString())
                .addBodyParameter("status", statusMhs.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("debug", "onResponse : Success");
                        loading.dismissLoading();
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
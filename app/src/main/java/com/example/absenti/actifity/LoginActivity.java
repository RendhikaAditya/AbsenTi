package com.example.absenti.actifity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity{

    EditText edtEmail;
    EditText edtPassword;
    Button btnLogin;
    ImageView show;

    LoadingDialog loading;
    Context mContext;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login Lecture");

        edtEmail = findViewById(R.id.editEmail);
        edtPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);

        loading = new LoadingDialog(this);
        AndroidNetworking.initialize(this);

        sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.getDsnLoginStatus()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogin();
            }
        });

    }

    private void requestLogin() {
        loading.startLoading();
        AndroidNetworking.post(UtilsApi.BASE_URL+"login_dosen.php")
                .addBodyParameter("email", edtEmail.getText().toString())
                .addBodyParameter("password", edtPassword.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code").equals("1")) {
                                Toast.makeText(getApplicationContext(), "" + response.getString("response"), Toast.LENGTH_SHORT).show();
//                                JSONObject jsonObject = response.getJSONObject("dosen");
//                                String nama = jsonObject.getString("dsnNama");
//                                sharedPrefManager.saveSPString(SharedPrefManager.SP_DOSEN_NAMA, nama);
//                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN_DOSEN, true);
                                sharedPrefManager.setDsnLoginStatus(true);
                                sharedPrefManager.setIdUser(response.getString("id"));
//                                sharedPrefManager.setIdUser(response.getString("id"));
                                startActivity(new Intent(LoginActivity.this, DosenActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "Cek Username Dan Password", Toast.LENGTH_SHORT).show();
                                loading.dismissLoading();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("debug", "onFailure: ERROR > " + anError);
                        loading.dismissLoading();
                    }
                });

//        mApiService.loginRequest(edtEmail.getText().toString(), edtPassword.getText().toString())
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        Log.d("Response", response.toString());
//                        if (response.isSuccessful()) {
//                            try {
//                                JSONObject jsonRESULT = new JSONObject(response.body().string());
//                                if (jsonRESULT.getString("code").equals("1")) {
//                                    Toast.makeText(mContext, "" + jsonRESULT.getString("response"), Toast.LENGTH_SHORT).show();
//                                    JSONObject jsonObject = jsonRESULT.getJSONObject("dosen");
//                                    String nama = jsonObject.getString("dsnNama");
//                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_DOSEN_NAMA, nama);
//                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN_DOSEN, true);
//                                    startActivity(new Intent(mContext, DosenActivity.class)
//                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
//                                    finish();
//                                }else{
//                                    Toast.makeText(mContext, "" + jsonRESULT.getString("response"), Toast.LENGTH_SHORT).show();
//                                    loading.dismissLoading();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            loading.dismissLoading();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e("debug", "onFailure: ERROR > " + t.toString());
//                        loading.dismissLoading();
//                    }
//                });
    }
}

package com.example.absenti.actifity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class LoginMhsActivity extends AppCompatActivity {


    EditText edtEmail;
    EditText edtPassword;
    TextView txtRegister;
    Button btnLogin;

    LoadingDialog loading;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mhs);
        getSupportActionBar().setTitle("Login Student");
        loading = new LoadingDialog(this);
        AndroidNetworking.initialize(this);

        edtEmail = findViewById(R.id.editEmail);
        edtPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegister = findViewById(R.id.register);

        sharedPrefManager = new SharedPrefManager(this);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLogin();
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMhsActivity.this, RegisterMhsActivity.class));
            }
        });
    }

    private void requestLogin() {
        loading.startLoading();

        AndroidNetworking.post(UtilsApi.BASE_URL+"login.php")
                .addBodyParameter("email",edtEmail.getText().toString())
                .addBodyParameter("password",edtPassword.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loading.dismissLoading();
                        try {
                            if (response.getString("code").equals("1")) {
                                String id =response.getString("id");
                                Log.e("sukses","sukses : "+id);
                                Toast.makeText(getApplicationContext(), id+" " + response.getString("response"), Toast.LENGTH_SHORT).show();
//                                String nama = response.getString("mhsNama");
//                                sharedPrefManager.saveSPString(SharedPrefManager.SP_MHS_NAMA, nama);
//                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN_MHS, true);

                                sharedPrefManager.setMhsLoginStatus(true);
                                sharedPrefManager.setIdUser(id);
                                Intent intent = new Intent(LoginMhsActivity.this, MhsActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "" + response.getString("response"), Toast.LENGTH_SHORT).show();
                                loading.dismissLoading();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("debug", "onFailure: ERROR > " +anError);
                        loading.dismissLoading();
                    }
                });
//        mApiService.loginMhsRequest(edtEmail.getText().toString(), edtPassword.getText().toString())
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        Log.d("Response", response.toString());
//                        if (response.isSuccessful()) {
//                            try {
//                                JSONObject jsonRESULT = new JSONObject(response.body().string());
//                                if (jsonRESULT.getString("code").equals("1")) {
//                                    Toast.makeText(mContext, "" + jsonRESULT.getString("response"), Toast.LENGTH_SHORT).show();
//                                    JSONObject jsonObject = jsonRESULT.getJSONObject("mahasiswa");
//                                    String nama = jsonObject.getString("mhsNama");
//                                    sharedPrefManager.saveSPString(SharedPrefManager.SP_MHS_NAMA, nama);
//                                    sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN_MHS, true);
//                                    startActivity(new Intent(mContext, MhsActivity.class)
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
package com.example.absenti.actifity;

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

public class RegisterMhsActivity extends AppCompatActivity {

    EditText edtNim;
    EditText edtNama;
    EditText edtEmail;
    EditText edtPassword;
    Button btnSignup;
    LoadingDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mhs);
        getSupportActionBar().setTitle("Registrasi Mahasiswa");
        AndroidNetworking.initialize(this);

        edtNim = findViewById(R.id.editNim);
        edtNama = findViewById(R.id.editNama);
        edtEmail = findViewById(R.id.editEmail);
        edtPassword = findViewById(R.id.editPassword);
        btnSignup = findViewById(R.id.btnSignUp);

        loading = new LoadingDialog(this);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtNim.getText().toString())) {
                    edtNim.setError("NIM is still empty!");
                } else if (TextUtils.isEmpty(edtNama.getText().toString())) {
                    edtNama.setError("Name is still empty!");
                } else if (TextUtils.isEmpty(edtPassword.getText().toString())) {
                    edtPassword.setError("Password is still empty!");
                } else {
                    requestRegister();
                }
            }
        });
    }

    private void requestRegister() {
        loading.startLoading();
        AndroidNetworking.post(UtilsApi.BASE_URL+"register.php")
                .addBodyParameter("nim", edtNim.getText().toString())
                .addBodyParameter("nama", edtNama.getText().toString())
                .addBodyParameter("password", edtPassword.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("debug", "onResponse : Success"+response.getString("response"));
                            loading.dismissLoading();
                            if (response.getString("code").equalsIgnoreCase("1")){
                                Toast.makeText(getApplicationContext(),"SUCCESSFUL", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterMhsActivity.this, LoginMhsActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(),"Gagal Register "+response.getString("code"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("debug","onFailure: ERROR > " +anError);
                        Toast.makeText(getApplicationContext(), "No Internet Access", Toast.LENGTH_SHORT).show();
                    }
                });
//        mApiService.registerRequest(edtNim.getText().toString(),
//                edtNama.getText().toString(),
//                edtEmail.getText().toString(),
//                edtPassword.getText().toString())
//                .enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful()){
//                            Log.i("debug", "onResponse : Success");
//                            loading.dismissLoading();
//                            try {
//                                JSONObject jsonRESULT = new JSONObject(response.body().string());
//                                Toast.makeText(mContext,"SUCCESSFUL", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(mContext, LoginActivity.class));
//                                if (!jsonRESULT.getString("error").equals("false")){
//                                    String error_msg = jsonRESULT.getString("error_msg");
//                                    Toast.makeText(mContext, error_msg, Toast.LENGTH_SHORT).show();
//                                }
//
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }catch (IOException e){
//                                e.printStackTrace();
//                            }
//                        }else {
//                            Log.i("debug", "onResponse: FAILED");
//                            loading.dismissLoading();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.e("debug","onFailure: ERROR > " + t.getMessage());
//                        Toast.makeText(mContext, "No Internet Access", Toast.LENGTH_SHORT).show();
//                    }
//                });
    }
}
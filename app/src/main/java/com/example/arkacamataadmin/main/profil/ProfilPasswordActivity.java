package com.example.arkacamataadmin.main.profil;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.arkacamataadmin.config.Config;
import com.example.arkacamataadmin.config.SharePreference;
import com.example.arkacamataadmin.config.UserAPIServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.arkacamataadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    SharePreference sharePreference;
    EditText et_password_lama, et_password_baru, et_password_ulangi;
    Button btn_simpan;
    String id_tb_admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharePreference = new SharePreference(this);
        HashMap<String, String> user = sharePreference.getUserDetails();
        id_tb_admin = user.get(sharePreference.KEY_ID_ADMIN);

        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        et_password_lama = findViewById(R.id.et_password_lama);
        et_password_baru = findViewById(R.id.et_password_baru);
        et_password_ulangi = findViewById(R.id.et_password_ulangi);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_simpan){
            simpan();
        }
    }

    private void simpan() {
        et_password_lama.setError(null);
        et_password_baru.setError(null);
        et_password_ulangi.setError(null);
        String password_lama = et_password_lama.getText().toString();
        String password_baru = et_password_baru.getText().toString();
        String password_ulangi = et_password_ulangi.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password_lama)){
            et_password_lama.setError("Silahkann diisi..");
            focusView = et_password_lama;
            cancel = true;
        } if (TextUtils.isEmpty(password_baru)){
            et_password_baru.setError("Silahkann diisi..");
            focusView = et_password_baru;
            cancel = true;
        }  if (TextUtils.isEmpty(password_ulangi)){
            et_password_ulangi.setError("Silahkann diisi..");
            focusView = et_password_ulangi;
            cancel = true;
        } if (!password_ulangi.equals(password_baru)){
            et_password_ulangi.setError("Password tidak sama..");
            focusView = et_password_ulangi;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            final ProgressDialog pDialog = new ProgressDialog(this);
            pDialog.setMessage("Tunggu sebentar...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("id_tb_admin",id_tb_admin);
            builder.addFormDataPart("password_lama",password_lama);
            builder.addFormDataPart("password_baru",password_baru);
            MultipartBody requestBody = builder.build();

            UserAPIServices api = Config.getRetrofit(Config.URL).create(UserAPIServices.class);
            Call<ResponseBody> post = api.profil_password(requestBody);
            post.enqueue(new Callback<ResponseBody>(){
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    pDialog.dismiss();
                    try {
                        String json = response.body().string();
                        JSONObject jsonObj = new JSONObject(json);
                        Config.jsonArray = jsonObj.getJSONArray("result");
                        for(int i=0;i<Config.jsonArray.length();i++) {
                            JSONObject c = Config.jsonArray.getJSONObject(i);
                            String status = c.getString("status");

                            if (status.equals("1")) {
                                Toast.makeText(getApplicationContext(), "Berhasil Mengubah Password.", Toast.LENGTH_LONG).show();
                                onBackPressed();
                            } else if (status.equals("2")) {
                                Toast.makeText(getApplicationContext(), "Password Lama Salah.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal Mengubah Password.", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Tidak bisa mengirim data!", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Tidak bisa mengirim data!!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Tidak bisa mengirim data!!!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
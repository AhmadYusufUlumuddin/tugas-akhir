package com.example.arkacamataadmin.main.kategori;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.arkacamataadmin.config.Config;
import com.example.arkacamataadmin.config.UserAPIServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.arkacamataadmin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KategoriTambahActivity extends AppCompatActivity implements View.OnClickListener {
    String id_tb_kategori, nama_kategori, foto_kategori, urlPath_foto;
    Button btn_simpan, btn_batal;
    FloatingActionButton fab;
    EditText et_nama_kategori;
    ImageView imv;
    static final int REQUEST_CODE_GALLERY = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_tambah);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Kategori");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        et_nama_kategori = findViewById(R.id.et_nama_kategori);
        imv = findViewById(R.id.imv);
        fab = findViewById(R.id.fab);
        btn_batal = findViewById(R.id.btn_batal);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_batal.setOnClickListener(this);
        btn_simpan.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_batal){
            onBackPressed();
        } else if (v == btn_simpan){
            simpan();
        } else if (v == fab){
            EasyImage.openGallery(this,REQUEST_CODE_GALLERY);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> list, EasyImage.ImageSource imageSource, int type) {
                switch (type){
                    case REQUEST_CODE_GALLERY:
                        Glide.with(KategoriTambahActivity.this)
                                .load(list.get(0).getAbsolutePath())
                                .thumbnail(0.5f)
                                .centerInside()
                                .into(imv);
                        urlPath_foto = list.get(0).getAbsolutePath();
                        break;
                }
            }
        });
    }

    private void simpan(){
        et_nama_kategori.setError(null);
        nama_kategori = et_nama_kategori.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nama_kategori)){
            et_nama_kategori.setError("Silahkann diisi..");
            focusView = et_nama_kategori;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (TextUtils.isEmpty(urlPath_foto)){
                Toast.makeText(getApplicationContext(), "Silahkan Pilih Foto.", Toast.LENGTH_LONG).show();
            } else {
                final ProgressDialog pDialog = new ProgressDialog(this);
                pDialog.setMessage("Tunggu sebentar...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                File file = Config.setFileResize(urlPath_foto,1);
                builder.addFormDataPart("foto", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                builder.addFormDataPart("nama_kategori",nama_kategori);
                MultipartBody requestBody = builder.build();

                UserAPIServices api = Config.getRetrofit(Config.URL).create(UserAPIServices.class);
                Call<ResponseBody> post = api.kategori_tambah(requestBody);
                post.enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        pDialog.dismiss();
                        try {
                            String json = response.body().string();
                            Log.d("catatan",json);
                            JSONObject jsonObj = new JSONObject(json);
                            Config.jsonArray = jsonObj.getJSONArray("result");
                            for(int i=0;i<Config.jsonArray.length();i++) {
                                JSONObject c = Config.jsonArray.getJSONObject(i);
                                String status = c.getString("status");

                                if (status.equals("1")) {
                                    Toast.makeText(getApplicationContext(), "Berhasil Mengubah Kategori.", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gagal Mengubah Kategori.", Toast.LENGTH_LONG).show();
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
}
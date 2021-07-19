package com.example.arkacamataadmin.main.home;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.arkacamataadmin.config.Config;
import com.example.arkacamataadmin.config.UserAPIServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.arkacamataadmin.R;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KacamataEditActivity extends AppCompatActivity implements View.OnClickListener {
    String id_tb_kacamata, nama_kacamata, harga_kacamata, deskripsi_kacamata, foto_kacamata, file_3d,
            id_tb_kategori, nama_kategori, urlPath_foto, urlPath_3d;
    EditText et_nama_kacamata, et_harga_kacamata, et_deskripsi_kacamata, et_file_3d;
    Spinner sp_kategori;
    ArrayList<String> list_kategori_id = new ArrayList<>();
    ArrayList<String> list_kategori_nama = new ArrayList<>();
    Button btn_simpan, btn_batal, btn_cari;
    FloatingActionButton fab;
    ImageView imv;
    static final int REQUEST_CODE_GALLERY = 001, REQUEST_CODE_3D = 002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kacamata_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Kacamata");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle b = getIntent().getExtras();
        id_tb_kacamata = b.getString("id_tb_kacamata");
        nama_kacamata = b.getString("nama_kacamata");
        harga_kacamata = b.getString("harga_kacamata");
        deskripsi_kacamata = b.getString("deskripsi_kacamata");
        foto_kacamata = b.getString("foto_kacamata");
        file_3d = b.getString("file_3d");
        id_tb_kategori = b.getString("id_tb_kategori");
        nama_kategori = b.getString("nama_kategori");

        et_nama_kacamata = findViewById(R.id.et_nama_kacamata);
        et_harga_kacamata = findViewById(R.id.et_harga_kacamata);
        et_deskripsi_kacamata = findViewById(R.id.et_deskripsi_kacamata);
        et_file_3d = findViewById(R.id.et_file_3d);
        sp_kategori = findViewById(R.id.sp_kategori);
        imv = findViewById(R.id.imv);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_batal = findViewById(R.id.btn_batal);
        btn_cari = findViewById(R.id.btn_cari);
        fab = findViewById(R.id.fab);
        btn_simpan.setOnClickListener(this);
        btn_batal.setOnClickListener(this);
        btn_cari.setOnClickListener(this);
        fab.setOnClickListener(this);

        Glide.with(this).load(Config.URL_FOTO_KACAMATA+foto_kacamata)
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(android.R.drawable.stat_notify_error)
                .into(imv);

        et_nama_kacamata.setText(nama_kacamata);
        et_harga_kacamata.setText(harga_kacamata);
        et_deskripsi_kacamata.setText(deskripsi_kacamata);
        et_file_3d.setText(file_3d);

        list_kategori_id.add(id_tb_kategori);
        list_kategori_nama.add(nama_kategori);
        sp_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_tb_kategori = list_kategori_id.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                id_tb_kategori = list_kategori_id.get(0);
            }
        });

        getKategori();
    }

    private void getKategori() {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Tunggu sebentar...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        UserAPIServices api = Config.getRetrofit(Config.URL).create(UserAPIServices.class);
        Call<ResponseBody> post = api.kategori();
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
                        String id_tb_kategori_   = c.getString("id_tb_kategori");
                        String nama_kategori_    = c.getString("nama_kategori");
                        String foto_kategori_    = c.getString("foto_kategori");

                        if (!id_tb_kategori_.equals(id_tb_kategori)){
                            list_kategori_id.add(id_tb_kategori_);
                            list_kategori_nama.add(nama_kategori_);
                        }
                    }
                    sp_kategori.setAdapter(new ArrayAdapter<String>(KacamataEditActivity.this, android.R.layout.simple_spinner_dropdown_item, list_kategori_nama));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(KacamataEditActivity.this, "Tidak bisa mengirim data!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(KacamataEditActivity.this, "Tidak bisa mengirim data!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(KacamataEditActivity.this, "Tidak bisa mengirim data!!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_batal){
            onBackPressed();
        } else if (v == btn_cari){
            new MaterialFilePicker()
                    .withActivity(this)
                    .withRequestCode(REQUEST_CODE_3D)
                    .withFilter(Pattern.compile(".*\\.sfb$")) // Filtering files and directories by file name using regexp
                    .start();
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
                        Glide.with(KacamataEditActivity.this)
                                .load(list.get(0).getAbsolutePath())
                                .thumbnail(0.5f)
                                .centerInside()
                                .into(imv);
                        urlPath_foto = list.get(0).getAbsolutePath();
                        break;
                }
            }
        });
        if (REQUEST_CODE_3D == requestCode && resultCode == RESULT_OK){
            urlPath_3d = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            String nama_file = urlPath_3d.substring(urlPath_3d.lastIndexOf("/")+1);
            et_file_3d.setText(nama_file);
        }
    }

    private void simpan(){
        et_nama_kacamata.setError(null);
        et_harga_kacamata.setError(null);
        et_deskripsi_kacamata.setError(null);
        nama_kacamata = et_nama_kacamata.getText().toString();
        harga_kacamata = et_harga_kacamata.getText().toString();
        deskripsi_kacamata = et_deskripsi_kacamata.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(nama_kacamata)){
            et_nama_kacamata.setError("Silahkann diisi..");
            focusView = et_nama_kacamata;
            cancel = true;
        }
        if (TextUtils.isEmpty(harga_kacamata)){
            et_harga_kacamata.setError("Silahkann diisi..");
            focusView = et_harga_kacamata;
            cancel = true;
        }
        if (TextUtils.isEmpty(deskripsi_kacamata)){
            et_deskripsi_kacamata.setError("Silahkann diisi..");
            focusView = et_deskripsi_kacamata;
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
            if (!TextUtils.isEmpty(urlPath_foto)){
                Log.d("catatan",urlPath_foto);
                File file = Config.setFileResize(urlPath_foto,1);
                builder.addFormDataPart("foto", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                builder.addFormDataPart("foto_lama",foto_kacamata);
            }
            if (!TextUtils.isEmpty(urlPath_3d)){
                File file_3d_baru = new File(urlPath_3d);
                builder.addFormDataPart("file", file_3d_baru.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file_3d_baru));
                builder.addFormDataPart("file_lama",file_3d);
            }
            builder.addFormDataPart("id_tb_kacamata",id_tb_kacamata);
            builder.addFormDataPart("nama_kacamata",nama_kacamata);
            builder.addFormDataPart("harga_kacamata",harga_kacamata);
            builder.addFormDataPart("deskripsi_kacamata",deskripsi_kacamata);
            builder.addFormDataPart("id_tb_kategori",id_tb_kategori);
            MultipartBody requestBody = builder.build();

            UserAPIServices api = Config.getRetrofit(Config.URL).create(UserAPIServices.class);
            Call<ResponseBody> post = api.kacamata_edit(requestBody);
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
                                Toast.makeText(getApplicationContext(), "Berhasil Mengubah Kacamata.", Toast.LENGTH_LONG).show();
                                onBackPressed();
                            } else {
                                Toast.makeText(getApplicationContext(), "Gagal Mengubah Kacamata.", Toast.LENGTH_LONG).show();
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
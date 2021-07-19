package com.example.arkacamataadmin.main.kategori;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.arkacamataadmin.R;
import com.example.arkacamataadmin.config.Config;
import com.example.arkacamataadmin.config.UserAPIServices;
import com.example.arkacamataadmin.config.adapter.MyAdapterKategori;
import com.example.arkacamataadmin.config.adapter.MyAdapterKategori2;
import com.example.arkacamataadmin.config.item.ItemKategori;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KategoriFragment extends Fragment {
    MyAdapterKategori2 myAdapterKategori2;
    RecyclerView recyclerView_kategori;
    ArrayList<ItemKategori> itemKategoriArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kategori, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView_kategori = view.findViewById(R.id.recyclerView_kategori);
        recyclerView_kategori.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),KategoriTambahActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
    }

    private void getData() {
        itemKategoriArrayList.clear();
        final ProgressDialog pDialog = new ProgressDialog(getContext());
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
                        String id_tb_kategori   = c.getString("id_tb_kategori");
                        String nama_kategori    = c.getString("nama_kategori");
                        String foto_kategori    = c.getString("foto_kategori");

                        itemKategoriArrayList.add(new ItemKategori(id_tb_kategori,nama_kategori,foto_kategori));
                    }
                    myAdapterKategori2 = new MyAdapterKategori2(itemKategoriArrayList,getContext());
                    recyclerView_kategori.setAdapter(myAdapterKategori2);
                    myAdapterKategori2.SetOnItemClickListener(new MyAdapterKategori2.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            alertHapus(itemKategoriArrayList.get(position).getId_tb_kategori());
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Tidak bisa mengirim data!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Tidak bisa mengirim data!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getContext(), "Tidak bisa mengirim data!!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void alertHapus(String id_tb_kategori_) {
        new AlertDialog.Builder(getContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah anda yakin?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hapus(id_tb_kategori_);
                    }
                })
                .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void hapus(String id_tb_kategori_){
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Tunggu sebentar...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("id_tb_kategori",id_tb_kategori_);
        MultipartBody requestBody = builder.build();

        UserAPIServices api = Config.getRetrofit(Config.URL).create(UserAPIServices.class);
        Call<ResponseBody> post = api.kategori_hapus(requestBody);
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
                            Toast.makeText(getContext(), "Berhasil Menghapus Kategori.", Toast.LENGTH_LONG).show();
                            getData();
                        } else {
                            Toast.makeText(getContext(), "Gagal Menghapus Kategori.", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Tidak bisa mengirim data!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Tidak bisa mengirim data!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(getContext(), "Tidak bisa mengirim data!!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
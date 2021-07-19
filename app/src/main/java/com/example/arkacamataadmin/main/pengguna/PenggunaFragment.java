package com.example.arkacamataadmin.main.pengguna;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arkacamataadmin.R;
import com.example.arkacamataadmin.config.Config;
import com.example.arkacamataadmin.config.UserAPIServices;
import com.example.arkacamataadmin.config.adapter.MyAdapterKategori;
import com.example.arkacamataadmin.config.adapter.MyAdapterPengguna;
import com.example.arkacamataadmin.config.item.ItemKacamata;
import com.example.arkacamataadmin.config.item.ItemKategori;
import com.example.arkacamataadmin.config.item.ItemPengguna;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PenggunaFragment extends Fragment {
    ArrayList<ItemPengguna> itemPenggunaArrayList = new ArrayList<>();
    RecyclerView recyclerView_pengguna;
    MyAdapterPengguna myAdapterPengguna;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pengguna, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView_pengguna = view.findViewById(R.id.recyclerView_pengguna);
        recyclerView_pengguna.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        getData();
    }

    private void getData() {
        itemPenggunaArrayList.clear();
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Tunggu sebentar...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        UserAPIServices api = Config.getRetrofit(Config.URL).create(UserAPIServices.class);
        Call<ResponseBody> post = api.pengguna();
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
                        String id_tb_pengguna          = c.getString("id_tb_pengguna");
                        String nama   = c.getString("nama");
                        String email          = c.getString("email");
                        String alamat          = c.getString("alamat");
                        String jenis_kelamin          = c.getString("jenis_kelamin");

                        itemPenggunaArrayList.add(new ItemPengguna(id_tb_pengguna, nama, email, alamat, jenis_kelamin));
                    }

                   myAdapterPengguna = new MyAdapterPengguna(itemPenggunaArrayList,getContext());
                    recyclerView_pengguna.setAdapter(myAdapterPengguna);
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
package com.example.arkacamataadmin.main.home;

import android.app.ProgressDialog;
import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.arkacamataadmin.R;
import com.example.arkacamataadmin.config.Config;
import com.example.arkacamataadmin.config.UserAPIServices;
import com.example.arkacamataadmin.config.adapter.MyAdapterKacamata;
import com.example.arkacamataadmin.config.adapter.MyAdapterKategori;
import com.example.arkacamataadmin.config.item.ItemKacamata;
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

public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener  {
    SliderLayout mDemoSlider;
    ArrayList<String> item_id_kacamata_terbaru    = new ArrayList<>();
    ArrayList<String> item_nama_kacamata_terbaru  = new ArrayList<>();
    ArrayList<String> item_foto_kacamata_terbaru   = new ArrayList<>();
    ArrayList<ItemKategori> itemKategoriArrayList = new ArrayList<>();
    ArrayList<ItemKacamata> itemKacamataArrayList = new ArrayList<>();
    MyAdapterKategori myAdapterKategori;
    RecyclerView recyclerView_kategori;
    MyAdapterKacamata myAdapterKacamata;
    RecyclerView recyclerView_kacamata;
    boolean set_slide = true;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);
        recyclerView_kategori = view.findViewById(R.id.recyclerView_kategori);
        recyclerView_kategori.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView_kacamata = view.findViewById(R.id.recyclerView_kacamata);
        recyclerView_kacamata.setLayoutManager(new GridLayoutManager(getContext(), 2));

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), KacamataTambahActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        getData();
    }

    private void getData() {
//        item_id_kacamata_terbaru.clear();
//        item_foto_kacamata_terbaru.clear();
//        item_nama_kacamata_terbaru.clear();
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Tunggu sebentar...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        UserAPIServices api = Config.getRetrofit(Config.URL).create(UserAPIServices.class);
        Call<ResponseBody> post = api.home();
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
                        String id_tb_kacamata          = c.getString("id_tb_kacamata");
                        String nama_kacamata   = c.getString("nama_kacamata");
                        String foto_kacamata          = c.getString("foto_kacamata");

                        if (set_slide){
                            item_id_kacamata_terbaru.add(id_tb_kacamata);
                            item_nama_kacamata_terbaru.add(nama_kacamata);
                            item_foto_kacamata_terbaru.add(foto_kacamata);
                        }
                    }

                    itemKategoriArrayList.clear();
                    itemKategoriArrayList.add(new ItemKategori("0","Semua","semua.jpg"));
                    Config.jsonArray2 = jsonObj.getJSONArray("result2");
                    for(int i=0;i<Config.jsonArray2.length();i++) {
                        JSONObject c = Config.jsonArray2.getJSONObject(i);
                        String id_tb_kategori          = c.getString("id_tb_kategori");
                        String nama_kategori   = c.getString("nama_kategori");
                        String foto_kategori          = c.getString("foto_kategori");

                        itemKategoriArrayList.add(new ItemKategori(id_tb_kategori,nama_kategori,foto_kategori));
                    }
                    myAdapterKategori = new MyAdapterKategori(itemKategoriArrayList,getContext());
                    recyclerView_kategori.setAdapter(myAdapterKategori);
                    myAdapterKategori.SetOnItemClickListener(new MyAdapterKategori.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            getDataKacamata(itemKategoriArrayList.get(position).getId_tb_kategori());
                        }
                    });

                    if (set_slide){
                        setSlide();
                        set_slide = false;
                    }
                    getDataKacamata("0");
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

    private void getDataKacamata(String id) {
        itemKacamataArrayList.clear();
        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Tunggu sebentar...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();

        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        builder.addFormDataPart("id_tb_kategori",id);
        MultipartBody requestBody = builder.build();

        UserAPIServices api = Config.getRetrofit(Config.URL).create(UserAPIServices.class);
        Call<ResponseBody> post = api.kacamata(requestBody);
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
                        String id_tb_kacamata          = c.getString("id_tb_kacamata");
                        String nama_kacamata   = c.getString("nama_kacamata");
                        String foto_kacamata          = c.getString("foto_kacamata");
                        String harga_kacamata          = c.getString("harga_kacamata");
                        String nama_kategori          = c.getString("nama_kategori");

                        itemKacamataArrayList.add(new ItemKacamata(id_tb_kacamata, nama_kacamata, foto_kacamata, harga_kacamata, nama_kategori));
                    }
                    myAdapterKacamata = new MyAdapterKacamata(itemKacamataArrayList,getContext());
                    recyclerView_kacamata.setAdapter(myAdapterKacamata);
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

    private void setSlide(){
//        mDemoSlider.removeAllSliders();
        for (int i=0;i < item_id_kacamata_terbaru.size();i++){
            TextSliderView textSliderView = new TextSliderView(getContext());
            textSliderView.description(item_nama_kacamata_terbaru.get(i)).image(Config.URL_FOTO_KACAMATA+item_foto_kacamata_terbaru.get(i)).setScaleType(BaseSliderView.ScaleType.CenterCrop);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("id_tb_kacamata",item_id_kacamata_terbaru.get(i));
            textSliderView.setOnSliderClickListener(this);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Fade);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(5000);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent intent = new Intent(getContext(), KacamataDetailActivity.class);
        intent.putExtra("id_tb_kacamata",slider.getBundle().get("id_tb_kacamata").toString());
        startActivity(intent);
    }
}
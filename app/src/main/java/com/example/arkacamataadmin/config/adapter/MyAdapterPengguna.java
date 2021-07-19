package com.example.arkacamataadmin.config.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arkacamataadmin.R;
import com.example.arkacamataadmin.config.Config;
import com.example.arkacamataadmin.config.item.ItemKacamata;
import com.example.arkacamataadmin.config.item.ItemPengguna;
import com.example.arkacamataadmin.main.home.KacamataDetailActivity;

import java.util.ArrayList;

public class MyAdapterPengguna extends RecyclerView.Adapter<MyAdapterPengguna.ViewHolder> {
    private ArrayList<ItemPengguna> itemPenggunas;
    private Context context;

    public MyAdapterPengguna(final ArrayList<ItemPengguna> itemPenggunas, Context context){
        this.itemPenggunas  = itemPenggunas;
        this.context    = context;
    }

    @Override
    public MyAdapterPengguna.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_pengguna,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapterPengguna.ViewHolder holder, int position) {
        holder.tv_nama.setText(itemPenggunas.get(position).getNama());
        holder.tv_email.setText(itemPenggunas.get(position).getEmail());
        holder.tv_jenis_kelamin.setText(itemPenggunas.get(position).getJenis_kelamin());
        holder.tv_alamat.setText(itemPenggunas.get(position).getAlamat());
    }

    @Override
    public int getItemCount() {
        return itemPenggunas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_nama, tv_email, tv_jenis_kelamin, tv_alamat;
        ImageView imv;
        CardView cardView;

        public ViewHolder(View view){
            super(view);
            tv_nama   = view.findViewById(R.id.tv_nama);
            tv_email   = view.findViewById(R.id.tv_email);
            tv_alamat   = view.findViewById(R.id.tv_alamat);
            tv_jenis_kelamin   = view.findViewById(R.id.tv_jenis_kelamin);
            imv  = view.findViewById(R.id.imv);
            cardView = view.findViewById(R.id.cardView);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}

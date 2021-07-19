package com.example.arkacamataadmin.config.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arkacamataadmin.R;
import com.example.arkacamataadmin.config.Config;
import com.example.arkacamataadmin.config.item.ItemKategori;
import com.example.arkacamataadmin.main.kategori.KategoriEditActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MyAdapterKategori2 extends RecyclerView.Adapter<MyAdapterKategori2.ViewHolder> {
    private ArrayList<ItemKategori> itemKategoris;
    private Context context;
    OnItemClickListener mItemClickListener;

    public MyAdapterKategori2(final ArrayList<ItemKategori> itemKategoris, Context context){
        this.itemKategoris  = itemKategoris;
        this.context    = context;
    }

    @Override
    public MyAdapterKategori2.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_kategori_2,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapterKategori2.ViewHolder holder, int position) {
        holder.tv_nama.setText(itemKategoris.get(position).getNama_kategori());
        Glide.with(context).load(Config.URL_FOTO_KATEGORI+itemKategoris.get(position).getFoto_kategori())
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imv);
    }

    @Override
    public int getItemCount() {
        return itemKategoris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_nama;
        ImageView imv;
        FloatingActionButton fab_edit, fab_hapus;

        public ViewHolder(View view){
            super(view);
            tv_nama   = view.findViewById(R.id.tv_nama);
            imv  = view.findViewById(R.id.imv);
            fab_edit = view.findViewById(R.id.fab_edit);
            fab_hapus = view.findViewById(R.id.fab_hapus);
            view.setOnClickListener(this);
            fab_edit.setOnClickListener(this);
            fab_hapus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == fab_edit){
                Intent intent = new Intent(context, KategoriEditActivity.class);
                intent.putExtra("id_tb_kategori",itemKategoris.get(getAdapterPosition()).getId_tb_kategori());
                intent.putExtra("nama_kategori",itemKategoris.get(getAdapterPosition()).getNama_kategori());
                intent.putExtra("foto_kategori",itemKategoris.get(getAdapterPosition()).getFoto_kategori());
                context.startActivity(intent);
            } else if (v == fab_hapus) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}

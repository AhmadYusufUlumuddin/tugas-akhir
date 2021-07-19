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
import com.example.arkacamataadmin.main.home.KacamataDetailActivity;

import java.util.ArrayList;

public class MyAdapterKacamata extends RecyclerView.Adapter<MyAdapterKacamata.ViewHolder> {
    private ArrayList<ItemKacamata> itemKacamatas;
    private Context context;

    public MyAdapterKacamata(final ArrayList<ItemKacamata> itemKacamatas, Context context){
        this.itemKacamatas  = itemKacamatas;
        this.context    = context;
    }

    @Override
    public MyAdapterKacamata.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_kacamata,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapterKacamata.ViewHolder holder, int position) {
        holder.tv_nama.setText(itemKacamatas.get(position).getNama_kacamata());
        holder.tv_harga.setText(itemKacamatas.get(position).getHarga_kacamata());
        holder.tv_kategori.setText(itemKacamatas.get(position).getNama_kategori());
        Glide.with(context).load(Config.URL_FOTO_KACAMATA+itemKacamatas.get(position).getFoto_kacamata())
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.imv);
    }

    @Override
    public int getItemCount() {
        return itemKacamatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_nama, tv_harga, tv_kategori;
        ImageView imv;
        CardView cardView;

        public ViewHolder(View view){
            super(view);
            tv_nama   = view.findViewById(R.id.tv_nama);
            tv_harga   = view.findViewById(R.id.tv_harga);
            tv_kategori   = view.findViewById(R.id.tv_kategori);
            imv  = view.findViewById(R.id.imv);
            cardView = view.findViewById(R.id.cardView);
            view.setOnClickListener(this);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == cardView){
                Intent i = new Intent(context, KacamataDetailActivity.class);
                i.putExtra("id_tb_kacamata",itemKacamatas.get(getAdapterPosition()).getId_tb_kacamata());
                context.startActivity(i);
            }
        }
    }
}

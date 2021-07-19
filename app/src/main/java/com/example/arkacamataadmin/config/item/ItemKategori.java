package com.example.arkacamataadmin.config.item;

public class ItemKategori {
    String id_tb_kategori, nama_kategori, foto_kategori;

    public ItemKategori(String id_tb_kategori, String nama_kategori, String foto_kategori){
        this.id_tb_kategori = id_tb_kategori;
        this.nama_kategori = nama_kategori;
        this.foto_kategori = foto_kategori;
    }

    public String getFoto_kategori() {
        return foto_kategori;
    }

    public String getId_tb_kategori() {
        return id_tb_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }
}

package com.example.arkacamataadmin.config.item;

public class ItemKacamata {
    String id_tb_kacamata, nama_kacamata, foto_kacamata, harga_kacamata, nama_kategori;

    public ItemKacamata(String id_tb_kacamata, String nama_kacamata, String foto_kacamata, String harga_kacamata, String nama_kategori){
        this.id_tb_kacamata = id_tb_kacamata;
        this.nama_kacamata = nama_kacamata;
        this.foto_kacamata = foto_kacamata;
        this.harga_kacamata = harga_kacamata;
        this.nama_kategori = nama_kategori;
    }

    public String getNama_kategori() {
        return nama_kategori;
    }

    public String getFoto_kacamata() {
        return foto_kacamata;
    }

    public String getHarga_kacamata() {
        return harga_kacamata;
    }

    public String getId_tb_kacamata() {
        return id_tb_kacamata;
    }

    public String getNama_kacamata() {
        return nama_kacamata;
    }
}

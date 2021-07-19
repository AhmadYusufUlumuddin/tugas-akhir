package com.example.arkacamataadmin.config.item;

public class ItemPengguna {
    String id_tb_pengguna, nama, email, alamat, jenis_kelamin;

    public  ItemPengguna(String id_tb_pengguna, String nama, String email, String alamat, String jenis_kelamin){
        this.id_tb_pengguna = id_tb_pengguna;
        this.nama = nama;
        this.email = email;
        this.alamat = alamat;
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getNama() {
        return nama;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getId_tb_pengguna() {
        return id_tb_pengguna;
    }

    public String getEmail() {
        return email;
    }
}

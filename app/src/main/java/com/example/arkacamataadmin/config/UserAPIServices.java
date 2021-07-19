package com.example.arkacamataadmin.config;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface UserAPIServices {
    @POST("signin")
    Call<ResponseBody> signin(@Body RequestBody file);

    @GET("home")
    Call<ResponseBody> home();

    @POST("kacamata")
    Call<ResponseBody> kacamata(@Body RequestBody file);

    @POST("kacamata_detail")
    Call<ResponseBody> kacamata_detail(@Body RequestBody file);

    @POST("kacamata_edit")
    Call<ResponseBody> kacamata_edit(@Body RequestBody file);

    @POST("kacamata_tambah")
    Call<ResponseBody> kacamata_tambah(@Body RequestBody file);

    @POST("kacamata_hapus")
    Call<ResponseBody> kacamata_hapus(@Body RequestBody file);

    @GET("pengguna")
    Call<ResponseBody> pengguna();

    @GET("kategori")
    Call<ResponseBody> kategori();

    @GET("kategori_detail")
    Call<ResponseBody> kategori_detail();

    @POST("kategori_tambah")
    Call<ResponseBody> kategori_tambah(@Body RequestBody file);

    @POST("kategori_edit")
    Call<ResponseBody> kategori_edit(@Body RequestBody file);

    @POST("kategori_hapus")
    Call<ResponseBody> kategori_hapus(@Body RequestBody file);

    @POST("profil_edit")
    Call<ResponseBody> profil_edit(@Body RequestBody file);

    @POST("profil_password")
    Call<ResponseBody> profil_password(@Body RequestBody file);

    @GET
    Call<ResponseBody> kacamata_download_3d(@Url String fileUrl);
}

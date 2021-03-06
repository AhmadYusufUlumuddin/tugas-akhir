package com.example.arkacamataadmin.config;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {
    public static final String IMAGE_DIRECTORY_NAME = "AR Kacamata";
    public static final String BASE_URL = "http://192.168.18.207/ci_arkacamata/";
    public static final String URL = BASE_URL+"Api_admin/";
    public static JSONArray jsonArray, jsonArray2 = null;
    public static final String URL_FOTO_KATEGORI = BASE_URL+"assets/upload/kategori/";
    public static final String URL_FOTO_KACAMATA = BASE_URL+"assets/upload/kacamata/";
    public static final String URL_3D = BASE_URL+"assets/upload/3d/";

    public static Retrofit getRetrofit(String url) {
        return new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static File setFileResize(String filePath, int index_ke){
        File imgFileOrig = new File(filePath);
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        int index = imgFileOrig.getName().lastIndexOf('.')+1;
        String ext = imgFileOrig.getName().substring(index).toLowerCase();
        String type = mime.getMimeTypeFromExtension(ext);
        int file_size = Integer.parseInt(String.valueOf(imgFileOrig.length()/1024));
        int size_resize=0;

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) :  ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Bitmap b = BitmapFactory.decodeFile(imgFileOrig.getAbsolutePath());
        int origWidth = b.getWidth();
        int origHeight = b.getHeight();

        if (file_size>1000){
            origWidth = origWidth/2;
            origHeight = origHeight/2;
        }

        if (rotationAngle == 90){
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            b = Bitmap.createBitmap(b, 0, 0, origWidth, origHeight, matrix, true);
        }

        Bitmap b2;
        if (rotationAngle == 90){
            b2 = Bitmap.createScaledBitmap(b, origHeight, origWidth, false);
        } else {
            b2 = Bitmap.createScaledBitmap(b, origWidth, origHeight, false);
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        if (file_size<=500){
            size_resize=50;
        } else if (file_size>500){
            size_resize=50;
        }

        if (type.equals("image/jpeg") || type.equals("image/jpg")){
            b2.compress(Bitmap.CompressFormat.JPEG,size_resize , outStream);
        } else if (type.equals("image/png")){
            b2.compress(Bitmap.CompressFormat.PNG,size_resize , outStream);
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Config.IMAGE_DIRECTORY_NAME);
        if(!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File file = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp+index_ke+ ".jpg");
        try {
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(outStream.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}

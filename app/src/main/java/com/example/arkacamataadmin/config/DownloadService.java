package com.example.arkacamataadmin.config;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.arkacamataadmin.R;
import com.example.arkacamataadmin.main.home.KacamataDetailActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class DownloadService extends IntentService {

    public DownloadService() {
        super("Download Service");
    }

    private int totalFileSize;
    private String file_3d = "";
    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null){
            file_3d = intent.getStringExtra("file_3d");
        }

        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel("AR Kacamata", getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(mChannel);
        }

        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, KacamataDetailActivity.class),0);
        builder = new NotificationCompat.Builder(this)
                .setContentText("Download")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Downloading File")
                .setAutoCancel(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setChannelId("AR Kacamata");
        }
        mNotificationManager.notify(101,builder.build());
        initDownload();
    }

    private void initDownload(){
        String file_url = Config.BASE_URL+"assets/upload/3d/"+file_3d;
        UserAPIServices api = Config.getRetrofit(Config.URL).create(UserAPIServices.class);
        Call<ResponseBody> request = api.kacamata_download_3d(file_url);
        try {
            downloadFile(request.execute().body());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    private void downloadFile(ResponseBody body) throws IOException {
        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);

        final File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Config.IMAGE_DIRECTORY_NAME);
        final File outputFile = new File(mediaStorageDir.getPath() + File.separator + file_3d);
        if(!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        OutputStream output = new FileOutputStream(outputFile);
        long total = 0;
        long startTime = System.currentTimeMillis();
        int timeCount = 1;
        while ((count = bis.read(data)) != -1) {
            total += count;
            totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
            double current = Math.round(total / (Math.pow(1024, 2)));
            int progress = (int) ((total * 100) / fileSize);
            long currentTime = System.currentTimeMillis() - startTime;
            Download download = new Download();
            download.setTotalFileSize(totalFileSize);
            if (currentTime > 1000 * timeCount) {
                download.setCurrentFileSize((int) current);
                download.setProgress(progress);
                sendNotification(download);
                timeCount++;
            }
            output.write(data, 0, count);
        }
        onDownloadComplete();
        output.flush();
        output.close();
        bis.close();

    }

    private void sendNotification(Download download){
        sendIntent(download);
        builder.setProgress(100,download.getProgress(),false);
        builder.setContentText("Downloading file "+ download.getCurrentFileSize() +"/"+totalFileSize +" MB");
        mNotificationManager.notify(101,builder.build());
    }

    private void sendIntent(Download download){
        Intent intent = new Intent(KacamataDetailActivity.MESSAGE_PROGRESS);
        intent.putExtra("download",download);
        LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
    }

    private void onDownloadComplete(){
        Download download = new Download();
        download.setProgress(100);
        sendIntent(download);

        mNotificationManager.cancel(0);
        builder.setProgress(0,0,false);
        builder.setContentText("File Downloaded");
        mNotificationManager.notify(0, builder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        mNotificationManager.cancel(0);
    }
}

package vn.co.vns.runningman.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by thanhnv on 10/4/16.
 */
public class DownloadFileService extends IntentService{
    public static final int UPDATE_PROGRESS = 8344;

    public DownloadFileService() {
        super("DownloadFileService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String urlToDownload = intent.getStringExtra("url");
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        try {
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();
            String path =getFilesDir().getAbsolutePath();
            // download the file
            String filename=urlToDownload.substring(urlToDownload.lastIndexOf("/")+1);
            InputStream input = new BufferedInputStream(connection.getInputStream());
            OutputStream output = new FileOutputStream(path+"/"+filename);
            Log.d("File name: ",filename);
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                Bundle resultData = new Bundle();
                resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                receiver.send(UPDATE_PROGRESS, resultData);
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle resultData = new Bundle();
        resultData.putInt("progress" ,100);
        receiver.send(UPDATE_PROGRESS, resultData);
    }
}

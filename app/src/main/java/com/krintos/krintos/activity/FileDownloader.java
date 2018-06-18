package com.krintos.krintos.activity;

/**
 * Created by root on 6/25/17.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import info.androidhive.loginandregistration.R;

public class FileDownloader {
    private static final int  MEGABYTE = 1024 * 1024;

    public static void downloadFile(String fileUrl, File directory){

        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();
            byte[] data = new byte[8 * MEGABYTE];
            float total = 0;
            int read_bytes = 0;
            Mathematics.handler.post(new Runnable() {
                @Override
                public void run() {
                    Mathematics.downprog.setVisibility(View.VISIBLE);

                }
            });
            while((read_bytes = inputStream.read(data)) !=-1  ){
                total += read_bytes;
                fileOutputStream.write(data, 0, read_bytes );
                Mathematics.downprog.setProgress((int)((total / totalSize)*100));


            }

            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}



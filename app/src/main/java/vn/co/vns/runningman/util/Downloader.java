package vn.co.vns.runningman.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import vn.co.vns.runningman.RunningApp;
import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.Kqdq;

/**
 * Created by thanhnv on 10/21/16.
 */
public class Downloader {

    public Downloader(){

    }

    public static boolean downloadFile(Context mContext,String urlToDownload, int singleFile){
        boolean isSucess=false;
        long total = 0;
        try {
            SharedPreference.getInstance(mContext).putInt("updatedStock", 1);
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            long fileLength = connection.getContentLength();
            String path =mContext.getFilesDir().getAbsolutePath();
            String filename=urlToDownload.substring(urlToDownload.lastIndexOf("/")+1);
            // download the file
            File myFile = new File(path+"/"+filename);
            if(myFile.exists())
                myFile.delete();
            //Start download
            InputStream input = new BufferedInputStream(connection.getInputStream());
            OutputStream output = new FileOutputStream(path+"/"+filename);
            Log.d("File name: ",filename);
            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                Bundle resultData = new Bundle();
                resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                //receiver.send(UPDATE_PROGRESS, resultData);
                output.write(data, 0, count);
                Log.d("Size: ", "Downloaded "+ count + ":" + total);
                if(fileLength==total){
                    UnzipDataFile objUnZip = new UnzipDataFile(mContext, "");
                    try {
                        objUnZip.unzip(new File(path+"/"+filename), new File(path+"/"+filename.replaceFirst("[.][^.]+$", "")+"/"),singleFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("Finished", "Downloaded ");
            output.flush();
            output.close();
            input.close();
            isSucess=true;
        } catch (IOException e) {
            e.printStackTrace();
            isSucess=false;
        }
        return isSucess;
    }

    //Use
    public static boolean downloadFiles(Context mContext,String urlAllday,String urlDay){
        Log.d("Downloader", "Start download ");
       boolean isSucess=false;
        long total = 0;
        try {
            SharedPreference.getInstance(mContext).putInt("updatedStock", 1);
            URL url = new URL(urlAllday);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            long fileLength = connection.getContentLength();
            String path =mContext.getFilesDir().getAbsolutePath();
            // download the file
            String filename=urlAllday.substring(urlAllday.lastIndexOf("/")+1);

            InputStream input = new BufferedInputStream(connection.getInputStream());
            OutputStream output = new FileOutputStream(path+"/"+filename);
            Log.d("File name: ",filename);
            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                Bundle resultData = new Bundle();
                resultData.putInt("progress" ,(int) (total * 100 / fileLength));
                //receiver
                // .send(UPDATE_PROGRESS, resultData);
                output.write(data, 0, count);
                Log.d("Size: ", "Downloaded "+ count + ":" + total);
                if(fileLength==total && urlAllday!=urlDay){
                    UnzipDataFile objUnZip = new UnzipDataFile(mContext, "");
                    int allFile=0; // All file
                    objUnZip.unzip(new File(path+"/"+filename), new File(path+"/"+filename.replaceFirst("[.][^.]+$", "")+"/"),allFile);
                  downloadFile(mContext,urlDay,allFile);
                }
            }

            Log.d("Finished", "Downloaded ");
            output.flush();
            output.close();
            input.close();
            isSucess=true;
        } catch (IOException e) {
            Log.d("The end", "Downloaded ");
            e.printStackTrace();
        }
        return isSucess;
    }

    /**
     * Download finance file with id
     */

    private static final int BUFFER_SIZE = 8192;
    /**
     * Downloads a file from a URL
     * @throws IOException
     */
    public void downloadFile(String ticker, final IDownloadSuccessCallback callback) {
        try {
            if (!RunningApp.getInstance().getCookie().isEmpty()) {
                String DOWNLOAD_URL="http://www.cophieu68.vn/export/reportfinance.php?id=" + ticker;
                URL url = new URL(DOWNLOAD_URL);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestProperty("Cookie", RunningApp.getInstance().getCookie());
                httpConn.setRequestProperty("Accept-Language", "ru,en-GB;q=0.8,en;q=0.6");
                httpConn.setRequestProperty("Accept-Charset", "utf-8");
                httpConn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                int responseCode = httpConn.getResponseCode();

                // always check HTTP response code first
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String fileName = "";
                    String disposition = httpConn.getHeaderField("Content-Disposition");
                    String contentType = httpConn.getContentType();
                    int contentLength = httpConn.getContentLength();

                    if (disposition != null) {
                        // extracts file name from header field
                        int index = disposition.indexOf("filename=");
                        if (index > 0) {
                            fileName = disposition.substring(index + 10,
                                    disposition.length() - 1);
                        }
                    } else {
                        // extracts file name from URL
                        fileName = DOWNLOAD_URL.substring(DOWNLOAD_URL.lastIndexOf("/") + 1,
                                DOWNLOAD_URL.length());
                    }

                    System.out.println("Content-Type = " + contentType);
                    System.out.println("Content-Disposition = " + disposition);
                    System.out.println("Content-Length = " + contentLength);
                    System.out.println("fileName = " + fileName);

                    // opens input stream from the HTTP connection
                    InputStream inputStream = httpConn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream, "UTF-16");
                    BufferedReader br = new BufferedReader(isr);
                    String[] miniFileName = fileName.split(";");
                    String saveFilePath = RunningApp.getAppContext().getFilesDir().getAbsolutePath() + File.separator + miniFileName[0];
                    System.out.println("file path = " + saveFilePath);
//                    FileOutputStream outputStream = new FileOutputStream(saveFilePath);
//                    OutputStreamWriter myOutWriter = new OutputStreamWriter(outputStream);
//                    BufferedWriter writer = new BufferedWriter(new FileWriter(new File(saveFilePath)));
                    String[] baseRow = null;
                    String csvLine;
                    while ((csvLine = br.readLine()) != null) {
                        //Log.i("Data get", str);
                        //String[] row = str.split("\t", -1);
//                        writer.write(str);
//                        writer.newLine();
//                        myOutWriter.write(str);
                        String[] row = csvLine.split("\t", -1);
                        if(row[0].contains("CAN DOI KE TOAN")|| row[0].contains("KET QUA KINH DOANH")){
                            baseRow = row;
                        }else if (!row[0].contains("--")) {
                            addRowToDB(row, baseRow, ticker.toUpperCase());
                        }
                    }
                    br.close();
//                    myOutWriter.close();
//                    outputStream.close();
                    inputStream.close();
                    System.out.println("File downloaded");
                    httpConn.disconnect();
                    callback.onSuccess(saveFilePath);
                } else {
                    System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                    httpConn.disconnect();
                    callback.onError();
                }
                httpConn.disconnect();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
            callback.onError();
        }
    }

    private void addRowToDB(String[] row, String[] baseRow, String ticker) {
        if(baseRow == null) return;
        if(row ==null || row[0].trim().length()==0) return;
        ArrayList<Kqdq> listKqdq = new ArrayList<>();
        String year = "";
        String content = "";
        Kqdq item = new Kqdq();
        for(int i=0; i<row.length; i++){
            if(i==0){
                content = row[0];
            }
            if(baseRow[i].trim().length() == 4 && !baseRow[i].equals(year)){
                if(!year.equals("")) {
                    listKqdq.add(item);
                }
                year = baseRow[i].trim();
                item = new Kqdq();
                item.setTicker(ticker);
                item.setYear(year);
                item.setContent(content);
                item.setTotal(row[i]);
            }
            if(row[i].trim().length()>0){
                if (baseRow[i].contains("Q1")) {
                    item.setQ1(row[i]);
                } else if (baseRow[i].contains("Q2")) {
                    item.setQ2(row[i]);
                } else if (baseRow[i].contains("Q3")) {
                    item.setQ3(row[i]);
                } else if (baseRow[i].contains("Q4")) {
                    item.setQ4(row[i]);
                }
            }
        }
        listKqdq.add(item);
        for(int i = 0; i < listKqdq.size() ;i++){
            //Log.i("Data each row:", listKqdq.get(i).toString() + " ~");
            (new MySQLiteHelper(RunningApp.getAppContext())).addFincialItem(listKqdq.get(i));
        }
    }

    public interface IDownloadSuccessCallback{
        void onSuccess(String path);
        void onError();
    }

}

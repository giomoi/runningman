package vn.co.vns.runningman.util;

/**
 * Created by thanhnv on 4/13/16.
 */

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import vn.co.vns.runningman.activity.MainActivity;
import vn.co.vns.runningman.model.MySQLiteHelper;
import vn.co.vns.runningman.object.Stock;

/**
 * This class handle parsing csv and adding data to SQLite.
 */
public class UnzipDataFile {
    private int BUFFER_SIZE = 8192;
    private Context mContext;
    private String DEFAULT_LOCATION = "";
    private String fileName = "";
    private String contentsFile = "contents";
    private String TAG = UnzipDataFile.class.getSimpleName();

    public UnzipDataFile(Context mContext) {
        this.mContext = mContext;
    }

    //
    public UnzipDataFile(Context context, String fileNameUnZip) {
        mContext = context;
        fileName = fileNameUnZip;
        DEFAULT_LOCATION = mContext.getFilesDir().getAbsolutePath() + "/unzipped/";
        //DEFAULT_LOCATION="";
    }

    //
//    /**
//     * Unzip file and store data into Database
//     * @param isDefault true - it will unzip default file from asstet/ false - it will unzip data from fileName
//     */
//
    public void unzip(File zipFile, File targetDirectory, int numberFile) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        fileName = targetDirectory.toString();
        Log.d(TAG, "Start: " + fileName);
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } catch (Exception e) {
                    Log.d(TAG, e.getMessage());
                } finally {
                    fout.close();
                }
                /* if time should be restored as well
                long time = ze.getTime();
                if (time > 0)
                    file.setLastModified(time);
                */
            }
        } catch (Exception e) {
//            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        } finally {
            zis.close();
        }


//        Calendar cal = Calendar.getInstance();
//        int hourofday = cal.get(Calendar.HOUR_OF_DAY);
//        String dayJP=(hourofday>15) ? Constant.nowDay : Constant.beforeDay;
        String dayVN = Utils.convertPointDateVN(fileName.substring(fileName.length() - 8, fileName.length()));
        String dayVN1 = fileName.substring(fileName.length() - 8, fileName.length());

        if (fileName.contains("Upto")) {
            String fileNameHNX = fileName + "/CafeF.HNX.Upto" + dayVN + ".csv";
            Log.i(TAG, "HNX-Upto");
            buildDataIntoDb(fileNameHNX, numberFile); //4 file
            String fileNameHSX = fileName + "/CafeF.HSX.Upto" + dayVN + ".csv";
            Log.i(TAG, "HSX-Upto");
            buildDataIntoDb(fileNameHSX, numberFile); //4 file
        } else {
            Log.i(TAG, "HNX");
            String fileNameHNX = fileName + "/CafeF.HNX." + dayVN + ".csv";
            buildDataIntoDb(fileNameHNX, numberFile); //4 file
            Log.i(TAG, "HSX");
            String fileNameHSX = fileName + "/CafeF.HSX." + dayVN + ".csv";
            buildDataIntoDb(fileNameHSX, numberFile); //4 file
        }
    }

    //    /**
//     * Parse data from each file in .zip
//     */
    public void buildDataIntoDb(String csvFile, int numberFile) {
        Log.d(TAG, "Start insert DB" + csvFile);
        try {
            FileInputStream fisContents = new FileInputStream(csvFile);
            CSVFile fileContent = new CSVFile(fisContents, csvFile);
            ArrayList<Stock> contents = getArrayContent(fileContent.read());

            //Adding data from Array to SQLite
            MySQLiteHelper insertStock = new MySQLiteHelper(mContext);
             if (numberFile == 0) {
                if (fileName.contains("Upto")) {
                    insertStock.insertStock(contents);
                    Log.d(TAG, "SmallVolume");
                } else {
                    insertStock.insertStockBigVolume(contents);
                    //insertStock.insertStockVolumePriceAgreement(contents);
                    Log.d(TAG, "BigVolume");
                }
            } else {
                if (Singleton.getInstance().getNumberScreen() == Constant.MAIN_BIG_VOLUME) {
                    if (insertStock.insertStock(contents)) {
                        insertStock.insertStockBigVolume(contents);
                    }
                }
            }

        } catch (IOException e) {
//            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        Log.d(TAG, "buildDataIntoDb Success");
    }


    //    /**
//     * Bind list data of ContentQuestion to objects
//     * @param list ArrayList<String[]>
//     * @return ArrayList<ContentQuestion////     */
    private ArrayList<Stock> getArrayContent(ArrayList<String[]> list) {
        ArrayList<Stock> result = new ArrayList<>();
        int listSize = list.size();
        if (listSize > 0) {
            for (int i = 0; i < listSize; i++) {
                String[] line = list.get(i);
                Stock item = new Stock(line[0], line[1], Float.valueOf(line[2]), Float.valueOf(line[3]), Float.valueOf(line[4]), Float.valueOf(line[5]), Integer.parseInt(line[6]), 1);
//                Singleton.getInstance().setTicker(item.getTicker());
                Singleton.getInstance().setListStock(item);
                result.add(item);
            }
        }
        Log.i("Number of content", result.size() + "");

        return result;
    }

}

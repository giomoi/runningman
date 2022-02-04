package vn.co.vns.runningman.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vn.co.vns.runningman.object.Kqdq;

import static vn.co.vns.runningman.util.Constant.beforeDayFormat;

public class CSVFile {
    private String filePath;
    private InputStream inputStream;
    private ArrayList<String[]> resultList;
    private String TAG=CSVFile.class.getSimpleName();

    public CSVFile(InputStream inputStream, String filePath) {
        this.filePath = filePath;
        this.inputStream = inputStream;
    }

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public CSVFile() {

    }

    /**
     * Read csv from inputStream.
     *
     * @return Array String[]
     */
    public ArrayList<String[]> read() {
        Log.d(TAG, Constant.maxDay + " : " + String.valueOf(beforeDayFormat));
        resultList = new ArrayList<String[]>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 10240);
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if (handleData(row)) break;
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }
        //resultList.remove(0);
        return resultList;
    }

//    public ArrayList<String[]> readRandomLine(){
//        Log.d("Date: ", Constant.maxDay+" : "+String.valueOf(beforeDayFormat));
//        resultList = new ArrayList<String[]>();
//        try {
//            File file = new File(filePath);
//            RandomAccessFile raf = new RandomAccessFile(file, "r");
//            //raf.seek(file.length());
//            String csvLine;
//            while ((csvLine = raf.readLine()) != null) {
//                String[] row = csvLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
//                if(handleData(row)) break;
//            }
//            raf.close();
//        }
//        catch (IOException ex) {
//            throw new RuntimeException("Error in reading CSV file: "+ex);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                inputStream.close();
//            }
//            catch (IOException e) {
//                throw new RuntimeException("Error while closing input stream: "+e);
//            }
//        }
//        //resultList.remove(0);
//        return resultList;
//    }

    private boolean handleData(String[] row) throws ParseException {
        boolean isCheckUpdate = false;
        if (Utils.isDateValid(Utils.convertStringDate(row[1]))) {
            String yearDate = Utils.convertStringDate(row[1]);
            String stringBeforeYear = Utils.getBeforeYear(row[1]);
            //String beforeDate=Utils.convertStringDate(stringBeforeYear);
            //Date dateBeforeYear = Utils.convertStringToDate(stringBeforeYear);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(yearDate);
            Log.d("Date: ", row[1]);
            Date maxDate = null;
            Log.d("date " + beforeDayFormat + ": ", Utils.converDateToString(beforeDayFormat));
            //YYYY-MM-DD
            //String date30=Utils.converDateToString(Constant.before30DayFormat);
            //20161128=>Mon 11 2016
            if (Constant.maxDay != null)
                maxDate = Utils.convertStringToDateString(Constant.maxDay);
            Date before30Day = beforeDayFormat;
            //
            if (Constant.maxDay == null || (maxDate != null && beforeDayFormat.after(maxDate))) {
                Log.d(TAG,"Compare date: "+ beforeDayFormat + ">" + maxDate);
                maxDate = sdf.parse(Utils.converDateToString(beforeDayFormat));
            } else {
                Log.d(TAG,"Compare date: "+ beforeDayFormat + "<=" + maxDate);
                maxDate = sdf.parse(Utils.converDateToString(maxDate));

            }
            //if(Constant.maxDay==null) {
            //date2 = sdf.parse(Utils.converDateToString(Constant.before30DayFormat));
            //}else{
            //date2 = sdf.parse(Constant.beforeDayBigVolume);
            //}
            //maxDate-date 2<date 1  03/04 -12/04 date2<dateSelect 11/04
            //if (date2.before(date1) || date2.equals(date1)) {
            if (maxDate.before(Utils.convertStringToDateString(row[1]))
//                    || (maxDate.equals(Utils.convertStringToDateString(row[1])) && Constant.maxDay == null)
//                    || Singleton.getInstance().getNumberScreen() == Constant.MAIN_HOME
            ) {
                //Check only stock
                if(row[0].length()<4)
                resultList.add(row);
                Log.d(TAG,"Max date OK: "+ maxDate +":" +" Day: "+Utils.convertStringToDateString(row[1]) + ": Max day: " +Constant.maxDay);
            } else {
                Log.d(TAG,"Stop : "+ maxDate +":" +" Day: "+Utils.convertStringToDateString(row[1]) + ": Max day: " +Constant.maxDay);
                isCheckUpdate = true;
            }
        }
        return isCheckUpdate;
    }


    /**
     *
     */
    /**
     * Read csv from inputStream.
     *
     * @return Array String[]
     */
    public ArrayList<String[]> readFinanceFile(String fileName) {
        ArrayList<String[]> resultList = new ArrayList<String[]>();
        String ticker = fileName.replace("kqdq_", "").replace(".csv", "");
        try {
            String[] baseRow = null;
            if (inputStream == null) {
                inputStream = new FileInputStream(new File(fileName));
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-16"));
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                Log.i("Data get", csvLine);
                String[] row = csvLine.split("\t", -1);
                if (row[0].contains("CAN DOI KE TOAN") || row[0].contains("KET QUA KINH DOANH")) {
                    baseRow = row;
                } else if (!row[0].contains("--")) {
                    addRowToDB(row, baseRow, ticker.toUpperCase());
                }
//                resultList.add(row);
            }
            reader.close();
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //resultList.remove(0);
        return resultList;
    }

    private void addRowToDB(String[] row, String[] baseRow, String ticker) {
        if (baseRow == null) return;
        if (row == null || row[0].trim().length() == 0) return;
        ArrayList<Kqdq> listKqdq = new ArrayList<>();
        String year = "";
        String content = "";
        Kqdq item = new Kqdq();
        for (int i = 0; i < row.length; i++) {
            if (i == 0) {
                content = row[0];
            }
            if (baseRow[i].trim().length() == 4 && !baseRow[i].equals(year)) {
                if (!year.equals("")) {
                    listKqdq.add(item);
                }
                year = baseRow[i].trim();
                item = new Kqdq();
                item.setTicker(ticker);
                item.setYear(year);
                item.setContent(content);
                item.setTotal(row[i]);
            }
            if (row[i].trim().length() > 0) {
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
        for (int i = 0; i < listKqdq.size(); i++) {
            Log.i("Data each row:", listKqdq.get(i).toString() + " ~");
        }
    }


}

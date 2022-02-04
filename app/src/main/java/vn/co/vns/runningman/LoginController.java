package vn.co.vns.runningman;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hoangtuan on 2/7/17.
 */
public class LoginController {
    private static LoginController _instance = new LoginController();
    private String TAG = LoginController.class.getSimpleName();
    private String LOGIN_URL = "http://www.cophieu68.vn/account/login.php";
    private LoginController(){

    }

    public static LoginController getInstance() {
        if (_instance == null) {
            _instance = new LoginController();
        }
        return _instance;
    }


//    public void login(final ILoginCallback callback){
//        CustomStringRequest request = new CustomStringRequest(StringRequest.Method.POST, LOGIN_URL, new CustomStringRequest.IResponseStringCallback() {
//            @Override
//            public void onSuccess(String response) {
//                Log.i("response", response);
//                //callback.onSuccess();
//            }
//
//            @Override
//            public void onError() {
//                callback.onError();
//            }
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("Cookie", "");
//                params.put("Accept-Language", "ru,en-GB;q=0.8,en;q=0.6");
//                params.put("Accept-Charset", "utf-8");
//                params.put("Connection", "keep-alive");
//                params.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.1.2; en-au; SO-03E Build/10.1.E.0.305) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30");
//                params.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//                return params;
//            }
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("login", "1");
//                params.put("username", "van_thanh04@yahoo.com");
//                params.put("tpassword", "123456789");
//                return params;
//            }
//
//            @Override
//            protected Response<String> parseNetworkResponse(NetworkResponse response) {
//                Log.i("header", response.headers.toString());
//                Map<String, String> responseHeaders = response.headers;
//                String rawCookies = responseHeaders.get("Set-Cookie");
//                Log.i("cookies", rawCookies);
//                StartApp.getInstance().setCookie(rawCookies);
//                return super.parseNetworkResponse(response);
//            }
//        };
//        StartApp.getInstance().addToRequestQueue(request, "");
//    }

    /**********************************************************/
    public void loginHttpPost(final ILoginCallback callback){

        URL urlToRequest = null;
        HttpURLConnection urlConnection = null;
        String response = "";
        try {
            urlToRequest = new URL(LOGIN_URL);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.1.2; en-au; SO-03E Build/10.1.E.0.305) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30");

            HashMap<String, String> params = new HashMap<>();
            params.put("login", "1");
            params.put("username", "van_thanh04@yahoo.com");
            params.put("tpassword", "123456789");

            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            os.close();
            urlConnection.connect();
            int responseCode=urlConnection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line = urlConnection.getHeaderField("Set-Cookie");
                Log.i("Data Cookie", line);
//                BufferedReader br=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                while ((line=br.readLine()) != null) {
//                    Log.i("Data get", line);
//                    response+=line;
//                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    /**********************************************************/


    String dataSend = "tpassword=123456789&login=1&username=van_thanh04@yahoo.com";

    public synchronized String POST_req(final ILoginCallback callback) {
        if(RunningApp.getInstance().getCookie().isEmpty()) {
            URL addr = null;
            try {
                addr = new URL(LOGIN_URL);
            } catch (MalformedURLException e) {
                return "Unformat URL";
            }
            StringBuilder data = new StringBuilder();
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) addr.openConnection();
                conn.setRequestMethod("POST");
            } catch (IOException e) {
                return "Open connection error";
            }

            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Accept-Language", "ru,en-GB;q=0.8,en;q=0.6");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            conn.setRequestProperty("Cookie", "");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.1.2; en-au; SO-03E Build/10.1.E.0.305) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //conn.setInstanceFollowRedirects(true);
            //set_cookie(conn);

            //POST data:
            data.append(dataSend);
            try {
                conn.connect();
            } catch (IOException e) {
                return "Connecting error";
            }
            DataOutputStream dataOS = null;
            try {
                dataOS = new DataOutputStream(conn.getOutputStream());
            } catch (IOException e2) {
                return "Out stream error";
            }
            try {
                dataOS.writeBytes(data.toString());
            } catch (IOException e) {
                return "Out stream error 1";
            }

        /*If redirect: */
            int status;
            try {
                status = conn.getResponseCode();
            } catch (IOException e2) {
                return "Response error";
            }

            java.io.InputStream in = null;
            try {
                in = conn.getInputStream();
            } catch (IOException e) {
                return "In stream error";
            }
            InputStreamReader reader = null;
            try {
                reader = new InputStreamReader(in, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return "In stream error";
            }
            char[] buf = new char[8192];
            try {
                reader.read(buf);
            } catch (IOException e) {
                return "In stream error";
            }
            get_cookie(conn, callback);
            return (new String(buf));
        }
        return "";
    }
    public void get_cookie(HttpURLConnection conn, final ILoginCallback callback) {
        Map m = conn.getHeaderFields();
        if (m.containsKey("Set-Cookie")) {
            String cookies = "";
            Collection c = (Collection) m.get("Set-Cookie");
            for (Iterator i = c.iterator(); i.hasNext();) {
                cookies += i.next() + ",";
            }
            //cookie.setCookie(mContext.getString(R.string.host_url), cookies);
            Log.i("Cookie check", "cookie set " +  cookies);
            if(cookies.contains("username")) {
                RunningApp.getInstance().setCookie(cookies);
                callback.onSuccess();
            }else{
                callback.onError();
            }
        }
    }
    public void set_cookie(HttpURLConnection conn) {
    }

    public interface ILoginCallback{
        void onSuccess();
        void onError();
    }
}

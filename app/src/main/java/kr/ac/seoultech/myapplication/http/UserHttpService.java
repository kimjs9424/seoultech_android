package kr.ac.seoultech.myapplication.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class UserHttpService {

    private final static String TAG = UserHttpService.class.getSimpleName();

    private final static String URL_SERVER = "http://10.20.26.22:8080/todoapi";

    //private Context context;

    public Context context;

    public UserHttpService(Context context) {
        this.context = context;
    }

    public void join(final String loginId, final String password, final String name) {

        AsyncTask<Void, Void, Map<String, Object>> task = new AsyncTask<Void, Void, Map<String, Object>>() {
            @Override
            protected Map<String, Object> doInBackground(Void... params) {

                try {
                    URL url = new URL(URL_SERVER + "/join");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");

                    //데이터 전송
                    StringBuilder sb = new StringBuilder();
                    sb.append("loginId=").append(loginId).append("&");
                    sb.append("password=").append(password).append("&");
                    sb.append("name=").append(name);

                    OutputStream os = connection.getOutputStream();
                    os.write(sb.toString().getBytes());
                    os.flush();

                    //데이터 수신
                    InputStream is = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    char[] buffer = new char[4092];
                    int size = 0;
                    StringBuilder resultSb = new StringBuilder();

                    while ((size = br.read(buffer)) != -1) {
                        resultSb.append(buffer, 0, size);
                    }
                    Log.d(TAG, "result : " + resultSb.toString());


                    JSONObject jsonObject = new JSONObject(resultSb.toString());
                    String token = jsonObject.getString("token");
                    Long loginUserId = jsonObject.getLong("id");


                    Map<String, Object> result = new HashMap<>();
                    result.put("token", token);
                    result.put("loginUserId", loginUserId);
                    return result;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }


            @Override
            protected void onPostExecute(Map<String, Object> result) {

                SharedPreferences sp =
                        context.getSharedPreferences("LOGIN_SETTING", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token", (String) result.get("token"));
                editor.putLong("loginUserId", (Long) result.get("loginUserId"));
                editor.commit();  //저장

                Toast.makeText(context, "" + result, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "result : " + result);
            }
        };
        task.execute();




    }



}

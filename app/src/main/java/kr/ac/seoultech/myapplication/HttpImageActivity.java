package kr.ac.seoultech.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpImageActivity extends AppCompatActivity
        implements View.OnClickListener {

    private ImageView imgProfile;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_image);

        imgProfile = (ImageView) findViewById(R.id.img_profile);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        findViewById(R.id.btn_download).setOnClickListener(this);
        // -> View.OnClickListener 상속

    }

    @Override
    public void onClick(View v) {
        final String imageUri = "http://cfile201.uf.daum.net/image/241A9A3F567FEF7D01F524";

        callByAsyncTask(imageUri);

        //  callByThreadAndHandler(imageUri);
    }

    public void callByAsyncTask(String imageUri) {

        AsyncTask<String, Void, Bitmap> task = new AsyncTask<String, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(String... params) {
                String imageUri = params[0];

                try {
                    URL url = new URL(imageUri);
                    URLConnection urlConnection = url.openConnection();
                    InputStream is = urlConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    return bitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                imgProfile.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            }
        };
        task.execute(imageUri);
        progressBar.setVisibility(View.VISIBLE);
    }


    public void callByThreadAndHandler(final String imageUri) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {        //오버라이드 단축키 ctrl + o
                Bitmap bitmap = (Bitmap) msg.obj;
                imgProfile.setImageBitmap(bitmap);
                progressBar.setVisibility(View.GONE);
            }
        };

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    URL url = new URL(imageUri);
                    URLConnection urlConnection = url.openConnection();
                    InputStream is = urlConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);

                    Message msg = Message.obtain();
                    msg.obj = bitmap;

                    handler.sendMessage(msg);           //handleMessage메소드를 안드로이드 OS에서 sendMegssage 메소드로 받는다.

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();

        progressBar.setVisibility(View.VISIBLE);

    }


}

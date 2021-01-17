package com.gossipride.bartappfromscratch;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    String[] urls;
    Bitmap[] bitmaps;
    int correctLocation = 0;
    ArrayList<Integer> answers = new ArrayList<>();


    public void start (View view)
    {
        ImageDownloadTask task = new ImageDownloadTask();

        try {
            bitmaps = task.execute(urls).get();
            Log.i("bitmap", "success download: " + bitmaps.length);
            btnStart.setVisibility(View.INVISIBLE);
            RelativeLayout gameRelativeLayout = (RelativeLayout)findViewById(R.id.gameRelativeLayout);
            gameRelativeLayout.setVisibility(View.VISIBLE);
            play();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void play() {

        answers.clear();
        ImageView[] imageViews = {
                (ImageView) findViewById(R.id.imageView0),
                (ImageView) findViewById(R.id.imageView1),
                (ImageView) findViewById(R.id.imageView2),
                (ImageView) findViewById(R.id.imageView3)
        };


        Random rnd = new Random();
        String name = "";
        correctLocation = rnd.nextInt(5);
        for (int i = 0; i < 4; i++) {
            if (i == correctLocation)
                answers.add(correctLocation);
            else {
                int incorrect = rnd.nextInt(5);
                while (incorrect == correctLocation || answers.contains(incorrect))
                    incorrect = rnd.nextInt(5);
                answers.add(incorrect);
            }
        }

        for (int i = 0; i < 4; i++)
            imageViews[i].setImageBitmap(bitmaps[answers.get(i)]);

    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            btnStart = (Button)findViewById(R.id.btnStart);

            urls = new String[]
                    {
                            "https://upload.wikimedia.org/wikipedia/en/0/02/Homer_Simpson_2006.png",
                            "https://upload.wikimedia.org/wikipedia/en/0/0b/Marge_Simpson.png",
                            "https://upload.wikimedia.org/wikipedia/en/a/aa/Bart_Simpson_200px.png",
                            "https://upload.wikimedia.org/wikipedia/en/thumb/e/ec/Lisa_Simpson.png/220px-Lisa_Simpson.png",
                            "https://upload.wikimedia.org/wikipedia/en/thumb/9/9d/Maggie_Simpson.png/220px-Maggie_Simpson.png"
                    };

    }


        public class ImageDownloadTask extends AsyncTask<String, Void, Bitmap[]> {
            @Override
            protected Bitmap[] doInBackground(String[] urls) {

                Bitmap[] bitmaps = new Bitmap[urls.length];
                int index = 0;
                for (String url : urls) {
                    bitmaps[index++] = downloadImg(url);
                }
                return bitmaps;
            }


            private Bitmap downloadImg(String UrlString) {

                URL url;
                HttpURLConnection urlConnection;
                try {
                    url = new URL(UrlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    InputStream in = urlConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);

                    return bitmap;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }
    }

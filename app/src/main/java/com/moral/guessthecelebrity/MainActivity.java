package com.moral.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private static final String websiteURL = "http://www.posh24.se/kandisar";
    private static final int min = 1;
    private static final int max = 86;
    private static final int range = max - min + 1;
    private int position;
    private String alt;
    private String alt2;
    private String alt3;
    private String alt4;
    private ImageView celebImageView;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;


    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... urls) {
            String src = "";
            try {
                // Connect to the website and get the html dom
                Document doc = Jsoup.connect(urls[0]).get();

                // Get all elements with the img tag
                Elements imgs = doc.getElementsByTag("img");

                Element img = imgs.get(position);

                src = img.absUrl("src");
                alt = img.attr("alt");
                alt2 = imgs.get((int)(Math.random() * range) + min).attr("alt");
                alt3 = imgs.get((int)(Math.random() * range) + min).attr("alt");
                alt4 = imgs.get((int)(Math.random() * range) + min).attr("alt");



            }catch (IOException ex) {
                ex.printStackTrace();
            }

        try {
            // set up url
            URL url = new URL(src);

            // set up connection
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            return bitmap;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onPlay(View view) {
        setContentView(R.layout.play_main);
        start();
        setButtonOptions();

    }

    public void onCheck(View view) {
        Button button = (Button)view;
        String buttonText = button.getText().toString();
        if(buttonText.equals(alt)) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
            start();
            setButtonOptions();
        }
        else {
            Toast.makeText(this, "Wrong Answer", Toast.LENGTH_SHORT).show();
        }

    }

    private void start() {
        // update position
        this.position = (int)(Math.random() * this.range) + this.min;

        ImageDownloader imgs = new ImageDownloader();
        Bitmap bitmap = null;
        celebImageView = (ImageView)findViewById(R.id.celebImageView);

        // start with a random photo
        try {
            bitmap = imgs.execute(websiteURL).get();
            this.celebImageView.setImageBitmap(bitmap);
            Log.i("Alt", this.alt);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setButtonOptions() {
        this.button1 = (Button) findViewById(R.id.button1);
        this.button2 = (Button) findViewById(R.id.button2);
        this.button3 = (Button) findViewById(R.id.button3);
        this.button4 = (Button) findViewById(R.id.button4);

        this.button1.setText(this.alt);
        this.button2.setText(this.alt);
        this.button3.setText(this.alt);
        this.button4.setText(this.alt);

        int correctButton = (int)(Math.random() *(4-1+1))+1;
        if(correctButton == 1) {
            button1.setText(alt);
            button2.setText(alt2);
            button3.setText(alt3);
            button4.setText(alt4);
        }
        else if(correctButton == 2) {
            button2.setText(alt);
            button1.setText(alt3);
            button3.setText(alt2);
            button4.setText(alt4);
        }
        else if(correctButton == 3) {
            button3.setText(alt);
            button2.setText(alt4);
            button1.setText(alt2);
            button4.setText(alt3);
        }
        else {
            button4.setText(alt);
            button2.setText(alt4);
            button3.setText(alt3);
            button1.setText(alt2);
        }
    }
}

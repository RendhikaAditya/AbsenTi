package com.example.absenti.actifity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.absenti.R;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class QrActivity extends AppCompatActivity {
    ImageView imageView;
    Button btnDownload;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
//        getSupportActionBar().hide();

        Intent intent = new Intent(getIntent());
        String idqr = intent.getStringExtra("IDQR");

        imageView = findViewById(R.id.imgQr);
        Picasso.get().load("http://absen.dataku.xyz/api/tmp/"+idqr+".png").into(imageView);

        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setVisibility(View.GONE);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getIntent());
                String idqr = intent.getStringExtra("IDQR");
                new DownloadImage().execute("http://absen.dataku.xyz/api/tmp/"+idqr+".png");
            }
        });
//        Bitmap bitmap = getIntent().getParcelableExtra("pic");
//        imageView.setImageBitmap(bitmap);

    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(QrActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Download Image Tutorial");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... URL) {

            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // Set the bitmap into ImageView
            imageView.setImageBitmap(result);
            // Close progressdialog
            mProgressDialog.dismiss();
        }
    }
}
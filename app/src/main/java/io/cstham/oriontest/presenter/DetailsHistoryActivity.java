package io.cstham.oriontest.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;

import io.cstham.oriontest.R;


public class DetailsHistoryActivity extends FragmentActivity {

    public static boolean isDetailsHistoryActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //==========================================================================================
        TextView titleTxtView = findViewById(R.id.activity_details__title);
        TextView contentTxtView = findViewById(R.id.activity_details__content);
        ImageView photoImgView = findViewById(R.id.activity_details__urlImage);


        titleTxtView.setText(getIntent().getStringExtra("title"));
        contentTxtView.setText(getIntent().getStringExtra("content"));
        loadImage(photoImgView, getIntent().getStringExtra("urlImage"));

    }

    @Override
    public void onBackPressed() {
        isDetailsHistoryActivity = true;
        finish();
    }

    private void loadImage(final ImageView img, String imageUrl){
        AndroidNetworking.get(imageUrl)
                .setPriority(Priority.HIGH)
                .setBitmapMaxHeight(500)
                .setBitmapMaxWidth(500)
                .setBitmapConfig(Bitmap.Config.ARGB_8888)
                .build()
                .getAsBitmap(new BitmapRequestListener() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img.setImageBitmap(response);
                    }
                    @Override
                    public void onError(ANError error) {
                        img.setImageResource(R.drawable.error404);
                    }
                });
    }




}


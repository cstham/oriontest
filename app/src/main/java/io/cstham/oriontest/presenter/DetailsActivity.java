package io.cstham.oriontest.presenter;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.BitmapRequestListener;

import io.cstham.oriontest.R;
import io.cstham.oriontest.database.HistoryDatabaseHelper;

import static io.cstham.oriontest.presenter.DetailsHistoryActivity.isDetailsHistoryActivity;

public class DetailsActivity extends FragmentActivity {

    private HistoryDatabaseHelper db;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //==========================================================================================
        isDetailsHistoryActivity = false;
        TextView titleTxtView = findViewById(R.id.activity_details__title);
        TextView contentTxtView = findViewById(R.id.activity_details__content);
        ImageView photoImgView = findViewById(R.id.activity_details__urlImage);


        titleTxtView.setText(getIntent().getStringExtra("title"));
        contentTxtView.setText(getIntent().getStringExtra("content"));

        loadImage(photoImgView, getIntent().getStringExtra("urlImage"));

        //insert history records to database
        db = new HistoryDatabaseHelper(this);

        try {
            long id = db.insertHistory(
                    getIntent().getStringExtra("title"),
                    getIntent().getStringExtra("author"),
                    getIntent().getStringExtra("description"),
                    getIntent().getStringExtra("url"),
                    getIntent().getStringExtra("urlImage"),
                    getIntent().getStringExtra("content"));

            prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong("history_id", id);
            editor.apply();

        }
        catch (SQLiteException e){
            if (e.getMessage().contains("no such table")){
                System.out.println("No such table");
            }
        }




    }

    @Override
    public void onBackPressed() {
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

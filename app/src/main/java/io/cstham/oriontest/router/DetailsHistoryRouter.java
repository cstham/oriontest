package io.cstham.oriontest.router;

import android.content.Context;
import android.content.Intent;

import io.cstham.oriontest.presenter.DetailsHistoryActivity;

public class DetailsHistoryRouter {

    public static void startActivity(Context mContext, String  title, String content,
                                     String urlImage, String author, String description, String url)
    {
        Intent intent = new Intent(mContext, DetailsHistoryActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content",content);
        intent.putExtra("urlImage", urlImage);
        intent.putExtra("author", author);
        intent.putExtra("description", description);
        intent.putExtra("url", url);

        mContext.startActivity(intent);
    }
}


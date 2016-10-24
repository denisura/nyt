package com.github.denisura.nytseacher.ui.search.results;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.customtabsclient.CustomTabActivityHelper;
import com.github.denisura.nytseacher.data.model.Article;

import static com.github.denisura.nytseacher.utils.IntentUtils.getPendingShareIntent;

public class ArticleViewModel {
    private final Context mContext;
    private Article mArticle;

    public ArticleViewModel(Context context) {
        mContext = context;
    }

    public Article getArticle() {
        return mArticle;
    }

    public void setArticle(Article article) {
        mArticle = article;
    }

    public String getThumbnail() {
        return mArticle.getThumbnail();
    }

    public String getHeadline() {
        return mArticle.getHeadline();
    }

    public void onArticleSelected() {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_share);

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setActionButton(bitmap, "Share Link", getPendingShareIntent(mContext, mArticle.getWebUrl()), true)
                .addDefaultShareMenuItem()
                .build();
        CustomTabActivityHelper.openCustomTab((Activity) mContext, customTabsIntent, Uri.parse(mArticle.getWebUrl()),
                (activity, uri) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    activity.startActivity(intent);
                });
    }
}

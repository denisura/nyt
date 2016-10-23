package com.github.denisura.nytseacher.ui.search.results;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.data.model.Article;

import timber.log.Timber;

class BasicViewHolder extends RecyclerView.ViewHolder {

    private TextView mHeadline;
    private ImageView mThumbnail;

    BasicViewHolder(View itemView) {
        super(itemView);
        mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        mHeadline = (TextView) itemView.findViewById(R.id.headline);
    }

    void bind(Context context, Article article) {
        Timber.d(article.getThumbnail());
        Glide.with(context).load(article.getThumbnail())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mThumbnail);
        mHeadline.setText(article.getHeadline());
    }
}

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

class BasicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mHeadline;
    private ImageView mThumbnail;
    private Article mArticle;

    BasicViewHolder(View itemView) {
        super(itemView);
        mThumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
        mHeadline = (TextView) itemView.findViewById(R.id.headline);

    }

    void bind(Context context, Article article, SearchResultsAdapter.OnItemClickListener listener) {
        Timber.d(article.getThumbnail());
        Glide.with(context).load(article.getThumbnail())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mThumbnail);
        mHeadline.setText(article.getHeadline());

        // Setup the click listener
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Triggers click upwards to the adapter on click
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(article);
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        int position = getAdapterPosition(); // gets item position
        if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
            Timber.d("web url %s", mArticle.getWebUrl());
        }
    }
}

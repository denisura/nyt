package com.github.denisura.nytseacher.ui.search.results;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.data.model.Article;

class TextViewHolder extends RecyclerView.ViewHolder {

    private TextView mHeadline;

    TextViewHolder(View itemView) {
        super(itemView);
        mHeadline = (TextView) itemView.findViewById(R.id.headline);
    }

    void bind(Context context, Article article, SearchResultsAdapter.OnItemClickListener listener) {
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
}

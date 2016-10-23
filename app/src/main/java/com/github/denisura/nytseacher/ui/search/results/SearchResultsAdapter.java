package com.github.denisura.nytseacher.ui.search.results;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.data.model.Article;
import com.github.denisura.nytseacher.data.model.Item;

import java.util.ArrayList;
import java.util.List;


class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int UNKNOWN_VIEW = -1;
    private static final int BASIC_VIEW = 0;
    private static final int TEXT_VIEW = 1;

    private List<Item> listElements = new ArrayList<>();
    // after reorientation test this member
    // or one extra request will be sent after each reorientation
    private boolean allItemsLoaded;
    private Context mContext;

    SearchResultsAdapter() {
    }

    void setContext(Context context) {
        mContext = context;
    }

    void addNewItems(List<Item> items) {
        if (items.size() == 0) {
            allItemsLoaded = true;
            return;
        }
        listElements.addAll(items);
    }

    boolean isAllItemsLoaded() {
        return allItemsLoaded;
    }

    @Override
    public long getItemId(int position) {

        return getItem(position).getId();
    }

    private Item getItem(int position) {
        return listElements.get(position);
    }

    @Override
    public int getItemCount() {
        return listElements.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case BASIC_VIEW:
                View basicView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_article_basic, parent, false);
                return new BasicViewHolder(basicView);
            case TEXT_VIEW:
                View textView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_article_text, parent, false);
                return new TextViewHolder(textView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItem(position) == null) {
            return;
        }
        Article article = (Article) getItem(position).getEntity();
        switch (getItemViewType(position)) {
            case BASIC_VIEW:
                ((BasicViewHolder) holder).bind(mContext, article);
                break;
            case TEXT_VIEW:
                ((TextViewHolder) holder).bind(mContext, article);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (getItem(position) == null) {
            return UNKNOWN_VIEW;
        }
        Article article = (Article) getItem(position).getEntity();
        if (article.hasMultimedia()) {
            return BASIC_VIEW;
        }
        return TEXT_VIEW;
    }
}
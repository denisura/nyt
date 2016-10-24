package com.github.denisura.nytseacher.ui.search.results;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.data.model.Article;
import com.github.denisura.nytseacher.data.model.Item;
import com.github.denisura.nytseacher.databinding.CardviewArticleBasicBinding;
import com.github.denisura.nytseacher.databinding.CardviewArticleTextBinding;

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

    SearchResultsAdapter() {}

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

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case BASIC_VIEW:
                CardviewArticleBasicBinding bindingBasic = DataBindingUtil
                        .inflate(layoutInflater, R.layout.cardview_article_basic, parent, false);
                return new BasicViewHolder(bindingBasic);
            case TEXT_VIEW:
                CardviewArticleTextBinding bindingText = DataBindingUtil
                        .inflate(layoutInflater, R.layout.cardview_article_text, parent, false);
                return new TextViewHolder(bindingText);
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
                ((BasicViewHolder) holder).bindArticle(article);
                break;
            case TEXT_VIEW:
                ((TextViewHolder) holder).bindArticle(article);
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
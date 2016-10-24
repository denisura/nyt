package com.github.denisura.nytseacher.ui.search.results;

import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.denisura.nytseacher.data.model.Article;
import com.github.denisura.nytseacher.databinding.CardviewArticleBasicBinding;

class BasicViewHolder extends RecyclerView.ViewHolder {

    private final CardviewArticleBasicBinding mBinding;
    private final ArticleViewModel mViewModel;

    BasicViewHolder(CardviewArticleBasicBinding binding) {
        super(binding.getRoot());
        mBinding = binding;
        mViewModel = new ArticleViewModel(binding.getRoot().getContext());
        mBinding.setViewModel(mViewModel);
    }

    public void bindArticle(Article article) {
        Glide.clear(mBinding.thumbnail);
        mBinding.headline.setText(article.getHeadline());
        mViewModel.setArticle(article);
        mBinding.executePendingBindings();
    }
}

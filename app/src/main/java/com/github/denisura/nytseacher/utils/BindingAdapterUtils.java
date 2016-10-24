package com.github.denisura.nytseacher.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


public class BindingAdapterUtils {
    @BindingAdapter({"bind:imageUrl"})
    public static void loadThumbnail(ImageView view, String url) {
        Glide.with(view.getContext())
                .load(url)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(view);
    }
}

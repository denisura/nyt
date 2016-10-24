package com.github.denisura.nytseacher.utils;

import android.content.Context;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.data.model.filter.SortOption;
import com.github.denisura.nytseacher.data.model.filter.SortOptions;

public class FormUtils {

    public static SortOptions getSortOptionsMap(Context context) {
        SortOptions map = new SortOptions();
        map.put(SortOption.SORT_RELEVANCY, context.getString(R.string.sort_option_relevancy));
        map.put(SortOption.SORT_NEWEST, context.getString(R.string.sort_option_newest));
        map.put(SortOption.SORT_OLDEST, context.getString(R.string.sort_option_oldest));
        return map;
    }
}

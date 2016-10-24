package com.github.denisura.nytseacher.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.github.denisura.nytseacher.data.model.SearchFilter;
import com.github.denisura.nytseacher.data.model.filter.SortOption;
import com.google.gson.Gson;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class StorageUtils {

    public static void saveFilterInSharedPreferences(Context context, SearchFilter filter) {
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mSettings.edit();
        Gson gson = new Gson();
        String jsonNewsDesk = gson.toJson(filter.getNewsDesk());
        editor.putString("filterNewsDesk", jsonNewsDesk);
        editor.putString("filterSort", filter.getSort());

        if (filter.getBeginDate() != null) {
            editor.putString("filterDate", JodaUtils.formatDate(filter.getBeginDate(), "yyyy-MM-dd"));
        }

        editor.commit();
    }

    public static SearchFilter getFilterFromSharedPreferences(Context context) {

        SearchFilter filter = new SearchFilter();
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = mSettings.getString("filterNewsDesk", "[]");
        filter.setSort(mSettings.getString("filterSort", SortOption.SORT_RELEVANCY));
        filter.setNewsDesk(gson.fromJson(json, ArrayList.class));

        String date = mSettings.getString("filterDate", null);
        if (date != null) {
            filter.setBeginDate(new LocalDate(date));
        }
        return filter;
    }
}

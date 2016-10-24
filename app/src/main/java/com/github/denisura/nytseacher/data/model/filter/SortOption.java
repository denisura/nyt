package com.github.denisura.nytseacher.data.model.filter;


import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SortOption {

    // Define the list of accepted constants and declare the NavigationMode annotation
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({SORT_NEWEST, SORT_OLDEST, SORT_RELEVANCY})
    public @interface SortTypes {
    }

    // Declare the constants
    public static final String SORT_NEWEST = "newest";
    public static final String SORT_OLDEST = "oldest";
    public static final String SORT_RELEVANCY = "relevancy";

    private String id;
    private String name;

    @SortTypes
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }



    public SortOption(@SortTypes String id, String name) {
        this.id = id;
        this.name = name;
    }

    //to display object as a string in spinner
    public String toString() {
        return name;
    }
}

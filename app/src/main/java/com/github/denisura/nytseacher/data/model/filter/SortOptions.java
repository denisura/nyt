package com.github.denisura.nytseacher.data.model.filter;


import java.util.LinkedHashMap;

public class SortOptions extends LinkedHashMap<String, SortOption> {

    public SortOption put(@SortOption.SortTypes String key, String value) {
        return super.put(key, new SortOption(key, value));
    }
}

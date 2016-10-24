package com.github.denisura.nytseacher.ui.filter;


import com.github.denisura.nytseacher.data.model.SearchFilter;

public interface FilterListener {
    void onFilterAction(SearchFilter filter);
}

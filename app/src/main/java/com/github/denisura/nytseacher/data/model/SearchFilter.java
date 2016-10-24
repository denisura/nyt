package com.github.denisura.nytseacher.data.model;

import com.github.denisura.nytseacher.data.model.filter.SortOption;

import org.joda.time.LocalDate;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class SearchFilter {
    LocalDate mBeginDate;
    String mSort = SortOption.SORT_RELEVANCY;
    ArrayList mNewsDesk = new ArrayList();

    public SearchFilter() {
    }

    public LocalDate getBeginDate() {
        return mBeginDate;
    }

    public void setBeginDate(LocalDate beginDate) {
        mBeginDate = beginDate;
    }

    @SortOption.SortTypes
    public String getSort() {
        return mSort;
    }

    public void setSort(@SortOption.SortTypes String sort) {
        mSort = sort;
    }

    public ArrayList getNewsDesk() {
        return mNewsDesk;
    }

    public void setNewsDesk(ArrayList newsDesk) {
        mNewsDesk = newsDesk;
    }
}

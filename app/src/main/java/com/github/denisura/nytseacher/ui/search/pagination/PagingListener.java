package com.github.denisura.nytseacher.ui.search.pagination;

import rx.Observable;

public interface PagingListener<T> {
    Observable<T> onNextPage(int offset);
}
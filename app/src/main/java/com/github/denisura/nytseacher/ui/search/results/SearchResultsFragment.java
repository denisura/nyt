package com.github.denisura.nytseacher.ui.search.results;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.data.model.Article;
import com.github.denisura.nytseacher.data.model.ArticleSearchResponse;
import com.github.denisura.nytseacher.data.model.Item;
import com.github.denisura.nytseacher.data.model.SearchFilter;
import com.github.denisura.nytseacher.data.network.ArticleSearchOptions;
import com.github.denisura.nytseacher.data.network.NewYorkTimesApi;
import com.github.denisura.nytseacher.data.network.NewYorkTimesService;
import com.github.denisura.nytseacher.ui.search.pagination.PaginationTool;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public class SearchResultsFragment extends Fragment {

    // the fragment initialization parameters
    private static final String ARG_QUERY = "query";
    private static final String ARG_FILTER = "filter";
    public RecyclerView mRecyclerView;
    private SearchResultsAdapter recyclerViewAdapter;
    private Subscription pagingSubscription;
    private NewYorkTimesApi _apiService;
    private String mQuery = "";
    private SearchFilter mFilter = new SearchFilter();

    public SearchResultsFragment() {
    }

    public static SearchResultsFragment newInstance(String query, SearchFilter filter) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUERY, query);
        args.putSerializable(ARG_FILTER, filter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_QUERY)) {
            mQuery = getArguments().getString(ARG_QUERY);
        }
        if (getArguments().containsKey(ARG_FILTER)) {
            mFilter = (SearchFilter) getArguments().getSerializable(ARG_FILTER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        setRetainInstance(true);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        init(savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        StaggeredGridLayoutManager recyclerViewLayoutManager =
                new StaggeredGridLayoutManager(
                        getContext().getResources().getInteger(R.integer.search_results_columns),
                        StaggeredGridLayoutManager.VERTICAL);

        // init adapter for the first time
        if (savedInstanceState == null) {
            recyclerViewAdapter = new SearchResultsAdapter();
            recyclerViewAdapter.setHasStableIds(true);
        }
        recyclerViewAdapter.setContext(getContext().getApplicationContext());
        mRecyclerView.setSaveEnabled(true);

        mRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        // if all items was loaded we don't need Pagination
        if (recyclerViewAdapter.isAllItemsLoaded()) {
            Timber.d("init: All Items Loaded");
            return;
        }
        _apiService = NewYorkTimesService.newService();

        ArticleSearchOptions.Builder builder = new ArticleSearchOptions.Builder(mQuery);
        builder.filter(mFilter);
        ArticleSearchOptions options = builder.build();
        Timber.d(options.toString());

        // RecyclerView pagination
        PaginationTool<ArticleSearchResponse> paginationTool =
                PaginationTool
                        .buildPagingObservable(mRecyclerView, offset -> {
                            builder.page(offset / 10);
                            return _apiService.articleSearch(builder.build());
                        })
                        .build();

        pagingSubscription = paginationTool
                .getPagingObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArticleSearchResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ArticleSearchResponse articleSearchResponse) {
                        int index = recyclerViewAdapter.getItemCount();
                        List<Item> items = new ArrayList<>();

                        List<Article> articles = articleSearchResponse.getResponse().getArticles();
                        for (Object entity : articles) {
                            items.add(new Item(index, entity));
                            index++;
                        }
                        recyclerViewAdapter.addNewItems(items);
                        recyclerViewAdapter.notifyItemInserted(recyclerViewAdapter.getItemCount() - articles.size());
                    }
                });
    }

    @Override
    public void onDestroyView() {
        if (pagingSubscription != null && !pagingSubscription.isUnsubscribed()) {
            pagingSubscription.unsubscribe();
        }
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(null);
        }
        recyclerViewAdapter.setContext(null);
        super.onDestroyView();
    }
}

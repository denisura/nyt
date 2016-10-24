package com.github.denisura.nytseacher.ui.search.results;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.data.model.Article;
import com.github.denisura.nytseacher.data.model.ArticleSearchResponse;
import com.github.denisura.nytseacher.data.model.Item;
import com.github.denisura.nytseacher.data.model.SearchFilter;
import com.github.denisura.nytseacher.data.network.ArticleSearchOptions;
import com.github.denisura.nytseacher.data.network.NewYorkTimesApi;
import com.github.denisura.nytseacher.data.network.NewYorkTimesService;
import com.github.denisura.nytseacher.databinding.WidgetRecyclerViewBinding;
import com.github.denisura.nytseacher.ui.search.pagination.PaginationTool;
import com.github.denisura.nytseacher.utils.NetworkUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static java.security.AccessController.getContext;

public class SearchResultsFragment extends Fragment {

    // the fragment initialization parameters
    private static final String ARG_QUERY = "query";
    private static final String ARG_FILTER = "filter";


    private WidgetRecyclerViewBinding mBinding;
    private SearchResultsViewModel mViewModel;

    public RecyclerView mRecyclerView;
    public TextView mEmptyRecycleView;

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
        args.putParcelable(ARG_FILTER, Parcels.wrap(filter));
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
            mFilter = Parcels.unwrap(getArguments().getParcelable(ARG_FILTER));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.widget_recycler_view, container, false);
        setRetainInstance(true);

        View view = mBinding.getRoot();
        //here data must be an instance of the class MarsDataProvider
        mViewModel = new SearchResultsViewModel(getContext());
        mBinding.setViewModel(mViewModel);

        mRecyclerView = mBinding.recyclerView;
        mEmptyRecycleView = mBinding.recyclerviewEmpty;

        init(savedInstanceState);

        return view;
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
                            if (!NetworkUtils.isNetworkAvailable(getContext())) {
                                Snackbar snackbar = Snackbar
                                        .make(mRecyclerView, getContext().getString(R.string.empty_search_results_no_network), Snackbar.LENGTH_LONG);
                                snackbar.show();
                                return null;
                            }

                            if (!NetworkUtils.isOnline()) {
                                Snackbar snackbar = Snackbar
                                        .make(mRecyclerView, getContext().getString(R.string.empty_search_results_offline), Snackbar.LENGTH_LONG);
                                snackbar.show();
                                return null;
                            }
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

                        if (recyclerViewAdapter.getItemCount() > 0) {
                            mEmptyRecycleView.setText("");
                        } else {
                            mEmptyRecycleView.setText(getContext().getString(R.string.empty_search_results_not_found));
                        }
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
        super.onDestroyView();
    }
}

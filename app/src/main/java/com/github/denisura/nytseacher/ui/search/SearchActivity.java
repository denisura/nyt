package com.github.denisura.nytseacher.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.data.model.SearchFilter;
import com.github.denisura.nytseacher.ui.SingleFragmentActivity;
import com.github.denisura.nytseacher.ui.filter.FilterFragment;
import com.github.denisura.nytseacher.ui.filter.FilterListener;
import com.github.denisura.nytseacher.ui.search.results.SearchResultsFragment;
import com.github.denisura.nytseacher.utils.StorageUtils;

import org.parceler.Parcels;

import timber.log.Timber;


public class SearchActivity extends SingleFragmentActivity implements FilterListener {

    static final String STATE_QUERY = "query";
    static final String STATE_FILTER = "filter";

    String mQuery = "";
    SearchFilter mSearchFilter;

    @Override
    protected Fragment createFragment() {
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            mQuery = getIntent().getStringExtra(SearchManager.QUERY);
            Timber.d("Query %s", mQuery);
        }
        return SearchResultsFragment.newInstance(mQuery, mSearchFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate");
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mQuery = savedInstanceState.getString(STATE_QUERY);
            mSearchFilter = Parcels.unwrap(savedInstanceState.getParcelable(STATE_FILTER));
        } else {
            //TODO pull search filter form Shared preferences
            mSearchFilter = StorageUtils.getFilterFromSharedPreferences(this);
            if (mSearchFilter==null){
                mSearchFilter = new SearchFilter();
            }
            Timber.d("Filter sort %s", mSearchFilter.getSort());
        }
        super.onCreate(savedInstanceState);
        if (!mQuery.equals("")) {
            getSupportActionBar().setTitle(mQuery);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.search_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_filter:
                FragmentManager fm = getSupportFragmentManager();
                FilterFragment filterFragment = FilterFragment.newInstance(
                        getResources().getString(R.string.dialog_title_advance_search),
                        mSearchFilter);
                filterFragment.show(fm, "dialog_filter");
                filterFragment.setListener(this);
                break;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_QUERY, mQuery);
        savedInstanceState.putParcelable(STATE_FILTER, Parcels.wrap(mSearchFilter));
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onFilterAction(SearchFilter filter) {
        mSearchFilter = filter;


        StorageUtils.saveFilterInSharedPreferences(this, filter);

        FragmentManager fm = getSupportFragmentManager();
        mActivityFragment = createFragment();
        fm.beginTransaction()
                .replace(R.id.fragment_container, mActivityFragment)
                .commit();
    }
}

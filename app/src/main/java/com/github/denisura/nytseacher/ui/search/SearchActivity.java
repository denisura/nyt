package com.github.denisura.nytseacher.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.ui.SingleFragmentActivity;
import com.github.denisura.nytseacher.ui.search.results.SearchResultsFragment;

import timber.log.Timber;


public class SearchActivity extends SingleFragmentActivity {

    static final String STATE_QUERY = "query";

    String mQuery = "";

    @Override
    protected Fragment createFragment() {
        if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
            mQuery = getIntent().getStringExtra(SearchManager.QUERY);
            Timber.d("Query %s", mQuery);
        }
        return SearchResultsFragment.newInstance(mQuery);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d("onCreate");
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            mQuery = savedInstanceState.getString(STATE_QUERY);
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_QUERY, mQuery);
        super.onSaveInstanceState(savedInstanceState);
    }
}

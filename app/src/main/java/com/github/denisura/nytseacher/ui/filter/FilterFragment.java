package com.github.denisura.nytseacher.ui.filter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.data.model.SearchFilter;
import com.github.denisura.nytseacher.data.model.filter.SortOption;
import com.github.denisura.nytseacher.data.model.filter.SortOptions;
import com.github.denisura.nytseacher.utils.FormUtils;
import com.github.denisura.nytseacher.utils.JodaUtils;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.joda.time.LocalDate;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class FilterFragment extends DialogFragment {

    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_NEWS_DECK = "DialogNewsDeck";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_NEWS_DESK = 1;

    private static final String ARG_FILTER = "options";
    private static final String ARG_TITLE = "title";


    public SearchFilter mSearchFilter;
    public TextView mBeginDate;
    public MaterialBetterSpinner mSort;
    public TextView mNewsDeck;
    private FilterListener mCallbacks;

    public FilterFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FilterFragment newInstance(String title, SearchFilter filter) {
        FilterFragment frag = new FilterFragment();
        Bundle args = new Bundle();
        //TODO replace putParceble
        args.putParcelable(ARG_FILTER, Parcels.wrap(filter));
        args.putString(ARG_TITLE, title);
        frag.setArguments(args);
        return frag;
    }

    public void setListener(FilterListener listener) {
        mCallbacks = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter, null);

        mBeginDate = (TextView) v.findViewById(R.id.beginDate);
        mSort = (MaterialBetterSpinner) v.findViewById(R.id.sortspinner);
        mNewsDeck = (TextView) v.findViewById(R.id.news_desk);

        if (getArguments().containsKey(ARG_FILTER)) {
            mSearchFilter = Parcels.unwrap(getArguments().getParcelable(ARG_FILTER));
            if (mSearchFilter != null) {
                if (mSearchFilter.getBeginDate() != null) {
                    mBeginDate.setText(JodaUtils.formatLocalDate(mSearchFilter.getBeginDate()));
                }
            }
        }

        mNewsDeck.setText(android.text.TextUtils.join(", ", mSearchFilter.getNewsDesk()));

        mNewsDeck.setOnClickListener(view -> {
            NewsDeckPickerFragment dialog = NewsDeckPickerFragment.newInstance(
                    getContext().getResources().getString(R.string.dialog_title_news_desk),
                    mSearchFilter.getNewsDesk());
            dialog.setTargetFragment(FilterFragment.this, REQUEST_NEWS_DESK);
            dialog.show(getFragmentManager(), DIALOG_NEWS_DECK);
        });

        mBeginDate.setOnClickListener(view -> {
            DatePickerFragment dialog = DatePickerFragment.newInstance(
                    getContext().getResources().getString(R.string.dialog_title_begin_date),
                    mSearchFilter.getBeginDate());
            dialog.setTargetFragment(FilterFragment.this, REQUEST_DATE);
            dialog.show(getFragmentManager(), DIALOG_DATE);
        });

        SortOptions sortOptions = FormUtils.getSortOptionsMap(getContext());
        ArrayAdapter<SortOption> arrayAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line,
                Collections.list(Collections.enumeration(sortOptions.values())));
        mSort.setAdapter(arrayAdapter);
        mSort.setText(sortOptions.get(mSearchFilter.getSort()).getName());
        mSort.setOnItemClickListener((adapterView, view, i, l) -> mSearchFilter.setSort(arrayAdapter.getItem(i).getId()));


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getString(ARG_TITLE))
                .setView(v)
                .setPositiveButton(getContext().getResources().getString(R.string.dialog_action_filter),
                        (dialog, whichButton) -> mCallbacks.onFilterAction(mSearchFilter)
                )
                .setNegativeButton(
                        getContext().getResources().getString(R.string.dialog_action_back),
                        (dialog, whichButton) -> {
                        }
                );

        return dialogBuilder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_DATE:
                LocalDate date = (LocalDate) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                mSearchFilter.setBeginDate(date);
                mBeginDate.setText(JodaUtils.formatLocalDate(date));

                break;
            case REQUEST_NEWS_DESK:
                ArrayList newsDeckItems = (ArrayList) data.getSerializableExtra(NewsDeckPickerFragment.EXTRA_SELECTED);
                mSearchFilter.setNewsDesk(newsDeckItems);
                mNewsDeck.setText(android.text.TextUtils.join(", ", mSearchFilter.getNewsDesk()));
                break;
        }
    }
}
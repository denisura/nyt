package com.github.denisura.nytseacher.ui.filter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.data.model.filter.NewsDeckCursor;

import java.util.ArrayList;

import timber.log.Timber;

public class NewsDeckPickerFragment extends DialogFragment {

    public static final String EXTRA_SELECTED = "SELECTED";
    private static final String ARG_SELECTED = "selected";
    private static final String ARG_TITLE = "title";
    ArrayList mItemsSelected = new ArrayList();

    public static NewsDeckPickerFragment newInstance(String title, ArrayList itemsSelected) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TITLE, title);
        args.putSerializable(ARG_SELECTED, itemsSelected);
        NewsDeckPickerFragment fragment = new NewsDeckPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mItemsSelected = (ArrayList) getArguments().getSerializable(ARG_SELECTED);
        if (mItemsSelected == null) {
            mItemsSelected = new ArrayList();
        }

        String title = getArguments().getString(ARG_TITLE);
        NewsDeckCursor cursor = new NewsDeckCursor(
                getContext().getResources().getStringArray(R.array.string_array_news_deck),
                mItemsSelected
        );

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMultiChoiceItems(cursor, NewsDeckCursor.COLUMN_SELECTED, NewsDeckCursor.COLUMN_VALUE,
                        (dialog, item, isChecked) -> {
                            cursor.moveToPosition(item);
                            String selectedItem = cursor.getString(cursor.getColumnIndex(NewsDeckCursor.COLUMN_VALUE));
                            Timber.d("onCLick %d %s", item, selectedItem);
                            if (isChecked) {
                                mItemsSelected.add(selectedItem);
                            } else if (mItemsSelected.contains(selectedItem)) {
                                mItemsSelected.remove(selectedItem);
                            }
                        });
        builder.setPositiveButton(
                getContext().getResources().getString(R.string.dialog_action_save)
                , (dialog, which) -> {
                    sendResult(Activity.RESULT_OK, mItemsSelected);
                    dialog.dismiss();
                });

        return builder.create();
    }

    private void sendResult(int resultCode, ArrayList itemsSelected) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED, itemsSelected);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

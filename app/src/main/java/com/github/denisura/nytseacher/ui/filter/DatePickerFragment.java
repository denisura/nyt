package com.github.denisura.nytseacher.ui.filter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.github.denisura.nytseacher.R;
import com.github.denisura.nytseacher.databinding.DialogDateBinding;

import org.joda.time.LocalDate;

import java.util.Date;
import java.util.GregorianCalendar;


public class DatePickerFragment extends DialogFragment {

    public static final String EXTRA_DATE = "DATE";
    private static final String ARG_DATE = "date";
    private static final String ARG_TITLE = "title";
    public DatePicker mDatePicker;

    private DialogDateBinding mBinding;


    public static DatePickerFragment newInstance(String title, LocalDate date) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putSerializable(ARG_DATE, date);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LocalDate date = (LocalDate) getArguments().getSerializable(ARG_DATE);
        String title = getArguments().getString(ARG_TITLE);

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_date, null, false);
        mDatePicker = mBinding.dialogDateDatePicker;
        View view = mBinding.getRoot();
        if (date == null) {
            date = new LocalDate();
        }
        int year = date.getYear();
        int month = date.getMonthOfYear() - 1;
        int day = date.getDayOfMonth();

        mDatePicker.init(year, month, day, null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(title)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                    int year1 = mDatePicker.getYear();
                    int month1 = mDatePicker.getMonth();
                    int day1 = mDatePicker.getDayOfMonth();
                    Date date1 = new GregorianCalendar(year1, month1, day1).getTime();
                    LocalDate localDate = LocalDate.fromDateFields(date1);
                    sendResult(Activity.RESULT_OK, localDate);
                })
                .create();
    }


    private void sendResult(int resultCode, LocalDate date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

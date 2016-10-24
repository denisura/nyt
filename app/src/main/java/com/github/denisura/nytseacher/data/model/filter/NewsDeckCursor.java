package com.github.denisura.nytseacher.data.model.filter;

import android.database.MatrixCursor;

import java.util.ArrayList;


public class NewsDeckCursor extends MatrixCursor {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VALUE = "value";
    public static final String COLUMN_SELECTED = "selected";

    public NewsDeckCursor(String[] items, ArrayList itemsSelected) {
        super(new String[]{COLUMN_ID, COLUMN_VALUE, COLUMN_SELECTED});
        for (String item : items) {
            addRow(new Object[]{getCount(), item, (itemsSelected.contains(item) ? 1 : 0)});
        }
    }
}
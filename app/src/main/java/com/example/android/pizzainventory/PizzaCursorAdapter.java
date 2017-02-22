package com.example.android.pizzainventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;


public class PizzaCursorAdapter extends CursorAdapter {
    public PizzaCursorAdapter(Context c, Cursor cursor) {
        super(c, cursor, 0);
    }
    @Override
    public View newView(Context c, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(c).inflate(R.layout.list_item, parent, false);
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /* Working on this part */
    }
}

package com.example.android.pizzainventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


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
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView quantity = (TextView) view.findViewById(R.id.quantity);
        TextView price = (TextView) view.findViewById(R.id.price);
        String nameString = cursor.getString(cursor.getColumnIndex("Name"));
        int quantity_int = cursor.getInt(cursor.getColumnIndex("Quantity"));
        double price_double = cursor.getInt(cursor.getColumnIndex("Price"));
        name.setText(nameString);
        quantity.setText(String.valueOf(quantity_int));
        price.setText(String.valueOf(price_double));
    }
}

package com.example.android.pizzainventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pizzainventory.data.PizzaContract;


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
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        int nameColumnIndex = cursor.getColumnIndex(PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE);
        String productName = cursor.getString(nameColumnIndex);
        String quantity = cursor.getString(quantityColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        nameTextView.setText(productName);
        quantityTextView.setText(quantity);
        priceTextView.setText(price);
    }
}

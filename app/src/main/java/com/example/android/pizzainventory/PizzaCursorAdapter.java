package com.example.android.pizzainventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pizzainventory.data.PizzaContract;


class PizzaCursorAdapter extends CursorAdapter {
    PizzaCursorAdapter(Context c, Cursor cursor) {
        super(c, cursor, 0);
    }
    @Override
    public View newView(Context c, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(c).inflate(R.layout.list_item, parent, false);
    }
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        Button b = (Button) view.findViewById(R.id.instant_sell);
        TextView name = (TextView) view.findViewById(R.id.name);
        final TextView quantity = (TextView) view.findViewById(R.id.quantity);
        TextView price = (TextView) view.findViewById(R.id.price);
        final TextView sales = (TextView) view.findViewById(R.id.sales);
        final String nameString = cursor.getString(cursor.getColumnIndex(context.getString(R.string.name_column_index)));
        final int quantity_int = cursor.getInt(cursor.getColumnIndex(context.getString(R.string.quantity_column_index)));
        final double price_double = cursor.getInt(cursor.getColumnIndex(context.getString(R.string.price_column_index)));
        final int sales_int = cursor.getInt(cursor.getColumnIndex(context.getString(R.string.sales_column_index)));
        final String photoString = cursor.getString(cursor.getColumnIndex("Photo"));
        final Uri productUri = ContentUris.withAppendedId(PizzaContract.PizzaEntry.CONTENT_URI, Long.parseLong(cursor.getString(cursor.getColumnIndex(PizzaContract.PizzaEntry._ID))));
        name.setText("Name: " + nameString);
        quantity.setText(String.valueOf(quantity_int));
        price.setText("Price: " + String.valueOf(price_double));
        sales.setText(String.valueOf(sales_int));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityNum = 0;
                if(!TextUtils.isEmpty(quantity.getText().toString())) {
                    quantityNum = Integer.parseInt(quantity.getText().toString());
                }
                int salesNum = 0;
                if(!TextUtils.isEmpty(sales.getText().toString())) {
                    salesNum = Integer.parseInt(sales.getText().toString());
                }
                if(quantityNum > 0) {
                    quantityNum--;
                    salesNum++;
                    sales.setText(String.valueOf(salesNum));
                    sales.setText(String.valueOf(quantityNum));
                    ContentValues cv = new ContentValues();
                    cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME, nameString);
                    cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PHOTO, photoString);
                    cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE, price_double);
                    cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY, quantityNum);
                    cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_SALES, salesNum);
                    int rows = context.getContentResolver().update(productUri, cv, null, null);
                    if(rows == 0) {
                        Toast.makeText(v.getContext(), "Error occurred", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}

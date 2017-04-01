package com.example.android.pizzainventory;

import android.support.v4.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pizzainventory.data.PizzaContract;

public class PizzaDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri photoUri;
    Uri productUri = getIntent().getData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_detail);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        if(c.moveToFirst()) {
            int nameColumnIndex = c.getColumnIndex(PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = c.getColumnIndex(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = c.getColumnIndex(PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY);
            int photoColumnIndex = c.getColumnIndex(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PHOTO);
            int salesColumnIndex = c.getColumnIndex(PizzaContract.PizzaEntry.COLUMN_PRODUCT_SALES);
            String nameString = c.getString(nameColumnIndex);
            int quantityNum = c.getInt(quantityColumnIndex);
            float priceNum = c.getFloat(priceColumnIndex);
            String photoString = c.getString(photoColumnIndex);
            int salesNum = c.getInt(salesColumnIndex);
            TextView name = (TextView) findViewById(R.id.detail_name);
            TextView quantity = (TextView) findViewById(R.id.detail_quantity);
            TextView price = (TextView) findViewById(R.id.detail_price);
            ImageView photo = (ImageView) findViewById(R.id.imageView);
            TextView sales = (TextView) findViewById(R.id.detail_sold);
            name.setText(nameString);
            quantity.setText(Integer.toString(quantityNum));
            price.setText(Float.toString(priceNum));
            sales.setText(Integer.toString(salesNum));
            if(!photoString.isEmpty()) {
                photoUri = Uri.parse(photoString);
                photo.setImageURI(photoUri);
            }
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle args) {
        String[] projection = {
                PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME,
                PizzaContract.PizzaEntry.COLUMN_PRODUCT_PHOTO,
                PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE,
                PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY,
                PizzaContract.PizzaEntry.COLUMN_PRODUCT_SALES
        };
        return new CursorLoader(this,
                productUri,
                projection,
                null,
                null,
                null);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

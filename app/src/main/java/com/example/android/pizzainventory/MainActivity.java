package com.example.android.pizzainventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.pizzainventory.data.PizzaContract;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(i);
            }
        });
        ListView l = (ListView) findViewById(R.id.list_view);
        PizzaCursorAdapter p = new PizzaCursorAdapter(this, null);
        l.setAdapter(p);
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent productDetail = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentPizza = ContentUris.withAppendedId(PizzaContract.PizzaEntry.CONTENT_URI, l);
                productDetail.setData(currentPizza);
                startActivity(productDetail);
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                PizzaContract.PizzaEntry._ID,
                PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME,
                PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE,
                PizzaContract.PizzaEntry.COLUMN_PRODUCT_SALES,
                PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY,
                PizzaContract.PizzaEntry.COLUMN_PRODUCT_PHOTO
        };
        return new CursorLoader(this, PizzaContract.PizzaEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

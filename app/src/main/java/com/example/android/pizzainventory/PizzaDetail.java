package com.example.android.pizzainventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pizzainventory.data.PizzaContract;

public class PizzaDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri photoUri;
    Uri productUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_detail);
        productUri = getIntent().getData();
        getSupportLoaderManager().initLoader(0, null, this);
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
            name.setText(" " + nameString);
            quantity.setText(" " + Integer.toString(quantityNum));
            price.setText(" " + Float.toString(priceNum));
            String salesString = Integer.toString(salesNum);
            Log.e("PizzaDetail", "salesString: " + salesString);
            sales.setText(" " + Integer.toString(salesNum));
            Log.e("PizzaDetail", "photo Uri: " + photoString);
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
    public void orderProduct(View v) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_more_string));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    public void showDeleteConfirmationDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteProduct() {
        if (productUri != null) {
            int rowsDeleted = getContentResolver().delete(productUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, R.string.failed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.sucessful, Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
    public void shipmentReceived(View v) {
        int quantity = 0;
        TextView quantityTextView = (TextView) findViewById(R.id.detail_quantity);
        String quantityString = quantityTextView.getText().toString().trim();
        if(!quantityString.isEmpty()) {
            quantity = Integer.parseInt(quantityString);
        }
        quantity++;
        quantityTextView.setText(String.valueOf(quantity));
    }
    public void soldProduct(View v) {
        int quantity = 0;
        int sold = 0;
        TextView quantityTextView = (TextView) findViewById(R.id.detail_quantity);
        TextView soldTextView = (TextView) findViewById(R.id.detail_sold);
        String quantityString = quantityTextView.getText().toString().trim();
        String soldString = soldTextView.getText().toString().trim();
        if(!quantityString.isEmpty()) {
            quantity = Integer.parseInt(quantityString);
        }
        if(!soldString.isEmpty()) {
            sold = Integer.parseInt(soldString);
        }
        if(quantity == 0) {
            Toast.makeText(this, R.string.no_products, Toast.LENGTH_LONG).show();
            return;
        }
        quantity--;
        sold++;
        quantityTextView.setText(String.valueOf(quantity));
        soldTextView.setText(String.valueOf(sold));
    }
}

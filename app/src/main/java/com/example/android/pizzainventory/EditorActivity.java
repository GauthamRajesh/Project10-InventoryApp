package com.example.android.pizzainventory;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.pizzainventory.data.PizzaContract;

import java.io.File;


public class EditorActivity extends AppCompatActivity {
    public static Uri picUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
    }
    public void getPic(View v) {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 0);
    }
    public void takePic(View v) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri("product.jpg"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1);
        }
    }
    public Uri getPhotoFileUri(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PizzaInventory");
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d("PizzaInventory", "failed to create directory");
            }
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                picUri = getPhotoFileUri("product.jpg");
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == 0 && resultCode == RESULT_OK && data != null) {
            picUri = data.getData();
        }
    }
    public void saveProduct(View v) {
        EditText name = (EditText) findViewById(R.id.name);
        EditText quantity = (EditText) findViewById(R.id.quantity);
        EditText price = (EditText) findViewById(R.id.price);
        EditText sales = (EditText) findViewById(R.id.sales);
        String nameString = name.getText().toString().trim();
        String quantityString = quantity.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        String salesString = sales.getText().toString().trim();
        String photoString = picUri.toString();
        if (nameString == null || nameString.equals("")) {
            throw new IllegalArgumentException("Name required");
        }
        if (priceString == null || priceString.equals("")) {
            throw new IllegalArgumentException("Valid price required");
        }
        double priceNumber = Double.parseDouble(priceString);
        if (quantityString == null || quantityString.equals("")) {
            throw new IllegalArgumentException("Valid quantity required");
        }
        int quantityNumber = Integer.parseInt(quantityString);
        if (salesString == null || salesString.equals("")) {
            throw new IllegalArgumentException("Valid sales required");
        }
        int salesNumber = Integer.parseInt(salesString);
        if(photoString == null || photoString.equals("")) {
            throw new IllegalArgumentException("Valid photo required");
        }
        ContentValues cv = new ContentValues();
        cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME, nameString);
        cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY, quantityNumber);
        cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE, priceNumber);
        cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_SALES, salesNumber);
        cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PHOTO, photoString);
        Uri uri = getContentResolver().insert(PizzaContract.PizzaEntry.CONTENT_URI, cv);
        if(uri == null) {
            Log.e("EditorActivity", "Error inserting product");
        }
        else {
            Log.e("EditorActivity", "Inserted product");
        }
        finish();
    }
}



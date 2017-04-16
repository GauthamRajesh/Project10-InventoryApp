package com.example.android.pizzainventory;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.pizzainventory.data.PizzaContract;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class EditorActivity extends AppCompatActivity {
    public static final String LOG_TAG = "EditorActivity";
    private Uri mUri;
    private ImageView mButtonTakePicture;
    boolean isGalleryPicture = false;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.pizza";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mButtonTakePicture = (ImageView) findViewById(R.id.camerapic);
        mButtonTakePicture.setEnabled(false);
        requestPermissions();
    }
    public void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        2);
            }
        } else {
            mButtonTakePicture.setEnabled(true);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    mButtonTakePicture.setEnabled(true);
                }
            }
        }
    }
    public void getPic(View v) {
        Intent intent;
        Log.e(LOG_TAG, "While is set and the ifs are worked through.");

        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        Log.e(LOG_TAG, "Check write to external permissions");

        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 0);
    }
    public void takePic(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            File f = createImageFile();
            Log.d(LOG_TAG, "File: " + f.getAbsolutePath());
            mUri = FileProvider.getUriForFile(
                    this, FILE_PROVIDER_AUTHORITY, f);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, mUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        Log.i(LOG_TAG, "Received an \"Activity Result\"");
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code READ_REQUEST_CODE.
        // If the request code seen here doesn't match, it's the response to some other intent,
        // and the below code shouldn't run at all.

        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.  Pull that uri using "resultData.getData()"

            if (resultData != null) {
                mUri = resultData.getData();
                Log.i(LOG_TAG, "Uri: " + mUri.toString());
                isGalleryPicture = true;
            }
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Log.i(LOG_TAG, "Uri: " + mUri.toString());
            isGalleryPicture = false;
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
        String photoString = "";
        if(mUri != null) {
            if(!TextUtils.isEmpty(mUri.toString())) {
                photoString = mUri.toString();
            }
        }
        if (TextUtils.isEmpty(nameString)) {
            Toast.makeText(this, "Need a name", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, "Need a price", Toast.LENGTH_LONG).show();
            return;
        }
        double priceNumber = Double.parseDouble(priceString);
        if(priceNumber < 0) {
            Toast.makeText(this, "Need a positive price", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(quantityString)) {
            Toast.makeText(this, "Need a quantity", Toast.LENGTH_LONG).show();
            return;
        }
        int quantityNumber = Integer.parseInt(quantityString);
        if(quantityNumber < 0) {
            Toast.makeText(this, "Need a positive quantity", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(salesString)) {
            Toast.makeText(this, "Need sales", Toast.LENGTH_LONG).show();
            return;
        }
        int salesNumber = Integer.parseInt(salesString);
        if(salesNumber < 0) {
            Toast.makeText(this, "Need positive sales", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(photoString)) {
            Toast.makeText(this, "Need a photo", Toast.LENGTH_LONG).show();
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME, nameString);
        cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY, quantityNumber);
        cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE, priceNumber);
        cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_SALES, salesNumber);
        cv.put(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PHOTO, photoString);
        Uri uri = getContentResolver().insert(PizzaContract.PizzaEntry.CONTENT_URI, cv);
        if(uri == null) {
            Log.e(getString(R.string.editor_activity), getString(R.string.inserting_product_error));
        }
        else {
            Log.e(getString(R.string.editor_activity), getString(R.string.insert_product));
        }
        finish();
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File albumF = getAlbumDir();
        return File.createTempFile(imageFileName, ".jpg", albumF);
    }
    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = new File(Environment.getExternalStorageDirectory()
                    + "/dcim/"
                    + getString(R.string.app_name));

            Log.d(LOG_TAG, "Dir: " + storageDir);

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d(LOG_TAG, "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }
}



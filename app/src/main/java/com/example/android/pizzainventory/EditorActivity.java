package com.example.android.pizzainventory;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class EditorActivity extends AppCompatActivity {
    Uri galleryUri;
    Uri cameraUri;
    FileOutputStream fos;
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
                cameraUri = getPhotoFileUri("product.jpg");
                Bitmap takenImage = BitmapFactory.decodeFile(cameraUri.getPath());
                File f = new File(getApplicationContext().getCacheDir(), "product.jpg");
                try {
                    boolean b = f.createNewFile();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(takenImage, 2000, 2000, false);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                byte[] bitmapdata = bos.toByteArray();
                try {
                    fos = new FileOutputStream(f);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                }
                catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                ImageView ivPreview = (ImageView) findViewById(R.id.camerapic);
                ivPreview.setImageURI(Uri.fromFile(f));
            } else {
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == 0 && resultCode == RESULT_OK && data != null) {
            galleryUri = data.getData();
            ImageView camera = (ImageView) findViewById(R.id.camerapic);
            camera.setImageURI(galleryUri);
        }
    }
}



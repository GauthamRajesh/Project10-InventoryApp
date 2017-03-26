package com.example.android.pizzainventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

public class PizzaProvider extends ContentProvider {
    private static final int ALL_PIZZAS = 50;
    private static final int SPECIFIC_PIZZA = 51;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUriMatcher.addURI(PizzaContract.CONTENT_AUTHORITY, PizzaContract.PATH_PIZZAS,  ALL_PIZZAS);
        sUriMatcher.addURI(PizzaContract.CONTENT_AUTHORITY, PizzaContract.PATH_PIZZAS + "/#", SPECIFIC_PIZZA);
    }
    private PizzaDbHelper mDbHelper;
    @Override
    public boolean onCreate() {
        mDbHelper = new PizzaDbHelper(getContext());
        return true;
    }
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor c;
        int match = sUriMatcher.match(uri);
        switch(match) {
            case ALL_PIZZAS:
                c = db.query(PizzaContract.PizzaEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SPECIFIC_PIZZA:
                selection = PizzaContract.PizzaEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                c = db.query(PizzaContract.PizzaEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case ALL_PIZZAS:
                return PizzaContract.PizzaEntry.CONTENT_LIST_TYPE;
            case SPECIFIC_PIZZA:
                return PizzaContract.PizzaEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ALL_PIZZAS:
                return updatePizza(uri, contentValues, selection, selectionArgs);
            case SPECIFIC_PIZZA:
                selection = PizzaContract.PizzaEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePizza(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updatePizza(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }
        if (values.containsKey(PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        if (values.containsKey(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = values.getAsInteger(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Product requires valid price");
            }
        }

        if (values.containsKey(PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity == null) {
                throw new IllegalArgumentException("Product requires valid quantity");
            }
        }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int numberRowsUpdated = db.update(PizzaContract.PizzaEntry.TABLE_NAME, values, selection, selectionArgs);

        if (numberRowsUpdated == 0) {
            Log.e("PizzaProvider", "No rows updated for " + uri);
        } else {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numberRowsUpdated;
    }
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case ALL_PIZZAS:
                return insertPizza(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertPizza(Uri uri, ContentValues values) {
        String name = values.getAsString(PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME);
        Integer price = values.getAsInteger(PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE);
        Integer quantity = values.getAsInteger(PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY);
        if (name == null) {
            throw new IllegalArgumentException("Name required");
        }
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Valid price required");
        }
        if (quantity == null) {
            throw new IllegalArgumentException("Valid quantity required");
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newRowId = db.insert(PizzaContract.PizzaEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Log.e("PizzaProvider", "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, newRowId);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch(match) {
            case ALL_PIZZAS:
                rowsDeleted = db.delete(PizzaContract.PizzaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case SPECIFIC_PIZZA:
                selection = PizzaContract.PizzaEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(PizzaContract.PizzaEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }
}

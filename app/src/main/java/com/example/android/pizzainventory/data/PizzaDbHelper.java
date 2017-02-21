package com.example.android.pizzainventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PizzaDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pizza.db";
    private static final int DATABASE_VERSION = 1;
    public PizzaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PIZZA_TABLE = "CREATE TABLE " + PizzaContract.PizzaEntry.TABLE_NAME + " ("
                + PizzaContract.PizzaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PizzaContract.PizzaEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + PizzaContract.PizzaEntry.COLUMN_PRODUCT_PHOTO + " TEXT NOT NULL, "
                + PizzaContract.PizzaEntry.COLUMN_PRODUCT_PRICE + " FLOAT NOT NULL DEFAULT 0, "
                + PizzaContract.PizzaEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + PizzaContract.PizzaEntry.COLUMN_PRODUCT_SALES + " INTEGER NOT NULL DEFAULT 0";
        db.execSQL(SQL_CREATE_PIZZA_TABLE);

    }
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PizzaContract.PizzaEntry.TABLE_NAME;
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}

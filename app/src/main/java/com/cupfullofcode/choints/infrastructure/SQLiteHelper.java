package com.cupfullofcode.choints.infrastructure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "choints.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_CHILDREN = "children";
    public static final String CHILDREN_COLUMN_ID = "id";
    public static final String CHILDREN_COLUMN_NAME = "name";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CHILDREN + "(" + CHILDREN_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CHILDREN_COLUMN_NAME + " TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // @TODO Make sure you can handle upgrades when the time comes.
    }
}

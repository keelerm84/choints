package com.cupfullofcode.choints.infrastructure;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "choints.db";
    private static final int DATABASE_VERSION = 4;

    public static final String TABLE_CHILDREN = "children";
    public static final String CHILDREN_COLUMN_ID = "id";
    public static final String CHILDREN_COLUMN_NAME = "name";

    public static final String TABLE_CHORES = "chores";
    public static final String CHORES_COLUMN_ID = "id";
    public static final String CHORES_COLUMN_DESCRIPTION = "description";
    public static final String CHORES_COLUMN_POINTS = "points";
    public static final String CHORES_COLUMN_CHILD_ID = "child_id";

    public static final String TABLE_REWARDS = "rewards";
    public static final String REWARDS_COLUMN_ID = "id";
    public static final String REWARDS_COLUMN_DESCRIPTION = "description";
    public static final String REWARDS_COLUMN_POINTS = "points";
    public static final String REWARDS_COLUMN_CHILD_ID = "child_id";

    public static final String TABLE_HISTORIES = "histories";
    public static final String HISTORIES_COLUMN_ID = "id";
    public static final String HISTORIES_COLUMN_DESCRIPTION = "description";
    public static final String HISTORIES_COLUMN_POINTS = "points";
    public static final String HISTORIES_COLUMN_CHILD_ID = "child_id";
    public static final String HISTORIES_COLUMN_CREATED_AT = "created_at";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CHILDREN + "(" + CHILDREN_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CHILDREN_COLUMN_NAME + " TEXT NOT NULL);");
        db.execSQL("CREATE TABLE " + TABLE_CHORES + "(" + CHORES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CHORES_COLUMN_DESCRIPTION + " TEXT NOT NULL, " + CHORES_COLUMN_POINTS + " INTEGER NOT NULL DEFAULT 0, " + CHORES_COLUMN_CHILD_ID + " INTEGER, FOREIGN KEY (" + CHORES_COLUMN_CHILD_ID + ") REFERENCES " + TABLE_CHILDREN + "(" + CHORES_COLUMN_CHILD_ID + "));");
        db.execSQL("CREATE TABLE " + TABLE_REWARDS + "(" + REWARDS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REWARDS_COLUMN_DESCRIPTION + " TEXT NOT NULL, " + REWARDS_COLUMN_POINTS + " INTEGER NOT NULL DEFAULT 0, " + REWARDS_COLUMN_CHILD_ID + " INTEGER, FOREIGN KEY (" + REWARDS_COLUMN_CHILD_ID + ") REFERENCES " + TABLE_CHILDREN + "(" + REWARDS_COLUMN_CHILD_ID + "));");
        db.execSQL("CREATE TABLE " + TABLE_HISTORIES + "(" + HISTORIES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + HISTORIES_COLUMN_DESCRIPTION + " TEXT NOT NULL, " + HISTORIES_COLUMN_POINTS + " INTEGER NOT NULL DEFAULT 0, " + HISTORIES_COLUMN_CHILD_ID + " INTEGER, " + HISTORIES_COLUMN_CREATED_AT + " TEXT NOT NULL, FOREIGN KEY (" + HISTORIES_COLUMN_CHILD_ID + ") REFERENCES " + TABLE_CHILDREN + "(" + HISTORIES_COLUMN_CHILD_ID + "));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

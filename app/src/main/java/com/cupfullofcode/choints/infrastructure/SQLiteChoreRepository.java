package com.cupfullofcode.choints.infrastructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.Chore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteChoreRepository implements ChoreRepository {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] allColumns = {SQLiteHelper.CHORES_COLUMN_ID, SQLiteHelper.CHORES_COLUMN_DESCRIPTION, SQLiteHelper.CHORES_COLUMN_POINTS, SQLiteHelper.CHORES_COLUMN_CHILD_ID};

    public SQLiteChoreRepository(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Chore> chores(Child child) {
        List<Chore> chores = new ArrayList<Chore>();

        Cursor cursor;
        cursor = database.query(SQLiteHelper.TABLE_CHORES, allColumns, SQLiteHelper.CHORES_COLUMN_CHILD_ID + " = " + child.id(), null, null, null, SQLiteHelper.CHORES_COLUMN_DESCRIPTION, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            chores.add(cursorToChore(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return chores;
    }

    public void add(String description, long points, Child child) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.CHORES_COLUMN_DESCRIPTION, description);
        values.put(SQLiteHelper.CHORES_COLUMN_POINTS, points);
        values.put(SQLiteHelper.CHORES_COLUMN_CHILD_ID, child.id());

        long insertId = database.insert(SQLiteHelper.TABLE_CHORES, null, values);
    }

    private Chore cursorToChore(Cursor cursor) {
        return new Chore(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getLong(2),
                cursor.getLong(3)
        );
    }
}


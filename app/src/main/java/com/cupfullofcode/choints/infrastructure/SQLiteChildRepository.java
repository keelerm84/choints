package com.cupfullofcode.choints.infrastructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cupfullofcode.choints.domain.Child;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteChildRepository implements ChildRepository {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] allColumns = {SQLiteHelper.CHILDREN_COLUMN_ID, SQLiteHelper.CHILDREN_COLUMN_NAME};

    public SQLiteChildRepository(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Child> children() {
        List<Child> children = new ArrayList<Child>();

        Cursor cursor;
        cursor = database.query(SQLiteHelper.TABLE_CHILDREN, allColumns, null, null, null, null, SQLiteHelper.CHILDREN_COLUMN_NAME, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            children.add(cursorToChild(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return children;
    }

    public void add(String name) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.CHILDREN_COLUMN_NAME, name);

        long insertId = database.insert(SQLiteHelper.TABLE_CHILDREN, null, values);
    }

    private Child cursorToChild(Cursor cursor) {
        return new Child(
                cursor.getLong(0),
                cursor.getString(1)
        );
    }
}

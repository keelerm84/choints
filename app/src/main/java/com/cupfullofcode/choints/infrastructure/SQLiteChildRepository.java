package com.cupfullofcode.choints.infrastructure;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.ChildRepository;

import java.util.ArrayList;
import java.util.List;

public class SQLiteChildRepository implements ChildRepository {
    private SQLiteHelper dbHelper;

    private String[] allColumns = {SQLiteHelper.CHILDREN_COLUMN_ID, SQLiteHelper.CHILDREN_COLUMN_NAME};

    public SQLiteChildRepository(SQLiteHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<Child> children() {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<Child> children = new ArrayList<Child>();

        Cursor cursor;
        cursor = database.query(SQLiteHelper.TABLE_CHILDREN, allColumns, null, null, null, null, SQLiteHelper.CHILDREN_COLUMN_NAME, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            children.add(cursorToChild(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();

        return children;
    }

    public void add(String name) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.CHILDREN_COLUMN_NAME, name);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long insertId = database.insert(SQLiteHelper.TABLE_CHILDREN, null, values);
        database.close();
    }

    private Child cursorToChild(Cursor cursor) {
        return new Child(
                cursor.getLong(0),
                cursor.getString(1)
        );
    }
}

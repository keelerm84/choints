package com.cupfullofcode.choints.infrastructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.Chore;
import com.cupfullofcode.choints.domain.ChoreRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteChoreRepository implements ChoreRepository {
    private SQLiteHelper dbHelper;

    private String[] allColumns = {SQLiteHelper.CHORES_COLUMN_ID, SQLiteHelper.CHORES_COLUMN_DESCRIPTION, SQLiteHelper.CHORES_COLUMN_POINTS, SQLiteHelper.CHORES_COLUMN_CHILD_ID};

    public SQLiteChoreRepository(SQLiteHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<Chore> chores(Child child) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        List<Chore> chores = new ArrayList<Chore>();

        Cursor cursor;
        cursor = database.query(SQLiteHelper.TABLE_CHORES, allColumns, SQLiteHelper.CHORES_COLUMN_CHILD_ID + " = " + child.id(), null, null, null, SQLiteHelper.CHORES_COLUMN_DESCRIPTION, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            chores.add(cursorToChore(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        database.close();

        return chores;
    }

    public void add(String description, long points, Child child) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.CHORES_COLUMN_DESCRIPTION, description);
        values.put(SQLiteHelper.CHORES_COLUMN_POINTS, points);
        values.put(SQLiteHelper.CHORES_COLUMN_CHILD_ID, child.id());

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long insertId = database.insert(SQLiteHelper.TABLE_CHORES, null, values);
        database.close();
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


package com.cupfullofcode.choints.infrastructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.History;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SQLiteHistoryRepository implements HistoryRepository {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] allColumns = {SQLiteHelper.HISTORIES_COLUMN_ID, SQLiteHelper.HISTORIES_COLUMN_DESCRIPTION, SQLiteHelper.HISTORIES_COLUMN_POINTS, SQLiteHelper.HISTORIES_COLUMN_CREATED_AT, SQLiteHelper.HISTORIES_COLUMN_CHILD_ID};

    public SQLiteHistoryRepository(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<History> histories(Child child) {
        List<History> histories = new ArrayList<History>();

        Cursor cursor;
        cursor = database.query(SQLiteHelper.TABLE_HISTORIES, allColumns, SQLiteHelper.HISTORIES_COLUMN_CHILD_ID + " = " + child.id(), null, null, null, SQLiteHelper.HISTORIES_COLUMN_DESCRIPTION, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            histories.add(cursorToHistory(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return histories;
    }

    public void add(String description, long points, long childId) {
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        String date = format.format(new Date());
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.HISTORIES_COLUMN_DESCRIPTION, description);
        values.put(SQLiteHelper.HISTORIES_COLUMN_POINTS, points);
        values.put(SQLiteHelper.HISTORIES_COLUMN_CHILD_ID, childId);
        values.put(SQLiteHelper.HISTORIES_COLUMN_CREATED_AT, date);

        long insertId = database.insert(SQLiteHelper.TABLE_HISTORIES, null, values);
    }

    private History cursorToHistory(Cursor cursor) {
        DateFormat formatter = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");

        try {
            return new History(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getLong(2),
                    formatter.parse(cursor.getString(3)),
                    cursor.getLong(4)
            );
        } catch (ParseException e) {
            Log.wtf("keelerm", e.getMessage());
            return null;
        }
    }
}

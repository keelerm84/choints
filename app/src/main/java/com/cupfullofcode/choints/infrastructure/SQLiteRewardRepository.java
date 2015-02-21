package com.cupfullofcode.choints.infrastructure;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.Reward;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLiteRewardRepository implements RewardRepository {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private String[] allColumns = {SQLiteHelper.REWARDS_COLUMN_ID, SQLiteHelper.REWARDS_COLUMN_DESCRIPTION, SQLiteHelper.REWARDS_COLUMN_POINTS, SQLiteHelper.REWARDS_COLUMN_CHILD_ID};

    public SQLiteRewardRepository(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Reward> rewards(Child child) {
        List<Reward> rewards = new ArrayList<Reward>();

        Cursor cursor;
        cursor = database.query(SQLiteHelper.TABLE_REWARDS, allColumns, SQLiteHelper.REWARDS_COLUMN_CHILD_ID + " = " + child.id(), null, null, null, SQLiteHelper.REWARDS_COLUMN_DESCRIPTION, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            rewards.add(cursorToReward(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return rewards;
    }

    public void add(String description, long points, Child child) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.REWARDS_COLUMN_DESCRIPTION, description);
        values.put(SQLiteHelper.REWARDS_COLUMN_POINTS, points);
        values.put(SQLiteHelper.REWARDS_COLUMN_CHILD_ID, child.id());

        long insertId = database.insert(SQLiteHelper.TABLE_REWARDS, null, values);
    }

    private Reward cursorToReward(Cursor cursor) {
        return new Reward(
                cursor.getLong(0),
                cursor.getString(1),
                cursor.getLong(2),
                cursor.getLong(3)
        );
    }
}


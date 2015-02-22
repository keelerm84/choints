package com.cupfullofcode.choints.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class History implements Serializable {
    protected long mId;
    protected String mDescription;
    protected long mPoints;
    protected long mChildId;
    protected Date mCreatedAt;

    public History(long id, String description, long points, Date createdAt, long childId) {
        mId = id;
        mDescription = description;
        mPoints = points;
        mCreatedAt = createdAt;
        mChildId = childId;
    }

    public String description() {
        return mDescription;
    }

    public long points() {
        return mPoints;
    }

    public long childId()

    {
        return mChildId;
    }

    public Date createdAt()

    {
        return mCreatedAt;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd hh:mm:ss");
        String date = format.format(mCreatedAt);

        String type = mPoints < 0 ? "Reward" : "Chore";
        String worthOrCost = mPoints < 0 ? "costing" : "worth";
        return "[" + date + "][" + type + "] " + mDescription + " " + worthOrCost + " " + Math.abs(mPoints) + " points";
    }
}

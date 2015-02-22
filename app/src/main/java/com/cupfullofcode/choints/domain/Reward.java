package com.cupfullofcode.choints.domain;

public class Reward {
    protected long mId;
    protected String mDescription;
    protected long mPoints;
    protected long mChildId;

    public Reward(long id, String description, long points, long childId) {
        mId = id;
        mDescription = description;
        mPoints = points;
        mChildId = childId;
    }

    public long points() {
        return mPoints;
    }

    public String description() {
        return mDescription;
    }

    public String toString() {
        return mDescription + " worth " + String.valueOf(mPoints) + " pts";
    }
}

package com.cupfullofcode.choints.domain;

public class Chore {
    protected long mId;
    protected String mDescription;
    protected long mPoints;
    protected long mChildId;

    public Chore(long id, String description, long points, long childId) {
        mId = id;
        mDescription = description;
        mPoints = points;
        mChildId = childId;
    }

    public String description() {
        return mDescription;
    }

    public long points() {
        return mPoints;
    }

    public String toString() {
        return mDescription + " worth " + String.valueOf(mPoints) + " pts";
    }
}

package com.cupfullofcode.choints.domain;

public class Reward {
    protected long id;
    protected String description;
    protected long points;
    protected long childId;

    public Reward(long id, String description, long points, long childId) {
        this.id = id;
        this.description = description;
        this.points = points;
        this.childId = childId;
    }

    public String toString() {
        return description + " worth " + String.valueOf(points) + " pts";
    }
}

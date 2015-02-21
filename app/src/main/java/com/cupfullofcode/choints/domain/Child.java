package com.cupfullofcode.choints.domain;

public class Child {
    protected long id;
    protected String name;

    public Child(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String name() {
        return name;
    }

    public long id() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}

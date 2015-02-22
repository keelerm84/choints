package com.cupfullofcode.choints.domain;

import java.util.List;

public interface ChoreRepository {
    public List<Chore> chores(Child child);

    public void add(String description, long points, Child child);
}

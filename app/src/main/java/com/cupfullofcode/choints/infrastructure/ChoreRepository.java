package com.cupfullofcode.choints.infrastructure;

import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.Chore;

import java.sql.SQLException;
import java.util.List;

public interface ChoreRepository {
    public List<Chore> chores(Child child);

    public void add(String description, long points, Child child);

    public void open() throws SQLException;

    public void close();
}

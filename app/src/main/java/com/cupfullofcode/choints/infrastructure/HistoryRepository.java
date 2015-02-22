package com.cupfullofcode.choints.infrastructure;

import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.History;

import java.sql.SQLException;
import java.util.List;

public interface HistoryRepository {
    public List<History> histories(Child child);

    public void add(String description, long points, long childId);

    public void open() throws SQLException;

    public void close();
}

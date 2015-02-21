package com.cupfullofcode.choints.infrastructure;

import com.cupfullofcode.choints.domain.Child;

import java.sql.SQLException;
import java.util.List;

public interface ChildRepository {
    public List<Child> children();
    public void open() throws SQLException;
    public void close();
}

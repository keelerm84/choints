package com.cupfullofcode.choints.infrastructure;

import com.cupfullofcode.choints.domain.Child;
import com.cupfullofcode.choints.domain.Reward;

import java.sql.SQLException;
import java.util.List;

public interface RewardRepository {
    public List<Reward> rewards(Child child);

    public void add(String description, long points, Child child);

    public void open() throws SQLException;

    public void close();
}

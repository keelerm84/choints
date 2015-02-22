package com.cupfullofcode.choints.domain;

import java.util.List;

public interface HistoryRepository {
    public List<History> histories(Child child);

    public void add(String description, long points, long childId);
}

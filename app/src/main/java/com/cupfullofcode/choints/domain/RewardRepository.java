package com.cupfullofcode.choints.domain;

import java.util.List;

public interface RewardRepository {
    public List<Reward> rewards(Child child);

    public void add(String description, long points, Child child);
}

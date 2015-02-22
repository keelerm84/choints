package com.cupfullofcode.choints.domain;

import java.util.List;

public interface ChildRepository {
    public List<Child> children();

    public void add(String name);
}

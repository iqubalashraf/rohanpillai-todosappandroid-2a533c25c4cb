package com.example.todosapp.dataModel;

/**
 * Created by ashrafiqubal on 10/02/18.
 */

public class CategoryNames {
    private String name = "";
    private int count = 0, id = 0;

    public CategoryNames() {
    }

    public CategoryNames(String name, int count, int id) {
        this.name = name;
        this.count = count;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

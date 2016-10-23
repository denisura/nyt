package com.github.denisura.nytseacher.data.model;

public class Item {

    private int id;
    private Object entity;

    public Item(int id, Object Entity) {
        this.id = id;
        this.entity = Entity;
    }

    public Object getEntity() {
        return entity;
    }

    public int getId() {
        return id;
    }
}
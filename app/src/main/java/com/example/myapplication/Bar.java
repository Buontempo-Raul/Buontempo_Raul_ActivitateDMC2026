package com.example.myapplication;

import java.io.Serializable;

public class Bar implements Serializable {
    // Enum pentru valori dintr-o mulțime finită
    public enum Type {
        PUB, LOUNGE, CLUB, CAFE, BISTRO
    }

    private String name;           // String
    private int capacity;          // Întreg
    private boolean isOpen;        // Boolean
    private double rating;         // Double (alt tip)
    private Type type;             // Enum

    public Bar(String name, int capacity, boolean isOpen, double rating, Type type) {
        this.name = name;
        this.capacity = capacity;
        this.isOpen = isOpen;
        this.rating = rating;
        this.type = type;
    }

    // Getters și Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "name='" + name + '\'' +
                ", capacity=" + capacity +
                ", isOpen=" + isOpen +
                ", rating=" + rating +
                ", type=" + type +
                '}';
    }
}
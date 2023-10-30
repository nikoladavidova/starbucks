package com.example.demo.starbucks;

public class Starbucks {
    private int id;
    private String name;
    private String location;
    private String date;
    private int rating;
    private String review;

    public Starbucks(int id, String name, String location, String date, int rating, String review) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.rating = rating;
        this.review = review;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}

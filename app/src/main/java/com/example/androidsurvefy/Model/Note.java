package com.example.androidsurvefy.Model;

public class Note {
    private String id;
    private String title;
    private String description;
    private String createdAt;

    public Note(String id, String title, String description, String createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

package com.example.DevExpress.model;

public class DevToolItem {
    private final String id;
    private final String title;
    private final String description;
    private final String iconSymbol;

    public DevToolItem(String id, String title, String description, String iconSymbol) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.iconSymbol = iconSymbol;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getIconSymbol() { return iconSymbol; }
}
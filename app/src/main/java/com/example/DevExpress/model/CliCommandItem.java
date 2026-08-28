package com.example.DevExpress.model;

public class CliCommandItem {
    private final String category; // "Docker", "Kubernetes", "Git"
    private final String command;  // e.g., "docker ps -a"
    private final String description; // e.g., "List all containers"

    public CliCommandItem(String category, String command, String description) {
        this.category = category;
        this.command = command;
        this.description = description;
    }

    public String getCategory() { return category; }
    public String getCommand() { return command; }
    public String getDescription() { return description; }
}
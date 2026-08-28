package com.example.DevExpress.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "api_request_history")
public class ApiRequestEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String method;      // GET, POST, PUT, DELETE
    private String url;
    private String requestBody;
    private int statusCode;
    private long timestamp;

    public ApiRequestEntity(String method, String url, String requestBody, int statusCode, long timestamp) {
        this.method = method;
        this.url = url;
        this.requestBody = requestBody;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMethod() { return method; }
    public String getUrl() { return url; }
    public String getRequestBody() { return requestBody; }
    public int getStatusCode() { return statusCode; }
    public long getTimestamp() { return timestamp; }
}
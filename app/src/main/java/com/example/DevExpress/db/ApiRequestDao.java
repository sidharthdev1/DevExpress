package com.example.DevExpress.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ApiRequestDao {

    @Insert
    void insert(ApiRequestEntity request);

    @Query("SELECT * FROM api_request_history ORDER BY timestamp DESC LIMIT 20")
    List<ApiRequestEntity> getRecentHistory();

    @Query("DELETE FROM api_request_history")
    void clearAll();
}
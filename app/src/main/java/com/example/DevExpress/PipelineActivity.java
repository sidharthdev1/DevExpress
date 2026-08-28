package com.example.DevExpress;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DevExpress.adapter.PipelineAdapter;
import com.example.DevExpress.model.PipelineItem;

import java.util.ArrayList;
import java.util.List;

public class PipelineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pipeline);

        RecyclerView rvPipelines = findViewById(R.id.rv_pipelines);
        rvPipelines.setLayoutManager(new LinearLayoutManager(this));

        PipelineAdapter adapter = new PipelineAdapter();
        rvPipelines.setAdapter(adapter);

        // Load mock pipeline execution data
        adapter.setItems(getMockPipelines());
    }

    private List<PipelineItem> getMockPipelines() {
        List<PipelineItem> list = new ArrayList<>();
        list.add(new PipelineItem(
                "deploy-prod-api",
                "main",
                "SUCCESS",
                "2m ago",
                "✓ Deployed to AWS ECS cluster us-east-1"
        ));
        list.add(new PipelineItem(
                "run-unit-tests",
                "feature/auth-v2",
                "FAILED",
                "14m ago",
                "✗ AssertionFailedError in AuthControllerTest.java:42"
        ));
        list.add(new PipelineItem(
                "build-docker-image",
                "main",
                "RUNNING",
                "Just now",
                "⚙ Step 3/5: Running npm build..."
        ));
        list.add(new PipelineItem(
                "db-migration-check",
                "staging",
                "SUCCESS",
                "1h ago",
                "✓ Schema migrations verified against PostgreSQL"
        ));
        return list;
    }
}
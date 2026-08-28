package com.example.DevExpress;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DevExpress.adapter.ToolsAdapter;
import com.example.DevExpress.model.DevToolItem;
import com.example.DevExpress.ApiTesterActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ToolsAdapter.OnToolClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvToolsGrid = findViewById(R.id.rv_tools_grid);
        rvToolsGrid.setLayoutManager(new GridLayoutManager(this, 2));

        ToolsAdapter adapter = new ToolsAdapter(this);
        rvToolsGrid.setAdapter(adapter);

        adapter.setTools(getInitialTools());
    }

    private List<DevToolItem> getInitialTools() {
        List<DevToolItem> tools = new ArrayList<>();
        tools.add(new DevToolItem("json_tool", "JSON Formatter", "Validate & format JSON payloads", "{ }"));
        tools.add(new DevToolItem("pipeline_tool", "CI/CD Monitor", "Track active build statuses", "🚀"));
        tools.add(new DevToolItem("api_tool", "REST API Tester", "Quick HTTP endpoint tests", "⚡"));
        tools.add(new DevToolItem("cli_tool", "CLI Cheatsheet", "Docker & K8s command list", "📖"));
        return tools;
    }

    @Override
    public void onToolClick(DevToolItem tool) {
        if ("json_tool".equals(tool.getId())) {
            startActivity(new Intent(this, JsonToolActivity.class));
        } else if ("pipeline_tool".equals(tool.getId())) {
            startActivity(new Intent(this, PipelineActivity.class));
        } else if ("cli_tool".equals(tool.getId())) {
            startActivity(new Intent(this, CliActivity.class));
        } else if ("api_tool".equals(tool.getId())) {
            startActivity(new Intent(this, ApiTesterActivity.class));
        } else {
            Toast.makeText(this, tool.getTitle() + " coming soon!", Toast.LENGTH_SHORT).show();
        }
    }
}
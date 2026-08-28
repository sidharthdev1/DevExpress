package com.example.DevExpress;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DevExpress.adapter.CliAdapter;
import com.example.DevExpress.model.CliCommandItem;

import java.util.ArrayList;
import java.util.List;

public class CliActivity extends AppCompatActivity {

    private CliAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cli_cheatsheet);

        RecyclerView rvCommands = findViewById(R.id.rv_cli_commands);
        EditText etSearch = findViewById(R.id.et_search_cli);

        rvCommands.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CliAdapter();
        rvCommands.setAdapter(adapter);

        // Load initial command database
        adapter.setItems(getCommandDatabase());

        // Real-time search listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private List<CliCommandItem> getCommandDatabase() {
        List<CliCommandItem> list = new ArrayList<>();

        // Docker Commands
        list.add(new CliCommandItem("Docker", "docker ps -a", "List all running and stopped containers"));
        list.add(new CliCommandItem("Docker", "docker exec -it <id> /bin/bash", "Open interactive bash shell in container"));
        list.add(new CliCommandItem("Docker", "docker-compose up -d --build", "Build and run container services in background"));
        list.add(new CliCommandItem("Docker", "docker system prune -a --volumes", "Remove all unused containers, networks, and images"));

        // Kubernetes Commands
        list.add(new CliCommandItem("Kubernetes", "kubectl get pods -n <namespace>", "List all pods in specific namespace"));
        list.add(new CliCommandItem("Kubernetes", "kubectl logs -f <pod_name>", "Stream live logs from a running pod"));
        list.add(new CliCommandItem("Kubernetes", "kubectl apply -f deployment.yaml", "Apply configuration file to cluster"));
        list.add(new CliCommandItem("Kubernetes", "kubectl describe pod <pod_name>", "View detailed status and events of a pod"));

        // Git Commands
        list.add(new CliCommandItem("Git", "git status", "Check state of working directory and staging area"));
        list.add(new CliCommandItem("Git", "git checkout -b feature/<name>", "Create and switch to a new branch"));
        list.add(new CliCommandItem("Git", "git log --oneline --graph", "View condensed visual commit history graph"));
        list.add(new CliCommandItem("Git", "git reset --hard HEAD~1", "Discard local changes and revert back 1 commit"));

        return list;
    }
}
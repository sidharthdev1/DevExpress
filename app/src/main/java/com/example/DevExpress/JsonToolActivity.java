package com.example.DevExpress;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.DevExpress.utils.JsonFormatter;

public class JsonToolActivity extends AppCompatActivity {

    private EditText etRawJson;
    private TextView tvStatus;
    private TextView tvOutputJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_tool);

        // Initialize UI Elements
        etRawJson = findViewById(R.id.et_raw_json);
        tvStatus = findViewById(R.id.tv_status);
        tvOutputJson = findViewById(R.id.tv_output_json);
        Button btnFormat = findViewById(R.id.btn_format);
        Button btnClear = findViewById(R.id.btn_clear);

        // Format Button Listener
        btnFormat.setOnClickListener(v -> processJson());

        // Clear Button Listener
        btnClear.setOnClickListener(v -> {
            etRawJson.setText("");
            tvOutputJson.setText("");
            tvStatus.setText("Formatted Output");
            tvStatus.setTextColor(getColor(R.color.text_secondary));
        });

        // Long press on output to copy formatted result to clipboard
        tvOutputJson.setOnLongClickListener(v -> {
            String textToCopy = tvOutputJson.getText().toString();
            if (!textToCopy.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Formatted JSON", textToCopy);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        });
    }

    private void processJson() {
        String input = etRawJson.getText().toString();
        JsonFormatter.Result result = JsonFormatter.format(input);

        if (result.isValid()) {
            tvStatus.setText("Status: Valid JSON");
            tvStatus.setTextColor(getColor(R.color.status_green));
            tvOutputJson.setText(result.getFormattedText());
        } else {
            tvStatus.setText("Status: Error");
            tvStatus.setTextColor(getColor(R.color.status_red));
            tvOutputJson.setText(result.getErrorMessage());
        }
    }
}
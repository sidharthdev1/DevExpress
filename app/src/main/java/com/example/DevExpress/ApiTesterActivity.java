package com.example.DevExpress;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DevExpress.adapter.ApiHistoryAdapter;
import com.example.DevExpress.db.ApiRequestDao;
import com.example.DevExpress.db.ApiRequestEntity;
import com.example.DevExpress.db.AppDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiTesterActivity extends AppCompatActivity {

    private Spinner spinnerMethod;
    private EditText etApiUrl, etRequestBody;
    private TextView tvStatusCode, tvResponseOutput, tvResponseHeaders;
    private Button btnSend, btnCopyCurl;

    private ApiHistoryAdapter historyAdapter;
    private ApiRequestDao requestDao;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_tester);

        spinnerMethod = findViewById(R.id.spinner_method);
        etApiUrl = findViewById(R.id.et_api_url);
        etRequestBody = findViewById(R.id.et_request_body);
        tvStatusCode = findViewById(R.id.tv_status_code);
        tvResponseOutput = findViewById(R.id.tv_response_output);
        btnSend = findViewById(R.id.btn_send_request);

        // Optional layout elements (add corresponding IDs in XML if using)
        tvResponseHeaders = findViewById(R.id.tv_response_headers);
        btnCopyCurl = findViewById(R.id.btn_copy_curl);

        // Init Room Database
        requestDao = AppDatabase.getInstance(this).apiRequestDao();

        // Setup Spinner
        String[] methods = {"GET", "POST", "PUT", "DELETE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, methods);
        spinnerMethod.setAdapter(adapter);

        // Setup History RecyclerView
        RecyclerView rvHistory = findViewById(R.id.rv_api_history);
        rvHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        historyAdapter = new ApiHistoryAdapter(this::reloadRequestFromHistory);
        rvHistory.setAdapter(historyAdapter);

        btnSend.setOnClickListener(v -> executeApiCall());

        if (btnCopyCurl != null) {
            btnCopyCurl.setOnClickListener(v -> copyCurlToClipboard());
        }

        // Load saved history on launch
        loadHistory();
    }

    private void reloadRequestFromHistory(ApiRequestEntity item) {
        etApiUrl.setText(item.getUrl());
        etRequestBody.setText(item.getRequestBody());

        for (int i = 0; i < spinnerMethod.getCount(); i++) {
            if (spinnerMethod.getItemAtPosition(i).toString().equalsIgnoreCase(item.getMethod())) {
                spinnerMethod.setSelection(i);
                break;
            }
        }
        Toast.makeText(this, "Loaded past request!", Toast.LENGTH_SHORT).show();
    }

    private void loadHistory() {
        executor.execute(() -> {
            List<ApiRequestEntity> recent = requestDao.getRecentHistory();
            mainHandler.post(() -> historyAdapter.setItems(recent));
        });
    }

    private void saveRequestToHistory(String method, String url, String body, int statusCode) {
        executor.execute(() -> {
            ApiRequestEntity entity = new ApiRequestEntity(method, url, body, statusCode, System.currentTimeMillis());
            requestDao.insert(entity);
            loadHistory();
        });
    }

    private void executeApiCall() {
        String urlString = etApiUrl.getText().toString().trim();
        String method = spinnerMethod.getSelectedItem().toString();
        String requestBody = etRequestBody.getText().toString().trim();

        if (urlString.isEmpty()) {
            Toast.makeText(this, "Please enter a target URL", Toast.LENGTH_SHORT).show();
            return;
        }

        btnSend.setEnabled(false);
        tvStatusCode.setText("SENDING...");
        tvStatusCode.setTextColor(Color.YELLOW);
        tvResponseOutput.setText("Fetching network response...");
        if (tvResponseHeaders != null) tvResponseHeaders.setText("Fetching headers...");

        executor.execute(() -> {
            HttpURLConnection connection = null;
            int statusCode = -1;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(method);
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);
                connection.setRequestProperty("Accept", "application/json");

                if (("POST".equals(method) || "PUT".equals(method)) && !requestBody.isEmpty()) {
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    try (OutputStream os = connection.getOutputStream()) {
                        byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }
                }

                statusCode = connection.getResponseCode();

                // Read response headers
                String headersText = parseHeaders(connection.getHeaderFields());

                // Read response body
                InputStream stream = (statusCode >= 200 && statusCode < 400)
                        ? connection.getInputStream()
                        : connection.getErrorStream();

                String responseText = readStream(stream);

                final int finalStatusCode = statusCode;
                mainHandler.post(() -> {
                    updateUi(finalStatusCode, responseText, headersText);
                    saveRequestToHistory(method, urlString, requestBody, finalStatusCode);
                });

            } catch (Exception e) {
                String errorMsg = e.getMessage() != null ? e.getMessage() : "Network connection failed";
                mainHandler.post(() -> updateUi(-1, "Error: " + errorMsg, ""));
            } finally {
                if (connection != null) connection.disconnect();
            }
        });
    }

    private String parseHeaders(Map<String, List<String>> headerFields) {
        if (headerFields == null || headerFields.isEmpty()) return "No headers received.";
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            if (entry.getKey() != null) {
                sb.append(entry.getKey()).append(": ").append(String.join(", ", entry.getValue())).append("\n");
            }
        }
        return sb.toString().trim();
    }

    private String readStream(InputStream stream) throws Exception {
        if (stream == null) return "No response body received.";
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString().trim();
    }

    private void updateUi(int statusCode, String result, String headers) {
        btnSend.setEnabled(true);
        tvResponseOutput.setText(result);
        if (tvResponseHeaders != null) tvResponseHeaders.setText(headers);

        if (statusCode >= 200 && statusCode < 300) {
            tvStatusCode.setText("HTTP " + statusCode + " OK");
            tvStatusCode.setTextColor(Color.GREEN);
        } else if (statusCode >= 400) {
            tvStatusCode.setText("HTTP " + statusCode + " ERROR");
            tvStatusCode.setTextColor(Color.RED);
        } else {
            tvStatusCode.setText("CONNECTION FAILED");
            tvStatusCode.setTextColor(Color.RED);
        }
    }

    private void copyCurlToClipboard() {
        String url = etApiUrl.getText().toString().trim();
        String method = spinnerMethod.getSelectedItem().toString();
        String body = etRequestBody.getText().toString().trim();

        if (url.isEmpty()) {
            Toast.makeText(this, "Enter a URL first", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder curl = new StringBuilder("curl -X ").append(method);
        curl.append(" '").append(url).append("'");
        curl.append(" \\\n  -H 'Accept: application/json'");

        if (("POST".equals(method) || "PUT".equals(method)) && !body.isEmpty()) {
            curl.append(" \\\n  -H 'Content-Type: application/json'");
            String escapedBody = body.replace("'", "'\\''");
            curl.append(" \\\n  -d '").append(escapedBody).append("'");
        }

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("cURL Command", curl.toString());
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "cURL command copied to clipboard!", Toast.LENGTH_SHORT).show();
        }
    }
}
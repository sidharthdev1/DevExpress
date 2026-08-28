package com.example.DevExpress.utils;

import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonFormatter {

    public static class Result {
        private final boolean valid;
        private final String formattedText;
        private final String errorMessage;

        public Result(boolean valid, String formattedText, String errorMessage) {
            this.valid = valid;
            this.formattedText = formattedText;
            this.errorMessage = errorMessage;
        }

        public boolean isValid() { return valid; }
        public String getFormattedText() { return formattedText; }
        public String getErrorMessage() { return errorMessage; }
    }

    public static Result format(String rawInput) {
        if (rawInput == null || rawInput.trim().isEmpty()) {
            return new Result(false, "", "Input is empty.");
        }

        String trimmed = rawInput.trim();
        try {
            if (trimmed.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(trimmed);
                return new Result(true, jsonObject.toString(4), null);
            } else if (trimmed.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(trimmed);
                return new Result(true, jsonArray.toString(4), null);
            } else {
                return new Result(false, "", "Invalid JSON format: Must start with '{' or '['.");
            }
        } catch (JSONException e) {
            return new Result(false, "", "Syntax Error: " + e.getMessage());
        }
    }
}
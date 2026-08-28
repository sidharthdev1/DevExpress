package com.example.DevExpress.model;

public class PipelineItem {
    private final String jobName;
    private final String branch;
    private final String status; // "SUCCESS", "FAILED", "RUNNING"
    private final String timeAgo;
    private final String logSummary;

    public PipelineItem(String jobName, String branch, String status, String timeAgo, String logSummary) {
        this.jobName = jobName;
        this.branch = branch;
        this.status = status;
        this.timeAgo = timeAgo;
        this.logSummary = logSummary;
    }

    public String getJobName() { return jobName; }
    public String getBranch() { return branch; }
    public String getStatus() { return status; }
    public String getTimeAgo() { return timeAgo; }
    public String getLogSummary() { return logSummary; }
}
package com.example.DevExpress.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DevExpress.R;
import com.example.DevExpress.model.PipelineItem;

import java.util.ArrayList;
import java.util.List;

public class PipelineAdapter extends RecyclerView.Adapter<PipelineAdapter.PipelineViewHolder> {

    private final List<PipelineItem> pipelineList = new ArrayList<>();

    public void setItems(List<PipelineItem> items) {
        this.pipelineList.clear();
        if (items != null) {
            this.pipelineList.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PipelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pipeline, parent, false);
        return new PipelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PipelineViewHolder holder, int position) {
        PipelineItem item = pipelineList.get(position);
        Context context = holder.itemView.getContext();

        holder.tvJobName.setText(item.getJobName());
        holder.tvBranch.setText("Branch: " + item.getBranch());
        holder.tvTimeAgo.setText(item.getTimeAgo());
        holder.tvLogSummary.setText(item.getLogSummary());
        holder.tvStatusBadge.setText(item.getStatus());

        // Color-code the status badge dynamically based on job state
        switch (item.getStatus().toUpperCase()) {
            case "SUCCESS":
                holder.tvStatusBadge.setBackgroundColor(ContextCompat.getColor(context, R.color.status_green));
                holder.tvStatusBadge.setTextColor(Color.WHITE);
                break;
            case "FAILED":
                holder.tvStatusBadge.setBackgroundColor(ContextCompat.getColor(context, R.color.status_red));
                holder.tvStatusBadge.setTextColor(Color.WHITE);
                break;
            case "RUNNING":
            default:
                holder.tvStatusBadge.setBackgroundColor(ContextCompat.getColor(context, R.color.accent_blue));
                holder.tvStatusBadge.setTextColor(Color.WHITE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return pipelineList.size();
    }

    static class PipelineViewHolder extends RecyclerView.ViewHolder {
        TextView tvStatusBadge, tvJobName, tvTimeAgo, tvBranch, tvLogSummary;

        public PipelineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStatusBadge = itemView.findViewById(R.id.tv_status_badge);
            tvJobName = itemView.findViewById(R.id.tv_job_name);
            tvTimeAgo = itemView.findViewById(R.id.tv_time_ago);
            tvBranch = itemView.findViewById(R.id.tv_branch);
            tvLogSummary = itemView.findViewById(R.id.tv_log_summary);
        }
    }
}
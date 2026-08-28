package com.example.DevExpress.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DevExpress.R;
import com.example.DevExpress.db.ApiRequestEntity;

import java.util.ArrayList;
import java.util.List;

public class ApiHistoryAdapter extends RecyclerView.Adapter<ApiHistoryAdapter.HistoryViewHolder> {

    public interface OnHistoryClickListener {
        void OnHistoryClick(ApiRequestEntity item);
    }

    private final List<ApiRequestEntity> historyList = new ArrayList<>();
    private final OnHistoryClickListener listener;

    public ApiHistoryAdapter(OnHistoryClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<ApiRequestEntity> items) {
        this.historyList.clear();
        if (items != null) {
            this.historyList.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_api_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        ApiRequestEntity item = historyList.get(position);

        holder.tvMethod.setText(item.getMethod());
        holder.tvUrl.setText(item.getUrl());
        holder.tvStatus.setText("HTTP " + item.getStatusCode());

        if (item.getStatusCode() >= 200 && item.getStatusCode() < 300) {
            holder.tvStatus.setTextColor(Color.GREEN);
        } else {
            holder.tvStatus.setTextColor(Color.RED);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.OnHistoryClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvMethod, tvStatus, tvUrl;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMethod = itemView.findViewById(R.id.tv_history_method);
            tvStatus = itemView.findViewById(R.id.tv_history_status);
            tvUrl = itemView.findViewById(R.id.tv_history_url);
        }
    }
}
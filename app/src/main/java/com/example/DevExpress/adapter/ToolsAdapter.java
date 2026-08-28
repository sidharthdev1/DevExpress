package com.example.DevExpress.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.DevExpress.R;
import com.example.DevExpress.model.DevToolItem;
import java.util.ArrayList;
import java.util.List;

public class ToolsAdapter extends RecyclerView.Adapter<ToolsAdapter.ToolViewHolder> {

    public interface OnToolClickListener {
        void onToolClick(DevToolItem tool);
    }

    private final List<DevToolItem> toolList = new ArrayList<>();
    private final OnToolClickListener listener;

    public ToolsAdapter(OnToolClickListener listener) {
        this.listener = listener;
    }

    public void setTools(List<DevToolItem> tools) {
        this.toolList.clear();
        if (tools != null) {
            this.toolList.addAll(tools);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ToolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dev_tool, parent, false);
        return new ToolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToolViewHolder holder, int position) {
        DevToolItem tool = toolList.get(position);
        holder.tvIcon.setText(tool.getIconSymbol());
        holder.tvTitle.setText(tool.getTitle());
        holder.tvDesc.setText(tool.getDescription());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onToolClick(tool);
            }
        });
    }

    @Override
    public int getItemCount() {
        return toolList.size();
    }

    static class ToolViewHolder extends RecyclerView.ViewHolder {
        TextView tvIcon, tvTitle, tvDesc;

        public ToolViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIcon = itemView.findViewById(R.id.tv_tool_icon);
            tvTitle = itemView.findViewById(R.id.tv_tool_title);
            tvDesc = itemView.findViewById(R.id.tv_tool_desc);
        }
    }
}
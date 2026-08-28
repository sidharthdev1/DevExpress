package com.example.DevExpress.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.DevExpress.R;
import com.example.DevExpress.model.CliCommandItem;

import java.util.ArrayList;
import java.util.List;

public class CliAdapter extends RecyclerView.Adapter<CliAdapter.CliViewHolder> {

    private final List<CliCommandItem> fullList = new ArrayList<>();
    private final List<CliCommandItem> filteredList = new ArrayList<>();

    public void setItems(List<CliCommandItem> items) {
        this.fullList.clear();
        this.filteredList.clear();
        if (items != null) {
            this.fullList.addAll(items);
            this.filteredList.addAll(items);
        }
        notifyDataSetChanged();
    }

    public void filter(String query) {
        filteredList.clear();
        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(fullList);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (CliCommandItem item : fullList) {
                if (item.getCommand().toLowerCase().contains(lowerQuery) ||
                        item.getDescription().toLowerCase().contains(lowerQuery) ||
                        item.getCategory().toLowerCase().contains(lowerQuery)) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CliViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cli_command, parent, false);
        return new CliViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CliViewHolder holder, int position) {
        CliCommandItem item = filteredList.get(position);
        holder.tvCategoryTag.setText(item.getCategory());
        holder.tvDescription.setText(item.getDescription());
        holder.tvCommand.setText(item.getCommand());

        // Tap on card to copy command directly to clipboard
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("CLI Command", item.getCommand());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Command copied!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    static class CliViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryTag, tvDescription, tvCommand;

        public CliViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryTag = itemView.findViewById(R.id.tv_category_tag);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvCommand = itemView.findViewById(R.id.tv_command);
        }
    }
}
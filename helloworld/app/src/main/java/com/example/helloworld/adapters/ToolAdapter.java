package com.example.helloworld.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld.R;
import com.example.helloworld.interfaces.ToolListener;
import com.example.helloworld.interfaces.ViewOnClick;
import com.example.helloworld.models.ToolItem;
import com.example.helloworld.viewHolders.ToolViewHolder;

import java.lang.reflect.Type;
import java.util.List;

public class ToolAdapter extends RecyclerView.Adapter<ToolViewHolder> {
    private List<ToolItem> toolItemList;
    private int current = -1;
    private ToolListener listener;

    public ToolAdapter(List<ToolItem> toolItemList, ToolListener listener) {
        this.toolItemList = toolItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ToolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tool_item,parent,false);
        return new ToolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToolViewHolder holder, int position) {
        holder.name.setText(toolItemList.get(position).getName());
        holder.icon.setImageResource(toolItemList.get(position).getIcon());
        holder.setViewOnClick(new ViewOnClick() {
            @Override
            public void onClick(int pos) {
                current = pos;
                listener.onSelect(toolItemList.get(pos).getName());
                notifyDataSetChanged();
            }
        });
        if(current==position) {
            holder.name.setTypeface(holder.name.getTypeface(), Typeface.BOLD);
        }
        else {
            holder.name.setTypeface(Typeface.DEFAULT);
        }
    }

    @Override
    public int getItemCount() {
        return toolItemList.size();
    }
}

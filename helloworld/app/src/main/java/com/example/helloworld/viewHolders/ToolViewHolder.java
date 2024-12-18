package com.example.helloworld.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloworld.R;
import com.example.helloworld.interfaces.ViewOnClick;

public class ToolViewHolder extends RecyclerView.ViewHolder {

    public ImageView icon;
    public TextView name;

    private ViewOnClick viewOnClick;

    public void setViewOnClick(ViewOnClick viewOnClick) {
        this.viewOnClick = viewOnClick;
    }

    public ToolViewHolder(@NonNull View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.tool_icon);
        name = itemView.findViewById(R.id.tool_name);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewOnClick.onClick(getAdapterPosition());
            }
        });
    }
}

package com.gram.landlord_client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gram.landlord_client.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.RegionViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;

    public RegionAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {this.onItemClickListener = listener;}

    @NonNull
    @Override
    public RegionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RegionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_region, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RegionViewHolder holder, int i) {
        holder.tv.setText("电信1区");
        if(onItemClickListener != null) holder.itemView.setOnClickListener((view) -> onItemClickListener.onItemClick(holder));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class RegionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_entrance)
        TextView tv;

        RegionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RegionViewHolder holder);
    }
}

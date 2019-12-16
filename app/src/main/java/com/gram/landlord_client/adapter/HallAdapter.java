package com.gram.landlord_client.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gram.landlord_client.R;
import com.gram.landlord_client.sdk.entity.HallTable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HallAdapter extends RecyclerView.Adapter<HallAdapter.HallViewHolder> {
    private Context context;
    private ArrayList<HallTable> hallTables;
    private OnItemClickListener onItemClickListener;

    public HallAdapter(Context context) {
        this.context = context;
        hallTables = new ArrayList<>(100);
    }

    public void setSeatMap(ArrayList<HallTable> hallTables) {
        this.hallTables.clear();
        this.hallTables.addAll(hallTables);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public HallViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new HallViewHolder(LayoutInflater.from(context).inflate(R.layout.item_table, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HallViewHolder holder, int i) {
        if(i <= 100) {
            HallTable hallTable = hallTables.get(i);
            holder.tvPlayerDetail.setText("现有玩家：");
            if(hallTable.isPlay()) holder.tvPlayerDetail.append(" 正在游戏");
            if(hallTable.getUserNames() == null) {
                holder.tvPlayerCount.setText("还没有人");
            } else {
                holder.tvPlayerCount.setText(String.format("人数有：%s", hallTable.getUserNames().size()));
                for(String tmp : hallTable.getUserNames()) holder.tvPlayerDetail.append(tmp + " ");
            }
            //必须要牌桌没有满员，没有游戏中时才能点击回调
            if(onItemClickListener != null && !hallTable.isPlay() && !hallTable.isFull())
                holder.itemView.setOnClickListener((view) -> onItemClickListener.onItemClick(hallTable.getTableNum()));
        }

    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class HallViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_player_count)
        TextView tvPlayerCount;
        @BindView(R.id.tv_player_detail)
        TextView tvPlayerDetail;

        HallViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int tableNum);
    }
}

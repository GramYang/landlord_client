package com.gram.landlord_client.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gram.landlord_client.R;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.util.LandlordUtil;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardsPack extends RecyclerView {
    public CardsPack(Context context) {
        this(context, null);
    }

    public CardsPack(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardsPack(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public class CardsPackAdapter extends Adapter<CardsPackViewHolder> {
        private Context context;
        private int itemType;
        private ArrayList<Integer> cardsKey; //持有的牌
        private ArrayList<Integer> cardsOutKey; //要打出去的牌

        public ArrayList<Integer> getCardsKey() {
            return cardsKey;
        }

        public CardsPackAdapter(Context context, int itemType, ArrayList<Integer> cardsKey) {
            this.context = context;
            this.itemType = itemType;
            this.cardsKey = cardsKey;
            cardsOutKey = new ArrayList<>();
        }

        public ArrayList<Integer> getCardsOutKey() {
            LandlordUtil.rankCards(cardsOutKey);
            return cardsOutKey;
        }

        public void clearCardsOutKey() {
            this.cardsOutKey.clear();
        }

        /**
         * 遍历要出的牌，然后从list中移除
         */
        public void throwCardsAndRefresh(ArrayList<Integer> chooseCards) {
            for(Integer card : chooseCards) cardsKey.remove(card);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public CardsPackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            if(itemType == 1) return new CardsPackViewHolder(LayoutInflater.from(context).
                    inflate(R.layout.single_card_small, viewGroup, false));
            if(itemType == 2) return new CardsPackViewHolder(LayoutInflater.from(context).
                    inflate(R.layout.single_card_big, viewGroup, false));
            return new CardsPackViewHolder(LayoutInflater.from(context).
                    inflate(R.layout.single_card_small, viewGroup, false));
        }

        @Override
        @SuppressWarnings("all")
        public void onBindViewHolder(@NonNull CardsPackViewHolder holder, int i) {
            holder.cardIn.setImageResource(Constants.cardsResource.get(cardsKey.get(i)));
            holder.cardIn.setVisibility(VISIBLE);
            holder.cardOut.setImageResource(Constants.cardsResource.get(cardsKey.get(i)));
            holder.cardOut.setVisibility(GONE);
            //点击后牌弹出，并传出选中的牌
            holder.cardIn.setOnClickListener(v -> {
                holder.cardIn.setVisibility(GONE);
                holder.cardOut.setVisibility(VISIBLE);
                cardsOutKey.add(cardsKey.get(i));
            });
            //点击后牌缩回
            holder.cardOut.setOnClickListener(v -> {
                holder.cardOut.setVisibility(GONE);
                holder.cardIn.setVisibility(VISIBLE);
                cardsOutKey.remove(cardsKey.get(i));
            });
        }

        @Override
        public int getItemCount() {
            return cardsKey == null? 0 : cardsKey.size();
        }

        private @DrawableRes
        int getName2ResId(String resName) {
            int imgRid = 0;
            try {
                imgRid = R.mipmap.class.getDeclaredField(resName).getInt(R.mipmap.class);
            } catch (Exception e) {
                Logger.e(e.getMessage());
            }
            return imgRid;
        }

    }

    class CardsPackViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_in)
        ImageView cardIn;
        @BindView(R.id.card_out)
        ImageView cardOut;

        CardsPackViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

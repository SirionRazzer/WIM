package com.wim.wim.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wim.wim.R;
import com.wim.wim.model.Card;

import java.util.List;

/**
 * Created by Sadruddin on 12/24/2017.
 */

public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.CardViewHolder>{
    private List<Card> horizontalCardList;
    Context context;

    public RecyclerViewHorizontalListAdapter(List<Card> horizontalCardList, Context context){
        this.horizontalCardList = horizontalCardList;
        this.context = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View cardView;
        cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_card_item, parent, false);
        return new CardViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, final int position) {
        //holder.imageView.setImageResource(horizontalCardList.get(position).getProductImage());
        holder.headerview.setText(horizontalCardList.get(position).getHeader().toString());
        holder.txtview.setText(horizontalCardList.get(position).getTextQuote());
//        holder.txtview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String cardText = horizontalCardList.get(position).getTextQuote().toString();
//                Toast.makeText(context, cardText + " is selected", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return horizontalCardList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView txtview;
        TextView headerview;
        public CardViewHolder(View view) {
            super(view);
            txtview=view.findViewById(R.id.id_quote);
            headerview=view.findViewById(R.id.id_header);
        }
    }
}
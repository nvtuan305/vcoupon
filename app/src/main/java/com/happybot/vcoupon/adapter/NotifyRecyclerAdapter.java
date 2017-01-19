package com.happybot.vcoupon.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.model.Notify;
import com.happybot.vcoupon.adapter.NotifyRecyclerAdapter.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Admin on 12/16/2016.
 */
public class NotifyRecyclerAdapter extends
        RecyclerView.Adapter<RecyclerViewHolder> {

    private List<Notify> listNotify = new ArrayList<Notify>();

    public NotifyRecyclerAdapter(List<Notify> listNotify) {
        this.listNotify = listNotify;
    }

    public void updateList(List<Notify> notify) {
        listNotify = notify;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.layout_item_notification, viewGroup, false);
        return new RecyclerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int position) {
        viewHolder.ivNotify.setImageResource(listNotify.get(position).getAvatar());
        viewHolder.tvInfoNotify.setText(listNotify.get(position).getDescription());
        viewHolder.tvTimeNotify.setText(listNotify.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return listNotify.size();
    }

    public void addItem(int position, Notify notify) {
        listNotify.add(position, notify);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        listNotify.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * ViewHolder for item view of list
     */
    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public CircleImageView ivNotify;
        public TextView tvInfoNotify;
        public TextView tvTimeNotify;
        public TextView tvSignNotify;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            ivNotify = (CircleImageView) itemView.findViewById(R.id.ivNotify);
            tvInfoNotify = (TextView) itemView.findViewById(R.id.tvInfoNotify);
            tvTimeNotify = (TextView) itemView.findViewById(R.id.tvTimeNotify);
            tvSignNotify = (TextView) itemView.findViewById(R.id.tvSignNotify);
        }

        @Override
        public void onClick(View v) {

        }
    }
}

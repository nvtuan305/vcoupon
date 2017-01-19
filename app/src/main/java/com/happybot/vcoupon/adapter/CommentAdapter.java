package com.happybot.vcoupon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.model.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyễn Phương Tuấn on 18-Jan-17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

    static public List<Comment> comments = new ArrayList<>();
    private Context mContext = null;

    public CommentAdapter() {
        comments = new ArrayList<>();
    }

    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, null);
        mContext = layoutView.getContext();
        CommentViewHolder holder = new CommentViewHolder(layoutView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final CommentViewHolder holder, int position) {
        final Comment comment = comments.get(position);
        Picasso.with(mContext)
                .load(comment.get_user().getAvatar())
                .fit()
                .centerCrop()
                .error(R.drawable.ic_avatar_default)
                .into(holder.comment_item_avatar);
        holder.comment_item_name.setText(comment.get_user().getName());
        holder.comment_item_message.setText(comment.getMessage());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addData(List<Comment> commentList) {
        comments.addAll(commentList);
        notifyDataSetChanged();
    }

    public void updateData(List<Comment> commentList) {
        comments.clear();
        comments.addAll(commentList);
        notifyDataSetChanged();
    }
}

class CommentViewHolder extends RecyclerView.ViewHolder {
    ImageView comment_item_avatar;
    TextView comment_item_name;
    TextView comment_item_message;

    CommentViewHolder(final View itemView) {
        super(itemView);

        comment_item_avatar = (ImageView) itemView.findViewById(R.id.comment_item_avatar);
        comment_item_name = (TextView) itemView.findViewById(R.id.comment_item_name);
        comment_item_message = (TextView) itemView.findViewById(R.id.comment_item_message);
    }
}
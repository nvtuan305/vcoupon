package com.happybot.vcoupon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.VoucherDetailActivity;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.util.DateTimeConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionViewHolder> {

    private List<Promotion> promotions = new ArrayList<>();
    private Context mContext = null;

    public PromotionAdapter() {
        promotions = new ArrayList<>();
    }

    public PromotionAdapter(ArrayList<Promotion> promotions) {
        this.promotions = promotions;
    }

    @Override
    public PromotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_promotion, null);
        mContext = layoutView.getContext();
        PromotionViewHolder holder = new PromotionViewHolder(layoutView);

        return holder;
    }

    @Override
    public void onBindViewHolder(PromotionViewHolder holder, int position) {
        final Promotion promotion = promotions.get(position);
        User provider = promotion.getProvider();

        // Set promotion id
        holder.promotionId = promotion.getId();

        // Bind view
        // Load cover photo
        Picasso.with(mContext)
                .load(promotion.getCover())
                .fit()
                .centerCrop()
                .into(holder.imvCoverPhoto);

        holder.tvTitle.setText(promotion.getTitle());
        holder.tvCommentCount.setText(promotion.getCommentCount() + "");
        holder.tvPinnedCount.setText(promotion.getPinnedCount() + "");

        String remainTime = DateTimeConverter.getRemainTime(promotion.getEndDate());
        holder.tvRemainTime.setText(remainTime);

        // Set discount type
        String discountType = promotion.getDiscountType();
        if (discountType.equals("VND"))
            holder.tvDiscount.setText(promotion.getDiscount() + "k");
        else
            holder.tvDiscount.setText("-" + promotion.getDiscount() + discountType);

        // Load provider info
        Picasso.with(mContext)
                .load(provider.getAvatar())
                .fit()
                .centerCrop()
                .into(holder.imvProviderAvatar);
        holder.tvProviderName.setText(provider.getName());
        holder.tvProviderAddress.setText(provider.getAddress());

        // Disable or enabled get voucher button
        if (DateTimeConverter.getCurrentDateInMillis() >= promotion.getEndDate()) {
            holder.btnGetVoucher.setEnabled(false);
        } else {
            holder.btnGetVoucher.setEnabled(true);
        }

        // OnItemClickListener for recycle view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VoucherDetailActivity.class);
                intent.putExtra("DetailPromotion", promotion);
                mContext.startActivity(intent);
            }
        });

        holder.btnGetVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Get voucher
                Toast.makeText(v.getContext(), "Get voucher on click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public void addData(List<Promotion> promotionList) {
        promotions.addAll(promotionList);
        notifyDataSetChanged();
    }

    public void updateData(List<Promotion> promotionList) {
        promotions.clear();
        promotions.addAll(promotionList);
        notifyDataSetChanged();
    }
}

class PromotionViewHolder extends RecyclerView.ViewHolder {
    // View
    ImageView imvCoverPhoto;
    TextView tvTitle;
    TextView tvDiscount;
    ImageView imvProviderAvatar;
    TextView tvProviderName;
    TextView tvProviderAddress;
    TextView tvCommentCount;
    TextView tvPinnedCount;
    TextView tvRemainTime;
    Button btnGetVoucher;

    String promotionId = null;

    PromotionViewHolder(View itemView) {
        super(itemView);

        imvCoverPhoto = (ImageView) itemView.findViewById(R.id.imvCoverPhoto);
        tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        tvDiscount = (TextView) itemView.findViewById(R.id.tvDiscount);
        imvProviderAvatar = (ImageView) itemView.findViewById(R.id.imvProviderAvatar);
        tvProviderName = (TextView) itemView.findViewById(R.id.tvProviderName);
        tvProviderAddress = (TextView) itemView.findViewById(R.id.tvProviderAddress);
        tvCommentCount = (TextView) itemView.findViewById(R.id.tvCommentCount);
        tvPinnedCount = (TextView) itemView.findViewById(R.id.tvPinnedCount);
        tvRemainTime = (TextView) itemView.findViewById(R.id.tvRemainTime);
        btnGetVoucher = (Button) itemView.findViewById(R.id.btnGetVoucher);
    }
}

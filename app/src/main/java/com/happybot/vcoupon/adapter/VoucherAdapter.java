package com.happybot.vcoupon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.model.Voucher;
import com.happybot.vcoupon.util.DateTimeConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherViewHolder> {

    private List<Voucher> vouchers = new ArrayList<>();
    private Context mContext = null;

    public VoucherAdapter() {
        vouchers = new ArrayList<>();
    }

    public VoucherAdapter(ArrayList<Voucher> vouchers) {
        this.vouchers = vouchers;
    }

    @Override
    public VoucherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_received_voucher_item, null);
        mContext = layoutView.getContext();
        VoucherViewHolder holder = new VoucherViewHolder(layoutView);

        return holder;
    }

    @Override
    public void onBindViewHolder(VoucherViewHolder holder, int position) {
        Voucher voucher = vouchers.get(position);
        Promotion promotion = voucher.getPromotion();
        User provider = promotion.getProvider();

        // Set promotion id
        holder.promotionId = promotion.getId();

        // Bind view
        // Load provider avatar
        Picasso.with(mContext)
                .load(provider.getAvatar())
                .fit()
                .centerCrop()
                .error(R.drawable.ic_avatar_default)
                .into(holder.imvProviderAvatar);

        // Set title
        holder.tvPromotionTitle.setText(promotion.getTitle());
        // Set checked status
        if (voucher.isChecked())
            holder.tvCheckedStatus.setText("Đã sử dụng");
        else
            holder.tvCheckedStatus.setText("Chưa sử dụng");

        // Set voucher code
        holder.tvVoucherCode.setText(voucher.getVoucherCode());

        String remainTime = DateTimeConverter.getRemainTime(promotion.getEndDate());
        holder.tvRemainTime.setText(remainTime);

        // Load QR code image
        Picasso.with(mContext)
                .load(voucher.getQrCode())
                .fit()
                .centerCrop()
                .into(holder.imvQRCode);
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    public void addData(List<Voucher> vouchers) {
        this.vouchers.addAll(vouchers);
        notifyDataSetChanged();
    }

    public void updateData(List<Voucher> vouchers) {
        this.vouchers.clear();
        this.vouchers.addAll(vouchers);
        notifyDataSetChanged();
    }
}

class VoucherViewHolder extends RecyclerView.ViewHolder {
    // View
    TextView tvPromotionTitle;
    ImageView imvProviderAvatar;
    ImageView imvQRCode;
    TextView tvCheckedStatus;
    TextView tvVoucherCode;
    TextView tvRemainTime;

    String promotionId = null;

    VoucherViewHolder(View itemView) {
        super(itemView);

        tvPromotionTitle = (TextView) itemView.findViewById(R.id.tvPromotionTitle);
        imvQRCode = (ImageView) itemView.findViewById(R.id.imvQRCode);
        imvProviderAvatar = (ImageView) itemView.findViewById(R.id.imvProviderAvatarPromotion);
        tvCheckedStatus = (TextView) itemView.findViewById(R.id.tvCheckedStatus);
        tvVoucherCode = (TextView) itemView.findViewById(R.id.tvVoucherCode);
        tvRemainTime = (TextView) itemView.findViewById(R.id.tvPromotionRemainTime);

        // OnItemClickListener for recycle view
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Promotion id = " + promotionId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.happybot.vcoupon.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.util.DateTimeConverter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by don on 1/15/17.
 */

public class NearByPromotionAdapter extends PagerAdapter {

    List<Promotion> promotions;
    Context context;
    ImageView imvProviderAvatar;
    TextView tvTitle, tvDiscount, tvProviderAddress, tvCommentCount, tvPinnedCount, tvRemainTime;
    Button btnGetVoucher;

    public NearByPromotionAdapter(Context context) {
        this.context = context;
        this.promotions = new ArrayList<>();
    }

    public NearByPromotionAdapter(Context context, List<Promotion> promotions) {
        this.context = context;
        this.promotions = promotions;
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

    // Returns total number of pages
    @Override
    public int getCount() {
        return promotions.size();
    }

    @Override
    public Object instantiateItem(View container, int position) {
        final Promotion promotion = promotions.get(position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_item_nearby_promotion, null);

        addControls(view);


        tvTitle.setText(promotion.getTitle());
        tvDiscount.setText("-" + promotion.getDiscount() + promotion.getDiscountType());
        tvCommentCount.setText(promotion.getCommentCount() + "");
        tvPinnedCount.setText(promotion.getPinnedCount() + "");

        String remainTime = DateTimeConverter.getRemainTime(promotion.getEndDate());
        tvRemainTime.setText(remainTime);

        Picasso.with(context)
                .load(promotion.getProvider().getAvatar())
                .fit()
                .centerCrop()
                .into(imvProviderAvatar);
        tvProviderAddress.setText(promotion.getProvider().getAddress());

        btnGetVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Get voucher on click" + promotion.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        ((ViewPager) container).addView(view, position);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((View) obj);
    }

    void addControls(View view) {
        imvProviderAvatar = (ImageView) view.findViewById(R.id.imvProviderAvatar);
        tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
        tvCommentCount = (TextView) view.findViewById(R.id.tvCommentCount);
        tvPinnedCount = (TextView) view.findViewById(R.id.tvPinnedCount);
        tvProviderAddress = (TextView) view.findViewById(R.id.tvProviderAddress);
        tvRemainTime = (TextView) view.findViewById(R.id.tvRemainTime);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        btnGetVoucher = (Button) view.findViewById(R.id.btnGetVoucher);
    }
}


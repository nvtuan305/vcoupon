package com.happybot.vcoupon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.activity.DetailProviderActivity;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.SubscribeBody;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderViewHolder> {

    static public List<User> providers = new ArrayList<>();
    static public boolean[] followedProvider;
    private Context mContext = null;

    public ProviderAdapter() {
        providers = new ArrayList<>();
    }

    public ProviderAdapter(ArrayList<User> providers) {
        this.providers = providers;
    }

    @Override
    public ProviderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_small_item, null);
        mContext = layoutView.getContext();
        ProviderViewHolder holder = new ProviderViewHolder(layoutView);
        followedProvider = new boolean[providers.size()];
        for (boolean x : followedProvider) {
            x = false;
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(final ProviderViewHolder holder, final int position) {
        //Provider provider = providers.get(position);
        final User provider = providers.get(position);
        final int positionSubscribe = position;
        // Load user info
        Picasso.with(mContext)
                .load(provider.getAvatar())
                .fit()
                .centerCrop()
                .error(R.drawable.ic_avatar_default)
                .into(holder.provider_small_item_avatar);
        holder.provider_small_item_name.setText(provider.getName());
        holder.provider_small_item_address.setText(provider.getAddress());

        holder.provider_small_item_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePreferenceHelper helper = new SharePreferenceHelper(view.getContext());
                UserRetrofitService userRetrofitService = new UserRetrofitService(view.getContext());

                if (followedProvider[positionSubscribe]) {
                    userRetrofitService.unfollowPromotion(helper.getUserId(), provider.getId(), new SubscribeDelegate((BaseActivity) view.getContext(), holder, positionSubscribe));
                } else {
                    SubscribeBody subscribeBody = new SubscribeBody(provider.getId(), "PROVIDER");
                    userRetrofitService.followPromotion(helper.getUserId(), subscribeBody, new SubscribeDelegate((BaseActivity) view.getContext(), holder, positionSubscribe));
                }
            }
        });

        // Disable or enabled get voucher button
        if (provider.isFollowing()) {
            holder.provider_small_item_follow.setText(R.string.unfollow_title);
        } else {
            holder.provider_small_item_follow.setText(R.string.follow_title);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailProviderActivity.class);
                intent.putExtra("DetailProvider", provider);
                intent.putExtra("Followed", followedProvider[positionSubscribe]);
                intent.putExtra("Position", position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return providers.size();
    }

    public void addData(List<User> providerList) {
        providers.addAll(providerList);
        notifyDataSetChanged();
    }

    public void clearData() {
        providers.clear();
        notifyDataSetChanged();
    }

    public void updateData(List<User> providerList) {
        providers.clear();
        providers.addAll(providerList);
        notifyDataSetChanged();
    }

    public class SubscribeDelegate extends ForegroundTaskDelegate<ResponseObject> {

        ProviderViewHolder holderSubscribe;
        int positionSubscribe;
        AppCompatActivity activitySubscribe;

        SubscribeDelegate(BaseActivity activity, ProviderViewHolder holder, int position) {
            super(activity);
            this.holderSubscribe = holder;
            this.positionSubscribe = position;
            this.activitySubscribe = activity;
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(ResponseObject responseObject, Throwable throwable) {
            super.onPostExecute(responseObject, throwable);
            if (throwable == null && responseObject != null && shouldHandleResultForActivity()) {
                Toast.makeText((Context) activitySubscribe, responseObject.getResultMessage(), Toast.LENGTH_LONG).show();
                if (followedProvider[positionSubscribe]) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("PROVIDER_" + providers.get(positionSubscribe).getId());
                    holderSubscribe.provider_small_item_follow.setText(R.string.follow_title);
                    followedProvider[positionSubscribe] = false;
                } else {
                    FirebaseMessaging.getInstance().subscribeToTopic("PROVIDER_" + providers.get(positionSubscribe).getId());
                    holderSubscribe.provider_small_item_follow.setText(R.string.unfollow_title);
                    followedProvider[positionSubscribe] = true;
                }
            } else {
                Toast.makeText((Context) activitySubscribe, throwable.getMessage(), Toast.LENGTH_LONG).show();
                if (followedProvider[positionSubscribe]) {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("PROVIDER_" + providers.get(positionSubscribe).getId());
                    holderSubscribe.provider_small_item_follow.setText(R.string.follow_title);
                    followedProvider[positionSubscribe] = false;
                } else {
                    FirebaseMessaging.getInstance().subscribeToTopic("PROVIDER_" + providers.get(positionSubscribe).getId());
                    holderSubscribe.provider_small_item_follow.setText(R.string.unfollow_title);
                    followedProvider[positionSubscribe] = true;
                }
            }
        }
    }
}

class ProviderViewHolder extends RecyclerView.ViewHolder {
    // View
    ImageView provider_small_item_avatar;
    TextView provider_small_item_name;
    TextView provider_small_item_address;
    Button provider_small_item_follow;

    ProviderViewHolder(final View itemView) {
        super(itemView);

        provider_small_item_avatar = (ImageView) itemView.findViewById(R.id.provider_small_item_avatar);
        provider_small_item_name = (TextView) itemView.findViewById(R.id.provider_small_item_name);
        provider_small_item_address = (TextView) itemView.findViewById(R.id.provider_small_item_address);
        provider_small_item_follow = (Button) itemView.findViewById(R.id.provider_small_item_follow);
    }
}

package com.happybot.vcoupon.fragment.category;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.adapter.PromotionAdapter;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.SubscribeBody;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.service.PromotionRetrofitService;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import java.util.List;

/**
 * Created by Nguyễn Phương Tuấn on 08-Dec-16.
 */

public class ClothesFragment extends Fragment {

    private static final String CLOTHES_CATEGORY_ID = "5842fbab0f0bc105b77eb74f";
    private boolean followedCategory;

    private SwipeRefreshLayout swipeRefreshLayoutClothes = null;
    private RecyclerView recyclerViewClothesPromotion = null;
    private LinearLayout emptyLayoutClothes = null;
    private View progressDialog = null;

    private LinearLayoutManager mLinearLayoutManager = null;
    private BaseActivity activity = null;
    private Context mContext = null;
    private PromotionAdapter adapter = new PromotionAdapter();

    // Delegate for api
    private ClothesFragment.GetClothesPromotionsDelegate getClothesPromotionsDelegate = null;
    private int currentPage = 1;

    private boolean canScroll = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    private Button btnFollowClothesCategory;

    public ClothesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clothes, container, false);

        swipeRefreshLayoutClothes = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayoutClothes);
        recyclerViewClothesPromotion = (RecyclerView) view.findViewById(R.id.recyclerViewClothesPromotion);
        recyclerViewClothesPromotion.setAdapter(adapter);
        recyclerViewClothesPromotion.setNestedScrollingEnabled(false);
        emptyLayoutClothes = (LinearLayout) view.findViewById(R.id.emptyLayoutClothes);
        progressDialog = view.findViewById(R.id.progressDialog);
        mContext = view.getContext();
        btnFollowClothesCategory = (Button) view.findViewById(R.id.btnFollowClothesCategory);
        followedCategory = false;
        if (followedCategory) {
            btnFollowClothesCategory.setText(R.string.unfollow_title);
        }
        else {
            btnFollowClothesCategory.setText(R.string.follow_title);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Subscribe category
        btnFollowClothesCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceHelper helper = new SharePreferenceHelper(v.getContext());
                UserRetrofitService userRetrofitService = new UserRetrofitService(v.getContext());

                if (followedCategory) {
                    userRetrofitService.unfollowPromotion(helper.getUserId(), CLOTHES_CATEGORY_ID, new ClothesFragment.SubscribeDelegate((BaseActivity)v.getContext()));
                }
                else {
                    SubscribeBody subscribeBody = new SubscribeBody(CLOTHES_CATEGORY_ID, "CATEGORY");
                    userRetrofitService.followPromotion(helper.getUserId(), subscribeBody, new ClothesFragment.SubscribeDelegate((BaseActivity)v.getContext()));
                }
            }
        });
        // Initialize recycle view
        recyclerViewClothesPromotion.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerViewClothesPromotion.setLayoutManager(mLinearLayoutManager);

        activity = (BaseActivity) getActivity();
        getClothesPromotionsDelegate = new GetClothesPromotionsDelegate(activity);


        // Load Clothes promotion
        loadClothesPromotion();

        // Initialize swipe refresh Layout
        swipeRefreshLayoutClothes.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        // Listener for load more data
        recyclerViewClothesPromotion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // Scroll down
                if (dy > 0) {
                    visibleItemCount = mLinearLayoutManager.getChildCount();
                    totalItemCount = mLinearLayoutManager.getItemCount();
                    pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();

                    if (canScroll) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            canScroll = false;
                            Log.d("KKKK", "Last Item Wow !");
                            loadClothesPromotion();
                        }
                    }
                }
            }
        });
    }

    //
    private void refreshData() {
        // Reset page to 1
        currentPage = 1;

        // Refresh data
        loadClothesPromotion();
    }

    public void loadClothesPromotion() {

        PromotionRetrofitService promotionRetrofitService = new PromotionRetrofitService(mContext);
        promotionRetrofitService.getPromotionByCategory(CLOTHES_CATEGORY_ID, currentPage, getClothesPromotionsDelegate);

        // Update next page
        currentPage++;
    }

    private class GetClothesPromotionsDelegate extends ForegroundTaskDelegate<List<Promotion>> {

        GetClothesPromotionsDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            if (!swipeRefreshLayoutClothes.isRefreshing()) {
                progressDialog.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPostExecute(List<Promotion> promotions, Throwable throwable) {
            super.onPostExecute(promotions, throwable);

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && promotions != null && shouldHandleResultForActivity()) {

                // Reset data when swipe to refresh data
                if (swipeRefreshLayoutClothes.isRefreshing()) {
                    adapter.updateData(promotions);
                    swipeRefreshLayoutClothes.setRefreshing(false);

                } else {
                    adapter.addData(promotions);
                }

                // Disable swipe down to load more if has no more promotion
                canScroll = promotions.size() > 0;
            }

            // Hide progress dialog
            progressDialog.setVisibility(View.GONE);

            // Show empty layout without any promotions
            showView();
        }
    }

    /**
     * Show empty layout if server response empty data
     */
    public void showView() {

        if (adapter.getItemCount() <= 0) {
            recyclerViewClothesPromotion.setVisibility(View.GONE);
            emptyLayoutClothes.setVisibility(View.VISIBLE);

        } else {
            recyclerViewClothesPromotion.setVisibility(View.VISIBLE);
            emptyLayoutClothes.setVisibility(View.GONE);
        }
    }
    private class SubscribeDelegate extends ForegroundTaskDelegate<ResponseObject> {

        SubscribeDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
        }

        @Override
        public void onPostExecute(ResponseObject responseObject, Throwable throwable) {
            super.onPostExecute(responseObject, throwable);
            if (throwable == null && responseObject != null && shouldHandleResultForActivity()) {
                Toast.makeText(getContext(), responseObject.getResultMessage(), Toast.LENGTH_LONG).show();
                if (followedCategory) {
                    btnFollowClothesCategory.setText(R.string.follow_title);
                    followedCategory = false;
                } else {
                    btnFollowClothesCategory.setText(R.string.unfollow_title);
                    followedCategory = true;
                }
            } else {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                if (followedCategory) {
                    btnFollowClothesCategory.setText(R.string.follow_title);
                    followedCategory = false;
                } else {
                    btnFollowClothesCategory.setText(R.string.unfollow_title);
                    followedCategory = true;
                }
            }
        }
    }
}
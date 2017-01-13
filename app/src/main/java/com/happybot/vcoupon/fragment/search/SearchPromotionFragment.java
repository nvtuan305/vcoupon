package com.happybot.vcoupon.fragment.search;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.adapter.PromotionAdapter;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.service.PromotionRetrofitService;
import com.happybot.vcoupon.service.UserRetrofitService;

import java.util.List;

/**
 * Created by Nguyễn Phương Tuấn on 11-Jan-17.
 */

public class SearchPromotionFragment extends Fragment {

    private String searchQuery = "Tap to Search";
    private SwipeRefreshLayout srSearchPromotionParentLayout = null;
    private RecyclerView rcvSearchPromotion = null;
    private LinearLayout lnSearchPromotionEmptyLayout = null;
    private View progressDialog = null;

    private LinearLayoutManager mLinearLayoutManager = null;
    private BaseActivity activity = null;
    private Context mContext = null;
    private PromotionAdapter adapter = null;

    // Delegate for api
    private GetSearchPromotionTripDelegate getSearchPromotionTripDelegate = null;
    private int currentPage = 1;

    private boolean canScroll = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    public SearchPromotionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_promotion, container, false);

        srSearchPromotionParentLayout = (SwipeRefreshLayout) view.findViewById(R.id.srSearchPromotionParentLayout);
        rcvSearchPromotion = (RecyclerView) view.findViewById(R.id.rcvSearchPromotion);
        lnSearchPromotionEmptyLayout = (LinearLayout) view.findViewById(R.id.lnSearchPromotionEmptyLayout);
        progressDialog = view.findViewById(R.id.progressDialog);
        mContext = view.getContext();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycle view
        rcvSearchPromotion.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        rcvSearchPromotion.setLayoutManager(mLinearLayoutManager);

        activity = (BaseActivity) getActivity();
        getSearchPromotionTripDelegate = new GetSearchPromotionTripDelegate(activity);


        // Load pinned promotion
        //loadPinnedPromotion();

        // Initialize srParentLayout
        srSearchPromotionParentLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        // Listener for load more data
        rcvSearchPromotion.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            loadSearchPromotion();
                        }
                    }
                }
            }
        });
    }

    /**
     * Refresh data when swipe down
     */
    private void refreshData() {
        // Reset page to 1
        currentPage = 1;

        // Refresh data
        loadSearchPromotion();
    }

    public void loadSearchPromotion() {

        PromotionRetrofitService promotionRetrofitService = new PromotionRetrofitService(mContext);
        promotionRetrofitService.getPromotionBySearch(searchQuery, currentPage, getSearchPromotionTripDelegate);

        // Update next page
        currentPage++;
    }

    private class GetSearchPromotionTripDelegate extends ForegroundTaskDelegate<List<Promotion>> {

        GetSearchPromotionTripDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            if (!srSearchPromotionParentLayout.isRefreshing()) {
                progressDialog.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPostExecute(List<Promotion> promotions, Throwable throwable) {
            super.onPostExecute(promotions, throwable);

            if (adapter == null) {
                adapter = new PromotionAdapter();
                rcvSearchPromotion.setAdapter(adapter);
            }

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && promotions != null && shouldHandleResultForActivity()) {

                // Reset data when swipe to refresh data
                if (srSearchPromotionParentLayout.isRefreshing()) {
                    adapter.updateData(promotions);
                    srSearchPromotionParentLayout.setRefreshing(false);

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
            rcvSearchPromotion.setVisibility(View.GONE);
            lnSearchPromotionEmptyLayout.setVisibility(View.VISIBLE);

        } else {
            rcvSearchPromotion.setVisibility(View.VISIBLE);
            lnSearchPromotionEmptyLayout.setVisibility(View.GONE);
        }
    }

    public void updateSearch(String searchQuery) {
        currentPage = 1;
        this.searchQuery = searchQuery;
        loadSearchPromotion();
    }
}

package com.happybot.vcoupon.fragment;

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

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.adapter.PromotionAdapter;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import java.util.List;

public class PinnedPromotionFragment extends Fragment {

    private SwipeRefreshLayout srParentLayout = null;
    private RecyclerView rcvPinnedPromotion = null;
    private LinearLayout emptyLayout = null;
    private View progressDialog = null;

    private LinearLayoutManager mLinearLayoutManager = null;
    private BaseActivity activity = null;
    private Context mContext = null;
    private PromotionAdapter adapter = null;

    // Delegate for api
    private GetPinnedTripDelegate getPinnedTripDelegate = null;
    private int currentPage = 1;

    private boolean canScroll = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    public PinnedPromotionFragment() {
        // TODO here
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pinned, container, false);
        srParentLayout = (SwipeRefreshLayout) view.findViewById(R.id.srParentLayout);
        rcvPinnedPromotion = (RecyclerView) view.findViewById(R.id.rcvPinnedPromotion);
        emptyLayout = (LinearLayout) view.findViewById(R.id.lnEmptyLayout);
        progressDialog = view.findViewById(R.id.progressDialog);
        mContext = view.getContext();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycle view
        rcvPinnedPromotion.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        rcvPinnedPromotion.setLayoutManager(mLinearLayoutManager);

        activity = (BaseActivity) getActivity();
        getPinnedTripDelegate = new GetPinnedTripDelegate(activity);

        // Load pinned promotion
        loadPinnedPromotion();

        // Initialize srParentLayout
        srParentLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        // Listener for load more data
        rcvPinnedPromotion.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            loadPinnedPromotion();
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
        loadPinnedPromotion();
    }

    public void loadPinnedPromotion() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(mContext);
        //helper.initializeSampleAuth();

        UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);
        userRetrofitService.getPinnedPromotion(helper.getUserId(), currentPage, getPinnedTripDelegate);

        // Update next page
        currentPage++;
    }

    private class GetPinnedTripDelegate extends ForegroundTaskDelegate<List<Promotion>> {

        GetPinnedTripDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            if (!srParentLayout.isRefreshing()) {
                progressDialog.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPostExecute(List<Promotion> promotions, Throwable throwable) {
            super.onPostExecute(promotions, throwable);

            if (adapter == null) {
                adapter = new PromotionAdapter();
                rcvPinnedPromotion.setAdapter(adapter);
            }

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && promotions != null && shouldHandleResultForActivity()) {

                // Reset data when swipe to refresh data
                if (srParentLayout.isRefreshing()) {
                    adapter.updateData(promotions);
                    srParentLayout.setRefreshing(false);

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
            rcvPinnedPromotion.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);

        } else {
            rcvPinnedPromotion.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }
}
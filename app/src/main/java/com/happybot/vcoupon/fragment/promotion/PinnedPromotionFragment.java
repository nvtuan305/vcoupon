package com.happybot.vcoupon.fragment.promotion;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.lang.ref.WeakReference;
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
    private UserRetrofitService userRetrofitService = null;
    private int currentPage = 1;

    private boolean canScroll = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    private SharePreferenceHelper helper = null;

    public PinnedPromotionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_promotion_pinned, container, false);
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

        // Initialize retrofit service
        activity = (BaseActivity) getActivity();
        userRetrofitService = new UserRetrofitService(mContext);
        getPinnedTripDelegate = new GetPinnedTripDelegate(activity, this);
        activity.listOfForegroundTaskDelegates.add(getPinnedTripDelegate);

        // Initialize SharePreferenceHelper
        helper = new SharePreferenceHelper(mContext);

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

        if (helper != null) {
            // Initialize auth info for testing
            userRetrofitService.getPinnedPromotion(helper.getUserId(),
                    currentPage,
                    getPinnedTripDelegate);

            // Update next page
            currentPage++;
        }
    }

    private class GetPinnedTripDelegate extends ForegroundTaskDelegate<List<Promotion>> {

        WeakReference<PinnedPromotionFragment> fragmentWeakReference = null;

        GetPinnedTripDelegate(BaseActivity activity) {
            super(activity);
        }

        GetPinnedTripDelegate(BaseActivity activity, PinnedPromotionFragment fragment) {
            super(activity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPreExecute() {
            PinnedPromotionFragment fragment = fragmentWeakReference.get();
            if (fragment != null) {
                if (!srParentLayout.isRefreshing()) {
                    fragment.progressDialog.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onPostExecute(List<Promotion> promotions, Throwable throwable) {
            super.onPostExecute(promotions, throwable);

            PinnedPromotionFragment fragment = fragmentWeakReference.get();
            if (fragment != null) {

                if (fragment.adapter == null) {
                    fragment.adapter = new PromotionAdapter();
                    fragment.rcvPinnedPromotion.setAdapter(adapter);
                }

                // If no error occur, server response data, fragment is not destroyed
                if (throwable == null && promotions != null && shouldHandleResultForActivity()) {

                    // Reset data when swipe to refresh data
                    if (fragment.srParentLayout.isRefreshing()) {
                        fragment.adapter.updateData(promotions);
                        fragment.srParentLayout.setRefreshing(false);

                    } else {
                        fragment.adapter.addData(promotions);
                    }

                    // Disable swipe down to load more if has no more promotion
                    fragment.canScroll = promotions.size() > 0;
                }

                // Hide progress dialog
                fragment.progressDialog.setVisibility(View.GONE);

                // Show empty layout without any promotions
                fragment.showView();
            }
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

    @Override
    public void onDestroy() {
        if (getPinnedTripDelegate != null)
            getPinnedTripDelegate.cancel();

        super.onDestroy();
    }
}
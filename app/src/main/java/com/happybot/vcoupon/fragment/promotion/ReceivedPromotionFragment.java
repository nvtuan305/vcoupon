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
import com.happybot.vcoupon.adapter.VoucherAdapter;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.Voucher;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import java.lang.ref.WeakReference;
import java.util.List;

public class ReceivedPromotionFragment extends Fragment {

    private SwipeRefreshLayout srParentLayout = null;
    private RecyclerView rcvReceiveVoucher = null;
    private LinearLayout emptyLayout = null;
    private View progressDialog = null;

    private LinearLayoutManager mLinearLayoutManager = null;
    private BaseActivity activity = null;
    private Context mContext = null;
    private VoucherAdapter adapter = null;

    // Delegate for api
    private GetReceivedVoucherDelegate getReceivedVoucherDelegate = null;
    private UserRetrofitService userRetrofitService = null;
    private int currentPage = 1;

    private boolean canScroll = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    private SharePreferenceHelper helper = null;

    public ReceivedPromotionFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_promotion_received, container, false);
        srParentLayout = (SwipeRefreshLayout) view.findViewById(R.id.srParentLayout);
        rcvReceiveVoucher = (RecyclerView) view.findViewById(R.id.rcvReceivedPromotion);
        emptyLayout = (LinearLayout) view.findViewById(R.id.lnEmptyLayout);
        progressDialog = view.findViewById(R.id.progressDialog);
        mContext = view.getContext();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycle view
        rcvReceiveVoucher.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        rcvReceiveVoucher.setLayoutManager(mLinearLayoutManager);

        // Initialize retrofit service
        activity = (BaseActivity) getActivity();
        userRetrofitService = new UserRetrofitService(mContext);
        getReceivedVoucherDelegate = new GetReceivedVoucherDelegate(activity, this);
        activity.listOfForegroundTaskDelegates.add(getReceivedVoucherDelegate);

        // Initialize SharePreferenceHelper
        helper = new SharePreferenceHelper(mContext);

        // Load pinned promotion
        loadReceivedVoucher();

        // Initialize srParentLayout
        srParentLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        // Listener for load more data
        rcvReceiveVoucher.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            loadReceivedVoucher();
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
        loadReceivedVoucher();
    }

    public void loadReceivedVoucher() {

        if (helper != null) {
            // Initialize auth info for testing
            userRetrofitService.getReceivedVoucher(helper.getUserId(),
                    currentPage,
                    getReceivedVoucherDelegate);

            // Update next page
            currentPage++;
        }
    }

    private class GetReceivedVoucherDelegate extends ForegroundTaskDelegate<List<Voucher>> {

        WeakReference<ReceivedPromotionFragment> fragmentWeakReference = null;

        GetReceivedVoucherDelegate(BaseActivity activity) {
            super(activity);
        }

        GetReceivedVoucherDelegate(BaseActivity activity, ReceivedPromotionFragment fragment) {
            super(activity);
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void onPreExecute() {
            ReceivedPromotionFragment fragment = fragmentWeakReference.get();
            if (fragment != null) {
                if (!srParentLayout.isRefreshing()) {
                    fragment.progressDialog.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onPostExecute(List<Voucher> vouchers, Throwable throwable) {
            super.onPostExecute(vouchers, throwable);

            ReceivedPromotionFragment fragment = fragmentWeakReference.get();
            if (fragment != null) {

                if (fragment.adapter == null) {
                    fragment.adapter = new VoucherAdapter();
                    fragment.rcvReceiveVoucher.setAdapter(adapter);
                }

                // If no error occur, server response data, fragment is not destroyed
                if (throwable == null && vouchers != null && shouldHandleResultForActivity()) {

                    // Reset data when swipe to refresh data
                    if (fragment.srParentLayout.isRefreshing()) {
                        fragment.adapter.updateData(vouchers);
                        fragment.srParentLayout.setRefreshing(false);

                    } else {
                        fragment.adapter.addData(vouchers);
                    }

                    // Disable swipe down to load more if has no more promotion
                    fragment.canScroll = vouchers.size() > 0;
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
            rcvReceiveVoucher.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);

        } else {
            rcvReceiveVoucher.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        if (getReceivedVoucherDelegate != null)
            getReceivedVoucherDelegate.cancel();

        super.onDestroy();
    }
}
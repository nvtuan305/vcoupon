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

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.adapter.ProviderAdapter;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.service.UserRetrofitService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyễn Phương Tuấn on 11-Jan-17.
 */

public class SearchProviderFragment extends Fragment {

    private SwipeRefreshLayout srSearchProviderParentLayout = null;
    private RecyclerView rcvSearchProvider = null;
    private LinearLayout lnSearchProviderEmptyLayout = null;
    private View progressDialog = null;

    private LinearLayoutManager mLinearLayoutManager = null;
    private BaseActivity activity = null;
    private Context mContext = null;
    private ProviderAdapter adapter = null;

    // Delegate for api
    private GetSearchProviderTripDelegate getSearchProviderTripDelegate = null;
    private int currentPage = 1;
    private String searchQuery = "";

    private boolean canScroll = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    public SearchProviderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_provider, container, false);
        srSearchProviderParentLayout = (SwipeRefreshLayout) view.findViewById(R.id.srSearchProviderParentLayout);
        rcvSearchProvider = (RecyclerView) view.findViewById(R.id.rcvSearchProvider);
        lnSearchProviderEmptyLayout = (LinearLayout) view.findViewById(R.id.lnSearchProviderEmptyLayout);
        progressDialog = view.findViewById(R.id.progressDialog);
        mContext = view.getContext();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycle view
        rcvSearchProvider.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        rcvSearchProvider.setLayoutManager(mLinearLayoutManager);

        activity = (BaseActivity) getActivity();
        getSearchProviderTripDelegate = new SearchProviderFragment.GetSearchProviderTripDelegate(activity);


        // Load SearchProvider
        //loadSearchProvider();

        // Initialize srSearchProviderParentLayout
        srSearchProviderParentLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        // Listener for load more data
        rcvSearchProvider.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                            loadSearchProvider();
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
        loadSearchProvider();
    }

    public void loadSearchProvider() {

        UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);
        userRetrofitService.getSearchProvider(searchQuery, currentPage, getSearchProviderTripDelegate);

        // Update next page
        currentPage++;
    }

    private class GetSearchProviderTripDelegate extends ForegroundTaskDelegate<List<User>> {

        GetSearchProviderTripDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            if (!srSearchProviderParentLayout.isRefreshing()) {
                progressDialog.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPostExecute(List<User> providers, Throwable throwable) {
            super.onPostExecute(providers, throwable);

            if (adapter == null) {
                adapter = new ProviderAdapter();
                rcvSearchProvider.setAdapter(adapter);
            }

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && providers != null && shouldHandleResultForActivity()) {

                // Reset data when swipe to refresh data
                if (srSearchProviderParentLayout.isRefreshing()) {
                    adapter.updateData(providers);
                    srSearchProviderParentLayout.setRefreshing(false);

                } else {
                    adapter.addData(providers);
                }

                // Disable swipe down to load more if has no more promotion
                canScroll = providers.size() > 0;
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
            rcvSearchProvider.setVisibility(View.GONE);
            lnSearchProviderEmptyLayout.setVisibility(View.VISIBLE);

        } else {
            rcvSearchProvider.setVisibility(View.VISIBLE);
            lnSearchProviderEmptyLayout.setVisibility(View.GONE);
        }
    }
    public void updateSearch(String searchQuery) {
        currentPage = 1;
        this.searchQuery = searchQuery;
        loadSearchProvider();
    }
}
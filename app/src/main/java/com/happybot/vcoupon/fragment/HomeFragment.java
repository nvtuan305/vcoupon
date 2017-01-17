package com.happybot.vcoupon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.adapter.CustomSwipeAdapter;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.fragment.category.CategoryFragment;
import com.happybot.vcoupon.model.SubscribingTopic;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Nguyễn Phương Tuấn on 05-Dec-16.
 */

public class HomeFragment extends Fragment {

    ViewPager viewPager;
    CustomSwipeAdapter adapter;
    RelativeLayout near_by, food, clothes,technology;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        Log.d("FIREBASE ", "Refreshed token: " + refreshedToken);
        //Subscribe FCM Topic
        SharePreferenceHelper helper = new SharePreferenceHelper(getApplicationContext());
        UserRetrofitService userRetrofitService = new UserRetrofitService(getActivity());
        userRetrofitService.getSubscribingTopic(helper.getUserId(), new SubscribingTopicDelegate((BaseActivity) getActivity()));

        //set up slide show
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        adapter = new CustomSwipeAdapter(this.getActivity());
        viewPager.setAdapter(adapter);
        CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        near_by = (RelativeLayout) view.findViewById(R.id.home_nearby);
        food = (RelativeLayout) view.findViewById(R.id.home_food);
        clothes = (RelativeLayout) view.findViewById(R.id.home_clothes);
        technology = (RelativeLayout) view.findViewById(R.id.home_technology);

        near_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("position", "0");
                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_container, categoryFragment, "home");
                ft.addToBackStack("home");
                ft.commit();
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("position", "1");
                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_container, categoryFragment, "food");
                ft.addToBackStack("food");
                ft.commit();
            }
        });

        clothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("position", "2");
                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_container, categoryFragment, "clothes");
                ft.addToBackStack("clothes");
                ft.commit();
            }
        });

        technology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("position", "3");
                CategoryFragment categoryFragment = new CategoryFragment();
                categoryFragment.setArguments(bundle);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.main_container, categoryFragment, "technology");
                ft.addToBackStack("technology");
                ft.commit();
            }
        });

        return view;
    }
    class SubscribingTopicDelegate extends ForegroundTaskDelegate<List<SubscribingTopic>> {

        SubscribingTopicDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(List<SubscribingTopic> subscribingTopics, Throwable throwable) {
            super.onPostExecute(subscribingTopics, throwable);
            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && subscribingTopics != null && shouldHandleResultForActivity()) {
                //Subscribe FCM Topic
                for (SubscribingTopic subscribingTopic :subscribingTopics) {
                    FirebaseMessaging.getInstance().subscribeToTopic(subscribingTopic.getSubscribeType() + "_" + subscribingTopic.get_publisherId());
                }
            }
        }
    }
}

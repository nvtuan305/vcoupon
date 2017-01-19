package com.happybot.vcoupon.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.adapter.NotifyRecyclerAdapter;
import com.happybot.vcoupon.model.Notify;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nguyễn Phương Tuấn on 05-Dec-16.
 */


/**
 * Created by Admin on 05-Dec-16.
 */
public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotifyRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Notify> listNotify = new ArrayList<Notify>();

    public NotificationFragment() {

        //Init data
        listNotify.add(
                new Notify(R.drawable.provider_logo_demo, "Giảm giá đến 40% ngày BlackFriday tại FassiveShop", "Monday"));
        listNotify.add(
                new Notify(R.drawable.provider_logo_demo, "Bùng nổ cùng ngày mua sắm với FassiveShop khuyến mãi lên đến 50%", "2h"));
        listNotify.add(
                new Notify(R.drawable.sale_02, "Giảm giá đến 40% nhân ngày khai trương shop tại Điện Biên Phủ", "1h"));
        listNotify.add(
                new Notify(R.drawable.sale_02, "Giảm giá đến 40% nhân ngày khai trương shop tại Điện Biên Phủ", "1h"));
        listNotify.add(
                new Notify(R.drawable.sale_02, "Giảm giá đến 40% nhân ngày khai trương shop tại Điện Biên Phủ", "1h"));
        listNotify.add(
                new Notify(R.drawable.sale_01, "Giảm giá đến 40% ngày BlackFriday tại FassiveShop", "1h"));
        listNotify.add(
                new Notify(R.drawable.sale_01, "Giảm giá đến 40% ngày BlackFriday tại FassiveShop", "1h"));
        listNotify.add(
                new Notify(R.drawable.sale_01, "Giảm giá đến 40% ngày BlackFriday tại FassiveShop", "1h"));
        listNotify.add(
                new Notify(R.drawable.sale_01, "Giảm giá đến 40% ngày BlackFriday tại FassiveShop", "1h"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // Connect view
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerNotify);

        // If the size of views will not change as the data changes.
        recyclerView.setHasFixedSize(true);

        // Setting the LayoutManager.
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Setting the adapter.
        adapter = new NotifyRecyclerAdapter(listNotify); // Dữ liệu truyền vào
        recyclerView.setAdapter(adapter);

        return view;
    }
}



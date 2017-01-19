package com.happybot.vcoupon.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.model.Address;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.util.BitmapHelper;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.padding;

/**
 * Created by TQV on 1/16/2017.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private Promotion promotion;

    public MapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        promotion = intent.getParcelableExtra("DetailPromotion");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    public void addMarker(GoogleMap googleMap, String title, double lat, double lng) {
        Bitmap icon = BitmapHelper.getBitmapFromXmlLayout(getContext(), R.drawable.ic_location_small);

        MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng))
                .title(title)
                .icon(BitmapDescriptorFactory.fromBitmap(icon));

        googleMap.addMarker(marker);
    }

    LatLngBounds.Builder builder = new LatLngBounds.Builder();

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (promotion != null) {
            List<Address> addressList = promotion.getAddresses();

            MapsInitializer.initialize(getActivity().getApplicationContext());

            mGoogleMap = googleMap;
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mGoogleMap.getUiSettings().setMapToolbarEnabled(false);



            for (int i = 0; i < addressList.size(); i++) {
                double lat = addressList.get(i).getLatitude();
                double lng = addressList.get(i).getLongitude();

                addMarker(mGoogleMap, promotion.getProvider().getAddress(), lat, lng);
                builder.include(new LatLng(lat, lng));
            }


        }

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
                mGoogleMap.moveCamera(cu);
                mGoogleMap.animateCamera(cu);
            }
        });
    }



    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
}
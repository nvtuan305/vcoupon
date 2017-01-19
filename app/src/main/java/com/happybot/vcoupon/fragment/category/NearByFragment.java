package com.happybot.vcoupon.fragment.category;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.adapter.NearByPromotionAdapter;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.AddressRequestBody;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.service.PromotionRetrofitService;
import com.happybot.vcoupon.util.BitmapHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NearByFragment extends Fragment {

    View view;
    private GetNearByPromotionDelegate getNearByPromotionDelegate = null;
    NearByPromotionAdapter adapter;
    ViewPager vpPromotions;
    Context context;

    static final int RC_HANDLE_ALL_PERM = 1;
    static boolean RS_PERM = false;

    //Google map
    private GoogleMap googleMap;
    private SupportMapFragment mSupportMapFragment;
    LocationManager locationManager;
    Criteria criteria;

    public NearByFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nearby, container, false);
        getNearByPromotionDelegate = new GetNearByPromotionDelegate((BaseActivity) getActivity());
        context = this.getContext();
        locationManager = (LocationManager) this.getContext().getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();

        RS_PERM = checkPermissions();

        if (RS_PERM) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.map, mSupportMapFragment).commit();


            if (mSupportMapFragment != null) {
                mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap map) {
                        googleMap = map;
                        if (googleMap != null) {
                            googleMap.getUiSettings().setAllGesturesEnabled(true);

                            // For showing a move to my location button
                            googleMap.setMyLocationEnabled(true);

                            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
                            if (location != null) {
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                                        .zoom(14)                   // Sets the zoom
                                        .build();                   // Creates a CameraPosition from the builder
                                map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                Geocoder gCoder = new Geocoder(context);
                                List<Address> addresses = null;
                                try {
                                    addresses = gCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (addresses != null && addresses.size() > 0) {
                                    PromotionRetrofitService promotionRetrofitService = new PromotionRetrofitService(context);
                                    promotionRetrofitService.getNearByPromotion(new AddressRequestBody(location.getLongitude(), location.getLatitude(),
                                            addresses.get(0).getCountryName(), addresses.get(0).getAdminArea()), getNearByPromotionDelegate);
                                }
                            }


                        }
                    }
                });
            }
        }

        vpPromotions = (ViewPager) view.findViewById(R.id.vpPromotions);
        adapter = new NearByPromotionAdapter(this.getContext());
        vpPromotions.setAdapter(adapter);
        return view;
    }

    // Check all permission
    public boolean checkPermissions() {
        String[] requestPermission = new String[]{
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CHANGE_NETWORK_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.WRITE_SETTINGS};

        List<String> permissionNeedRequest = new ArrayList<>();
        int rs;

        for (String permission : requestPermission) {
            rs = ActivityCompat.checkSelfPermission(this.getContext(), permission);
            if (rs != PackageManager.PERMISSION_GRANTED)
                permissionNeedRequest.add(permission);
        }

        if (!permissionNeedRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this.getActivity(),
                    permissionNeedRequest.toArray(new String[permissionNeedRequest.size()]),
                    RC_HANDLE_ALL_PERM);
            return false;
        }

        return true;
    }

    // Handle result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode) {
            case RC_HANDLE_ALL_PERM:
                if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    RS_PERM = true;
                else
                    RS_PERM = false;

                break;

            default:
                break;
        }
    }

    private class GetNearByPromotionDelegate extends ForegroundTaskDelegate<List<Promotion>> {

        GetNearByPromotionDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(final List<Promotion> promotions, Throwable throwable) {
            super.onPostExecute(promotions, throwable);

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && promotions != null && shouldHandleResultForActivity()) {
                //Toast.makeText(mContext, user.getRole(), Toast.LENGTH_LONG).show();
                adapter.addData(promotions);
                vpPromotions.setOffscreenPageLimit(adapter.getCount() - 1);
                for (int i = 0; i < promotions.size(); i++) {
                    for (int j = 0; j < promotions.get(i).getAddresses().size(); j++) {
                        double lat = promotions.get(i).getAddresses().get(j).getLatitude();
                        double lng = promotions.get(i).getAddresses().get(j).getLongitude();

                        Bitmap icon = BitmapHelper.getBitmapFromXmlLayout(getContext(), R.drawable.ic_location_green);

                        MarkerOptions marker = new MarkerOptions().position(new LatLng(lat, lng))
                                .title(promotions.get(i).getProvider().getName())
                                .snippet(promotions.get(i).getAddresses().get(j).toString())
                                .icon(BitmapDescriptorFactory.fromBitmap(icon));

                        googleMap.addMarker(marker);
                    }
                }

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        LatLng latLng = marker.getPosition();

                        marker.showInfoWindow();
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(latLng)
                                .zoom(googleMap.getCameraPosition().zoom)
                                .build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                        for (int i = 0; i < promotions.size(); i++) {
                            for (int j = 0; j < promotions.get(i).getAddresses().size(); j++) {
                                double lat = promotions.get(i).getAddresses().get(j).getLatitude();
                                double lng = promotions.get(i).getAddresses().get(j).getLongitude();
                                if (latLng.latitude == lat && latLng.longitude == lng) {
                                    vpPromotions.setCurrentItem(i);
                                    return true;
                                }
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }
}

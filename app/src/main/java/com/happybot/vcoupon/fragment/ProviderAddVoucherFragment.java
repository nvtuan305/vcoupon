package com.happybot.vcoupon.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.Address;
import com.happybot.vcoupon.model.Promotion;
import com.happybot.vcoupon.model.PromotionBody;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.service.PromotionRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Nguyễn Phương Tuấn on 12-Jan-17.
 */

public class ProviderAddVoucherFragment extends Fragment {

    private static final String FOOD_CATEGORY_ID = "5842fbab0f0bc105b77eb74e";
    private static final String CLOTHES_CATEGORY_ID = "5842fbab0f0bc105b77eb74f";
    private static final String TECHNOLOGY_CATEGORY_ID = "5842fbab0f0bc105b77eb750";
    Spinner spinner;
    Button btn_provider_add_promotion;
    EditText provider_add_promotion_title,
            provider_add_promotion_condition,
            provider_add_promotion_amountLimit,
            provider_add_promotion_discount,
            provider_add_promotion_startTime,
            provider_add_promotion_endTime;
    String dateTime = "";

    public ProviderAddVoucherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_add_voucher, container, false);

        findView(view);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, getAllCategories());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        provider_add_promotion_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTime(view, 0);
            }
        });
        provider_add_promotion_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTime(view, 1);
            }
        });
        btn_provider_add_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInfo()) {
                    postPromotion(view);
                }
            }
        });


        return view;
    }

    public void setDateTime(View v, int type){
        final int typeView = type;
        final View view = v;
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        dateTime = hourOfDay + ":" + minute + " ";
                        final DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(),
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        dateTime += dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                        if (typeView == 0) {
                                            provider_add_promotion_startTime.setText(dateTime);
                                            convertDateTime(dateTime);
                                        }
                                        else {
                                            provider_add_promotion_endTime.setText(dateTime);
                                        }
                                    }
                                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                }, 8, 0, false);
        timePickerDialog.show();
    }

    public void findView(View v) {
        spinner = (Spinner) v.findViewById(R.id.provider_add_promotion_category);
        btn_provider_add_promotion = (Button) v.findViewById(R.id.btn_provider_add_promotion);
        provider_add_promotion_title = (EditText)  v.findViewById(R.id.provider_add_promotion_title);
        provider_add_promotion_condition = (EditText)  v.findViewById(R.id.provider_add_promotion_condition);
        provider_add_promotion_amountLimit = (EditText)  v.findViewById(R.id.provider_add_promotion_amountLimit);
        provider_add_promotion_discount = (EditText)  v.findViewById(R.id.provider_add_promotion_discount);
        provider_add_promotion_startTime = (EditText)  v.findViewById(R.id.provider_add_promotion_startTime);
        provider_add_promotion_endTime = (EditText)  v.findViewById(R.id.provider_add_promotion_endTime);
    }

    public void postPromotion(View view) {
        Address a = new Address("32", "Nguyễn Trãi", "Phường 3", "Quận 5", "TP.HCM", "Vietnam",10.758226, 106.6787392);
        List<Address> address = new ArrayList<Address>();
        address.add(a);
        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(view.getContext());
        PromotionBody promotionBody = new PromotionBody(getCategoryID(),                                                            //categoryID
                                                        sharePreferenceHelper.getUserId(),                                          //providerID
                                                        provider_add_promotion_title.getText().toString(),                          //title
                                                        "http://www.unimedia.vn/wp-content/uploads/2016/05/pt2000.jpg",             //cover
                                                        provider_add_promotion_condition.getText().toString(),                      //Condition
                                                        convertDateTime(provider_add_promotion_startTime.getText().toString()),     //start Time
                                                        convertDateTime(provider_add_promotion_endTime.getText().toString()),       //endTime
                                                        Integer.parseInt(provider_add_promotion_amountLimit.getText().toString()),  //Limit
                                                        Integer.parseInt(provider_add_promotion_discount.getText().toString()),     //% discount
                                                        address);                                                                   //addresses
        PromotionRetrofitService promotionRetrofitService = new PromotionRetrofitService(view.getContext());
        promotionRetrofitService.postPromotion(promotionBody, new PostPromotionsDelegate((BaseActivity) view.getContext()));
    }

    public long convertDateTime(String dateTime) {
        long unixtime = 0;
        SimpleDateFormat format = new SimpleDateFormat("hh:mm dd-MM-yyyy");
        format.setTimeZone(TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().getID()));
        try
        {
            unixtime = format.parse(dateTime).getTime();
            unixtime = unixtime/1000;
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return unixtime;
    }

    public String getCategoryID() {
        String[] categoriesID = getAllCategoriesID();
        return categoriesID[spinner.getSelectedItemPosition()];
    }

    public boolean checkInfo() {
        // Check data input
        if (provider_add_promotion_title.getText().toString().equals("")) {
            provider_add_promotion_title.setError(getString(R.string.provider_add_promotion_title_eror));
            return false;
        }

        if (provider_add_promotion_condition.getText().toString().equals("")) {
            provider_add_promotion_condition.setError(getString(R.string.provider_add_promotion_condition_eror));
            return false;
        }
        if (provider_add_promotion_amountLimit.getText().toString().equals("")) {
            provider_add_promotion_amountLimit.setError(getString(R.string.provider_add_promotion_amountLimit_eror));
            return false;
        }
        if (provider_add_promotion_discount.getText().toString().equals("")) {
            provider_add_promotion_discount.setError(getString(R.string.provider_add_promotion_discount_eror));
            return false;
        }
        if (provider_add_promotion_startTime.getText().toString().equals("")) {
            provider_add_promotion_startTime.setError(getString(R.string.provider_add_promotion_startTime_eror));
            return false;
        }
        if (provider_add_promotion_endTime.getText().toString().equals("")) {
            provider_add_promotion_endTime.setError(getString(R.string.provider_add_promotion_endTime_eror));
            return false;
        }
        return true;
    }

    public String[] getAllCategories() {

        //call api getAllCategories
        String[] categories = new String[] {"FOOD",
                                            "CLOTHES",
                                            "TECHNOLOGY"
                                        };
        return categories;
    }

    public String[] getAllCategoriesID() {

        //call api getAllCategoriesID
        String[] categoriesID = new String[] {  "5842fbab0f0bc105b77eb74e",
                                                "5842fbab0f0bc105b77eb74f",
                                                "5842fbab0f0bc105b77eb750"
        };
        return categoriesID;
    }

    private class PostPromotionsDelegate extends ForegroundTaskDelegate<ResponseObject> {

        PostPromotionsDelegate(BaseActivity activity) {
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
                clearView();
            } else {
                Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void clearView() {
        //clear all view for next promotion
    }
}
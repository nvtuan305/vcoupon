package com.happybot.vcoupon.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.Address;
import com.happybot.vcoupon.model.PromotionBody;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.service.PromotionRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Nguyễn Phương Tuấn on 12-Jan-17.
 */

public class ProviderAddVoucherFragment extends Fragment {

    private static final String FIREBASE_STORAGE = "gs://vcoupon-1275f.appspot.com/";
    Spinner provider_add_promotion_category, provider_add_promotion_discount_type;
    Button btn_provider_add_promotion;
    LinearLayout provider_add_promotion_choose_image;
    ImageView provider_add_promotion_chosen_image;
    CheckBox provider_add_promotion_isOneCode;
    EditText provider_add_promotion_image_url,
            provider_add_promotion_title,
            provider_add_promotion_condition,
            provider_add_promotion_amountLimit,
            provider_add_promotion_discount,
            provider_add_promotion_startTime,
            provider_add_promotion_endTime,
            provider_add_promotion_address;
    String dateTime = "";
    List<Address> addressList = new ArrayList<Address>();

    public ProviderAddVoucherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_add_voucher, container, false);

        findView(view);

        ArrayAdapter<String> spinnerCategoryAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, getAllCategories());
        spinnerCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provider_add_promotion_category.setAdapter(spinnerCategoryAdapter);

        ArrayAdapter<String> spinnerDiscountTypeAdapter = new ArrayAdapter<>(view.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, getAllDiscountType());
        spinnerDiscountTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provider_add_promotion_discount_type.setAdapter(spinnerDiscountTypeAdapter);

        provider_add_promotion_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111);
            }
        });
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
        provider_add_promotion_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(getActivity());
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

        return view;
    }

    public void setDateTime(View v, int type) {
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
                                        } else {
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
        provider_add_promotion_category = (Spinner) v.findViewById(R.id.provider_add_promotion_category);
        provider_add_promotion_discount_type = (Spinner) v.findViewById(R.id.provider_add_promotion_discount_type);
        btn_provider_add_promotion = (Button) v.findViewById(R.id.btn_provider_add_promotion);
        provider_add_promotion_choose_image = (LinearLayout) v.findViewById(R.id.provider_add_promotion_choose_image);
        provider_add_promotion_chosen_image = (ImageView) v.findViewById(R.id.provider_add_promotion_chosen_image);
        provider_add_promotion_image_url = (EditText) v.findViewById(R.id.provider_add_promotion_image_url);
        provider_add_promotion_title = (EditText) v.findViewById(R.id.provider_add_promotion_title);
        provider_add_promotion_condition = (EditText) v.findViewById(R.id.provider_add_promotion_condition);
        provider_add_promotion_amountLimit = (EditText) v.findViewById(R.id.provider_add_promotion_amountLimit);
        provider_add_promotion_isOneCode = (CheckBox) v.findViewById(R.id.provider_add_promotion_isOneCode);
        provider_add_promotion_discount = (EditText) v.findViewById(R.id.provider_add_promotion_discount);
        provider_add_promotion_startTime = (EditText) v.findViewById(R.id.provider_add_promotion_startTime);
        provider_add_promotion_endTime = (EditText) v.findViewById(R.id.provider_add_promotion_endTime);
        provider_add_promotion_address = (EditText) v.findViewById(R.id.provider_add_promotion_address);
    }

    public void postPromotion(View view) {
        SharePreferenceHelper sharePreferenceHelper = new SharePreferenceHelper(view.getContext());
        PromotionBody promotionBody = new PromotionBody(getCategoryID(),                    //categoryID
                sharePreferenceHelper.getUserId(),                                          //providerID
                provider_add_promotion_title.getText().toString(),                          //title
                provider_add_promotion_image_url.getText().toString(),                      //cover
                provider_add_promotion_condition.getText().toString(),                      //Condition
                convertDateTime(provider_add_promotion_startTime.getText().toString()),     //start Time
                convertDateTime(provider_add_promotion_endTime.getText().toString()),       //endTime
                Integer.parseInt(provider_add_promotion_amountLimit.getText().toString()),  //Limit
                provider_add_promotion_isOneCode.isChecked(),                               //Mã dùng chung hay dùng riêng cho user
                Integer.parseInt(provider_add_promotion_discount.getText().toString()),     //discount
                getDiscountType(),                                                          //discountType
                addressList);                                                               //addresses
        PromotionRetrofitService promotionRetrofitService = new PromotionRetrofitService(view.getContext());
        promotionRetrofitService.postPromotion(promotionBody, new PostPromotionsDelegate((BaseActivity) view.getContext()));
    }

    public long convertDateTime(String dateTime) {
        long unixtime = 0;
        SimpleDateFormat format = new SimpleDateFormat("hh:mm dd-MM-yyyy");
        format.setTimeZone(TimeZone.getTimeZone(Calendar.getInstance().getTimeZone().getID()));
        try {
            unixtime = format.parse(dateTime).getTime();
            unixtime = unixtime / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unixtime;
    }

    public String getCategoryID() {
        String[] categoriesID = getAllCategoriesID();
        return categoriesID[provider_add_promotion_category.getSelectedItemPosition()];
    }

    public int getDiscountType() {
        return provider_add_promotion_discount_type.getSelectedItemPosition();
    }

    public boolean checkInfo() {
        // Check data input
        if (provider_add_promotion_image_url.getText().toString().equals("")) {
            provider_add_promotion_image_url.setError(getString(R.string.provider_add_promotion_choose_image_error));
            return false;
        }
        if (provider_add_promotion_title.getText().toString().equals("")) {
            provider_add_promotion_title.setError(getString(R.string.provider_add_promotion_title_error));
            return false;
        }

        if (provider_add_promotion_condition.getText().toString().equals("")) {
            provider_add_promotion_condition.setError(getString(R.string.provider_add_promotion_condition_error));
            return false;
        }
        if (provider_add_promotion_amountLimit.getText().toString().equals("")) {
            provider_add_promotion_amountLimit.setError(getString(R.string.provider_add_promotion_amountLimit_error));
            return false;
        }
        if (provider_add_promotion_discount.getText().toString().equals("")) {
            provider_add_promotion_discount.setError(getString(R.string.provider_add_promotion_discount_error));
            return false;
        }
        if (provider_add_promotion_startTime.getText().toString().equals("")) {
            provider_add_promotion_startTime.setError(getString(R.string.provider_add_promotion_startTime_error));
            return false;
        }
        if (provider_add_promotion_endTime.getText().toString().equals("")) {
            provider_add_promotion_endTime.setError(getString(R.string.provider_add_promotion_endTime_error));
            return false;
        }
        if (provider_add_promotion_address.getText().toString().equals("")) {
            provider_add_promotion_address.setError(getString(R.string.provider_add_promotion_address_error));
            return false;
        }
        return true;
    }

    public String[] getAllCategories() {
        //call api getAllCategories
        String[] categories = new String[]{"Ăn uống",
                "Quần áo",
                "Công Nghệ"
        };
        return categories;
    }

    public String[] getAllDiscountType() {
        String[] discountType = new String[]{"%",
                "VND"
        };
        return discountType;
    }

    public String[] getAllCategoriesID() {

        //call api getAllCategoriesID
        String[] categoriesID = new String[]{"5842fbab0f0bc105b77eb74e",
                "5842fbab0f0bc105b77eb74f",
                "5842fbab0f0bc105b77eb750"
        };
        return categoriesID;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Select cover
        if (requestCode == 111 && resultCode == RESULT_OK) {
            if (data == null) {
                //error
                return;
            }
            final Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            provider_add_promotion_chosen_image.setImageBitmap(bitmap);
            uploadImageFirebase(bitmap);
        }
        //Choose Address
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //get result from AutoComplete
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);

                //parse Address latitude longitude
                List<android.location.Address> addressListParse = null;
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                try {
                    addressListParse = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                //create address
                Address address = new Address();
                address.setNumber(addressListParse.get(0).getFeatureName());
                address.setStreet(addressListParse.get(0).getThoroughfare());
                address.setWard(addressListParse.get(0).getSubLocality());
                address.setDistrict(addressListParse.get(0).getSubAdminArea());
                if (addressListParse.get(0).getLocality() != null) {
                    address.setProvince(addressListParse.get(0).getLocality());
                }
                else {
                    address.setProvince("Ho Chi Minh City");
                }
                if (addressListParse.get(0).getCountryName() != null) {
                    address.setCountry(addressListParse.get(0).getCountryName());
                }
                else {
                    address.setCountry("Vietnam");
                }
                address.setLatitude(addressListParse.get(0).getLatitude());
                address.setLongitude(addressListParse.get(0).getLongitude());

                //add to addressList
                addressList.add(address);

                Log.i("Place: " , place.getName().toString());
                //updateUI
                String strAddress = provider_add_promotion_address.getText().toString();
                strAddress = strAddress + address.getStreet() + "; ";
                provider_add_promotion_address.setText(strAddress);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i("Place message: ", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    //Upload image to Firebase Storage, then get download url.
    private void uploadImageFirebase(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(FIREBASE_STORAGE);
        StorageReference mountainImagesRef = storageRef.child("promotions/coverImage/" + new SharePreferenceHelper(getContext()).getUserId() + Calendar.getInstance().getTimeInMillis() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.d("Firebase downloadURL-->", "" + downloadUrl);
                provider_add_promotion_image_url.setText(downloadUrl.toString());
            }
        });

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
        provider_add_promotion_chosen_image.setImageResource(R.drawable.bg_image_primary);
        provider_add_promotion_image_url.setText("");
        provider_add_promotion_title.setText("");
        provider_add_promotion_condition.setText("");
        provider_add_promotion_amountLimit.setText("");
        provider_add_promotion_discount.setText("");
        provider_add_promotion_startTime.setText("");
        provider_add_promotion_endTime.setText("");
        provider_add_promotion_address.setText("");
    }
}
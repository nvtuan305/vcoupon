package com.happybot.vcoupon.fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.activity.BaseActivity;
import com.happybot.vcoupon.dialog.ReceiveVoucherCodeDialog;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.PromotionRequestBody;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.model.UserRequestBody;
import com.happybot.vcoupon.model.Voucher;
import com.happybot.vcoupon.model.retrofit.ResponseObject;
import com.happybot.vcoupon.model.retrofit.VoucherResponse;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Admin on 1/4/2017.
 */

public class EditProfileFragment extends Fragment {

    private static final String FIREBASE_STORAGE = "gs://vcoupon-1275f.appspot.com/";

    /*
        List info can edit
        "email": "support@goixe.vn",
        "phoneNumber": "19002022",
        "address": "121 Điện Biên Phủ, Q.1, HCM",
        "password": "provider",
        "fanpage": "www.facebook.com/Goi-Xe/",
        "website": "www.goixe.com",
        "avatar": "http://www.oxfordeagle.com/wp-content/uploads/2016/06/uber.png",
        "name": "Go-iXe"
    */

    private EditText etName;
    private EditText etPassword;
    private EditText etRePassword;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private EditText etAddress;
    private EditText etWebsite;
    private EditText etFanpage;
    private LinearLayout btnChooseAvatar;
    private String urlAvatar;
    private CircleImageView civAvatar;
    private Button btnSave;

    private Button btnPin;
    private Button btnUnpin;
    private Button btnGetVoucher;

    private UserRequestBody userRequestBody = null;
    private PromotionRequestBody promotionRequestBody = null;

    private UpdateUserInfoDelegate updateUserInfoDelegate = null;
    private PinPromotionDelegate pinPromotionDelegate = null;
    private UnpinPromotionDelegate unpinPromotionDelegate = null;
    private ReceiveVoucherDelegate receiveVoucherDelegate = null;

    private Context mContext = null;

    private String roleUser;
    SharePreferenceHelper spHelper = null;

    public EditProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        etName          = (EditText) view.findViewById(R.id.etName);
        etPassword      = (EditText) view.findViewById(R.id.etPassword);
        etRePassword    = (EditText) view.findViewById(R.id.etRePassword);
        etEmail         = (EditText) view.findViewById(R.id.etEmail);
        etPhoneNumber   = (EditText) view.findViewById(R.id.etPhoneNumber);
        etAddress       = (EditText) view.findViewById(R.id.etAddress);
        etWebsite       = (EditText) view.findViewById(R.id.etWebsite);
        etFanpage       = (EditText) view.findViewById(R.id.etFanpage);
        btnChooseAvatar = (LinearLayout) view.findViewById(R.id.btnChooseAvatar);
        civAvatar       = (CircleImageView) view.findViewById(R.id.civAvatar);
        btnSave         = (Button) view.findViewById(R.id.btnSave);

        btnPin          = (Button) view.findViewById(R.id.btnPin);
        btnUnpin        = (Button) view.findViewById(R.id.btnUnpin);
        btnGetVoucher   = (Button) view.findViewById(R.id.btnGetVoucher);

        promotionRequestBody = new PromotionRequestBody(new String("5856a5f1a4ae7504c1856a37"));

        updateUserInfoDelegate = new UpdateUserInfoDelegate((BaseActivity) getActivity());
        pinPromotionDelegate = new PinPromotionDelegate((BaseActivity) getActivity());
        unpinPromotionDelegate = new UnpinPromotionDelegate((BaseActivity) getActivity());
        receiveVoucherDelegate = new ReceiveVoucherDelegate((BaseActivity) getActivity());

        mContext = view.getContext();

        // Initialize SharePreferenceHelper
        spHelper = new SharePreferenceHelper(mContext);

        // Show view based on role user
        roleUser = spHelper.getUserRole();

        if (roleUser.equals("NORMAL")) {
            etAddress.setVisibility(View.INVISIBLE);
            etWebsite.setVisibility(View.INVISIBLE);
            etFanpage.setVisibility(View.INVISIBLE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check input
                // dont match password
                if (!etPassword.getText().equals(etRePassword.getText())) {
                    Toast.makeText(mContext, "Password không trùng khớp!", Toast.LENGTH_SHORT).show();
                }

                userRequestBody = new UserRequestBody(etName.getText().toString(), urlAvatar,
                        etEmail.getText().toString(), etAddress.getText().toString(),
                        etWebsite.getText().toString(), etFanpage.getText().toString());

                updateUserInfo();

            }
        });

        btnPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinPromotion();
            }
        });

        btnUnpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unpinPromotion("");
            }
        });

        btnGetVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiveVoucher();
            }
        });

        btnChooseAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111);
            }
        });

        return view;
    }

    public void updateUserInfo() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(mContext);
        UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);
        userRetrofitService.updateUserInfo(helper.getUserId(), userRequestBody, updateUserInfoDelegate);
    }

    private class UpdateUserInfoDelegate extends ForegroundTaskDelegate<User> {

        UpdateUserInfoDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(User user, Throwable throwable) {
            super.onPostExecute(user, throwable);

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && user != null && shouldHandleResultForActivity()) {
                Toast.makeText(mContext, user.getName(), Toast.LENGTH_LONG).show();
            }

            // Show empty layout without any promotions
            // showView(user);
        }
    }

    // PIN
    public void pinPromotion() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(mContext);
        UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);
        userRetrofitService.pinPromotion(helper.getUserId(), 1, promotionRequestBody, pinPromotionDelegate);
    }

    private class PinPromotionDelegate extends ForegroundTaskDelegate<ResponseObject> {

        PinPromotionDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(ResponseObject responseObject, Throwable throwable) {
            super.onPostExecute(responseObject, throwable);

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && responseObject != null && shouldHandleResultForActivity()) {
                Toast.makeText(mContext, responseObject.getResultMessage(), Toast.LENGTH_LONG).show();
            }

            // Show empty layout without any promotions
            // showView(user);
        }
    }

    // UNPIN
    public void unpinPromotion(String promotionId) {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(mContext);
        UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);
        userRetrofitService.unpinPromotion(helper.getUserId(), promotionId, promotionRequestBody, unpinPromotionDelegate);
    }

    private class UnpinPromotionDelegate extends ForegroundTaskDelegate<ResponseObject> {

        UnpinPromotionDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(ResponseObject responseObject, Throwable throwable) {
            super.onPostExecute(responseObject, throwable);

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && responseObject != null && shouldHandleResultForActivity()) {
                Toast.makeText(mContext, responseObject.getResultMessage(), Toast.LENGTH_LONG).show();
            }

            // Show empty layout without any promotions
            // showView(user);
        }
    }

    // RECEIVE VOUCHER
    public void receiveVoucher() {
        // Initialize auth info for testing
        SharePreferenceHelper helper = new SharePreferenceHelper(mContext);
        UserRetrofitService userRetrofitService = new UserRetrofitService(mContext);
        // Hard code PromotionId
        userRetrofitService.receiveVoucher("587c428ab2ad6e0011d88323", receiveVoucherDelegate);
    }

    private class ReceiveVoucherDelegate extends ForegroundTaskDelegate<Voucher> {

        ReceiveVoucherDelegate(BaseActivity activity) {
            super(activity);
        }

        @Override
        public void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public void onPostExecute(Voucher voucher, Throwable throwable) {
            super.onPostExecute(voucher, throwable);

            // If no error occur, server response data, fragment is not destroyed
            if (throwable == null && voucher != null && shouldHandleResultForActivity()) {
                Toast.makeText(mContext, voucher.getQrCode(), Toast.LENGTH_LONG).show();
            }

            // Show empty layout without any promotions
            // showVoucher(voucher);
        }
    }

    private void showVoucher(Voucher voucher) {

    }

    // UPLOAD IMAGE
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
            civAvatar.setImageBitmap(bitmap);
            uploadImageFirebase(bitmap);
        }
    }

    //Upload image to Firebase Storage, then get download url.
    private void uploadImageFirebase(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(FIREBASE_STORAGE);
        StorageReference mountainImagesRef = storageRef.child("profileUser/avatar/" + new SharePreferenceHelper(getContext()).getUserId() + Calendar.getInstance().getTimeInMillis() + ".jpg");
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
                urlAvatar = downloadUrl.toString();
            }
        });

    }
}
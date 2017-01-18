package com.happybot.vcoupon.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.happybot.vcoupon.R;
import com.happybot.vcoupon.dialog.ReceiveVoucherCodeDialog;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;
import com.happybot.vcoupon.model.User;
import com.happybot.vcoupon.model.UserRequestBody;
import com.happybot.vcoupon.model.Voucher;
import com.happybot.vcoupon.service.UserRetrofitService;
import com.happybot.vcoupon.util.SharePreferenceHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseActivity {

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
    private EditText etEmail;
    private LinearLayout btnChooseAvatar;
    private String urlAvatar;
    private CircleImageView civAvatar;
    private Button btnSave;

    private UserRequestBody userRequestBody = null;
    private UpdateUserInfoDelegate updateUserInfoDelegate = null;

    private Context mContext = null;

    private String roleUser;
    SharePreferenceHelper spHelper = null;

    // TEST RECEIVE VOUCHER
    private ReceiveVoucherDelegate receiveVoucherDelegate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName          = (EditText) findViewById(R.id.etName);
        etEmail         = (EditText) findViewById(R.id.etEmail);
        btnChooseAvatar = (LinearLayout) findViewById(R.id.btnChooseAvatar);
        civAvatar       = (CircleImageView) findViewById(R.id.civAvatar);
        btnSave         = (Button) findViewById(R.id.btnSave);

        // Set value transfer from profile
        Intent intent = this.getIntent();
        etName.setText(intent.getStringExtra("name"));
        etEmail.setText(intent.getStringExtra("email"));
        urlAvatar = intent.getStringExtra("avatar");
        Picasso.with(mContext).load(urlAvatar).into(civAvatar);

        updateUserInfoDelegate = new UpdateUserInfoDelegate((BaseActivity) this);
        receiveVoucherDelegate = new ReceiveVoucherDelegate((BaseActivity) this);

        mContext = getApplicationContext();

        // Initialize SharePreferenceHelper
        spHelper = new SharePreferenceHelper(mContext);

        // Show view based on role user
        roleUser = spHelper.getUserRole();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                userRequestBody = new UserRequestBody(etName.getText().toString(), urlAvatar,
//                                                etEmail.getText().toString(), "", "", "");
//
//                updateUserInfo();

                // TEST RECEIVE
                receiveVoucher();
                showReceiveVoucherCodeDialog();

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
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), uri);
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
        StorageReference mountainImagesRef = storageRef.child("profileUser/avatar/" + new SharePreferenceHelper(getApplicationContext()).getUserId() + Calendar.getInstance().getTimeInMillis() + ".jpg");
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

    // TEST DIALOG RECEIVE VOUCHER
    public void showReceiveVoucherCodeDialog() {
        DialogFragment dialog = new ReceiveVoucherCodeDialog();
        dialog.show(getSupportFragmentManager(), "NoticeDialogFragment");
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
}

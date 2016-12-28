package com.happybot.vcoupon.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.happybot.vcoupon.application.VCouponApplication;
import com.happybot.vcoupon.dialog.ProgressDialogFragment;
import com.happybot.vcoupon.foregroundtask.ForegroundTaskDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @LayoutRes
    protected int getRootLayoutRes() {
        return 0;
    }

    protected View rootLayout;

    protected final boolean FORCE_SCREEN_ORIENTATION_PORTRAIT = true;

    protected final String TAG_PROGRESS_DIALOG = "VCouponProgressDialog";

    // List of foreground task
    protected List<ForegroundTaskDelegate> listOfForegroundTaskDelegates;

    public final VCouponApplication getAMADApplication() {
        return (VCouponApplication) getApplication();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FORCE_SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        rootLayout = getLayoutInflater().inflate(getRootLayoutRes(), null);
        setContentView(rootLayout);

        ButterKnife.bind(this);

        listOfForegroundTaskDelegates = new Vector<>();
    }

    @Override
    protected void onDestroy() {
        for (ForegroundTaskDelegate delegate : listOfForegroundTaskDelegates) {
            if (delegate != null) {
                delegate.cancel();
            }
        }

        super.onDestroy();
    }

    public void showProgressDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);

        if (prevFrag == null) {
            ProgressDialogFragment newFrag = ProgressDialogFragment.newInstance();
            ft.add(newFrag, TAG_PROGRESS_DIALOG);
        } else {
            ft.remove(prevFrag);
            getSupportFragmentManager().popBackStackImmediate();
        }

        ft.commitAllowingStateLoss();
    }

    public void dismissProgressDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prevFrag = getSupportFragmentManager().findFragmentByTag(TAG_PROGRESS_DIALOG);

        if (prevFrag != null) {
            ft.remove(prevFrag);
            ProgressDialogFragment dlgFrag = (ProgressDialogFragment) prevFrag;

            if (dlgFrag.getDialog() != null && dlgFrag.getDialog().isShowing() && dlgFrag.isResumed()) {
                dlgFrag.dismissAllowingStateLoss();
                getSupportFragmentManager().popBackStackImmediate();
            }
        }

        ft.commitAllowingStateLoss();
    }
}

package com.everlastxgb.beautyshow.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.application.BeautyShowApplication;
import com.everlastxgb.beautyshow.common.CommonMethods;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

/**
 *
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 *
 * @create_time: 2015-4-1 PM 3:16:11
 *
 */

public class BaseActivity extends Activity {

    public static final String KEY_MODEL = "model";
    public static final String KEY_URL = "url";
    public static final String KEY_INDEX = "index";
    public static final String KEY_DISPLAY = "display";
    public static final long MS_DELAY_REFRESH = 500;

    protected ImageView titlebarBackView, titlebarRightView;
    protected TextView titlebarTitleTextView;
    protected EditText titlebarEditText;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }


    @Override
    protected void onResume() {
        super.onResume();

        PgyFeedbackShakeManager.setShakingThreshold(1500);
        PgyFeedbackShakeManager.register(this);

        TCAgent.onResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PgyFeedbackShakeManager.unregister();

        TCAgent.onPause(this);
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        try {
            View view1 = findViewById(R.id.titlebar_title_tv);
            View view2 = findViewById(R.id.titlebar_back_imv);
            View view3 = findViewById(R.id.titlebar_ok_imv);
            View view4 = findViewById(R.id.titlebar_search_edit);

            if (view1 instanceof TextView) {
                titlebarTitleTextView = (TextView) view1;
            }

            if (view2 instanceof ImageView) {
                titlebarBackView = (ImageView) view2;
                titlebarBackView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BaseActivity.this.finish();
                    }
                });
            }

            if (view3 instanceof ImageView) {
                titlebarRightView = (ImageView) view3;
            }

            if (view4 instanceof EditText) {
                titlebarEditText = (EditText) view4;
            }

        } catch (Exception e) {
        }
    }


    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (titlebarTitleTextView != null) {
            titlebarTitleTextView.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        if (titlebarTitleTextView != null) {
            titlebarTitleTextView.setText(titleId);
        }
    }


    @Override
    public void finish() {
        CommonMethods.hideKeyboard(this);
        super.finish();
        toggleOutAnimation();
    }

    public BaseActivity self() {
        return this;
    }

    public BeautyShowApplication application() {
        BeautyShowApplication application = (getApplication() instanceof BeautyShowApplication) ? (BeautyShowApplication) getApplication() : null;
        return application;
    }

    public void manuPullDown(PullToRefreshBase<?> pullToRefreshView) {
        Mode preMode = pullToRefreshView.getMode();
        pullToRefreshView.setMode(Mode.PULL_FROM_START);
        pullToRefreshView.setRefreshing();
        pullToRefreshView.setMode(preMode);
    }

    public void changeViewable(View view, boolean viewable) {
        if (view != null) {
            int visibility = viewable ? View.VISIBLE : View.GONE;
            view.setVisibility(visibility);
        }
    }

    /**
     * default is true
     *
     * @param viewable
     */
    public void toggleBackButton(boolean viewable) {
        int visibility = viewable ? View.VISIBLE : View.GONE;
        if (titlebarBackView != null) {
            titlebarBackView.setVisibility(visibility);
        }
    }

    public void toggleOutAnimation() {
        try {
            Activity curActivity = this;
//			if (curActivity instanceof ) {
//				overridePendingTransition(R.anim.hold, android.R.anim.fade_out);
//			}

        } catch (Exception e) {
        }
    }

    public void postDelayed(Runnable runnable, long delayMillis) {
        mHandler.postDelayed(runnable, delayMillis);
    }

}

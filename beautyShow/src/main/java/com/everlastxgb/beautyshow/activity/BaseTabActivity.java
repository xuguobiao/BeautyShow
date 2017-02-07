package com.everlastxgb.beautyshow.activity;

import android.os.Bundle;
import android.view.KeyEvent;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.common.UIHelper;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */

public class BaseTabActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        changeViewable(titlebarBackView, false);

    }

    private long mkeyTime;
    private final static long INTERVAL_DOUBLECLICK = 1500;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mkeyTime) > INTERVAL_DOUBLECLICK) {
                mkeyTime = System.currentTimeMillis();
                UIHelper.showToastShort(self(), getString(R.string.tips_onemore2exit));
                return true;
            } else {
                moveTaskToBack(true);
            }
        }

        return super.onKeyDown(keyCode, event);
    }

}

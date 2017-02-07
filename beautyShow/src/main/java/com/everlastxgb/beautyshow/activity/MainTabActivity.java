package com.everlastxgb.beautyshow.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.util.AsynThreadPool;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */

public class MainTabActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_tab);
        setTabs();
    }

    private void setTabs() {
        addTab(R.string.tab_home, R.drawable.selector_tab_discover, TabHomeActivity.class);
        addTab(R.string.tab_gallery, R.drawable.selector_tab_discover, TabGalleryActivity.class);

        addTab(R.string.tab_rank, R.drawable.selector_tab_list, TabRankActivity.class);
        addTab(R.string.tab_more, R.drawable.selector_tab_personal, TabMoreActivity.class);
    }

    private void addTab(int labelId, int drawableId, Class<?> c) {
        TabHost tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab-" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
        TextView title = (TextView) tabIndicator.findViewById(R.id.title);
        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

//		icon.setOnClickListener(new OnDoubleClickListener(c));

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);

    }

    private class OnDoubleClickListener implements OnClickListener {
        private boolean waitDouble = true;
        private static final int DOUBLE_CLICK_TIME = 350; // 两次单击的时间间隔

        private Class<?> c;

        public OnDoubleClickListener(Class<?> c) {
            this.c = c;
        }

        @Override
        public void onClick(final View v) {
            if (waitDouble == true) {
                waitDouble = false;
                AsynThreadPool.getInstance().runThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(DOUBLE_CLICK_TIME);
                            if (waitDouble == false) {
                                waitDouble = true;
                                singleClick(v);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else {
                waitDouble = true;
                doubleClick(v);
            }
        }

        private void singleClick(View v) {
        }

        private void doubleClick(View v) {
            if (c == TabHomeActivity.class) {
                TabHomeActivity.Self.refresh();
            } else if (c == TabGalleryActivity.class) {
                TabGalleryActivity.Self.refresh();
            } else if (c == TabRankActivity.class) {
                TabRankActivity.Self.refresh();
            }
        }
    }

    //
    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // // TODO Auto-generated method stub
    // if (keyCode == KeyEvent.KEYCODE_BACK) {
    // moveTaskToBack(true);
    // return true;
    // }
    //
    // return super.onKeyDown(keyCode, event);
    // }
    @Override
    public void finish() {
        this.moveTaskToBack(true);
    }

}
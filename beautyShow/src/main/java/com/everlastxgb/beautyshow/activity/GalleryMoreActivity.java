package com.everlastxgb.beautyshow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.adapter.AutoGridAdapter;
import com.everlastxgb.beautyshow.common.UIHelper;
import com.everlastxgb.beautyshow.common.URLs;
import com.everlastxgb.beautyshow.controller.ApiController;
import com.everlastxgb.beautyshow.controller.ResultCallback;
import com.everlastxgb.beautyshow.model.GirlListModel;
import com.everlastxgb.beautyshow.model.GirlModel;
import com.everlastxgb.beautyshow.model.ItemModel;
import com.everlastxgb.beautyshow.widget.fab.FloatingActionMenu;
import com.everlastxgb.beautyshow.widget.fab.FloatingActionMenu.OnMenuToggleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */

public class GalleryMoreActivity extends BaseActivity {

    private String defaultPage = "";
    private String mNextPage = "";

    private PullToRefreshGridView pullToRefreshView;
    private AutoGridAdapter mAdapter;
    private GirlListModel mListModel = new GirlListModel(GirlModel.TYPE_GALLERY);

    private FloatingActionMenu floatingActionMenu;
    private View bottomLayout;

    private View gridLayout_gallery, gridLayout_girl;
    private GridView gridView_gallery;
    private TextView gridTitle_gallery;
    private AutoGridAdapter adapter_gallery;
    private GirlListModel listModel_gallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_more);
        setTitle(R.string.tab_gallery);

        initGridLayout();
        initListView();
        initActionMenu();

        titlebarTitleTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pullToRefreshView.getRefreshableView().setSelection(0);
            }
        });
        // changeViewable(titlebarRightView, true);
        // titlebarRightView.setImageResource(R.drawable.icon_document_n);
        // titlebarRightView.setOnClickListener(new View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // Intent intent = new Intent(self(), GirlDetailActivity.class);
        // intent.putExtra(GirlDetailActivity.KEY_MODEL, mGirlModel);
        // startActivity(intent);
        // }
        // });

        Object object = getIntent().getSerializableExtra(KEY_MODEL);
        if (object instanceof ItemModel) {
            ItemModel itemModel = (ItemModel) object;
            defaultPage = itemModel.getId();

            String title = itemModel.getTitle();
            // mListModel.setTitle(title);
            setTitle(title);
        }

        postDelayed(new Runnable() {
            @Override
            public void run() {
                manuPullDown(pullToRefreshView);
            }
        }, MS_DELAY_REFRESH);
    }

    private void initGridLayout() {
        gridLayout_girl = findViewById(R.id.gridLayout_girl);
        gridLayout_girl.setVisibility(View.GONE);

        gridLayout_gallery = findViewById(R.id.gridLayout_gallery);

        gridLayout_gallery.setVisibility(View.GONE);
        gridView_gallery = (GridView) gridLayout_gallery.findViewById(R.id.gridView);
        gridTitle_gallery = (TextView) gridLayout_gallery.findViewById(R.id.gridTitle);

        listModel_gallery = new GirlListModel(GirlModel.TYPE_GALLERY);
        adapter_gallery = new AutoGridAdapter(this, listModel_gallery, gridView_gallery);
        adapter_gallery.setParameters(-1, -1, -1, 25, 2);
        gridView_gallery.setAdapter(adapter_gallery);

        gridView_gallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                GirlModel model = listModel_gallery.getList().get(arg2);
                Intent intent = new Intent(self(), GalleryDetailActivity.class);
                intent.putExtra(KEY_MODEL, model);
                startActivity(intent);
            }
        });

    }

    private void initListView() {

        pullToRefreshView = (PullToRefreshGridView) findViewById(R.id.pullToRefreshGridView);

        mAdapter = new AutoGridAdapter(this, mListModel, pullToRefreshView.getRefreshableView());
        pullToRefreshView.setAdapter(mAdapter);
        pullToRefreshView.setMode(Mode.PULL_FROM_START);
        pullToRefreshView.getRefreshableView().setSelector(android.R.color.transparent);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                // String label =
                // DateUtils.formatDateTime(getApplicationContext(),
                // System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME |
                // DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // // Update the LastUpdatedLabel
                // refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                startThread(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> arg0) {
                startThread(false);
            }
        });

        pullToRefreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                GirlModel girlModel = mListModel.getList().get(arg2);
                Intent intent = new Intent(self(), GalleryDetailActivity.class);
                intent.putExtra(KEY_MODEL, girlModel);
                startActivity(intent);
            }
        });

    }

    private int mPreviousVisibleItem;

    private void initActionMenu() {
        bottomLayout = findViewById(R.id.bottomLayout);
        bottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionMenu.close(true);
            }
        });
        bottomLayout.setVisibility(View.GONE);
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floatActionMenu);
        floatingActionMenu.hideMenuButton(false);
        floatingActionMenu.setOnMenuToggleListener(new OnMenuToggleListener() {

            @Override
            public void onMenuToggle(boolean opened) {
                // int visi = opened ? View.VISIBLE : View.GONE;
                startAnimation(opened, -1);
            }
        });

        pullToRefreshView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > mPreviousVisibleItem) {
                    floatingActionMenu.hideMenuButton(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
                    floatingActionMenu.showMenuButton(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
            }
        });

    }

    private void startAnimation(boolean opened, long durationMillis) {
        int ani = opened ? R.anim.bottomright_scale_in : R.anim.bottomright_scale_out;
        Animation animation = AnimationUtils.loadAnimation(self(), ani);
        if (durationMillis >= 0) {
            animation.setDuration(durationMillis);
        }
        if (opened) {
            bottomLayout.setVisibility(View.VISIBLE);
        } else {
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bottomLayout.setVisibility(View.GONE);
                }
            });
        }
        animation.setFillBefore(true);
        bottomLayout.startAnimation(animation);
    }

    private void initData(GirlListModel galleryListModel) {

        listModel_gallery.getList().clear();
        listModel_gallery.getList().addAll(galleryListModel.getList());
        adapter_gallery.notifyDataSetChanged();

        String galleryTitle = galleryListModel.getTitle().equals("") ? getString(R.string.other_hot_gallery) : galleryListModel.getTitle();
        gridTitle_gallery.setText(galleryTitle);

        int galVisi = listModel_gallery.getList().size() > 0 ? View.VISIBLE : View.GONE;
        gridLayout_gallery.setVisibility(galVisi);
        GirlDetailActivity.setGridViewHeightBasedOnChildren(gridView_gallery);

        boolean hasBottom = galVisi == View.VISIBLE;
        int bottomVisi = hasBottom ? View.VISIBLE : View.GONE;
        // startAnimation(false, 0);

        if (hasBottom) {
            floatingActionMenu.showMenuButton(true);
        } else {
            floatingActionMenu.hideMenuButton(true);
        }

    }

    private void startThread(final boolean isLoadingNew) {

        final String url = isLoadingNew ? defaultPage : mNextPage;
        ApiController.getGalleryMore(URLs.getValidUrl(url), new MyResultCallback(GalleryMoreActivity.this, isLoadingNew));
        // ApiController.getTodayGirlListModel(resultCallback2);

        if (isLoadingNew) {
            pullToRefreshView.getRefreshableView().setSelection(0);
        }
    }

    public class MyResultCallback extends ResultCallback<GirlListModel[]> {
        private boolean isLoadingNew = true;

        public MyResultCallback(Activity activity, boolean isLoadingNew) {
            super(activity);
            this.isLoadingNew = isLoadingNew;
        }

        @Override
        public void onResult(GirlListModel[] result, boolean success, String errMsg) {
            pullToRefreshView.onRefreshComplete();
            if (success) {
                // mGirlModel.setHref(detailModel.getGirlHref());// for
                // right-click
                // to jump to
                // girl-detail
                if (isLoadingNew) {
                    mListModel.getList().clear();
                }
                mListModel.getList().addAll(result[0].getList());
                mAdapter.notifyDataSetChanged();
                setTitle(result[0].getTitle());

                mNextPage = result[0].getPageModel().getNextHref();

                Mode mode = mNextPage.equals("") ? Mode.PULL_FROM_START : Mode.BOTH;
                pullToRefreshView.setMode(mode);

                initData(result[1]);

            } else {
                UIHelper.showToastShort(self(), errMsg + "");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && floatingActionMenu.isOpened()) {
            floatingActionMenu.close(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

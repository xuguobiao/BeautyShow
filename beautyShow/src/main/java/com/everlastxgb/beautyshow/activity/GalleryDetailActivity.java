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
import android.widget.ListView;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.adapter.AutoGridAdapter;
import com.everlastxgb.beautyshow.adapter.PicListAdapter;
import com.everlastxgb.beautyshow.common.UIHelper;
import com.everlastxgb.beautyshow.common.URLs;
import com.everlastxgb.beautyshow.controller.ApiController;
import com.everlastxgb.beautyshow.controller.ResultCallback;
import com.everlastxgb.beautyshow.model.GalleryDetailModel;
import com.everlastxgb.beautyshow.model.GirlListModel;
import com.everlastxgb.beautyshow.model.GirlModel;
import com.everlastxgb.beautyshow.model.PicListModel;
import com.everlastxgb.beautyshow.widget.fab.FloatingActionMenu;
import com.everlastxgb.beautyshow.widget.fab.FloatingActionMenu.OnMenuToggleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-9-1 10:45:03
 */

public class GalleryDetailActivity extends BaseActivity {

    private GirlModel mGirlModel = new GirlModel();
    private GalleryDetailModel mDetailModel = new GalleryDetailModel();

    private String defaultPage = "";
    private String mNextPage = "";

    private PullToRefreshListView pullToRefreshView;
    private View mHeaderView;
    private PicListAdapter mAdapter;
    private PicListModel mListModel = new PicListModel();

    private TextView title1, title2, title3;

    private FloatingActionMenu floatingActionMenu;
    private View bottomLayout;

    private View gridLayout_gallery, gridLayout_girl;
    private GridView gridView_gallery, gridView_girl;
    private TextView gridTitle_gallery, gridTitle_girl;
    private AutoGridAdapter adapter_gallery, adapter_girl;
    private GirlListModel listModel_gallery, listModel_girl;

    private boolean mIsLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_detail);
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
        if (object instanceof GirlModel) {
            mGirlModel = (GirlModel) object;
            defaultPage = mGirlModel.getHref();

            String title = mGirlModel.getName();
            // mListModel.setTitle(title);
            setTitle(title);
        }

        initData(mDetailModel);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                manuPullDown(pullToRefreshView);
            }
        }, MS_DELAY_REFRESH);
    }

    private void initGridLayout() {
        gridLayout_gallery = findViewById(R.id.gridLayout_gallery);
        gridLayout_girl = findViewById(R.id.gridLayout_girl);

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

        /*********************/
        /*********************/

        gridLayout_girl.setVisibility(View.GONE);
        gridView_girl = (GridView) gridLayout_girl.findViewById(R.id.gridView);
        gridTitle_girl = (TextView) gridLayout_girl.findViewById(R.id.gridTitle);

        listModel_girl = new GirlListModel(GirlModel.TYPE_GIRL);
        adapter_girl = new AutoGridAdapter(this, listModel_girl, gridView_girl);
        adapter_girl.setParameters(-1, -1, -1, 25, 3);
        gridView_girl.setAdapter(adapter_girl);

        gridView_girl.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                GirlModel model = listModel_girl.getList().get(arg2);
                Intent intent = new Intent(self(), GirlDetailActivity.class);
                intent.putExtra(KEY_MODEL, model);
                startActivity(intent);
            }
        });

    }

    private void initListView() {

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        mHeaderView = getLayoutInflater().inflate(R.layout.listview_header, pullToRefreshView, false);
        title1 = (TextView) mHeaderView.findViewById(R.id.title1);
        title2 = (TextView) mHeaderView.findViewById(R.id.title2);
        title3 = (TextView) mHeaderView.findViewById(R.id.title3);

        mHeaderView.setLayoutParams(layoutParams);

        pullToRefreshView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
        pullToRefreshView.getRefreshableView().addHeaderView(mHeaderView);

        mAdapter = new PicListAdapter(this, mListModel);
        pullToRefreshView.setAdapter(mAdapter);
        pullToRefreshView.setMode(Mode.PULL_FROM_START);
        pullToRefreshView.getRefreshableView().setSelector(android.R.color.transparent);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // String label =
                // DateUtils.formatDateTime(getApplicationContext(),
                // System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME |
                // DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // // Update the LastUpdatedLabel
                // refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

                startListThread(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> arg0) {
                startListThread(false);
            }
        });


        pullToRefreshView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(self(), ViewPagerActivity.class);
                intent.putExtra(KEY_MODEL, mListModel.getList());
                intent.putExtra(KEY_INDEX, (int) arg3);
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
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        startListThread(false);
                    }
                }
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

    private void initData(GalleryDetailModel detailModel) {

        // setTitle(detailModel.getTitle());
        setTextView(title1, detailModel.getTitle());
        setTextView(title2, detailModel.getDesc());
        setTextView(title3, detailModel.getInfo());

        GirlListModel galleryListModel = detailModel.getRecommendGallery();
        listModel_gallery.getList().clear();
        listModel_gallery.getList().addAll(galleryListModel.getList());
        adapter_gallery.notifyDataSetChanged();

        String galleryTitle = galleryListModel.getTitle().equals("") ? getString(R.string.recommend_gallery) : galleryListModel.getTitle();
        gridTitle_gallery.setText(galleryTitle);

        int galVisi = listModel_gallery.getList().size() > 0 ? View.VISIBLE : View.GONE;
        gridLayout_gallery.setVisibility(galVisi);
        GirlDetailActivity.setGridViewHeightBasedOnChildren(gridView_gallery);

        GirlListModel girlListModel = detailModel.getTheseGirls();
        listModel_girl.getList().clear();
        listModel_girl.getList().addAll(girlListModel.getList());
        adapter_girl.notifyDataSetChanged();

        String girlTitle = girlListModel.getTitle().equals("") ? getString(R.string.girl_profile) : girlListModel.getTitle();
        gridTitle_girl.setText(girlTitle);
        int girlVisi = listModel_girl.getList().size() > 0 ? View.VISIBLE : View.GONE;
        gridLayout_girl.setVisibility(girlVisi);
        GirlDetailActivity.setGridViewHeightBasedOnChildren(gridView_girl);

        boolean hasBottom = galVisi == View.VISIBLE && girlVisi == View.VISIBLE;
        int bottomVisi = hasBottom ? View.VISIBLE : View.GONE;
        // startAnimation(false, 0);

        if (hasBottom) {
            floatingActionMenu.showMenuButton(true);
        } else {
            floatingActionMenu.hideMenuButton(true);
        }

    }

    private void setTextView(TextView textView, String text) {
        int visibility = text == null || text.equals("") ? View.GONE : View.VISIBLE;
        textView.setVisibility(visibility);
        textView.setText(text);
    }

    private void startListThread(final boolean isLoadingNew) {

        if (isLoadingNew) {
            pullToRefreshView.getRefreshableView().setSelection(0);
        }

        if (mIsLoading) {
            return;
        }

        mIsLoading = true;

        final String url = isLoadingNew ? defaultPage : mNextPage;
        ApiController.getGalleryDetail(URLs.getValidUrl(url), new MyResultCallback(GalleryDetailActivity.this, isLoadingNew));
        // ApiController.getTodayGirlListModel(resultCallback2);


    }

    public class MyResultCallback extends ResultCallback<GalleryDetailModel> {
        private boolean isLoadingNew = true;

        public MyResultCallback(Activity activity, boolean isLoadingNew) {
            super(activity);
            this.isLoadingNew = isLoadingNew;
        }

        @Override
        public void onResult(GalleryDetailModel result, boolean success, String errMsg) {
            pullToRefreshView.onRefreshComplete();
            mIsLoading = false;
            if (success) {
                mDetailModel = result;
                initData(mDetailModel);
                // mGirlModel.setHref(detailModel.getGirlHref());// for
                // right-click
                // to jump to
                // girl-detail
                if (isLoadingNew) {
                    mListModel.getList().clear();
                }
                mListModel.getList().addAll(result.getPic().getList());
                mAdapter.notifyDataSetChanged();

                mNextPage = result.getPic().getPageModel().getNextHref();

                Mode mode = mNextPage.equals("") ? Mode.PULL_FROM_START : Mode.BOTH;
                pullToRefreshView.setMode(mode);
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

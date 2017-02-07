package com.everlastxgb.beautyshow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.adapter.AutoGridAdapter;
import com.everlastxgb.beautyshow.common.UIHelper;
import com.everlastxgb.beautyshow.common.URLs;
import com.everlastxgb.beautyshow.controller.ApiController;
import com.everlastxgb.beautyshow.controller.ResultCallback;
import com.everlastxgb.beautyshow.model.GirlListModel;
import com.everlastxgb.beautyshow.model.GirlModel;
import com.everlastxgb.beautyshow.model.ItemListModel;
import com.everlastxgb.beautyshow.model.ItemModel;
import com.everlastxgb.beautyshow.util.Logger;
import com.everlastxgb.beautyshow.widget.MultiLineRadioGroup;
import com.everlastxgb.beautyshow.widget.fab.FloatingActionButton;
import com.everlastxgb.beautyshow.widget.fab.FloatingActionMenu;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */
public class TabGalleryActivity extends BaseTabActivity {

    private PullToRefreshGridView pullToRefreshView;

    private GirlListModel mListModel = new GirlListModel(GirlModel.TYPE_GALLERY);

    private AutoGridAdapter picTitleAdapter;

    private String defaultUrl = URLs.GALLERYS;
    private String nextPageUrl = "";

    private FloatingActionMenu floatingActionMenu;
    private ItemModel sortItemModel = new ItemModel();
    private ItemListModel sortItemListModel = new ItemListModel();

    private ArrayList<ItemListModel> filterList = new ArrayList<ItemListModel>();
    private View topLayout;
    private LinearLayout groupLayout;

    private boolean mIsLoading = false;


    public static TabGalleryActivity Self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Self = this;
        setContentView(R.layout.tab_gallery);
        setTitle(R.string.tab_gallery);

        initGridView();
        initActionMenu();
        initTopLayout();

        titlebarTitleTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pullToRefreshView.getRefreshableView().setSelection(0);
            }
        });
        postDelayed(new Runnable() {

            @Override
            public void run() {
                manuPullDown(pullToRefreshView);
            }
        }, MS_DELAY_REFRESH);
    }

    public void refresh() {
        manuPullDown(pullToRefreshView);
    }

    private void initGridView() {
        pullToRefreshView = (PullToRefreshGridView) findViewById(R.id.pullToRefreshGridView);
        picTitleAdapter = new AutoGridAdapter(this, mListModel, pullToRefreshView.getRefreshableView());
        pullToRefreshView.setAdapter(picTitleAdapter);
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

                startListThread(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> arg0) {
                startListThread(false);
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
        floatingActionMenu = (FloatingActionMenu) findViewById(R.id.floatActionMenu);
        floatingActionMenu.hideMenuButton(false);
        floatingActionMenu.setClosedOnTouchOutside(true);
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

    private void initActionButton(ItemListModel sortItemListModel) {
        floatingActionMenu.removeAllMenuButtons();
        for (final ItemModel itemModel : sortItemListModel.getList()) {
            ContextThemeWrapper wrapper = new ContextThemeWrapper(this, R.style.MenuButtonsSmall_Me);
            FloatingActionButton actionButton = new FloatingActionButton(wrapper);
            actionButton.setLabelText(itemModel.getTitle());
            actionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    checkActionButtonInMenu(view);
                    startSort(itemModel);
                }
            });
            floatingActionMenu.addMenuButton(actionButton);
        }
    }

    private void checkActionButtonInMenu(View view) {
        for (int i = 0; i < floatingActionMenu.getChildCount(); i++) {
            View child = floatingActionMenu.getChildAt(i);
            boolean enabled = child == view ? false : true;
            child.setEnabled(enabled);
        }
    }

    private void startSort(ItemModel itemModel) {
        // request here
        sortItemModel = itemModel;
        manuPullDown(pullToRefreshView);

    }

    private void initTopLayout() {

        topLayout = findViewById(R.id.topLayout);
        groupLayout = (LinearLayout) topLayout.findViewById(R.id.groupLayout);
        topLayout.setVisibility(View.GONE);
        topLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleTopLayout(false, -1);
            }
        });

        titlebarRightView.setVisibility(View.GONE);
        titlebarRightView.setImageResource(R.drawable.selector_category);
        titlebarRightView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                boolean viewable = topLayout.getVisibility() != View.VISIBLE;
                toggleTopLayout(viewable, -1);
            }
        });
    }

    private void toggleTopLayout(boolean viewable, long delayMS) {
        int visi = viewable ? View.VISIBLE : View.GONE;
        startAnimation(viewable, delayMS);
    }

    private void startAnimation(boolean opened, long delayMillis) {
        int ani = opened ? R.anim.topright_scale_in : R.anim.topright_scale_out;
        Animation animation = AnimationUtils.loadAnimation(self(), ani);
        if (delayMillis >= 0) {
            animation.setStartOffset(delayMillis);
        }
        if (opened) {
            topLayout.setVisibility(View.VISIBLE);
        } else {
            animation.setAnimationListener(new AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    topLayout.setVisibility(View.GONE);
                }
            });
        }
        animation.setFillBefore(true);
        topLayout.startAnimation(animation);
    }

    private void initFilter() {
        Logger.log("initFilter in, size->" + filterList.size());
        int rightVisi = filterList.size() == 0 ? View.GONE : View.VISIBLE;
        titlebarRightView.setVisibility(rightVisi);

        for (ItemListModel itemListModel : filterList) {
            View groupView = LayoutInflater.from(this).inflate(R.layout.radiogroup_title, null);
            TextView title = (TextView) groupView.findViewById(R.id.title);
            MultiLineRadioGroup radioGroup = (MultiLineRadioGroup) groupView.findViewById(R.id.raidoGroup);
            title.setText(itemListModel.getTitle());
            int titleVisi = itemListModel.getTitle().equals("") ? View.GONE : View.VISIBLE;
            title.setVisibility(titleVisi);
            Logger.log("initFilter, title->" + itemListModel.getTitle());
            for (ItemModel itemModel : itemListModel.getList()) {
                radioGroup.append(itemModel.getTitle());
                Logger.log("initFilter, add->" + itemModel.getTitle());
            }
            final ItemListModel itemListModel2 = itemListModel;
            radioGroup.setOnCheckChangedListener(new MultiLineRadioGroup.OnCheckedChangedListener() {
                @Override
                public void onItemChecked(MultiLineRadioGroup group, int position, boolean checked) {
                    if (checked) {
                        clearCheckExcept(group);
                        ItemModel itemModel = itemListModel2.getList().get(position);
                        startFilter(itemModel.getId());
                    } else {
                        startFilter(URLs.GALLERYS);
                    }
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            groupLayout.addView(groupView, params);
        }
    }

    private void clearCheckExcept(MultiLineRadioGroup exceptRadioGroup) {
        for (int i = 0; i < groupLayout.getChildCount(); i++) {
            MultiLineRadioGroup radioGroup = (MultiLineRadioGroup) groupLayout.getChildAt(i).findViewById(R.id.raidoGroup);
            if (radioGroup != exceptRadioGroup)
                radioGroup.clearChecked();
        }
    }

    private void startFilter(String url) {
        defaultUrl = URLs.getValidUrl(url);
        // setTitle(itemModel.getTitle());
        manuPullDown(pullToRefreshView);

        toggleTopLayout(false, 100);
    }

    private void startListThread(final boolean isLoadingNew) {
        if (isLoadingNew) {
            pullToRefreshView.getRefreshableView().setSelection(0);
        }

        if (mIsLoading) {
            return;
        }

        mIsLoading = true;
        final String url = isLoadingNew ? defaultUrl : nextPageUrl;
        String sort = isLoadingNew ? sortItemModel.getId() : "";
        ApiController.getGalleryListModel(URLs.getValidUrl(url), sort, new MyResultCallback(self(), isLoadingNew));
    }

    public class MyResultCallback extends ResultCallback<GirlListModel> {
        private boolean isLoadingNew = true;

        public MyResultCallback(Activity activity, boolean isLoadingNew) {
            super(activity);
            this.isLoadingNew = isLoadingNew;
        }

        @Override
        public void onResult(GirlListModel listModel, boolean success, String errMsg) {
            pullToRefreshView.onRefreshComplete();
            mIsLoading = false;
            if (success) {

                if (isLoadingNew) {
                    mListModel.getList().clear();
                }
                mListModel.getList().addAll(listModel.getList());
                picTitleAdapter.notifyDataSetChanged();

                nextPageUrl = listModel.getPageModel().getNextHref();

                Mode mode = nextPageUrl.equals("") ? Mode.PULL_FROM_START : Mode.BOTH;
                pullToRefreshView.setMode(mode);

                setTitle(listModel.getTitle());

                if (sortItemListModel.getList().size() == 0) {
                    sortItemListModel = listModel.getSortListModel();
                    initActionButton(sortItemListModel);
                }
                floatingActionMenu.showMenuButton(true);

                if (filterList.size() == 0) {
                    filterList = listModel.getFilterListModelList();
                    initFilter();
                }

            } else {
                UIHelper.showToastShort(self(), errMsg + "");
            }
            int tipVisibility = mListModel.getList().size() == 0 ? View.VISIBLE : View.GONE;
            findViewById(R.id.bigtip_layout).setVisibility(tipVisibility);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && topLayout.getVisibility() == View.VISIBLE) {
            toggleTopLayout(false, 0);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}

package com.everlastxgb.beautyshow.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.adapter.RankListAdapter;
import com.everlastxgb.beautyshow.common.UIHelper;
import com.everlastxgb.beautyshow.controller.ApiController;
import com.everlastxgb.beautyshow.controller.ResultCallback;
import com.everlastxgb.beautyshow.model.GirlListModel;
import com.everlastxgb.beautyshow.model.GirlModel;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */
public class SearchResultActivity extends BaseActivity {

    private PullToRefreshListView pullToRefreshView;

    private GirlListModel mListModel = new GirlListModel(GirlModel.TYPE_RANK);

    private RankListAdapter mAdapter;

    private String nextPageUrl = "";
    private String searchName = "";

    public static final String KEY_NAME = "key_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        searchName = getIntent().getStringExtra(KEY_NAME);
        setTitle("\"" + searchName + "\"");

        initListView();

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

    private void initListView() {
        pullToRefreshView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView);
        mAdapter = new RankListAdapter(this, mListModel);
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
                GirlModel girlModel = mListModel.getList().get(arg2);
                Intent intent = new Intent(self(), GirlDetailActivity.class);
                intent.putExtra(KEY_MODEL, girlModel);
                startActivity(intent);
            }
        });

    }

    private void startListThread(final boolean isLoadingNew) {
        if (isLoadingNew) {
            pullToRefreshView.getRefreshableView().setSelection(0);
        }

        // final String url = isLoadingNew ? defaultUrl : nextPageUrl;
        // String sort = isLoadingNew ? sortItemModel.getId() : "";
        ApiController.searchGirl(searchName, new MyResultCallback(self(), isLoadingNew));
    }

    public class MyResultCallback extends ResultCallback<GirlListModel> {
        private boolean isLoadingNew = true;

        public MyResultCallback(Activity activity, boolean isLoadingNew) {
            super(activity);
            this.isLoadingNew = isLoadingNew;
        }

        @Override
        public void onResult(GirlListModel result, boolean success, String errMsg) {
            pullToRefreshView.onRefreshComplete();
            if (success) {

                if (isLoadingNew) {
                    mListModel.getList().clear();
                }
                mListModel.getList().addAll(result.getList());
                mAdapter.notifyDataSetChanged();

                nextPageUrl = result.getPageModel().getNextHref();

                Mode mode = nextPageUrl.equals("") ? Mode.PULL_FROM_START : Mode.BOTH;
                pullToRefreshView.setMode(mode);

            } else {
                UIHelper.showToastShort(self(), errMsg + "");
            }
            int tipVisibility = mListModel.getList().size() == 0 ? View.VISIBLE : View.GONE;
            findViewById(R.id.bigtip_layout).setVisibility(tipVisibility);
        }
    }

}

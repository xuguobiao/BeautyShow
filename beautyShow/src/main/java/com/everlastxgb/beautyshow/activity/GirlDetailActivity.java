package com.everlastxgb.beautyshow.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.adapter.AutoGridAdapter;
import com.everlastxgb.beautyshow.common.UIHelper;
import com.everlastxgb.beautyshow.common.URLs;
import com.everlastxgb.beautyshow.controller.ApiController;
import com.everlastxgb.beautyshow.controller.ResultCallback;
import com.everlastxgb.beautyshow.model.GirlDetailModel;
import com.everlastxgb.beautyshow.model.GirlListModel;
import com.everlastxgb.beautyshow.model.GirlModel;
import com.everlastxgb.beautyshow.model.ItemModel;
import com.everlastxgb.beautyshow.model.PicModel;
import com.everlastxgb.beautyshow.util.ImageUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */

public class GirlDetailActivity extends BaseActivity {

    private GirlModel girlModel = new GirlModel();
    private GirlDetailModel mDetailModel = new GirlDetailModel();
    private String headThumb = "";

    private ImageView headImageView;
    private TextView infoTextView, descTextView, starTipTextView;
    private RatingBar ratingBar;

    private View gridLayout_gallery, gridLayout_girl;
    private GridView gridView_gallery, gridView_girl;
    private TextView gridTitle_gallery, gridTitle_girl;
    private AutoGridAdapter adapter_gallery, adapter_girl;
    private GirlListModel listModel_gallery, listModel_girl;

    private ProgressDialog progressDialog;

    private PullToRefreshScrollView pullToRefreshScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.girl_detail);
        setTitle(R.string.description);

        progressDialog = new ProgressDialog(this);

        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToRefreshScrollView);

        headImageView = (ImageView) findViewById(R.id.headImageView);
        infoTextView = (TextView) findViewById(R.id.infoTextView);
        descTextView = (TextView) findViewById(R.id.descTextView);
        starTipTextView = (TextView) findViewById(R.id.starTipTextView);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        initGridLayout();
        initScrollView();

        headImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<PicModel> modeList = new ArrayList<PicModel>();
                PicModel picModel = new PicModel();
                picModel.setPic(headThumb);
                picModel.setBigPic(mDetailModel.getPic());
                modeList.add(picModel);

                Intent intent = new Intent(self(), ViewPagerActivity.class);
                intent.putExtra(KEY_MODEL, modeList);
                intent.putExtra(KEY_INDEX, 0);
                intent.putExtra(KEY_DISPLAY, false);
                startActivity(intent);

            }
        });

        Object object = getIntent().getSerializableExtra(KEY_MODEL);
        if (object instanceof GirlModel) {
            girlModel = (GirlModel) object;
            mDetailModel.setName(girlModel.getName());
            mDetailModel.setPic(girlModel.getPic());
            mDetailModel.setHref(girlModel.getHref());
            mDetailModel.setDescription(girlModel.getName());
        }

        headThumb = mDetailModel.getPic();
        initData(mDetailModel, true);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                manuPullDown(pullToRefreshScrollView);
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
        adapter_gallery.setParameters(-1, -1, -1, 8, 2);
        gridView_gallery.setAdapter(adapter_gallery);

        gridView_gallery.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                GirlModel model = listModel_gallery.getList().get(arg2);
                Intent intent = new Intent(GirlDetailActivity.this, GalleryDetailActivity.class);
                intent.putExtra(KEY_MODEL, model);
                startActivity(intent);
            }
        });
        gridTitle_gallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String morePage = listModel_gallery.getMorePage();
                String title = listModel_gallery.getTitle();
                ItemModel itemModel = new ItemModel();
                itemModel.setId(morePage);
                itemModel.setTitle(title);

                Intent intent = new Intent(self(), GalleryMoreActivity.class);
                intent.putExtra(KEY_MODEL, itemModel);
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
        adapter_girl.setParameters(-1, -1, -1, 8, 3);
        gridView_girl.setAdapter(adapter_girl);

        gridView_girl.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                GirlModel model = listModel_girl.getList().get(arg2);
                Intent intent = new Intent(GirlDetailActivity.this, GirlDetailActivity.class);
                intent.putExtra(KEY_MODEL, model);
                startActivity(intent);
            }
        });

    }

    private void initScrollView() {
        pullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                startUpdate();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
    }

    private void initData(GirlDetailModel detailModel, boolean firtTime) {
        setTitle(detailModel.getName());
        if (firtTime && !headThumb.equals("")) {
            ImageLoader.getInstance().displayImage(detailModel.getPic(), headImageView, ImageUtils.getListOptions());
        }
        infoTextView.setText(detailModel.getInfo());

        String desc = detailModel.getDescription();
        desc = desc.equals("") ? detailModel.getName() : desc;
        descTextView.setText(desc);

        starTipTextView.setText(detailModel.getStarTip());
        ratingBar.setRating(detailModel.getStar());

        GirlListModel galleryListModel = detailModel.getGallery();
        listModel_gallery.getList().clear();
        listModel_gallery.getList().addAll(galleryListModel.getList());
        listModel_gallery.setMorePage(galleryListModel.getMorePage());
        listModel_gallery.setTitle(galleryListModel.getTitle());
        adapter_gallery.notifyDataSetChanged();

        gridTitle_gallery.setText(galleryListModel.getTitle());

        int galVisi = listModel_gallery.getList().size() > 0 ? View.VISIBLE : View.GONE;
        gridLayout_gallery.setVisibility(galVisi);
        setGridViewHeightBasedOnChildren(gridView_gallery);

        GirlListModel favorListModel = detailModel.getFavor();
        listModel_girl.getList().clear();
        listModel_girl.getList().addAll(favorListModel.getList());
        adapter_girl.notifyDataSetChanged();

        gridTitle_girl.setText(favorListModel.getTitle());
        int favVisi = listModel_girl.getList().size() > 0 ? View.VISIBLE : View.GONE;
        gridLayout_girl.setVisibility(favVisi);
        setGridViewHeightBasedOnChildren(gridView_girl);

    }

    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        if (!(gridView.getAdapter() instanceof AutoGridAdapter)) {
            return;
        }
        AutoGridAdapter adapter = (AutoGridAdapter) gridView.getAdapter();
        int totalHeight = 0;
        double count = (double) adapter.getCount();
        double colunm = adapter.getColunm();

        int row = (int) Math.ceil(count / colunm);
        for (int i = 0; i < row; i++) {
            View listItem = adapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    private void startUpdate() {

        ApiController.getGirlDetail(URLs.getValidUrl(mDetailModel.getHref()), resultCallback);
    }

    private ResultCallback resultCallback = new ResultCallback<GirlDetailModel>(this) {

        @Override
        public void onResult(GirlDetailModel result, boolean success, String errMsg) {
            pullToRefreshScrollView.onRefreshComplete();
            if (success) {
                mDetailModel = result;
                initData(mDetailModel, false);
            } else {
                UIHelper.showToastLong(self(), errMsg);
            }
        }
    };

}

package com.everlastxgb.beautyshow.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.common.Consts;
import com.everlastxgb.beautyshow.common.URLs;
import com.everlastxgb.beautyshow.model.GirlListModel;
import com.everlastxgb.beautyshow.model.GirlModel;
import com.everlastxgb.beautyshow.util.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */
public class AutoGridAdapter extends BaseAdapter {

    private GirlListModel listModel = new GirlListModel(GirlModel.TYPE_GIRL);
    private Context mContext;
    private GridView mGridView;
    private LayoutInflater mInflater;

    private float spaceDp = Consts.DIMEN_GRID_GAP;

    private float expectedWidthDp = Consts.DIMEN_GIRL_WIDTH;
    private float expectedHeightDp = Consts.DIMEN_GIRL_HEIGHT;

    private int minColunm = 1;
    private int colunm = 2;
    private float padding = 0f;

    public AutoGridAdapter(Context context, GirlListModel listModel, GridView gridView) {
        this.mContext = context;
        this.listModel = listModel;
        this.mInflater = LayoutInflater.from(mContext);
        this.mGridView = gridView;
        setDefaulSize();
        initGridView(calClounm());
    }

    /**
     * use the default value if less than 0
     *
     * @param expectedWidthDp
     * @param expectedHeightDp
     * @param expectedSpaceDp
     * @param parentPaddingDp
     */
    public void setParameters(float expectedWidthDp, float expectedHeightDp, float expectedSpaceDp, float parentPaddingDp, int minClounm) {
        if (expectedHeightDp > 0 && expectedWidthDp > 0) {
            this.expectedWidthDp = expectedWidthDp;
            this.expectedHeightDp = expectedHeightDp;
        }
        if (expectedSpaceDp >= 0) {
            this.spaceDp = expectedSpaceDp;
        }
        if (parentPaddingDp >= 0) {
            this.padding = parentPaddingDp;
        }
        if (minClounm >= 1) {
            this.minColunm = minClounm;
        }

        initGridView(calClounm());
    }

    private void setDefaulSize() {
        DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
        float density = metrics.density;
        int type = listModel.getType();
        switch (type) {
            case GirlModel.TYPE_GALLERY:
                expectedWidthDp = Consts.DIMEN_GALLERY_WIDTH;
                expectedHeightDp = Consts.DIMEN_GALLERY_HEIGHT;
                minColunm = 2;
                break;
            default:
                expectedWidthDp = Consts.DIMEN_GIRL_WIDTH;
                expectedHeightDp = Consts.DIMEN_GIRL_HEIGHT;
                minColunm = 3;
                break;
        }
    }

    private int calClounm() {
        DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
        float density = metrics.density;
        int wholeWidth = metrics.widthPixels - Math.round(padding * 2 * density);// parent.getWidth();
        int colunm = (int) (wholeWidth / (expectedWidthDp * density));
        if (colunm < minColunm) {
            colunm = minColunm;
        }
        this.colunm = colunm;
        return colunm;
    }

    public int getColunm() {
        return colunm;
    }

    private void initGridView(int colunm) {
        if (mGridView != null) {
            mGridView.setNumColumns(colunm);
            mGridView.setSelector(android.R.color.transparent);
        }
    }

    @Override
    public int getCount() {
        return listModel.getList().size();
    }

    @Override
    public Object getItem(int index) {
        return listModel.getList().get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GirlModel model = listModel.getList().get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.pictitle_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.itemTitleTextView);
            viewHolder.subTitle = (TextView) convertView.findViewById(R.id.itemSubTitleTextView);
            viewHolder.floatTitle = (TextView) convertView.findViewById(R.id.itemFloatTitleTextView);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.itemImageView);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            convertView.setTag(viewHolder);
            adjustView(convertView, position);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        switchLayout(viewHolder, model);

        viewHolder.title.setText(model.getName());
        viewHolder.floatTitle.setText(model.getName());

        int subVisi = model.getTag().equals("") ? View.GONE : View.VISIBLE;
        viewHolder.subTitle.setText(model.getTag());
        viewHolder.subTitle.setVisibility(subVisi);

        int rateVisi = model.getStar() < 0 ? View.GONE : View.VISIBLE;
        viewHolder.ratingBar.setVisibility(rateVisi);
        viewHolder.ratingBar.setRating(model.getStar());

        ImageLoader.getInstance().displayImage(URLs.getValidUrl(model.getPic()), viewHolder.imageView, ImageUtils.getListOptions());

        return convertView;
    }

    private void switchLayout(ViewHolder viewHolder, GirlModel model) {
        switch (model.getType()) {
            case GirlModel.TYPE_GALLERY:
                viewHolder.floatTitle.setVisibility(View.VISIBLE);
//			viewHolder.title.setVisibility(View.GONE);
//			viewHolder.subTitle.setVisibility(View.GONE);
                ((View) viewHolder.title.getParent()).setVisibility(View.GONE);
                break;
            default:
                viewHolder.floatTitle.setVisibility(View.GONE);
                ((View) viewHolder.title.getParent()).setVisibility(View.VISIBLE);
//			viewHolder.title.setVisibility(View.VISIBLE);
//			viewHolder.subTitle.setVisibility(View.VISIBLE);

                break;
        }
    }

    private void adjustView(View view, int position) {
        DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
        float density = metrics.density;
        int wholeWidth = metrics.widthPixels - Math.round(padding * 2 * density);// parent.getWidth();
        // int itemWidth = Math.round((wholeWidth - (colunm + 1) * spaceDp *
        // density) / colunm);//
        float h2w = expectedHeightDp / expectedWidthDp;
        int space = Math.round(spaceDp * density);

        int itemWidth = Math.round(wholeWidth / colunm);
        int itemHeight = Math.round(itemWidth * h2w);

        GridView.LayoutParams params = new GridView.LayoutParams(itemWidth, itemHeight);
        view.setLayoutParams(params);

        int halfSpace = Math.round(space / 2f);

        // boolean leftSide = position % colunm == 0;
        // boolean topSide = position < colunm;
        // boolean rightSide = position % colunm == colunm - 1;
        // boolean bottomSide = position + colunm >= listModel.getList().size();
        //
        // int paddingLeft = leftSide ? space : halfSpace;
        // int paddingTop = topSide ? space : halfSpace;
        // int paddingRight = rightSide ? space : halfSpace;
        // int paddingBottom = bottomSide? space : halfSpace;
        //
        // Logger.log("adjustView: position->" + position + "==" + paddingLeft
        // + ", " + paddingTop + ", " + paddingRight + ", " + paddingBottom );

        view.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
    }

    static class ViewHolder {
        public TextView title;
        public TextView subTitle;
        public TextView floatTitle;
        public ImageView imageView;
        public RatingBar ratingBar;
    }

}

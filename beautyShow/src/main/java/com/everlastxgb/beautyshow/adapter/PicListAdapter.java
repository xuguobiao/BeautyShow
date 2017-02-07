package com.everlastxgb.beautyshow.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.common.Consts;
import com.everlastxgb.beautyshow.common.URLs;
import com.everlastxgb.beautyshow.model.PicListModel;
import com.everlastxgb.beautyshow.model.PicModel;
import com.everlastxgb.beautyshow.util.ImageUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */
public class PicListAdapter extends BaseAdapter {

    private PicListModel listModel = new PicListModel();
    private Context mContext;

    private float spaceDp = Consts.DIMEN_LIST_GAP;

    private float expectedWidthDp = Consts.DIMEN_GALLERY_WIDTH_BIG;
    private float expectedHeightDp = Consts.DIMEN_GALLERY_HEIGHT_BIG;

    public PicListAdapter(Context context, PicListModel listModel) {
        this.mContext = context;
        this.listModel = listModel;
    }

    public void setPadding(float paddingDp) {
        if (paddingDp >= 0) {
            spaceDp = paddingDp;
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
        PicModel model = listModel.getList().get(position);
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.gallery_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.floatTitle = (TextView) convertView.findViewById(R.id.itemFloatTitleTextView);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.itemImageView);
//			adjustView(convertView, position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.floatTitle.setText(model.getName());

        ImageLoader.getInstance().displayImage(URLs.getValidUrl(model.getPic()), viewHolder.imageView, ImageUtils.getListOptions());

        return convertView;
    }

    private void adjustView(View view, int position) {
        DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
        float density = metrics.density;
        int wholeWidth = metrics.widthPixels;
        float h2w = expectedHeightDp / expectedWidthDp;
        int space = Math.round(spaceDp * density);

        int itemWidth = wholeWidth;
        int itemHeight = Math.round(itemWidth * h2w);

        ListView.LayoutParams params = new ListView.LayoutParams(itemWidth, itemHeight);
        view.setLayoutParams(params);

        view.setPadding(space, space, space, space);
    }

    static class ViewHolder {
        public TextView floatTitle;
        public ImageView imageView;
    }

}

package com.everlastxgb.beautyshow.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
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
public class RankListAdapter extends BaseAdapter {

    private GirlListModel listModel = new GirlListModel(GirlModel.TYPE_RANK);
    private Context mContext;

    public RankListAdapter(Context context, GirlListModel listModel) {
        this.mContext = context;
        this.listModel = listModel;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.rank_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.itemImageView);
            viewHolder.title1 = (TextView) convertView.findViewById(R.id.title1TextView);
            viewHolder.title2 = (TextView) convertView.findViewById(R.id.title2TextView);
            viewHolder.title3 = (TextView) convertView.findViewById(R.id.title3TextView);
            viewHolder.rankNum = (TextView) convertView.findViewById(R.id.rankTextView);
            viewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
            // adjustView(convertView, position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title1.setText(model.getName());
        setTextView(viewHolder.title2, model.getSubname());
        setTextView(viewHolder.title3, model.getTag());
        setTextView(viewHolder.rankNum, model.getRankNum());
        viewHolder.ratingBar.setRating(model.getStar());

        ImageLoader.getInstance().displayImage(URLs.getValidUrl(model.getPic()), viewHolder.imageView, ImageUtils.getListOptions());

        return convertView;
    }

    private void setTextView(TextView textView, String text) {
        int visi = text == null || text.equals("") ? View.GONE : View.VISIBLE;
        textView.setVisibility(visi);
        textView.setText(text);

    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView title1;
        public TextView title2;
        public TextView title3;
        public TextView rankNum;
        public RatingBar ratingBar;
    }

}

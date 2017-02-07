package com.everlastxgb.beautyshow.adapter;

/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.common.URLs;
import com.everlastxgb.beautyshow.model.PicModel;
import com.everlastxgb.beautyshow.util.ImageUtils;
import com.everlastxgb.beautyshow.widget.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<PicModel> picModelList;

    public ImagePagerAdapter(Context context, List<PicModel> picModelList) {
        this.context = context;
        this.picModelList = picModelList;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return picModelList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.viewpager_item, null);
            holder = new ViewHolder();
            holder.photoView = (PhotoView) view.findViewById(R.id.iv_photo);

            holder.downloadTextView = (TextView) view.findViewById(R.id.tv_download);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        // holder.imageView.setImageResource(imageIdList.get(getPosition(position)));
        PicModel picModel = picModelList.get(position);

        ImageLoader.getInstance().displayImage(URLs.getValidUrl(picModel.getPic()), holder.photoView, ImageUtils.getListOptions());

        int downloadVisi = View.GONE;
        holder.downloadTextView.setVisibility(downloadVisi);
        // if (picModel.getTotalSize() > 0) {
        // int progress = (int) ((float) (picModel.getCurrentSize()) / (float)
        // (picModel.getTotalSize()));
        // String tip = progress * 100 + "%";
        // holder.downloadTextView.setText(tip);
        // }
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    private static class ViewHolder {

        PhotoView photoView;
        TextView downloadTextView;
    }

}

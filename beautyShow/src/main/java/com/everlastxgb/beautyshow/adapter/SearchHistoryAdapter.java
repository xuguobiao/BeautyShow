package com.everlastxgb.beautyshow.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;

import java.util.ArrayList;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */
public class SearchHistoryAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<String> list = new ArrayList<String>();
    private final int layout = R.layout.select_list_item;

    public SearchHistoryAdapter(Context ctx, ArrayList<String> list) {
        this.ctx = ctx;
        this.list = list;
    }

    public int getCount() {
        return list.size();
    }

    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    public long getItemId(int arg0) {
        return arg0;
    }

    public View getView(int arg0, View arg1, ViewGroup arg2) {
        Holder hold;
        if (arg1 == null) {
            hold = new Holder();
            arg1 = View.inflate(ctx, layout, null);
            hold.txt = (TextView) arg1.findViewById(R.id.Search_more_moreitem_txt);
            hold.layout = (LinearLayout) arg1.findViewById(R.id.More_list_lishi);
            arg1.setTag(hold);
        } else {
            hold = (Holder) arg1.getTag();
        }
        hold.txt.setText(list.get(arg0));
        return arg1;
    }

    private static class Holder {
        LinearLayout layout;
        TextView txt;
    }
}

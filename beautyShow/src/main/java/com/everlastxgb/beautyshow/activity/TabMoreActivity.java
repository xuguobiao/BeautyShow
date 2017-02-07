package com.everlastxgb.beautyshow.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.common.Consts;
import com.everlastxgb.beautyshow.common.FileHelper;
import com.everlastxgb.beautyshow.common.FileManager;
import com.everlastxgb.beautyshow.common.UIHelper;
import com.everlastxgb.beautyshow.common.URLs;
import com.everlastxgb.beautyshow.util.AsynThreadPool;
import com.everlastxgb.beautyshow.util.ShareUtils;
import com.pgyersdk.feedback.PgyFeedback;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */
public class TabMoreActivity extends BaseTabActivity {
    private ProgressDialog progressDialog;
    private TextView clearSizeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_more);

        setTitle(R.string.tab_more);

        clearSizeTextView = (TextView) findViewById(R.id.more_item_clear_size);
        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        clearSizeTextView.setText(getCacheSize());
    }

    private String getCacheSize() {
        long bSize = FileHelper.getFolderSize(FileManager.getCacheDirFile(this));
        double mSize = bSize / (1024.0 * 1024.0);
        String size = Math.round(mSize * 100) / 100.0 + " M";

        return size;
    }

    private void deleteCacheFile() {
        progressDialog.setMessage(getString(R.string.deletecache_ing));
        progressDialog.show();
        AsynThreadPool.getInstance().runSingleThread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileHelper.deleteDirectory(FileManager.getCacheDirFile(self()).getAbsolutePath());
                } catch (Exception e) {
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            clearSizeTextView.setText(getCacheSize());
                            UIHelper.showToastShort(TabMoreActivity.this, getString(R.string.deletecache_sum));
                        }
                    });
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.more_item_help:
                UIHelper.showAlertDialog(this, getString(R.string.more_item_help), getString(R.string.tip_help));
                break;
            case R.id.more_item_inform:
                UIHelper.showAlertDialog(this, getString(R.string.more_item_inform), getString(R.string.tip_inform));
                break;
            case R.id.more_item_clear:
                deleteCacheFile();
                break;


            case R.id.more_item_fankui:
                PgyFeedback.getInstance().showDialog(this);
                break;
            case R.id.more_item_update:
                String version = "";
                try {
                    version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                } catch (Exception e) {
                }
                UIHelper.showAlertDialog(this, getString(R.string.more_item_update), getString(R.string.tip_update_format_v, version));
                break;
            case R.id.more_item_we:
                UIHelper.showAlertDialog(this, getString(R.string.more_item_we), getString(R.string.tip_we), R.drawable.kido_round);
                break;
            case R.id.more_item_share:
                ShareUtils.showShare(this.getApplicationContext(), Consts.SHARE_ICON_PATH, getString(R.string.app_name), getString(R.string.share_message), URLs.APP_HOME);
                break;
        }
    }

}

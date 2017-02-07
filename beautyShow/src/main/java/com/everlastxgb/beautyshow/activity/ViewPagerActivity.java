package com.everlastxgb.beautyshow.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.adapter.ImagePagerAdapter;
import com.everlastxgb.beautyshow.common.Consts;
import com.everlastxgb.beautyshow.common.FileHelper;
import com.everlastxgb.beautyshow.common.UIHelper;
import com.everlastxgb.beautyshow.model.PicModel;
import com.everlastxgb.beautyshow.util.ImageUtils;
import com.everlastxgb.beautyshow.util.Logger;
import com.everlastxgb.beautyshow.widget.photoview.HackyViewPager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.io.File;
import java.util.ArrayList;

public class ViewPagerActivity extends BaseActivity implements OnClickListener {

	private static final String ISLOCKED_ARG = "isLocked";

	private ViewPager mViewPager;
	private ImagePagerAdapter pagerAdapter;
	private ArrayList<PicModel> modelList = new ArrayList<PicModel>();

	private TextView indicatorTextView, saveTextView, downloadTextView;
	private View backView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);

		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
		indicatorTextView = (TextView) findViewById(R.id.tv_indicator);
		saveTextView = (TextView) findViewById(R.id.tv_save);
		downloadTextView = (TextView) findViewById(R.id.tv_download);
		backView = findViewById(R.id.iv_back);

		mViewPager.setOnClickListener(this);
		saveTextView.setOnClickListener(this);
		downloadTextView.setOnClickListener(this);
		backView.setOnClickListener(this);

		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG, false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}

		initViewPager();
	}

	private void initViewPager() {
		modelList = (ArrayList<PicModel>) getIntent().getSerializableExtra(KEY_MODEL);
		for (PicModel picModel : modelList) {
			if (picModel.getBigPic().equals("")) {
				picModel.setProgress(100);
			} else {
				boolean bigExists = ImageLoader.getInstance().getDiscCache().get(picModel.getBigPic()).exists();
//				if (bigExists) {
//					picModel.setPic(picModel.getBigPic());
//					picModel.setProgress(100);
//				}
			}
		}
		pagerAdapter = new ImagePagerAdapter(this, modelList);
		mViewPager.setAdapter(pagerAdapter);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				updateDownloadTip(arg0);
				updateIndicator(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		int index = getIntent().getIntExtra(KEY_INDEX, 0);
		mViewPager.setCurrentItem(index);
		updateIndicator(index);
		updateDownloadTip(index);

		boolean display = getIntent().getBooleanExtra(KEY_DISPLAY, true);
		int indiVisi = display ? View.VISIBLE : View.GONE;
		indicatorTextView.setVisibility(indiVisi);
	}

	private boolean isViewPagerActive() {
		return (mViewPager != null && mViewPager instanceof HackyViewPager);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(ISLOCKED_ARG, ((HackyViewPager) mViewPager).isLocked());
		}
		super.onSaveInstanceState(outState);
	}

	private void updateIndicator(int position) {
		indicatorTextView.setText(position + 1 + "/" + modelList.size());
	}

	private void updateDownloadTip(int position) {
		if (mViewPager.getCurrentItem() == position) {
			PicModel picModel = modelList.get(position);
			String tip = getString(R.string.download_src);
			if (picModel.getCurrentSize() > 0) {
				tip = getString(R.string.download) + " " + picModel.getProgress() + "%";
				downloadTextView.setEnabled(false);
			} else {
				downloadTextView.setEnabled(true);
			}
			downloadTextView.setText(tip);
			int visi = picModel.getProgress() >= 100 ? View.GONE : View.VISIBLE;
			downloadTextView.setVisibility(visi);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mViewPager) {
			// finish();
		} else if (v == saveTextView) {
			String url = modelList.get(mViewPager.getCurrentItem()).getPic();
			File cacheFile = ImageLoader.getInstance().getDiscCache().get(url);
			if (cacheFile != null ) {
				String newPath = Consts.FOLDER_PATH_SAVE + "/" + cacheFile.getName() + ".jpg";
				boolean success = FileHelper.Copy(cacheFile, newPath);
				if (success) {
					UIHelper.showToastLong(self(), getString(R.string.success_to_save) + newPath);
				} else {
					UIHelper.showToastLong(self(), getString(R.string.fail_to_save));
				}
			}
		} else if (v == downloadTextView) {
			int pos = mViewPager.getCurrentItem();
			new DownloadSrc(pos);
		} else if (v == backView) {
			finish();
		}
	}

	private class DownloadSrc implements ImageLoadingProgressListener, ImageLoadingListener {

		private int position;
		private PicModel picModel;
		private String srcUrl;

		public DownloadSrc(int position) {
			this.position = position;
			picModel = modelList.get(position);
			srcUrl = picModel.getPic().replace("/s/", "/");
			Logger.log("satrt Download src-> pos: " + position + ", srcUrl: " + srcUrl);
			ImageLoader.getInstance().loadImage(srcUrl, new ImageSize(1, 1), ImageUtils.getListOptions(), this, this);
		}

		@Override
		public void onProgressUpdate(String imageUri, View view, int current, int total) {
			int progress = (int) ((float) current / (float) total * 100);
			picModel.setCurrentSize(current);
			picModel.setTotalSize(total);
			picModel.setProgress(progress);
			updateDownloadTip(position);
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			Logger.log("onLoadingStarted-> pos: " + position + ", srcUrl: " + srcUrl);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			picModel.setCurrentSize(0);
			picModel.setTotalSize(0);
			picModel.setProgress(0);
			updateDownloadTip(position);
			UIHelper.showToastShort(self(), getString(R.string.fail_to_load_image));
		}

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			picModel.setProgress(100);
			updateDownloadTip(position);
			picModel.setPic(srcUrl);
			pagerAdapter.notifyDataSetChanged();
			// mViewPager.getChildAt(position).findViewById(R.id.iv_photo);
			Logger.log("onLoadingComplete-> pos: " + position + ", srcUrl: " + srcUrl);
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			picModel.setCurrentSize(0);
			picModel.setTotalSize(0);
			picModel.setProgress(0);
			updateDownloadTip(position);
		}

	}

}

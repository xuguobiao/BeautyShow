package com.everlastxgb.beautyshow.controller;

import android.app.Activity;

import java.lang.ref.WeakReference;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * 
 * @create_time: 2015-9-1 PM 6:00:21
 * 
 */

public abstract class ResultCallback<M> {
	private Activity mActivity;

	public ResultCallback(Activity activity) {
		mActivity = new WeakReference<>(activity).get();
	}

	public Activity getActivity() {
		return mActivity;
	}

	public void callbackInUI(final Object resultObject, final boolean success, final String errMsg) {

		if(mActivity == null){
			return;
		}

		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				onResult((M) resultObject, success, errMsg);
			}
		});
	}

	public abstract void onResult(M result, boolean success, String errMsg);

	// public void onRestoreData(Object data) {
	//
	// }
}

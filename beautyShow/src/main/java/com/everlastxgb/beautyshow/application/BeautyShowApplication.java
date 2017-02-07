package com.everlastxgb.beautyshow.application;

import android.app.Application;
import android.provider.SyncStateContract;

import com.everlastxgb.beautyshow.common.CommonMethods;
import com.everlastxgb.beautyshow.common.Consts;
import com.everlastxgb.beautyshow.common.FileHelper;
import com.everlastxgb.beautyshow.common.KeyValueStorage;
import com.everlastxgb.beautyshow.controller.HtmlController;
import com.everlastxgb.beautyshow.util.AsynThreadPool;
import com.everlastxgb.beautyshow.util.ImageUtils;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.views.PgyerDialog;
import com.tendcloud.tenddata.TCAgent;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * 
 * @create_time: 2015-9-1 10:45:03
 * 
 */

public class BeautyShowApplication extends Application {
	private final static String app_config_name = "app_config";
	private final static String user_list_name = "user_list";

	private final static String KEY_COOKIE = "user_cookie";
	private final static String KEY_LOGIN_INFO = "loginInfo_jsonstring";

	private KeyValueStorage mKeyValueStorage;
	private KeyValueStorage mUserListStorage;

	private String mUserAgent = "";
	private String mCookie = "";

	@Override
	public void onCreate() {
		super.onCreate();
		mKeyValueStorage = new KeyValueStorage(this, app_config_name);
		mUserListStorage = new KeyValueStorage(this, user_list_name);
		mUserAgent = CommonMethods.getUserAgent(this);

		ImageUtils.initImageLoader(this);
		
		HtmlController.setUserAgent(mUserAgent);
		
		PgyCrashManager.register(this);
		PgyerDialog.setDialogTitleBackgroundColor("#2196f3");
		
		TCAgent.init(this);

		AsynThreadPool.getInstance().runSingleThread(new Runnable() {
			@Override
			public void run() {
				FileHelper.copyAssetToSD(getApplicationContext(), Consts.SHARE_ICON_ASSET_NAME, Consts.SHARE_ICON_PATH);
			}
		});
	}


	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public String getUserAgent() {
		return mUserAgent;
	}

	public String getCookie() {
		if (mCookie == null || "".equals(mCookie)) {
			mCookie = mKeyValueStorage.getItem(KEY_COOKIE);
		}
		return mCookie;
	}

	public void setCookie(String cookie) {
		this.mCookie = cookie;
		mKeyValueStorage.setItem(KEY_COOKIE, cookie);
	}

	public void clearCookie() {
		this.mCookie = "";
		mKeyValueStorage.removeItem(KEY_COOKIE);
	}
}

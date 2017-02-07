/**
 *	Created on 2012-9-13, WQKeyValueStorage.java 
 *	@Author:  XuGuobiao
 *	@Comment: 
 */

package com.everlastxgb.beautyshow.common;

import java.util.ArrayList;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class KeyValueStorage {

	private Context mContext;
	private SharedPreferences mPreferences;

	private static final String DEFAUL_PRE_NAME = "app_key_value";

	public KeyValueStorage(Context context) {
		this(context, DEFAUL_PRE_NAME);
	}

	public KeyValueStorage(Context context, String preName) {
		mContext = context;
		mPreferences = mContext.getSharedPreferences(preName, 0);
	}

	public void setItem(String key, String value) {
		Editor editor = mPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public boolean isExisted(String key) {
		return mPreferences.contains(key);
	}

	public String getItem(String key) {
		return mPreferences.getString(key, "");
	}

	public void removeItem(String key) {
		Editor editor = mPreferences.edit();
		editor.remove(key);
		editor.commit();
	}

	public ArrayList<String> getKeyList() {
		Set<String> keySet = mPreferences.getAll().keySet();
		return new ArrayList<String>(keySet);
	}

	public int getItemsCount() {
		return mPreferences.getAll().size();
	}

	public void clear() {
		Editor editor = mPreferences.edit();
		editor.clear();
		editor.commit();
	}
}

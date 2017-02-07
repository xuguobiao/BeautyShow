package com.everlastxgb.beautyshow.common;

import java.io.File;

import android.content.Context;
import android.os.storage.StorageManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class FileManager {

	public static final File getCacheDirFile(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, Consts.FOLDER_CACHE_NAME);
		return cacheDir;
	}

}

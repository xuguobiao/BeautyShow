package com.everlastxgb.beautyshow.common;

import android.os.Environment;

public class Consts {

	public static final String FOLDER_NAME = "BeautyShow";
	public static final String FOLDER_CACHE_NAME = FOLDER_NAME + "/Cache";
	public static final String FOLDER_PATH = Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME;
	public static final String FOLDER_PATH_SAVE = FOLDER_PATH + "/Saved";

	public static final String SHARE_ICON_ASSET_NAME = "logo.png";
	public static final String SHARE_ICON_PATH = FOLDER_PATH + "/Share/" + SHARE_ICON_ASSET_NAME;

	public static final float DIMEN_GALLERY_WIDTH_BIG = 1000f;
	public static final float DIMEN_GALLERY_HEIGHT_BIG = 1500f;

	public static final float DIMEN_GALLERY_WIDTH = 200f;
	public static final float DIMEN_GALLERY_HEIGHT = 300f;

	public static final float DIMEN_GIRL_WIDTH = 150f;
	public static final float DIMEN_GIRL_HEIGHT = 210f + 30f;// + 10f;//10f for the
														// textview below

	public static final float DIMEN_GRID_GAP = 8f;
	public static final float DIMEN_LIST_GAP = 10f;
	
	public static final long DELAY_MS_REFRESH = 300;

}

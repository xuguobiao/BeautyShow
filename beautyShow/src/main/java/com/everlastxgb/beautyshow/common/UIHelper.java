package com.everlastxgb.beautyshow.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.everlastxgb.beautyshow.R;

public class UIHelper {

	public static void showToastLong(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	public static void showToastShort(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void showAlertDialog(Context context, String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message);
		builder.setPositiveButton(context.getString(R.string.confirm), null);
		builder.create().show();
	}

	public static void showAlertDialog(Context context, String title, String message, int iconId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title).setMessage(message).setIcon(iconId);
		builder.setPositiveButton(context.getString(R.string.confirm), null);
		builder.create().show();
	}

	public static void showAlertDialog(Context context, String title, String message, boolean success) {
		int iconId = success ? R.drawable.icon_right : R.drawable.icon_wrong;
		showAlertDialog(context, title, message, iconId);
	}

	public static void showTipDialog(Context context, String message) {
		// AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// builder.setTitle(context.getString(R.string.wenxin_tips)).setMessage(message);
		// builder.setPositiveButton(context.getString(R.string.confirm), null);
		// builder.create().show();
		showAlertDialog(context, context.getString(R.string.wenxin_tips), message);
	}

	public static void showActivity(Context context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}

//	public static void displayImageActivity(Context context, String url, int defaultResId) {
//		String headPic = url + "";
//		headPic = !headPic.equals("null") && !headPic.equals("") ? URLs.getValidUrl(headPic) : "drawable://" + defaultResId;
//		Intent intent = new Intent(context, DisplayImgActivity.class);
//		intent.putExtra(DisplayImgActivity.KEY_PIC_URL, headPic);
//		context.startActivity(intent);
//	}

}

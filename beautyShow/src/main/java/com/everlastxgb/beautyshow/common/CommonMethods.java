package com.everlastxgb.beautyshow.common;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.everlastxgb.beautyshow.util.Logger;

/**
 * 
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * 
 *         Created on 2015-4-7 PM 2:38:04
 * 
 */
public class CommonMethods {

	/**
	 * must called in UI-Thread
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserAgent(Context context) {
		String ua = null;
		// must be called in UI-Thread
		try {
			WebView webview;
			webview = new WebView(context);
			webview.layout(0, 0, 0, 0);
			WebSettings settings = webview.getSettings();
			ua = settings.getUserAgentString();
		} catch (Exception e) {
		}
		return ua;
	}

	public static int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void click2Call(Context context, String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void click2Websiter(Context context, String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static boolean checkNetState(Context context) {
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}

	/**
	 * 
	 * @param url
	 * @param map
	 * @return url like xxx.xxx?a=1&b=2&
	 */
	public static String buildUrl(String url, Map<String, String> map) {
		String urlTail = "";
		for (String key : map.keySet()) {
			String value = "";
			try {
				value = URLEncoder.encode(map.get(key) + "", HTTP.UTF_8);
			} catch (Exception e) {
			}
			urlTail += "&" + key + "=" + value;
		}
		String urlHead = url.endsWith("?") ? url : url + "?";

		return urlHead + urlTail.substring(1);
	}

	/**
	 * digitString.replaceAll("[^0-9]", "")
	 * 
	 * @param digitString
	 * @return
	 */
	public static long parse2Long(String digitString) {
		try {
			return Long.parseLong(digitString.replaceAll("[^0-9]", ""));
		} catch (Exception e) {
			return 0;
		}
	}

	public static int parse2Int(String digitString) {
		try {
			return Integer.parseInt(digitString.replaceAll("[^0-9]", ""));
		} catch (Exception e) {
			return 0;
		}
	}

	public static double parse2Double(String digitString) {
		try {
			return Double.parseDouble(digitString.trim());
		} catch (Exception e) {
			return 0;
		}
	}

	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
			imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	public static void showKeyboard(Activity activity, EditText editText) {
		((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(editText, 0);
	}

	public static String getPercent(int curIndex, int size) {
		String tip = size > 0 ? (curIndex % size + 1) + "/" + size : "";
		return tip;
	}

	public static void adJustHeightByFullWidth(View view, float height2Width) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) view.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
		view.getLayoutParams().height = (int) (height2Width * dm.widthPixels);
		view.requestLayout();
	}

	// date methods

	public static final String TIMEFORMAT_STRING0 = "yyyy-MM-dd HH:mm:ss";
	public static final String TIMEFORMAT_STRING1 = "yyyyMMddHHmm";
	public static final String TIMEFORMAT_STRING2 = "HH:mm:ss";
	public static final String TIMEFORMAT_STRING3 = "yyyy-MM-dd";
	public static final String TIMEFORMAT_STRING4 = "HH:mm";
	public static final String TIMEFORMAT_STRING5 = "yyMMddHHmmss";
	public static final String TIMEFORMAT_STRING6 = "yyyy-MM-dd HH:mm";

	public static final String[] TIMEFORMAT_STRINGS = { TIMEFORMAT_STRING0, TIMEFORMAT_STRING1, TIMEFORMAT_STRING2, TIMEFORMAT_STRING3, TIMEFORMAT_STRING4, TIMEFORMAT_STRING5, TIMEFORMAT_STRING6 };

	/**
	 * 
	 * @param format
	 *            0: yyyy-MM-dd HH:mm:ss, 1: yyyyMMddHHmm, 2: HH:mm:ss, 3:
	 *            yyyy-MM-dd 4: HH:mm, 5: yyMMddHHmmss, 6: yyyy-MM-dd HH:mm
	 * @return
	 */
	public static String getCurrentFormatTime(int format) {
		SimpleDateFormat timeFormat = new SimpleDateFormat(TIMEFORMAT_STRINGS[format]);
		Date date = new Date(System.currentTimeMillis());
		return timeFormat.format(date);
	}

	public static String getCurrentFormatTime(String format) {
		SimpleDateFormat timeFormat = new SimpleDateFormat(format);
		Date date = new Date(System.currentTimeMillis());
		return timeFormat.format(date);
	}

	public static String MD5(String input) {
		if (input == null || input.length() == 0)
			return "null";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			char[] charArray = input.toCharArray();
			byte[] byteArray = new byte[charArray.length];

			for (int i = 0; i < charArray.length; i++)
				byteArray[i] = (byte) charArray[i];

			byte[] md5Bytes = md5.digest(byteArray);

			StringBuffer hexValue = new StringBuffer();

			for (int i = 0; i < md5Bytes.length; i++) {
				int val = ((int) md5Bytes[i]) & 0xff;
				if (val < 16)
					hexValue.append("0");
				hexValue.append(Integer.toHexString(val));
			}

			return hexValue.toString().toUpperCase(Locale.getDefault());
		} catch (Exception e) {
			Logger.e("MD5->" + e.getMessage());
			return "null";
		}
	}

	public static String utc2Local(String utcTime) {
		SimpleDateFormat utcFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		utcFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gpsUTCDate = null;
		try {
			gpsUTCDate = utcFormater.parse(utcTime);
		} catch (Exception e) {
		}
		SimpleDateFormat localFormater = new SimpleDateFormat(TIMEFORMAT_STRING6);
		localFormater.setTimeZone(TimeZone.getDefault());
		String localTime = gpsUTCDate != null ? localFormater.format(gpsUTCDate.getTime()) : utcTime;
		return localTime;
	}

	/**
	 * 
	 * @param beginTime
	 *            with format TIMEFORMAT_STRING1
	 * @param endTime
	 *            with format TIMEFORMAT_STRING1
	 * @return day of the interval (end sub begin)
	 * @throws Exception
	 */
	public static int compareTime(String begin, String end) {
		try {
			SimpleDateFormat dataFormat = new SimpleDateFormat(TIMEFORMAT_STRING6);
			Date beginDate = (Date) dataFormat.parse(begin);
			Date endDate = (Date) dataFormat.parse(end);
			int diff = (endDate.getDay() - beginDate.getDay()); // ms
			return diff;
		} catch (Exception e) {
			return 0;
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}

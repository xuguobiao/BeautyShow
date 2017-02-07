package com.everlastxgb.beautyshow.common;

import android.webkit.URLUtil;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class URLs {

	public static final String SERVER_URL = "http://m.zngirls.com";

	public static final String GALLERYS = SERVER_URL + "/gallery/";
	public static final String RANK = SERVER_URL + "/rank/";
	public static final String Search = SERVER_URL + "/query.aspx";
	public static final String FIND = SERVER_URL + "/find";

	public static final String GIRLLIST_NEW = SERVER_URL + "/tag/new";
	public static final String SORT = SERVER_URL + "/sort.aspx";


	// app home url
	public static final String APP_HOME = "https://www.pgyer.com/beautyshow";

	public static final String getValidUrl(String url) {

		if (!URLUtil.isNetworkUrl(url)) {
			String mid = url.startsWith("/") ? "" : "/";
			url = URLs.SERVER_URL + mid + url;
		}
		return url;

	}

	// /http://m.zngirls.com/find/country/%E4%B8%AD%E5%9B%BD/bloodtype/A/professional/%E6%BC%94%E5%91%98/xingzuo/%E7%99%BD%E7%BE%8A%E5%BA%A7/age/16-42/height/145-154/chest/85-97/waist/50-60/hip/85-100/cup/D-I/
	public static final String buildFindUrl(String country, String professional, String xingzuo, String bloodtype, String age, String height, String chest, String waist, String hip, String cup) {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("country", country);
		paramMap.put("professional", professional);
		paramMap.put("xingzuo", xingzuo);
		paramMap.put("bloodtype", bloodtype);

		paramMap.put("age", age);
		paramMap.put("height", height);
		paramMap.put("chest", chest);
		paramMap.put("waist", waist);
		paramMap.put("hip", hip);
		paramMap.put("cup", cup);

		return buildFolderUrl(URLs.FIND, paramMap);
	}

	public static final String buildFolderUrl(String url, Map<String, Object> paramMap) {
		StringBuilder builder = new StringBuilder();
		builder.append(url);
		try {
			for (String key : paramMap.keySet()) {
				String value = paramMap.get(key) + "";
				if (!value.equals("") && !value.equals("null")) {
					builder.append("/" + key + "/" + URLEncoder.encode(paramMap.get(key) + "", "utf-8"));
				}
			}
		} catch (Exception e) {
		}

		return builder.toString();
	}
}

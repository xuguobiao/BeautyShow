package com.everlastxgb.beautyshow.controller;

import com.everlastxgb.beautyshow.common.URLs;
import com.everlastxgb.beautyshow.model.FilterConditionModel;
import com.everlastxgb.beautyshow.model.GalleryDetailModel;
import com.everlastxgb.beautyshow.model.GirlDetailModel;
import com.everlastxgb.beautyshow.model.GirlListModel;
import com.everlastxgb.beautyshow.model.GirlModel;
import com.everlastxgb.beautyshow.model.ItemListModel;
import com.everlastxgb.beautyshow.model.PageModel;
import com.everlastxgb.beautyshow.util.Logger;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-9-1 10:51:15
 */

public class HtmlController {

    /***************
     * the mobile web
     *************************/

    private static final String KEY_COOKIE = "Cookie";
    private static final String KEY_USERAGENT = "User-Agent";
    private static final String KEY_REFERER = "Referer";


    protected static final int TIMEOUT_CONNECTION = 10000;

    protected static final int HTTP_GET = 1001;
    protected static final int HTTP_POST = 1002;

    protected static String sUserAgent = "";
    protected static Map<String, String> sCookies = null;
    protected static String sCookiesString = "";

    private final static String REQUEST_DATA_ERROR = "数据请求失败";

    public static void setUserAgent(String ua) {
        sUserAgent = ua;
    }

    public static String getsUserAgent() {
        return sUserAgent;
    }

    public static void setCookie(Map<String, String> cookies) {
        sCookies = cookies;
        if (cookies != null) {
            String cookieStr = "";
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                cookieStr += entry.getKey() + "=" + entry.getValue() + ";";
            }
            sCookiesString = cookieStr;
        }
    }

    public static String getCookie() {
        String cookieString = "";
        if (sCookies != null) {
            for (Map.Entry<String, String> entry : sCookies.entrySet()) {
                cookieString += entry.getKey() + "=" + entry.getValue() + ";";
            }
        }
        return cookieString;
    }

    public static Map<String, String> getHeaders() {

        Map<String, String> headers = new HashMap<>();
        headers.put(KEY_USERAGENT, getsUserAgent());
        headers.put(KEY_COOKIE, getCookie());
        headers.put(KEY_REFERER, URLs.SERVER_URL); // 这里防止被识别为“盗链”

        return headers;
    }

    protected static Document requestData(String url, int httpMethod, Map<String, String> data) throws Exception {
        try {
            Logger.log("request url->" + url);
            Connection connection = Jsoup.connect(url).timeout(TIMEOUT_CONNECTION).userAgent(sUserAgent);
            if (data != null && data.size() > 0) {
                connection.data(data);
                Logger.log("request data->" + new JSONObject(data).toString());
            }
            if (sCookies != null) {
                connection.cookies(sCookies);
            }
            Document resultDocument = httpMethod == HTTP_POST ? connection.post() : connection.get();
            return resultDocument;

        } catch (Exception e) {
            throw new Exception(REQUEST_DATA_ERROR);
        }
    }

    public static void requestSort(String sort, String dir) throws Exception {
        try {
            HashMap<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("sort", sort);
            dataMap.put("dir", dir);
            Response res = Jsoup.connect(URLs.SORT).data(dataMap).method(Method.POST).execute();
            setCookie(res.cookies());
            Logger.log("requestSort, sort=" + sort + ", dir=" + dir + ", sCookies->" + new JSONObject(sCookies).toString());
        } catch (Exception e) {
            throw new Exception(REQUEST_DATA_ERROR);
        }
    }

    // public static void requestSort(HashMap<String, String> dataMap) throws
    // Exception {
    //
    // Document document = requestData(URLs.SORT, HTTP_POST, dataMap);
    //
    // }

    public static GirlListModel searchGirl(String name) throws Exception {
        GirlListModel listModel = new GirlListModel(GirlModel.TYPE_RANK);

        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("name", name);

        // String url = CommonMethods.buildUrl(URLs.Search, dataMap);

        // Document document = requestData(url, HTTP_GET, null);
        Document document = requestData(URLs.Search, HTTP_GET, dataMap);

        Element listElement = document.getElementById("listdiv");
        listModel = GirlListModel.parse(listElement, GirlModel.TYPE_RANK);

        String title = "";
        listModel.setTitle(title);

        Element pageElement = document.getElementById("pagediv");

        PageModel pageModel = PageModel.parse(pageElement);
        listModel.setPageModel(pageModel);

        Logger.log("searchGirl size->" + listModel.getList().size() + ", nextpage: " + pageModel.getNextHref() + ", title: " + title);

        return listModel;
    }

    public static GirlListModel getNewGirlListModel(String url) throws Exception {
        GirlListModel listModel = new GirlListModel(GirlModel.TYPE_GIRL);

        Document document = requestData(url, HTTP_GET, null);

        Element listElement = document.getElementById("dlist");
        listModel = GirlListModel.parse(listElement, GirlModel.TYPE_GIRL);

        String title = document.select("#dTitle").text();
        listModel.setTitle(title);

        Element pageElement = document.getElementById("pagediv");
        PageModel pageModel = PageModel.parse(pageElement);
        listModel.setPageModel(pageModel);

        ItemListModel sortListModel = ItemListModel.parseOrder(document.getElementById("page"));
        listModel.setSortListModel(sortListModel);

        // Elements ulElements = document.select("#menu ul");
        // for (Element filterUl : ulElements.subList(0, 2)) {
        // ItemListModel filterListModel = ItemListModel.parseFilter(filterUl);
        // if (filterListModel.getList().size() > 0)
        // listModel.getFilterListModelList().add(filterListModel);
        // }
        Elements ulElements = document.select("#menu ul li ul");
        for (Element filterUl : ulElements) {
            Element prevElement = filterUl.previousElementSibling();
            if (prevElement.attr("href").contains("tag") || prevElement.attr("href").equals("#")) {
                ItemListModel filterListModel = ItemListModel.parseFilter(filterUl);
                filterListModel.setTitle(prevElement.text());
                if (filterListModel.getList().size() > 0)
                    listModel.getFilterListModelList().add(filterListModel);
            }
        }

        FilterConditionModel conditionModel = FilterConditionModel.parse(document.select("body").first());
        listModel.setConditionModel(conditionModel);


        Logger.log("getNewGirlListModel size->" + listModel.getList().size() + ", nextpage: " + pageModel.getNextHref() + ", title: " + title);

        return listModel;
    }

    public static GirlListModel getGalleryGirlListModel(String url) throws Exception {
        GirlListModel listModel = new GirlListModel(GirlModel.TYPE_GIRL);

        Document document = requestData(url, HTTP_GET, null);

        Element listElement = document.getElementById("gallerydiv");
        listModel = GirlListModel.parse(listElement, GirlModel.TYPE_GALLERY);

        String title = document.select("#htitle").text();
        listModel.setTitle(title);

        Element pageElement = document.getElementById("pagediv");

        PageModel pageModel = PageModel.parse(pageElement);
        listModel.setPageModel(pageModel);

        ItemListModel sortListModel = ItemListModel.parseOrder(document.getElementById("page"));
        listModel.setSortListModel(sortListModel);

        // Elements ulElements1 = document.select("#menu");
        // Elements ulElements = document.select("#menu ul li ul");
        // for (Element filterUl : ulElements) {
        // Element prevElement = filterUl.previousElementSibling();
        // if (prevElement.attr("href").contains("tag") ||
        // prevElement.attr("href").equals("#")) {
        // ItemListModel filterListModel = ItemListModel.parseFilter(filterUl);
        // filterListModel.setTitle(prevElement.text());
        // if (filterListModel.getList().size() > 0)
        // listModel.getFilterListModelList().add(filterListModel);
        // }
        // }

        Elements filterUls = document.select("ul.ck-filter-gallery");
        for (Element filterUl : filterUls) {
            ItemListModel filterListModel = ItemListModel.parseFilter(filterUl);
            if (filterListModel.getList().size() > 0)
                listModel.getFilterListModelList().add(filterListModel);
        }

        Logger.log("getGalleryGirlListModel size->" + listModel.getList().size() + ", nextpage: " + pageModel.getNextHref() + ", title: " + title);

        return listModel;
    }

    public static GirlListModel getRankGirlListModel(String url) throws Exception {
        GirlListModel listModel = new GirlListModel(GirlModel.TYPE_RANK);

        Document document = requestData(url, HTTP_GET, null);

        Element listElement = document.getElementById("dlist");
        listModel = GirlListModel.parse(listElement, GirlModel.TYPE_RANK);

        String title = document.select("#dTitle").text();
        listModel.setTitle(title);

        Element pageElement = document.getElementById("pagediv");

        PageModel pageModel = PageModel.parse(pageElement);
        listModel.setPageModel(pageModel);

        ItemListModel sortListModel = ItemListModel.parseOrder(document.getElementById("page"));
        listModel.setSortListModel(sortListModel);

        // ItemListModel filterListModel1 =
        // ItemListModel.parseFilter(document.getElementById("mm-5"));
        // listModel.getFilterListModelList().add(filterListModel1);
        Elements ulElements = document.select("#menu ul li ul");
        for (Element filterUl : ulElements) {
            Element prevElement = filterUl.previousElementSibling();
            if (prevElement.attr("href").contains("rank")) {
                ItemListModel filterListModel = ItemListModel.parseFilter(filterUl);
                filterListModel.setTitle(prevElement.text());
                if (filterListModel.getList().size() > 0)
                    listModel.getFilterListModelList().add(filterListModel);
            }
        }

        Logger.log("getRankGirlListModel size->" + listModel.getList().size() + ", nextpage: " + pageModel.getNextHref() + ", title: " + title);

        return listModel;
    }

    public static GirlDetailModel getGirlDetailModel(String url) throws Exception {

        Document document = requestData(url, HTTP_GET, null);
        Element documentElement = document.getElementById("page");
        GirlDetailModel detailModel = GirlDetailModel.parse(documentElement);

        Logger.log("getGirlDetailModel->" + detailModel.getName());

        return detailModel;
    }

    public static GalleryDetailModel getGalleryDetailModel(String url) throws Exception {

        Document document = requestData(url, HTTP_GET, null);
        Element pageElement = document.getElementById("page");
        GalleryDetailModel detailModel = GalleryDetailModel.parse(pageElement);

        Logger.log("getGalleryDetailModel->" + detailModel.getTitle());

        return detailModel;
    }

    public static GirlListModel[] getGalleryMoreGirlListModel(String url) throws Exception {
        GirlListModel[] listModels = new GirlListModel[2];
        Document document = requestData(url, HTTP_GET, null);
        Element galleryListElement = document.getElementById("dphoto");
        GirlListModel galleryListModel = GirlListModel.parse(galleryListElement, GirlModel.TYPE_GALLERY);
        String galleryTitle = document.select("#cspan").text();
        galleryListModel.setTitle(galleryTitle);
        // String morePage = document.select("#cspan a").attr("href");
        // galleryListModel.setMorePage(morePage);

        Element favorListElement = document.getElementById("dfavor");
        GirlListModel favorListModel = GirlListModel.parse(favorListElement, GirlModel.TYPE_GALLERY);
        String favorTitle = document.select("#Span1").text();
        favorListModel.setTitle(favorTitle);

        listModels[0] = galleryListModel;
        listModels[1] = favorListModel;

        Logger.log("getGalleryMoreGirlListModel parse... galleryTitle:" + galleryTitle + ", favorTitle:" + favorTitle);

        return listModels;
    }

}

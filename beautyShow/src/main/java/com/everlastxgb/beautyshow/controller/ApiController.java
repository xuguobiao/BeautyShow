package com.everlastxgb.beautyshow.controller;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.model.GalleryDetailModel;
import com.everlastxgb.beautyshow.model.GirlDetailModel;
import com.everlastxgb.beautyshow.model.GirlListModel;
import com.everlastxgb.beautyshow.util.AsynThreadPool;
import com.everlastxgb.beautyshow.util.Logger;

import java.util.HashMap;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * <p/>
 * Created on 2015-4-7 PM 2:38:04
 */
public class ApiController {

    public static enum REQUEST_WHAT {
        GIRL_LIST, GALLERY_LIST, GIRL_DETAIL, GALLERY_DETAIL, GALLERY_MORE, RANK_LIST, SEARCH
    }

    /**
     * return GirlListModel
     *
     * @param resultCallback
     */
    public static void getGirlListModel(String url, String sort, ResultCallback<GirlListModel> resultCallback) {
        commonControl(REQUEST_WHAT.GIRL_LIST, url, null, resultCallback, sort);
    }

    // /**
    // * return GirlListModel
    // *
    // * @param resultCallback
    // */
    // public static void getTodayGirlListModel(ResultCallback resultCallback) {
    // Object resultObject = null;
    // boolean success = false;
    // ArrayList<GirlListModel> listModels = new ArrayList<GirlListModel>();
    // try {
    // Document document = requestData(URLs.GIRL_TODAY, HTTP_GET, null);
    // listModels = GirlListModel.parse(document);
    // GirlListModel listModel = listModels.get(0);
    // Logger.log("listModel size->" + listModel.getList().size() +
    // ", nextpage: " + listModel.getNextPage());
    // resultObject = listModel;
    // success = true;
    // } catch (Exception e) {
    // Logger.e(e.getMessage());
    // resultObject = e.getMessage();
    // success = false;
    // }
    // callback(resultCallback, resultObject, success);
    // }

    /**
     * return GirlListModel
     *
     * @param resultCallback
     */
    public static void getGalleryListModel(String url, String sort, ResultCallback<GirlListModel> resultCallback) {
        commonControl(REQUEST_WHAT.GALLERY_LIST, url, null, resultCallback, sort);
    }

    public static void getRankListModel(String url, String sort, ResultCallback<GirlListModel> resultCallback) {
        commonControl(REQUEST_WHAT.RANK_LIST, url, null, resultCallback, sort);
    }

    /**
     * return GirlDetailModel
     *
     * @param url
     * @param resultCallback
     */
    public static void getGirlDetail(String url, ResultCallback<GirlDetailModel> resultCallback) {
        commonControl(REQUEST_WHAT.GIRL_DETAIL, url, null, resultCallback);
    }

    /**
     * return GalleryDetailModel
     *
     * @param url
     * @param resultCallback
     */

    public static void getGalleryDetail(String url, ResultCallback<GalleryDetailModel> resultCallback) {
        commonControl(REQUEST_WHAT.GALLERY_DETAIL, url, null, resultCallback);
    }

    public static void getGalleryMore(String url, ResultCallback<GirlListModel[]> resultCallback) {
        commonControl(REQUEST_WHAT.GALLERY_MORE, url, null, resultCallback);
    }

    public static void searchGirl(String name, ResultCallback<GirlListModel> resultCallback) {
        commonControl(REQUEST_WHAT.SEARCH, null, null, resultCallback, name);
    }

    private static void commonControl(final REQUEST_WHAT what, final String url, final HashMap<String, String> dataMap, final ResultCallback resultCallback, final String... args) {

        AsynThreadPool.getInstance().runThread(new Runnable() {
            @Override
            public void run() {
                Object resultObject = null;
                boolean success = false;
                String errMsg = "";
                try {
                    resultObject = toRequest(what, url, dataMap, args);
                    success = true;
                } catch (Exception e) {
                    String err = e.getMessage() + "";
                    Logger.e(err);
                    errMsg = err.equals("") || err.equals("null") ? resultCallback.getActivity().getString(R.string.getdata_fail) : err;
                    success = false;
                }
                if (resultCallback != null) {
                    resultCallback.callbackInUI(resultObject, success, errMsg);
                }
            }
        });
    }

    private static Object toRequest(REQUEST_WHAT what, String url, HashMap<String, String> dataMap, String... args) throws Exception {
        Object result = null;
        String sort = "";
        switch (what) {
            case GIRL_LIST:
                sort = args[0];
                if (sort != null && !sort.equals("")) {
                    HtmlController.requestSort(sort, "rank");
                }
                result = HtmlController.getNewGirlListModel(url);
                break;
            case GIRL_DETAIL:
                result = HtmlController.getGirlDetailModel(url);
                break;
            case GALLERY_LIST:
                sort = args[0];
                if (sort != null && !sort.equals("")) {
                    HtmlController.requestSort(sort, "gallery");
                }
                result = HtmlController.getGalleryGirlListModel(url);
                break;
            case GALLERY_DETAIL:
                result = HtmlController.getGalleryDetailModel(url);
                break;
            case GALLERY_MORE:
                result = HtmlController.getGalleryMoreGirlListModel(url);
                break;
            case RANK_LIST:
                sort = args[0];
                if (sort != null && !sort.equals("")) {
                    HtmlController.requestSort(sort, "rank");
                }
                result = HtmlController.getRankGirlListModel(url);
                break;
            case SEARCH:
                String name = args[0];
                result = HtmlController.searchGirl(name);
            default:
                break;
        }
        return result;
    }

}

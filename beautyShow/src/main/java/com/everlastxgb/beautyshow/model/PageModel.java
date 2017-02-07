//
//  Created by XuGuobiao  2015-9-5
//  Copyright 2012年 ever. All rights reserved
//
package com.everlastxgb.beautyshow.model;

import org.jsoup.nodes.Element;

public class PageModel extends Model {

	private String prevHref = "";
	private String curHref = "";
	private String nextHref = "";
	private String pageTip = "";

	public String getPrevHref() {
		return prevHref;
	}

	public void setPrevHref(String prevHref) {
		this.prevHref = prevHref;
	}

	public String getCurHref() {
		return curHref;
	}

	public void setCurHref(String curHref) {
		this.curHref = curHref;
	}

	public String getNextHref() {
		return nextHref;
	}

	public void setNextHref(String nextHref) {
		this.nextHref = nextHref;
	}

	public String getPageTip() {
		return pageTip;
	}

	public void setPageTip(String pageTip) {
		this.pageTip = pageTip;
	}

	public static PageModel parse(Element element) {
		PageModel pageModel = new PageModel();
		if (element != null) {
			String curHref = element.baseUri();
			String prevHref = element.select(".prev").attr("href");
			String nextHref = element.select(".next").attr("href");
			String pageTip = element.select(".page").text();
			pageModel.setCurHref(curHref);
			pageModel.setNextHref(nextHref);
			pageModel.setPageTip(pageTip);
			pageModel.setPrevHref(prevHref);
		}
		return pageModel;
	}
}

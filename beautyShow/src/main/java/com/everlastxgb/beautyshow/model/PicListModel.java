//
//  Created by XuGuobiao  2015-9-5
//  Copyright 2012年 ever. All rights reserved
//
package com.everlastxgb.beautyshow.model;

import java.util.ArrayList;

import org.jsoup.nodes.Element;

public class PicListModel extends Model {

	private ArrayList<PicModel> list = new ArrayList<PicModel>();
	private PageModel pageModel = new PageModel();

	public ArrayList<PicModel> getList() {
		return list;
	}

	public void setList(ArrayList<PicModel> list) {
		this.list = list;
	}

	public PageModel getPageModel() {
		return pageModel;
	}

	public void setPageModel(PageModel pageModel) {
		this.pageModel = pageModel;
	}

	public static PicListModel parse(Element listElement) throws Exception {
		PicListModel picListModel = new PicListModel();
		// String title = element.select("#htitle").text();
		// String desc = element.select("#ddesc").text();
		// String info = element.select("#ddinfo").text();

		if (listElement != null) {
			for (Element picElement : listElement.children()) {
				PicModel picModel = PicModel.parse(picElement);
				picListModel.getList().add(picModel);
			}
		}
		return picListModel;
	}

}

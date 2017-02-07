//
//  Created by XuGuobiao  2015年9月1日
//  Copyright 2012年 ever. All rights reserved
//
package com.everlastxgb.beautyshow.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.everlastxgb.beautyshow.common.CommonMethods;
import com.everlastxgb.beautyshow.util.Logger;

public class GirlDetailModel extends GirlModel {

	private String info = "";
	private String description = "";
	private String starTip = "";

	private GirlListModel gallery = new GirlListModel(GirlModel.TYPE_GALLERY);
	private GirlListModel favor = new GirlListModel(GirlModel.TYPE_GIRL);

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStarTip() {
		return starTip;
	}

	public void setStarTip(String starTip) {
		this.starTip = starTip;
	}

	public GirlListModel getGallery() {
		return gallery;
	}

	public void setGallery(GirlListModel gallery) {
		this.gallery = gallery;
	}

	public GirlListModel getFavor() {
		return favor;
	}

	public void setFavor(GirlListModel favor) {
		this.favor = favor;
	}

	public static GirlDetailModel parse(Element pageElement) throws Exception {
		GirlDetailModel detailModel = new GirlDetailModel();
		Element mainElement = pageElement.getElementById("dmain");

		String href = pageElement.baseUri();
		String name = mainElement.select("h1").text();
		String pic = mainElement.select("img").attr("src");

		float star = (float) CommonMethods.parse2Double(mainElement.select(".rating-stars").attr("data-rating"));
		star = (int)((star / 2f) * 10) / 10f; // one digit after dot

		Elements infos = mainElement.select("p span");
		String starTip = star + infos.get(0).text();
		infos.remove(0);
		String infoString = "";
		for (Element info : infos) {
			String oneline = info.text();
			String pref = oneline.length() <= 3 ? " " : "\n";
			infoString += pref + oneline;
		}

		String description = pageElement.select("#dinfo").text();

		detailModel.setHref(href);
		detailModel.setName(name);
		detailModel.setPic(pic);
		detailModel.setDescription(description);
		detailModel.setInfo(infoString.trim());
		detailModel.setStarTip(starTip);
		detailModel.setStar(star);

		/****************** extra info ****************/

		Element galleryListElement = pageElement.getElementById("dphoto");
		GirlListModel galleryListModel = GirlListModel.parse(galleryListElement, GirlModel.TYPE_GALLERY);
		String galleryTitle = pageElement.select("#cspan a").text();
		galleryListModel.setTitle(galleryTitle);
		String morePage = pageElement.select("#cspan a").attr("href");
		galleryListModel.setMorePage(morePage);

		Element favorListElement = pageElement.getElementById("dfavor");
		GirlListModel favorListModel = GirlListModel.parse(favorListElement, GirlModel.TYPE_GIRL);
		String favorTitle = pageElement.select("#Div1 header span span").text();
		favorListModel.setTitle(favorTitle);

		detailModel.setGallery(galleryListModel);
		detailModel.setFavor(favorListModel);

		Logger.log("GirlDetailModel parse... " + detailModel.getName());
		return detailModel;

	}
}

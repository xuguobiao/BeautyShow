//
//  Created by XuGuobiao  2015-9-4
//  Copyright 2012年 ever. All rights reserved
//
package com.everlastxgb.beautyshow.model;

import org.jsoup.nodes.Element;

public class PicModel extends Model {

	private String pic = "";
	private String name = "";
	private String bigPic = "";
	private int progress = 0;
	private int totalSize = 0;
	private int currentSize = 0;

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBigPic() {
		return bigPic;
	}

	public void setBigPic(String bigPic) {
		this.bigPic = bigPic;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public long getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(int currentSize) {
		this.currentSize = currentSize;
	}

	public static PicModel parse(Element element) {
		PicModel picModel = new PicModel();
		if (element != null) {
			String pic = element.select("img").attr("src");
			String name = element.select("img").attr("alt");
			String bigPic = pic.replace("/s/", "/");
			picModel.setPic(pic);
			picModel.setName(name);
			picModel.setBigPic(bigPic);
		}
		return picModel;
	}

}

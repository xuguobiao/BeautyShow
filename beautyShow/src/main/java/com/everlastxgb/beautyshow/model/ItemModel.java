//
//  Created by XuGuobiao  2015-9-4
//  Copyright 2012骞� ever. All rights reserved
//
package com.everlastxgb.beautyshow.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemModel extends Model {

	private String id = "";
	private String title = "";

	public ItemModel() {

	}

	public ItemModel(String id, String title) {
		this.id = id;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

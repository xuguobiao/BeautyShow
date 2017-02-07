//
//  Created by XuGuobiao  2015-9-13
//  Copyright 2012年 ever. All rights reserved
//
package com.everlastxgb.beautyshow.model;

import java.util.ArrayList;

import org.jsoup.nodes.Element;

public class FilterConditionModel extends Model {

	private ItemListModel country = new ItemListModel();
	private ItemListModel profession = new ItemListModel();
	private ItemListModel xingzuo = new ItemListModel();
	private ItemListModel bloodtype = new ItemListModel();

	public ItemListModel getCountry() {
		return country;
	}

	public void setCountry(ItemListModel country) {
		this.country = country;
	}

	public ItemListModel getProfession() {
		return profession;
	}

	public void setProfession(ItemListModel profession) {
		this.profession = profession;
	}

	public ItemListModel getXingzuo() {
		return xingzuo;
	}

	public void setXingzuo(ItemListModel xingzuo) {
		this.xingzuo = xingzuo;
	}

	public ItemListModel getBloodtype() {
		return bloodtype;
	}

	public void setBloodtype(ItemListModel bloodtype) {
		this.bloodtype = bloodtype;
	}

	public static FilterConditionModel parse(Element element) {
		FilterConditionModel conditionModel = new FilterConditionModel();
		if (element != null) {
			conditionModel.setCountry(ItemListModel.parseCondition(element.getElementById("sel_country")));
			conditionModel.setProfession(ItemListModel.parseCondition(element.getElementById("sel_professional")));
			conditionModel.setXingzuo(ItemListModel.parseCondition(element.getElementById("sel_xingzuo")));
			conditionModel.setBloodtype(ItemListModel.parseCondition(element.getElementById("sel_bloodtype")));
		}

		return conditionModel;
	}
}

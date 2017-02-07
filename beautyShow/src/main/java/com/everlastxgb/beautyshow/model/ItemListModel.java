package com.everlastxgb.beautyshow.model;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * 
 * @create_time: 2015-9-8 ����2:34:42
 * 
 */

public class ItemListModel extends Model {

	private String title = "";

	private ArrayList<ItemModel> list = new ArrayList<ItemModel>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<ItemModel> getList() {
		return list;
	}

	public void setList(ArrayList<ItemModel> list) {
		this.list = list;
	}

	public ArrayList<String> toStringList() {
		ArrayList<String> titleList = new ArrayList<String>();
		for (ItemModel itemModel : getList()) {
			titleList.add(itemModel.getTitle());
		}
		return titleList;
	}

	public static ItemListModel parseOrder(Element element) {
		ItemListModel itemListModel = new ItemListModel();
		if (element != null) {
			Elements sortElements = element.select(".ck-order-span a");
			for (Element sortElement : sortElements) {
				String id = sortElement.attr("onclick").replace("sort(\'", "").replace("\')", "");
				String title = sortElement.text();
				ItemModel itemModel = new ItemModel(id, title);
				itemListModel.getList().add(itemModel);
			}
		}
		return itemListModel;
	}

	public static ItemListModel parseFilter(Element ulElement) {
		ItemListModel itemListModel = new ItemListModel();
		if (ulElement != null) {
			for (Element liItem : ulElement.children()) {
				if (liItem.className().equals("mm-subtitle")) {
					String title = liItem.text();
					itemListModel.setTitle(title);
				} else {
					String name = liItem.select("a").text();
					String href = liItem.select("a").attr("href");
					ItemModel itemModel = new ItemModel(href, name);
					itemListModel.getList().add(itemModel);
				}
			}
		}
		return itemListModel;
	}

	public static ItemListModel parseCondition(Element selElement) {
		ItemListModel itemListModel = new ItemListModel();
		if (selElement != null) {
			for (Element option : selElement.children()) {
				String id = option.attr("value");
				String title = option.text();
				// if (!id.equals("")) {
				ItemModel itemModel = new ItemModel(id, title);
				itemListModel.getList().add(itemModel);
				// }
			}
		}
		return itemListModel;
	}
}

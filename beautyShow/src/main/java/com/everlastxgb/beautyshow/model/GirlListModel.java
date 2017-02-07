package com.everlastxgb.beautyshow.model;

import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.everlastxgb.beautyshow.util.Logger;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * 
 * @create_time: 2015-9-1 5:47:29
 * 
 */

public class GirlListModel extends Model {
	private String title = "";
	private int type = 0;

	private ArrayList<GirlModel> list = new ArrayList<GirlModel>();

	private String morePage = "";
	private PageModel pageModel = new PageModel();

	private ItemListModel sortListModel = new ItemListModel();

	private ArrayList<ItemListModel> filterListModelList = new ArrayList<ItemListModel>();

	private FilterConditionModel conditionModel = new FilterConditionModel();

	public GirlListModel(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArrayList<GirlModel> getList() {
		return list;
	}

	public void setList(ArrayList<GirlModel> list) {
		this.list = list;
	}

	public String getMorePage() {
		return morePage;
	}

	public void setMorePage(String morePage) {
		this.morePage = morePage;
	}

	public PageModel getPageModel() {
		return pageModel;
	}

	public void setPageModel(PageModel pageModel) {
		this.pageModel = pageModel;
	}

	public ItemListModel getSortListModel() {
		return sortListModel;
	}

	public void setSortListModel(ItemListModel sortListModel) {
		this.sortListModel = sortListModel;
	}

	public ArrayList<ItemListModel> getFilterListModelList() {
		return filterListModelList;
	}

	public void setFilterListModelList(ArrayList<ItemListModel> filterListModelList) {
		this.filterListModelList = filterListModelList;
	}

	public FilterConditionModel getConditionModel() {
		return conditionModel;
	}

	public void setConditionModel(FilterConditionModel conditionModel) {
		this.conditionModel = conditionModel;
	}

	public static GirlListModel parse(Element listElement, int type) {
		GirlListModel listModel = new GirlListModel(type);
		if (listElement != null) {
			Elements girlElements = listElement.children();
			for (Element girlElement : girlElements) {
				GirlModel girlModel = GirlModel.parse(girlElement, type);
				listModel.getList().add(girlModel);
			}
		}
		Logger.log("GirlListModel parse... " + listModel.getList().size());
		return listModel;
	}

}

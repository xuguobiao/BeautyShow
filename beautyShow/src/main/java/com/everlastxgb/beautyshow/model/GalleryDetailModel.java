package com.everlastxgb.beautyshow.model;

import org.jsoup.nodes.Element;

import com.everlastxgb.beautyshow.util.Logger;

public class GalleryDetailModel extends Model {

	private String title = "";
	private String desc = "";
	private String info = "";

	private String girlHref = "";

	private PicListModel pic = new PicListModel();
	private GirlListModel recommendGallery = new GirlListModel(GirlModel.TYPE_GALLERY);
	private GirlListModel theseGirls = new GirlListModel(GirlModel.TYPE_GIRL);

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getGirlHref() {
		return girlHref;
	}

	public void setGirlHref(String girlHref) {
		this.girlHref = girlHref;
	}

	public PicListModel getPic() {
		return pic;
	}

	public void setPic(PicListModel pic) {
		this.pic = pic;
	}

	public GirlListModel getTheseGirls() {
		return theseGirls;
	}

	public void setTheseGirls(GirlListModel theseGirls) {
		this.theseGirls = theseGirls;
	}

	public GirlListModel getRecommendGallery() {
		return recommendGallery;
	}

	public void setRecommendGallery(GirlListModel recommendGallery) {
		this.recommendGallery = recommendGallery;
	}

	public static GalleryDetailModel parse(Element documentElement) throws Exception {
		GalleryDetailModel detailModel = new GalleryDetailModel();

		String title = documentElement.select("#htitle").text();
		String desc = documentElement.select("#ddesc").text();
		String info = documentElement.select("#ddinfo").text();
		String girlHref = documentElement.select("#dgirl a").attr("href");
		detailModel.setTitle(title);
		detailModel.setDesc(desc);
		detailModel.setInfo(info);
		detailModel.setGirlHref(girlHref);

		Element picListElement = documentElement.getElementById("idiv");
		PicListModel picListModel = PicListModel.parse(picListElement);
		detailModel.setPic(picListModel);

		Element pageElement = documentElement.getElementById("pagediv");
		picListModel.setPageModel(PageModel.parse(pageElement));

		Element girlListElement = documentElement.getElementById("dgirl");
		GirlListModel girlListModel = GirlListModel.parse(girlListElement, GirlModel.TYPE_GIRL);
		detailModel.setTheseGirls(girlListModel);

		Element recommendListElement = documentElement.getElementById("dfavor");
		GirlListModel recommendListModel = GirlListModel.parse(recommendListElement, GirlModel.TYPE_GALLERY);
		detailModel.setRecommendGallery(recommendListModel);

		Logger.log("GalleryDetailModel parse... " + detailModel.getTitle());
		return detailModel;
	}
}

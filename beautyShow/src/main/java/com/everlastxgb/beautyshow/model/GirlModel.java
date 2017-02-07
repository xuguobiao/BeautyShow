package com.everlastxgb.beautyshow.model;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * 
 * @create_time: 2015-9-1 ����5:45:17
 * 
 */

public class GirlModel extends Model {
	public final static int TYPE_GIRL = 101;
	public final static int TYPE_GALLERY = 102;
	public final static int TYPE_RANK = 103;

	private int type;
	private String tag = "";
	private String name = "";
	private String subname = "";
	private String pic = "";
	private String href = "";

	private float star = -1f;
	private String birthday = "";
	private String rankNum = "";

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSubname() {
		return subname;
	}

	public void setSubname(String subname) {
		this.subname = subname;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public float getStar() {
		return star;
	}

	public void setStar(float star) {
		this.star = star;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRankNum() {
		return rankNum;
	}

	public void setRankNum(String rankNum) {
		this.rankNum = rankNum;
	}

	public static GirlModel parse(Element element, int type) {
		GirlModel girlModel = new GirlModel();
		girlModel.setType(type);
		if (element != null) {

			String href = element.select("a").attr("href");
			String pic = element.select("img").attr("src");

			String class_name = type == TYPE_GALLERY ? ".ck-gallery-title" : ".ck-title";
			String name = element.select(class_name).text();

			girlModel.setHref(href);
			girlModel.setName(name);
			girlModel.setPic(pic);

			if (type == TYPE_GIRL || type == TYPE_RANK) {
				float star = 0.0f;
				Elements starDivs = element.select(".rating-stars");
				if (starDivs.size() > 0) {
					for (Element starElement : starDivs.first().children()) {
						String className = starElement.className();
						float step = className.endsWith("full") ? 1f : className.endsWith("half") ? 0.5f : 0.0f;
						star += step;
					}
				} else {
					star = -1;
				}

				girlModel.setStar(star);
			}

			if (type == TYPE_GIRL) {
				String tag = element.select("li.ticket_btn span a").text();
				girlModel.setTag(tag);
			} else if (type == TYPE_RANK) {
				Elements titleElements = element.select("div.ck-content-wrap");
				String subname = titleElements.size() > 0 ? titleElements.get(0).text() : "";
				String tag = titleElements.size() > 1 ? titleElements.get(1).text() : "";

				String rank = element.select(".ck-rank-num").text();

				girlModel.setSubname(subname);
				girlModel.setTag(tag);
				girlModel.setRankNum(rank);
			}
		}

		return girlModel;
	}

}

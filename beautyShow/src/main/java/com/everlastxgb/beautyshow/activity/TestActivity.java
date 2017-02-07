package com.everlastxgb.beautyshow.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.everlastxgb.beautyshow.R;
import com.everlastxgb.beautyshow.util.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * @author: XuGuobiao
 * @email: everlastxgb@gmail.com
 * @create_time: 2015-4-1 PM 3:16:11
 */
public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
    }

    private static void beauty() {
        Document doc;
        try {
            doc = Jsoup.parse(new URL("http://www.zngirls.com/"), 5000);
            Elements boxes = doc.select("#post div.entry_box");
            for (Element box : boxes) {
                String tag = box.select("div.box_entry_title div.hot_tag").text();
                Logger.d("tag=====>" + tag);
                Elements girls = box.select("div.post_entry ul li");

                for (Element girlli : girls) {
                    String src = "";
                    String href = "";
                    String title = "";

                    // igalleryli girlli

                    src = girlli.select("img").attr("data-original");
                    href = girlli.select("div a").attr("href");
                    title = girlli.select("img").attr("alt");

                    if (girlli.className().equals("girlli")) {
                        title = girlli.select("div div a").text();
                    } else if (girlli.className().equals("igalleryli")) {
                        title = girlli.select("div a").text();
                    }

                    Logger.i("girl:[ src->" + src + "\n href->" + href + "\n title->" + title + " ]");
                }
                // Elements girlUl = box.getElementsByAttributeValue("class",
                // "post_entry").get(0).getElementsByTag("ul");
                // for (Element link : links) {
                // String linkHref = link.attr("href");
                // String linkText = link.text().trim();
                // System.out.println(linkHref);
                // System.out.println(linkText);
                // }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Logger.e(e.getMessage());
        }
    }

    public void onClick(View view) {
        new Thread() {
            public void run() {
                beauty();
            }
        }.start();
    }

}

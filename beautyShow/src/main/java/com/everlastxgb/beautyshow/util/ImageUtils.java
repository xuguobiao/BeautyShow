package com.everlastxgb.beautyshow.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.everlastxgb.beautyshow.common.FileManager;
import com.everlastxgb.beautyshow.controller.HtmlController;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

/**
 * @author Kido
 * @email everlastxgb@gmail.com
 * @create_time 16/6/18 11:39
 */
public class ImageUtils {

    /*********************
     * local methods below
     **************************/
    public static void initImageLoader(Context context) {
        File cacheDir = FileManager.getCacheDirFile(context);
        Logger.log("cacheDir->" + cacheDir.getPath());
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                // .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //
                // .memoryCacheSize(2 * 1024 * 1024) //
                .discCache(new UnlimitedDiskCache(cacheDir))
                .imageDownloader(new CustomImageDownaloder(context));

        if (Logger.isLogEnabled()) {
            builder.writeDebugLogs();
        }


        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(builder.build());
    }

    public static void display(ImageView imageView, DisplayImageOptions options) {

    }


    /**
     * 新闻列表中用到的图片加载配置
     */
    public static DisplayImageOptions getListOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // // 设置图片在下载期间显示的图片
//				.showImageOnLoading(R.drawable.defaultpic)
                // // 设置图片Uri为空或是错误的时候显示的图片
                // .showImageForEmptyUri(R.drawable.defaultpic_fail)
                // // 设置图片加载/解码过程中错误时候显示的图片
                // .showImageOnFail(R.drawable.defaultpic_fail)
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)
                // 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                // .decodingOptions(android.graphics.BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                .considerExifParams(true)
                // 设置图片下载前的延迟
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // 。preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//				.displayer(new FadeInBitmapDisplayer(100))// 淡入
                .extraForDownloader(HtmlController.getHeaders()) // set header for request image
                .build();
        return options;
    }


}

package com.everlastxgb.beautyshow.util;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 线程池统一管理类
 *
 * @author Kido
 * @email everlastxgb@gmail.com
 * @create_time 2016/5/26 11:12
 */
public class AsynThreadPool extends HandlerThread {
    private final static String THREAD_NAME = AsynThreadPool.class.getName();
    private volatile static AsynThreadPool sInstance;

    private Handler handler;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool(); // 自动分配大小的线程池
    private ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(20);// 延迟执行的线程池
    private ScheduledExecutorService scheduledSingleThreadPool = Executors.newSingleThreadScheduledExecutor();// 单一线程的线程池

    public AsynThreadPool() {
        super(THREAD_NAME);
        start();
        handler = new Handler(getLooper());//延迟任务
    }

    public static AsynThreadPool getInstance() {
        if (sInstance == null) {
            synchronized (AsynThreadPool.class) {
                if (sInstance == null)
                    sInstance = new AsynThreadPool();
            }
        }
        return sInstance;
    }


    /**
     * 运行即时线程，多线程并行无上限
     */
    public void runThread(Runnable run) {
        cachedThreadPool.submit(run);
    }

    /**
     * 运行即时线程于单线程池
     */
    public void runSingleThread(Runnable run) {
        scheduledSingleThreadPool.submit(run);
    }

    /**
     * 运行延迟线程于多线程池
     *
     * @param delayTime 单位为毫秒
     */
    public void runDelayThread(Runnable run, int delayTime) {
        scheduledThreadPool.schedule(run, delayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 运行定时延迟线程于多线程池
     *
     * @param delayTime scheduleTime 单位为毫秒
     */
    public ScheduledFuture<?> runDelayThread(Runnable run, int delayTime, int scheduleTime) {
        return scheduledThreadPool.scheduleAtFixedRate(run, delayTime, scheduleTime, TimeUnit.MILLISECONDS);
    }


    /**
     * 运行延迟线程于单线程池
     *
     * @param delayTime 单位为毫秒
     */
    public void runDelaySingleThread(Runnable run, int delayTime) {
        scheduledSingleThreadPool.schedule(run, delayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 运行延迟任务
     *
     * @param delayTime 单位为毫秒
     */
    public void postDelay(Runnable run, int delayTime) {
        handler.postDelayed(run, delayTime);
    }

    public void post(Runnable run) {
        handler.post(run);
    }

    public void removeHandlerRunnable(Runnable run) {
        handler.removeCallbacks(run);
    }


}


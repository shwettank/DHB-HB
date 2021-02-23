package com.dhb.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.dhb.R;
import com.dhb.dao.DbHelper;
import com.dhb.utils.AppConstants;
import com.dhb.utils.AppPreferenceManager;
import com.dhb.utils.Logger;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Stack;

import io.fabric.sdk.android.Fabric;

public class ApplicationController extends MultiDexApplication {

    public static ApplicationController applicationController;
    public static Stack<Activity> ACTIVITY_STACK = new Stack<Activity>();
    public static boolean IS_PAUSED = true;
    public static ImageLoader imageLoader;
    public static DisplayImageOptions IMAGELOADER_OPTIONS;
    public static ImageLoaderConfiguration IMAGELOADER_CONFIG;
    public static String pushToken="";
    public static Handler handler;
    public static Runnable timerRunnable;
    private File cacheDir;
    public static String selectedService;
    private AppPreferenceManager appPreferenceManager;
    MediaPlayer player;
    public static int beepCouner = 0;
    public static final int BEEP_COUNTER_15 = 15*60*1000;
    public static final int BEEP_COUNTER_5 = 5*60*1000;
    // after 15 min there will be two beep events 20 mins and 25 mins before completing 30 min inactive time.
    public static final int MAX_BEEP_COUNT = 2;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onCreate() {

        super.onCreate();

        applicationController = this;

        Fabric.with(this, new Crashlytics());
        //  pushAppSetup();
        cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        if (!cacheDir.exists()){
            cacheDir.mkdirs();
        }
        setImageLoaderConfiguration();
        DbHelper.init(applicationController);
        appPreferenceManager=new AppPreferenceManager(getApplicationContext());
        handler=new Handler();

        //ImageLoader.getInstance().init(IMAGELOADER_CONFIG);
        // setImageLoaderOptions();
        imageLoader=ImageLoader.getInstance();
        player = new MediaPlayer();

        timerRunnable=new Runnable() {
            @Override
            public void run() {

                if (!appPreferenceManager.getAPISessionKey().trim().isEmpty()
                        && appPreferenceManager.getRoleName().equalsIgnoreCase(AppConstants.DOCTOR)){

                    long lMin = appPreferenceManager.getLastApiTiming() / 60000;
                    long nMin = Calendar.getInstance().getTimeInMillis() / 60000;

                    if (lMin - nMin <= 0){

                        Logger.debug("Called Beep Function");
                        //Start Beep;
//						Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
//						long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};
//						v.vibrate(pattern, -1);
//
//						playBeep();

                        if (ApplicationController.beepCouner < MAX_BEEP_COUNT){
                            sendBroadcast(new Intent("com.dhb.restart_beep_service"));
                        }
                    }
                }
            }

        };

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                // for
                // release
                // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onLowMemory() {

        super.onLowMemory();

    }

    @Override
    public void onTerminate() {

        super.onTerminate();

    }

    private void setImageLoaderConfiguration() {

        IMAGELOADER_CONFIG = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .memoryCacheExtraOptions(480, 800)
                // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3)
                // default
                .threadPriority(Thread.NORM_PRIORITY - 1)
                // default
                .tasksProcessingOrder(
                        QueueProcessingType.FIFO)
                // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(
                        new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                // default
             //   .diskCache(new UnlimitedDiscCache(cacheDir))
                // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(
                        new HashCodeFileNameGenerator())
                // default
                .imageDownloader(
                        new BaseImageDownloader(
                                getApplicationContext()))                                 // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                //	.imageDecoder(new NutraBaseImageDecoder(true))
                .defaultDisplayImageOptions(
                        returnOptions())                         // default
                .writeDebugLogs().build();

        ImageLoader.getInstance()
                .init(ApplicationController.IMAGELOADER_CONFIG);
    }

    public static DisplayImageOptions returnOptions() {
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
    }

    private void setImageLoaderOptions()
    {

        IMAGELOADER_OPTIONS = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.top_icon)
                // resource or drawable
                .showImageForEmptyUri(
                        R.drawable.top_icon)
                // resource or drawable
                .showImageOnFail(R.drawable.top_icon)
                // resource or drawable
                .resetViewBeforeLoading(false)
                // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(false) // default
                .imageScaleType(
                        ImageScaleType.IN_SAMPLE_POWER_OF_2)                         // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();
    }

    public static void clearActivityStack() {

        for (Activity activity : ACTIVITY_STACK){
            activity.finish();
        }

    }

    public static String getSelectedService() {
        return selectedService;
    }

    public static void setSelectedService(String selectedService) {
        ApplicationController.selectedService = selectedService;
    }

    public void playBeep() {
        try {
            if (player.isPlaying()){
                player.stop();
                player.release();
                player = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = getAssets().openFd("beep.mp3");
            player.reset();
            player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    Log.i("Completion Listener", "Song Complete");
                    mp.stop();
                    mp.reset();
				        /* mp.setDataSource([nextElement]);
				           mp.prepare();
				           mp.start();*/
                }
            });
            player.prepare();
            player.setVolume(1f, 1f);
            player.setLooping(false);
            player.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

package com.qiantang.smartparty.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.nanchen.compresshelper.CompressHelper;
import com.nanchen.compresshelper.FileUtil;
import com.orhanobut.logger.Logger;
import com.qiantang.smartparty.MyApplication;
import com.qiantang.smartparty.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import static com.shuyu.gsyvideoplayer.GSYVideoBaseManager.TAG;


/**
 */

public class AppUtil {

    public static String getMsgCount(int msgCount) {
        return msgCount < 100 ? msgCount + "" : "99+";
    }

    public static Drawable getDrawable(int resId) {
        return resId == 0 ? null : ContextCompat.getDrawable(MyApplication.getContext(), resId);
    }

    public static Drawable getDrawable(int resId, int defaultres) {
        return resId == 0 ? getDrawable(defaultres) : ContextCompat.getDrawable(MyApplication.getContext(), resId);
    }

    public static int getColor(int resId) {
        return ContextCompat.getColor(MyApplication.getContext(), resId == 0 ? R.color.transparent : resId);
    }

    public static int getColor(int resId, int defaultColor) {
        return ContextCompat.getColor(MyApplication.getContext(), resId == 0 ? defaultColor : resId);
    }

    public static int getColor(String colorString) {
        return getColor(colorString, R.color.transparent);
    }

    public static int getColor(String colorString, int defaultColorRes) {
        return StringUtil.isEmpty(colorString) || !colorString.startsWith("#") ?
                getColor(defaultColorRes == 0 ? R.color.transparent : defaultColorRes) : Color.parseColor(colorString);
    }

    public static int getResId(int resId) {
        return MyApplication.getContext().getResources().getInteger(resId);
    }

    public static float getDimension(int resId) {
        return MyApplication.getContext().getResources().getDimension(resId);
    }

    public static float getDimensionPixelSize(int resId) {
        return MyApplication.getContext().getResources().getDimensionPixelSize(resId);
    }

    /**
     * 判断排行榜
     *
     * @param rank
     * @return
     */
    public static boolean isRankTop(int rank) {
        return rank > 3;
    }


    public static Drawable getRank(int rank) {
        int resId = 0;
        switch (rank) {
            case 1:
                resId = R.mipmap.rank_first;
                break;
            case 2:
                resId = R.mipmap.rank_second;
                break;
            case 3:
                resId = R.mipmap.rank_third;
                break;
        }

        return getDrawable(resId);
    }

    public static boolean isDebugMode() {
        boolean debugAble = false;
        ApplicationInfo appInfo = getApplicationInfo();
        if (appInfo != null) {
            debugAble = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) > 0;
        }
        return debugAble;
    }

    public static Bundle getMetadata() {
        ApplicationInfo appInfo = getApplicationInfo();
        if (appInfo != null) {
            return appInfo.metaData;
        }
        return null;
    }

    public static ApplicationInfo getApplicationInfo() {
        ApplicationInfo appInfo = null;
        try {
            appInfo = MyApplication.getContext().getPackageManager().getApplicationInfo(MyApplication.getContext().getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("get ApplicationInfo error!");
        }
        return appInfo;
    }

    public static void showKeyBoard(View view) {
        view.requestFocus();
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        //延迟500，为了更好的加载activity
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager mgr = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(view, 0);
            }
        }, 500);
    }

    public static void showSoftInput(View view) {
        InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    public static void hideKeyBoard(View view) {
        view.clearFocus();
        InputMethodManager mgr = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //弱引用的Handler,防止内存泄露
    public static class UnLeakHandler extends Handler {
        private final WeakReference<Context> context;

        public UnLeakHandler(Context context) {
            this.context = new WeakReference<>(context);
        }
    }

    //精确到小数点后几位,不四舍五入,直接废弃后面的值
    public static double getDecimalValue(double value, int count) {
        double newCount = Math.pow(10, count);
        return (int) (value * (int) newCount) / newCount;
    }

    public static Uri getResUri(int resId) {
        return Uri.parse("res://com.puxiang.mall/" + resId);
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static boolean isHideRightText(String text, boolean isHide) {
        return (TextUtils.isEmpty(text) || isHide);
    }

    public static boolean isHideRightImage(int text, boolean isHide) {
        return (text > 0 || isHide);
    }

    public static String getImageBase64(String path, Context context) throws IOException {

        return imageToBase64(compressImage(getUri(path, context)));
    }

    public static String imageToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Bitmap compressImage(Uri path) throws IOException {
        if (path == null) {
            return null;
        }
        File oldFile = FileUtil.getTempFile(MyApplication.getContext(), path);

        return CompressHelper.getDefault(MyApplication.getContext()).compressToBitmap(oldFile);
    }

    /**
     * path转uri
     */
    public static Uri getUri(String path, Context context) {
        Uri uri = null;
        if (path != null) {
            path = Uri.decode(path);
            Log.d(TAG, "path2 is " + path);
            ContentResolver cr = context.getContentResolver();
            StringBuffer buff = new StringBuffer();
            buff.append("(")
                    .append(MediaStore.Images.ImageColumns.DATA)
                    .append("=")
                    .append("'" + path + "'")
                    .append(")");
            Cursor cur = cr.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.ImageColumns._ID},
                    buff.toString(), null, null);
            int index = 0;
            for (cur.moveToFirst(); !cur.isAfterLast(); cur
                    .moveToNext()) {
                index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                index = cur.getInt(index);
            }
            if (index == 0) {
            } else {
                Uri uri_temp = Uri.parse("content://media/external/images/media/" + index);
                Log.d(TAG, "uri_temp is " + uri_temp);
                if (uri_temp != null) {
                    uri = uri_temp;
                }
            }
        }
        return uri;
    }
}

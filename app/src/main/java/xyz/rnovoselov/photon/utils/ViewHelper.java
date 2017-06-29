package xyz.rnovoselov.photon.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by roman on 04.03.17.
 */

public class ViewHelper {
    public static float getDensity(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.density;
    }

    // Ширины экрана в px
    public static int getScreenWidthPx(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int wt = displaymetrics.widthPixels;
        return wt;
    }

    // Ширины экрана в dp
    public static int getScreenWidthDp(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int wt = displaymetrics.widthPixels;
        int swDp = (int) convertPixelsToDp(wt, activity);
        return swDp;
    }

    // Высота экрана в dp
    public static int getScreenHeidthDp(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int ht = displaymetrics.heightPixels;
        int shDp = (int) convertPixelsToDp(ht, activity);
        return shDp;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    // Вернуть список дочерних к контейнеру вьюх, за исключением тех, которые добавлнеы в список exclude
    public static ArrayList<View> getChildsExcludeView(ViewGroup container, @IdRes int... excludeChilds) {
        ArrayList<View> childs = new ArrayList<>();

        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            for (int exclude : excludeChilds) {
                if (child.getId() != exclude) {
                    childs.add(child);
                }
            }
        }
        return childs;
    }

    public static void waitForMeasure(View view, OnMeasureCallback callback) {
        int width = view.getWidth();
        int heigth = view.getHeight();

        if (width > 0 && heigth > 0) {
            callback.onMeasure(view, width, heigth);
            return;
        }

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final ViewTreeObserver observer = view.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                callback.onMeasure(view, view.getWidth(), view.getHeight());
                return true;
            }
        });

    }

    public interface OnMeasureCallback {
        void onMeasure(View view, int width, int height);
    }
}

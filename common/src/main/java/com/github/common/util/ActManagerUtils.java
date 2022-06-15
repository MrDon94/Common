package com.github.common.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * activity管理
 */
public class ActManagerUtils {
    private List<Activity> sActivities = new ArrayList<>();
    private static ActManagerUtils mActManagerUtils;
    private ActManagerUtils() {
    }
    public static ActManagerUtils getInstance(){
        if (mActManagerUtils == null){
            mActManagerUtils = new ActManagerUtils();
        }
        return mActManagerUtils;
    }

    public void addActivity(Activity activity){
        sActivities.add(activity);
    }

    public void removeActivity(Activity activity){
        sActivities.remove(activity);
    }


    public void closeAllActivityExclude(Class<?>... excludeActivityClasses) {
        List<Class<?>> excludeList = Arrays.asList(excludeActivityClasses);
        synchronized (ActManagerUtils.class) {
            Iterator<Activity> iterator = sActivities.iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();

                if (excludeList.contains(next.getClass())) {
                    continue;
                }

                iterator.remove();
                next.finish();
            }
        }
    }

    public void closeAllActivity(){
        for (Activity activity:sActivities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}

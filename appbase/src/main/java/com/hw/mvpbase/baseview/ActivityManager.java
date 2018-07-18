package com.hw.mvpbase.baseview;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by hw on 6/16/17.<br>
 *
 * Actviity管理器
 */

public class ActivityManager {

    private static ActivityManager instance;
    private Stack<Activity> activityStack;

    private ActivityManager() {

    }

    public static ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public boolean isStackEmpty() {
        if (null == activityStack || activityStack.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 把一个activity压入栈中
     * @param activity
     */
    public void pushActivity(Activity activity) {
        if (null == activityStack) {
            activityStack = new Stack<Activity>();
        } else {
            activityStack.add(activity);
        }
    }

    /**
     * 移除一个activity
     * @param activity
     */
    public void popActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 获取栈顶的activity,后进先出原则
     * @return
     */
    public Activity getLastActivity() {
        if(activityStack == null && activityStack.size() == 0)
            return null;
        return activityStack.lastElement();
    }

    /**
     * activity是否在栈中
      * @param clazz
     * @return
     */
    public boolean isActivityInStack(Class<?> clazz) {
        if(activityStack == null)
            return false;
        for (Activity activity : activityStack) {
            Class<? extends Activity> aCliss = activity.getClass();
            if (clazz == aCliss) {
                return true;
            }
        }
        return false;
    }

    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getLastActivity();
                if (activity == null) {
                    break;
                }
                popActivity(activity);
                activity.finish();
            }
            activityStack.clear();
        }
    }

    public int getStackSize() {
        if(activityStack != null)
            return activityStack.size();
        return 0;
    }

}
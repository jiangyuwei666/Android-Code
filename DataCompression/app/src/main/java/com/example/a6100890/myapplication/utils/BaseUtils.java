package com.example.a6100890.myapplication.utils;

/**
 * Created by a6100890 on 2017/11/15.
 */

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 放一些最基本的工具方法
 */
public class BaseUtils {

    /**
     * 检查权限,有权限返回true,无权限返回false 并申请权限
     * @param context
     * @param permission
     * @param requestCode
     * @return
     */
    public static boolean checkPermission(Context context,String permission,int requestCode) {
        boolean hasPermission = false;
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
        } else {
            hasPermission = true;
        }

        return hasPermission;
    }
}

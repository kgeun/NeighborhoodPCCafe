package com.b2come.pcroom.applicationclass;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.b2come.pcroom.R;
import com.b2come.pcroom.activity.MainActivity;
import com.facebook.AccessToken;
import com.nhn.android.naverlogin.OAuthLogin;

/**
 * Created by KKLee on 2016. 11. 12..
 */

public class Util extends Activity {
    public void gpsAlert(){
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
        // 메세지
        alert_confirm.setMessage("위치 서비스를 활성화 해 주셔야 내 주변 PC방을 확인 할 수 있습니다.");
        // 확인 버튼 리스너
        alert_confirm.setPositiveButton("확인", null);
        // 다이얼로그 생성
        AlertDialog alert = alert_confirm.create();
        // 다이얼로그 타이틀
        alert.setTitle("위치 서비스");
        // 다이얼로그 보기
        alert.show();
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static double getDist(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static void setAppPreferences(Activity context, String key, String value) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("wooripc", 0);
        SharedPreferences.Editor prefEditor = pref.edit();
        Log.d("LOG", "key = " + key + "  //  value = " + value);
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    public static String getAppPreferences(Activity context, String key) {
        String returnValue = null;
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("wooripc", 0);

        returnValue = pref.getString(key, "");
        return returnValue;
    }

    public static void removeAppPreferences(Activity context, String key) {
        SharedPreferences pref = context.getSharedPreferences("wooripc", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        Log.d("LOG", "Remove key = " + key );
        prefEditor.remove(key);
        prefEditor.commit();
    }

    public static String isLoggeed(Context context){
        if(AccessToken.getCurrentAccessToken() != null){
            return "Facebook";
        }
        else if(MainActivity.mOAuthLoginModule.getAccessToken(context) != null){
            return "Naver";
        }
        else {
            return null;
        }

    }

}

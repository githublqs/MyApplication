package com.example.administrator.myapplication.fileupload;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/17.
 */

public class ImageResizer {

    /*
    使用Bitmap加Matrix来缩放图片到指定大小,刚好为指定大小

     */


    public static Drawable resizeImageWithMatrix(Bitmap bitmap, int w, int h)
    {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);

        return new BitmapDrawable(resizedBitmap);
    }
    //调用系统api 缩放到指定大小，保持原有宽高比
    public static Bitmap extractThumbnail(Bitmap bitmap, int w, int h){
        return ThumbnailUtils.extractThumbnail(bitmap,w,h);
    }
    //调用系统api 缩放到指定大小，保持原有宽高比
    public static Bitmap extractThumbnail(Bitmap bitmap, int w, int h,  int options){
        //ThumbnailUtils.OPTIONS_RECYCLE_INPUT
        //OPTIONS_NONE=0
        return ThumbnailUtils.extractThumbnail(bitmap,w,h,options);
    }





    /*
    使用BitmapFactory.Options的inSampleSize参数来缩放到指定大小
         缩放保持原有宽高逼不变 缩放比率计算,所以不够精确
          int sampleSize=(outWidth/width+outHeight/height)/2;
     */
    public static Drawable resizeImageWithInSampleSize(String path,
                                        int width,int height)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//不加载bitmap到内存中
        BitmapFactory.decodeFile(path,options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;

        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0)
        {
            int sampleSize=(outWidth/width+outHeight/height)/2;
            options.inSampleSize = sampleSize;
        }

        options.inJustDecodeBounds = false;
        return new BitmapDrawable(BitmapFactory.decodeFile(path, options));
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                  int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static  Bitmap decodeSampledBitmapFromFilePath(String path, int reqWidth, int reqHeight) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            return  decodeSampledBitmapFromFileDescriptor(fileDescriptor,reqWidth,reqHeight);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;

    }
    public static  Bitmap decodeSampledBitmapFromImageFileUri(Context context, Uri imageFileUri, int reqWidth, int reqHeight) {
        try {
            return  decodeSampledBitmapFromFileDescriptor(context.getContentResolver().openFileDescriptor(imageFileUri,"r").getFileDescriptor(),reqWidth,reqHeight);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public static Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {

        /*FileInputStream fileInputStream = (FileInputStream)snapShot.getInputStream(DISK_CACHE_INDEX);
        FileDescriptor fileDescriptor = fileInputStream.getFD();*/
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        //Log.d(TAG, "origin, w= " + width + " h=" + height);
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        //Log.d(TAG, "sampleSize:" + inSampleSize);
        return inSampleSize;
    }




}
package com.example.administrator.myapplication.fileupload;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/21.
 */

public class BitmapUtil {
    /** 保存方法 */
    public static boolean saveBitmap(Bitmap bm, File parent, String fileName,int quality) {
        boolean ret=false;
        if(!parent.exists()){
            parent.mkdirs();
        }
        File f = new File(parent, fileName);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, quality, out);
            out.flush();
            out.close();
            ret=true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;

    }
}

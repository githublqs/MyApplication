package com.example.administrator.myapplication.fileupload;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lqs on 2016/8/22.
 * 只允许输入30个字符数的filter，将汉子算成2个字符
 */

public class NameLengthFilter implements InputFilter {

    //Android:maxLength="30"是对EditText的字数进行控制的。不管中文还是英文。都是30个字
    int MAX_EN;// 最大英文/数字长度 一个汉字算两个字母
    String regEx = "[\\u4e00-\\u9fa5]"; // unicode编码，判断是否为汉字

    public NameLengthFilter(int mAX_EN) {
        super();
        MAX_EN = mAX_EN;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int destCount = dest.toString().length()
                + getChineseCount(dest.toString());
        int sourceCount = source.toString().length()
                + getChineseCount(source.toString());
        if (destCount + sourceCount > MAX_EN) {
            /*Toast.makeText(MainActivity.this, getString(R.string.count),
                    Toast.LENGTH_SHORT).show();*/
            return "";

        } else {
            return source;
        }
    }

    private int getChineseCount(String str) {
        int count = 0;
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count;
    }


}

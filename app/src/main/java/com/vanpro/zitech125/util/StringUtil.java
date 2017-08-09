package com.vanpro.zitech125.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 字符串处理辅助工具类
 *
 *
 * Created by jinsen on 15/9/15.
 */
public class StringUtil {

    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static final boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断字符串是否不为空
     * @param str
     * @return
     */
    public static final boolean isNotEmpty(String str){
        return !isEmpty(str);
    }


    public final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 将日期类型转为字符串
     *
     * @param date
     * @return
     */
    public static String dateTimeFormat(Date date) {
        return dateFormater.get().format(date);
    }

}

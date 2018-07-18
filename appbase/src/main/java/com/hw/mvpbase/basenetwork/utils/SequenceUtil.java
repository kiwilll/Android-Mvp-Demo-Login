package com.hw.mvpbase.basenetwork.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hw on 4/11/17.<br>
 */

public class SequenceUtil {

    /**
     * 生成sequence序列
     *
     * @return
     */
    public static String genSequence() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Date date = calendar.getTime();
        String currDate = formatter.format(date);
        return currDate + genRandomNum(10);
    }

    /**
     * 生成随机数字
     *
     * @param len
     * @return
     */
    private static String genRandomNum(int len) {
        StringBuffer sb = new StringBuffer();
        for(int i=0; i<len; i++) {
            sb.append((int) (10 * Math.random()));
        }
        return sb.toString();
    }

}

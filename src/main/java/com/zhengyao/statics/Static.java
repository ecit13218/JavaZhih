package com.zhengyao.statics;

import org.apache.http.cookie.Cookie;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2017/5/22.
 */
public class Static {
    //1主topicID
    public static BlockingQueue<String> topicID = new ArrayBlockingQueue<String>(50);
    public static AtomicInteger UserCount=new AtomicInteger(0);

    //2次topicID
    public static BlockingQueue<String> SecondtopicID = new ArrayBlockingQueue<String>(50000);
    public static ConcurrentHashMap<String,Integer> map=new ConcurrentHashMap<String, Integer>();
    public static String[] cookies=new String[]{"Mi4wQUFDQWF3Z3ZBQUFBY01KM0FaaE9DeGNBQUFCaEFsVk5WNUZLV1FBR18wampkd2tDZUdIYng2cHU5clFwdTNLckhn|1495474143|7af7680f0ce4ac8dd1f411d581c41d41010af28b;"};
    static {
    }
    //    //Mi4wQUZEQ243R3JrUXNBQUlKU1lBQ1hDeGNBQUFCaEFsVk5lOGNWV1FEUkxIMkZrdG5mLWp4eTBTbEhaejdldU9rVm5B|1492007551|10c21c3b1bab28a836483200300f73d8e38f6be2;
    //Mi4wQUVBQ1NFM2h3QXNBRUFMVFQwSE1DeGNBQUFCaEFsVk56b3hLV1FBa2NCZTF6MWFPUF9IR19NMUhsUTQ4U3Fta0p3|1495471393|b50ab3c57643cc3451fe30f0b2a44665506d2669
    public static String getCookie(){
        return cookies[0];
    }

}

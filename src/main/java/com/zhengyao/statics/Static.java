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
    public static String[] cookies=new String[]{"Mi4wQUFDQWF3Z3ZBQUFBa0VMei1DU3pDeGNBQUFCaEFsVk5DTll3V1FBV3lVTGQxZTVQWU1XYl94dDhDYnd6S1laZ3RR|1495423367|28ea0b4ffee962d0be2226e1f8719170a58d39ca"};
    static {
    }
    public static String getCookie(){
        return cookies[0];
    }

}

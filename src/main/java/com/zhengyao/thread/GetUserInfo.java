package com.zhengyao.thread;

import com.zhengyao.dao.ZhihuUserDao;
import com.zhengyao.entity.ZhiHuUser;
import com.zhengyao.statics.Static;
import com.zhengyao.util.SpringContextUtil;
import com.zhengyao.util.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/5/23.
 */
public class GetUserInfo extends Thread {
    private final CloseableHttpClient httpClient;
    private final HttpContext context;
    private final HttpGet httpget;
    private ZhihuUserDao zhihuUserDao= (ZhihuUserDao) SpringContextUtil.getBean("zhihuUserDao");

    public GetUserInfo(CloseableHttpClient httpClient, HttpGet httpget) {
        this.httpClient = httpClient;
        this.context = HttpClientContext.create();
        this.httpget = httpget;

    }
    @Override
    public void run() {
        try {
            //必须要手机发包才能访问
            httpget.setHeader("Cookie", "d_c0=\"AHDCdwGYTguPTtGBj-vSi-ejC8OM9-KRksY=|1487032899\"; _zap=0c11db2f-c1ae-478e-b47b-032ee7517e05; _ga=GA1.2.1454163689.1489975247; q_c1=1535925aa8bc487384ca31eea8da1a31|1495116908000|1487032899000; _xsrf=682220a4595e71745487f892be0f0f45; r_cap_id=\"NDUxOTZiNDE4OTdhNDA3NWFhNjM5MGUyNTNjYWE0MDE=|1495467084|4afe69028afdd053859f9dca41e35b095be1d632\"; cap_id=\"NDIwZTM2MDRhODNjNGYxMDliMjIxNmJhNTFhOWNkYzU=|1495467084|5b581f9cf81d71f6c2c511e79e97aa6d3c8b54d3\"; aliyungf_tc=AQAAAO4SETVaugIA1M10ew1auHs8fqkx; acw_tc=AQAAAFAJjwLRugIA1M10e7XR0l+BZT/X; s-q=403; s-i=7; sid=j4980kf8; s-t=autocomplete; __utma=51854390.1208297164.1492506085.1495465651.1495472789.68; __utmb=51854390.0.10.1495472789; __utmc=51854390; __utmz=51854390.1493110661.21.8.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/question/38597960; __utmv=51854390.100-1|2=registration_date=20140516=1^3=entry_date=20140516=1;"
                    + "z_c0=" + Static.getCookie());
            httpget.setHeader("User-Agent", "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7");
            CloseableHttpResponse response = httpClient.execute(
                    httpget, context);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String body = EntityUtils.toString(entity, "UTF-8");
                //System.out.println(body);
                ZhiHuUser user = setUserInfo(body);
                //System.out.println(user);
               // System.out.println("_________________________________________________");
                zhihuUserDao.insert(user);
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private ZhiHuUser setUserInfo(String body){
        //正则匹配
        Pattern p = Pattern.compile(Static.regex);
        Matcher m = p.matcher(body);
        ZhiHuUser user=new ZhiHuUser();
        while (m.find()) {
           // System.out.println(m.group());
          //  System.out.println("--------------------------------");
            makeUser(m.group(),user);
        }
        //System.out.println(user);
        return user;
    }
    private void makeUser(String content,ZhiHuUser user){
        if (content.startsWith("<title>"))
            user.setName(content.substring(8, content.length() - 13));
        if (content.startsWith("url"))
            user.setUserUrl(content.substring(4, content.length() - 2));
        if (content.startsWith("ellipsis"))
            user.setSignature(content.substring(17, content.length() - 2));
        if (content.startsWith("location"))
            user.setLocation(content.substring(22, content.length() - 2));
        if (content.startsWith("business"))
            user.setBussiness(content.substring(22, content.length() - 2));
        if (content.startsWith("position"))
            user.setPostion(content.substring(22, content.length() - 2));
        if (content.startsWith("education item"))
            user.setEducation(content.substring(23, content.length() - 2));
        if (content.startsWith("education-extra"))
            user.setEducationExtra(content.substring(29, content.length() - 2));
        if (content.startsWith("description"))
            user.setProfiles(content.substring(19, content.length() - 11));
        if (content.startsWith("data-gender"))
            user.setSex((content.substring(13, content.length() - 1)).equals("她") ? 0 : 1);
        if (content.endsWith("赞同"))
            user.setSuppose(StringUtil.stringToNumber(content, 0, content.length() - 11));
        if (content.endsWith("感谢"))
            user.setThanks(StringUtil.stringToNumber(content, 0, content.length() - 11));
        if (content.startsWith("提问"))
            user.setQuestion(StringUtil.stringToNumber(content, 21, content.length() - 7));
        if (content.startsWith("回答"))
            user.setAnswer(StringUtil.stringToNumber(content, 21, content.length() - 7));
        if (content.startsWith("文章"))
            user.setArticle(StringUtil.stringToNumber(content, 21, content.length() - 7));
        if (content.startsWith("公共编辑"))
            user.setPublicEdit(StringUtil.stringToNumber(content, 23, content.length() - 7));
        if (content.startsWith("关注了"))
            user.setFollowing(StringUtil.stringToNumber(content, 25, content.length() - 9));
        if (content.startsWith("关注者"))
            user.setFollowers(StringUtil.stringToNumber(content, 25, content.length() - 9));
        if (content.endsWith("人浏览"))
            user.setBewatched(StringUtil.stringToNumber(content, 0, content.length() - 13));
    }
}
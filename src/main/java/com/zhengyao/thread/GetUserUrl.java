package com.zhengyao.thread;

import com.zhengyao.dao.UserDao;
import com.zhengyao.statics.Static;
import com.zhengyao.util.SpringContextUtil;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicExpiresHandler;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetUserUrl extends Thread {
    private final CloseableHttpClient httpClient;
    private final HttpContext context;
    private final HttpPost httppost;
    private UserDao userDao = (UserDao) SpringContextUtil.getBean("userDao");//多线程中无法使用自动注入

    public GetUserUrl(CloseableHttpClient httpClient, HttpPost httppost) {
        this.httpClient = httpClient;
        this.context = HttpClientContext.create();
        this.httppost = httppost;


    }

    @Override
    public void run() {


        Integer offset = 0;

        try {
            while (true) {
                //System.out.println("map++"+MyQueue.UserCount.incrementAndGet());
                //1.经过实验 先要设置cookie 和X-Xsrftoken才能爬取
               // httppost.setHeader("Cookie", "_zap=94a32dc8-11ac-4fbb-9e3c-dbed5734268b; q_c1=b92a32c789ac45c7b1cc9ceb0a9b2e68|1495465889000|1495465889000; r_cap_id=\"ZThkNzJjZjQ5MmUwNDM0ZWJiZTU1MDJkMTc5NDFiNmQ=|1495465889|547c48f2ebcf02bc46c5dc906a6a6b5aa7fb10c0\"; cap_id=\"ZTg1NjdkNmE1ZTlmNGVmYWI4ZmJlY2I2N2I1MDhmNTk=|1495465889|785e5e104f75dab2358dbfe0c01ac4886ed0901c\"; d_c0=\"ABAC009BzAuPTq_AvkQLVlSZeeAPLo-yQ8k=|1495465890\"; aliyungf_tc=AQAAAMVqiWsGsAEAAuVH36b/scrf4Bte; acw_tc=AQAAAN8Xu2OpbAIAAuVH331DggTxE/NB; _xsrf=247b86855cfbc1de87bb39c473445efd; __utma=51854390.1972175653.1495465891.1495465891.1495465891.1; __utmb=51854390.0.10.1495465891; __utmc=51854390; __utmz=51854390.1495465891.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmv=51854390.100--|2=registration_date=20170514=1^3=entry_date=20170514=1; z_c0=Mi4wQUVBQ1NFM2h3QXNBRUFMVFQwSE1DeGNBQUFCaEFsVk56b3hLV1FBa2NCZTF6MWFPUF9IR19NMUhsUTQ4U3Fta0p3|1495467379|541449db5f01dcba2024276f20c291353e7bb74b");
                httppost.setHeader("Cookie","d_c0=\"AHDCdwGYTguPTtGBj-vSi-ejC8OM9-KRksY=|1487032899\"; _zap=0c11db2f-c1ae-478e-b47b-032ee7517e05; _ga=GA1.2.1454163689.1489975247; q_c1=1535925aa8bc487384ca31eea8da1a31|1495116908000|1487032899000; _xsrf=682220a4595e71745487f892be0f0f45; r_cap_id=\"NDUxOTZiNDE4OTdhNDA3NWFhNjM5MGUyNTNjYWE0MDE=|1495467084|4afe69028afdd053859f9dca41e35b095be1d632\"; cap_id=\"NDIwZTM2MDRhODNjNGYxMDliMjIxNmJhNTFhOWNkYzU=|1495467084|5b581f9cf81d71f6c2c511e79e97aa6d3c8b54d3\"; aliyungf_tc=AQAAAO4SETVaugIA1M10ew1auHs8fqkx; acw_tc=AQAAAFAJjwLRugIA1M10e7XR0l+BZT/X; s-q=403; s-i=7; sid=j4980kf8; s-t=autocomplete; __utma=51854390.1208297164.1492506085.1495465651.1495472789.68; __utmb=51854390.0.10.1495472789; __utmc=51854390; __utmz=51854390.1493110661.21.8.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/question/38597960; __utmv=51854390.100-1|2=registration_date=20140516=1^3=entry_date=20140516=1;"
                        + "z_c0="+Static.getCookie());
                httppost.setHeader("X-Xsrftoken", "682220a4595e71745487f892be0f0f45");
                httppost.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                //2 设置请求内容
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                formparams.add(new BasicNameValuePair("offset", offset.toString()));

                //本来还有个参数 start  但测试了很久 发现不用这个参数 有时候加了还可能报错 也可以...
                UrlEncodedFormEntity entityPost = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
                httppost.setEntity(entityPost);

                CloseableHttpResponse response = httpClient.execute(httppost, context);
                System.out.println("in test "+response+">>>>>>");
                HttpEntity entity = response.getEntity();
                System.out.println("in test "+entity+"<<<<<<<<");
                try {
                    if (entity != null) {

                        //在栈区创建可能快一点
                        String body = EntityUtils.toString(entity, "UTF-8");
                        File file = new File("D:\\新建文件夹\\爬虫\\test" + offset + ".txt");
                        if (!file.exists())
                            file.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(file, true);//加上true之后就是追加
                        byte[] bytes = body.getBytes();
                        fileOutputStream.write(bytes);
                        //跳出条件2
                        if (body.length() < 100) {
                            System.out.println("body"+body);
                            //  System.out.println("-------------------线程结束--------------------------");

                            continue;
                        }

                        //注:这里是每个分话题爬取的前多少个用户 我这里是前2000个 每个分话题想设置爬多少个就设置多少。
                        if (offset > 2000) {
                            System.out.println("-------------------线程爬取了20000个 结束-------------------");
                            break;
                        }
                        String regex = "people...[a-zA-z-]{0,200}\">";
                        Pattern p = Pattern.compile(regex);
                        Matcher m = p.matcher(body);

                        while (m.find()) {

                            //System.out.println(m.group());
                            String s = m.group();
                            //System.out.println(i);
                            String user = s.substring(8, s.length() - 3);
                            System.out.println(user);
                            if (!Static.map.contains(user)) {
                                System.out.println(Thread.currentThread() + ">>>>>");
                                Static.map.put(user, Static.UserCount.incrementAndGet());
                                userDao.insertUser(user);
                                System.out.println(user);
                            }

                            //System.out.println("Map大小" + Static.map.size());

                        }
                        System.out.println("topicid为" + httppost.getURI() + "的offset=" + offset);

                        EntityUtils.consume(entity);
                    }
                } finally {
                    response.close();
                    EntityUtils.consume(entity);
                    offset = offset + 20;
                }
            }
        } catch (ClientProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
